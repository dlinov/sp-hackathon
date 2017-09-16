package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, parameter, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.{PathMatchers, Route}
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.db.mongo.{ProjectsMongoDao, UsersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.swagger.annotations.Api
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext

@Api(value = "/projects")
trait ProjectRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  lazy val projectsDao = new ProjectsMongoDao(db)

  lazy val projectRoutes: Route =
    pathPrefix("projects") {
      concat(
        pathEnd {
          get {
            rejectEmptyResponse {
              complete {
                projectsDao.findAll.map(_.map(_.asUI))
              }
            }
          }
        }
      )
    }
}
