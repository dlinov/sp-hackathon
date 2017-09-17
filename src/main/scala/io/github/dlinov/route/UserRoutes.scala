package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse, _}
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{PathMatchers, Route}
import akka.util.Timeout
import io.github.dlinov.db.mongo.{UsersMongoDao, VolunteersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.{UiNewUser, UiUser, Volunteer}
import io.swagger.annotations._
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext

@Api(value = "/users")
trait UserRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  lazy val dao = new UsersMongoDao(db)
  private lazy val volunteersDao = new VolunteersMongoDao(db)

  @ApiOperation(httpMethod = "GET", response = classOf[UiUser], value = "Returns an user based on ID")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "userId", required = false, dataType = "string", paramType = "path", value = "ID of user that needs to be fetched")
  ))
  lazy val userRoutes: Route =
    pathPrefix("users") {
      concat(
        path("register") {
          post {
            entity(as[UiNewUser]) { user ⇒
              complete {
                for {
                  createdUser ← dao.createUser(user.toUser)
                  volunteer = Volunteer.create(createdUser._id)
                  createdVolunteer ← volunteersDao.createVolunteer(volunteer)
                } yield createdVolunteer.asUI(createdUser, Seq.empty)
              }
            }
          }
        },
        pathPrefix("login") {
          parameter("email", "pass") { (email, pass) =>
            pathEnd {
              rejectEmptyResponse {
                complete {
                  dao.login(email, pass).map(_.map(_.asUI))
                }
              }
            }
          }
        },
        pathPrefix(PathMatchers.Segment) { id ⇒
          get {
            pathEnd {
              rejectEmptyResponse {
                complete {
                  dao.findUser(id).map(_.map(_.asUI))
                }
              }
            }
          }
        },
        pathEnd {
          get {
            complete {
              dao.findAll.map(_.map(_.asUI))
            }
          }
        }
      )
    }
}
