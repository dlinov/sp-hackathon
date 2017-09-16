package io.github.dlinov.route

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.PathMatchers.JavaUUID
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.ParameterDirectives._
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.ui.UiNewUser

import scala.concurrent.ExecutionContext

trait AccountRoutes extends JsonSupport {
  import HLCJsonProtocol._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout
  def db: ActorRef

  lazy val accountRoutes: Route =
    pathPrefix("accounts") {
      concat(
        path("new") {
          post {
            parameter('userId.as[String]) { userId ⇒
              entity(as[UiNewUser]) { acc ⇒
                ???
//                complete(new AccountService(db).createAccount(acc))
              }
            }
          }
        },
        pathEnd {
          get {
            parameter('userIds.as[String]) { userIds ⇒
              val uuids = userIds.split(",").map(UUID.fromString).toSeq
              rejectEmptyResponse {
                ???
//                complete(new AccountService(db).getAccounts(uuids: _*))
              }
            }
          }
        }
      )
    }

}
