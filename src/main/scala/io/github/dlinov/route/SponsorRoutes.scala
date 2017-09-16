package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse, _}
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{PathMatchers, Route}
import akka.util.Timeout
import io.github.dlinov.db.mongo.{RewardsMongoDao, SponsorsMongoDao, UsersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.{UiNewReward, UiNewUser, UiSponsor, UiUser}
import io.swagger.annotations._
import org.mongodb.scala.MongoDatabase

import scala.concurrent.{ExecutionContext, Future}
import scalaz.OptionT
import scalaz.std.scalaFuture._

@Api(value = "/users")
trait SponsorRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  lazy val sponsorsDao = new SponsorsMongoDao(db)
  lazy val rewardDao = new RewardsMongoDao(db)
  lazy val usersDao = new UsersMongoDao(db)

  @ApiOperation(httpMethod = "GET", response = classOf[UiUser], value = "Returns an user based on ID")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "userId", required = false, dataType = "string", paramType = "path", value = "ID of user that needs to be fetched")
  ))
  lazy val userRoutes: Route =
    pathPrefix("sponsor") {
      concat(
        path("reward") {
          post {
            entity(as[UiNewReward]) { reward ⇒
              rewardDao.insert(reward.toReward)
              complete("done")
            }
          }
        },
        pathPrefix("rewards") {
          parameter("userId") { userId =>
            pathEnd {
              rejectEmptyResponse {
                complete {
                  val x: OptionT[Future, UiSponsor] = for {
                    sponsor <- OptionT(sponsorsDao.findById(userId))
                    user <- OptionT(usersDao.findById(userId))
                    rewards <- OptionT(rewardDao.findByIds(sponsor.rewardIds.map(_.toString)).map(Option(_)))
                  } yield UiSponsor.apply2(user, rewards)
                  x.run
                }
              }
            }
          }
        }
      )
    }
}
