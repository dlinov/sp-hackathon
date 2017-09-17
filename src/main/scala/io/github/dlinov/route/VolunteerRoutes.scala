package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse, _}
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{PathMatchers, Route}
import akka.util.Timeout
import io.github.dlinov.db.mongo.{ProjectsMongoDao, RewardsMongoDao, UsersMongoDao, VolunteersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.UiVolunteerApplication
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext
import scalaz.OptionT
import scalaz.std.scalaFuture._

trait VolunteerRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  private lazy val volunteersDao = new VolunteersMongoDao(db)
  private lazy val projectsDao = new ProjectsMongoDao(db)
  private lazy val rewardsDao = new RewardsMongoDao(db)
  private lazy val usersDao = new UsersMongoDao(db)

  lazy val volunteerRoutes: Route = pathPrefix("volunteers") {
    concat(
      path("apply") {
        post {
          entity(as[UiVolunteerApplication]) { application ⇒
            complete {
              (for {
                _ ← OptionT(volunteersDao.findById(application.userId))
                project ← OptionT(projectsDao.applyVolunteer(application.projectId, application.userId))
              } yield project.asUI).run
            }
          }
        }
      },
      path(Segment / "buy" / Segment) { (volunteerId, rewardId) ⇒
        post {
          complete {
            for {
              reward ← rewardsDao.assignVolunteerForReward(volunteerId, rewardId)
              _ ← volunteersDao.buyRewardFor(volunteerId, reward)
            } yield ""
          }
        }
      },
      pathEnd {
        get {
          complete {
            for {
              users ← usersDao.findAll
              volunteers ← volunteersDao.findAll
              projects ← projectsDao.findAll
            } yield volunteers.flatMap(v ⇒ {
              for {
                user ← users.find(_._id == v._id)
              } yield {
                val userProjects = projects.filter(_.volunteerIds.contains(v._id)).map(_.asUI)
                v.asUI(user, userProjects)
              }
            })
          }
        }
      }
    )
  }

}
