package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, path, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post, put}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.db.mongo.{ProjectsMongoDao, VolunteersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.ui.UiNewProject
import io.swagger.annotations.Api
import org.mongodb.scala.MongoDatabase

import scala.concurrent.{ExecutionContext, Future}

@Api(value = "/projects")
trait ProjectRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  lazy val projectsDao = new ProjectsMongoDao(db)
  lazy val volunteersDao = new VolunteersMongoDao(db)

  lazy val projectRoutes: Route =
    pathPrefix("projects") {
      concat(
        pathPrefix(Segment) { id ⇒
          path("complete") {
            put {
              complete {
                for {
                  finishedProject ← projectsDao.finishProject(id)
                  _ ← volunteersDao.addPointsTo(finishedProject.volunteerIds, finishedProject.price)
                } yield finishedProject.asUI
              }
            }
          }
        },
        pathEnd {
          concat(
            get {
              rejectEmptyResponse {
                complete {
                  projectsDao.findAll.map(_.map(_.asUI))
                }
              }
            },
            post {
              entity(as[UiNewProject]) { project ⇒
                complete {
                  projectsDao.createProject(project.toProject).map(_.asUI)
                }
              }
            }
          )
        }
      )
    }
}
