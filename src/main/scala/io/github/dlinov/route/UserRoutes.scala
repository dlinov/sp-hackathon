package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{PathMatchers, Route}
import akka.util.Timeout
import io.github.dlinov.db.mongo.UsersMongoDao
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.ui.UiNewUser
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext

trait UserRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  lazy val dao = new UsersMongoDao(db)

  lazy val userRoutes: Route =
    pathPrefix("users") {
      concat(
        path("register") {
          post {
            entity(as[UiNewUser]) { user ⇒
              dao.createUser(user.toUser)
              complete("done")
            }
          }
        },
        pathPrefix(PathMatchers.Segment) { id ⇒
          get {
            pathEnd {
              rejectEmptyResponse {
                complete {
                  dao.findUser(id).map(_.asUI)
                }
              }
            }
          }
        }
      )
    }
}
