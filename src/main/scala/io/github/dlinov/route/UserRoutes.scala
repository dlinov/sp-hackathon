package io.github.dlinov.route

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.server.Directives.{ as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse }
import akka.http.scaladsl.server.PathMatchers.JavaUUID
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.ParameterDirectives._
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.ui.{ UiNewUser, UiUser }

import scala.concurrent.ExecutionContext

trait UserRoutes extends JsonSupport {
  import HLCJsonProtocol._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  lazy val userRoutes: Route =
    pathPrefix("users") {
      concat(
        path("register") {
          post {
            entity(as[UiNewUser]) { user ⇒
              ???
//              complete(new UserService(db).registerUser(user))
            }
          }
        },
        pathPrefix(JavaUUID) { id ⇒
          get {
            pathEnd {
              rejectEmptyResponse {
                ???
//                complete(new UserService(db).findUser(id))
              }
            }
          }
        }
      )
    }
}
