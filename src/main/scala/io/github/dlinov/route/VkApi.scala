package io.github.dlinov.route

import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import com.vk.api.sdk.client.actors.UserActor
import io.github.dlinov.social.VkPoster

import scala.language.postfixOps


trait VkApi {

  val vkPoster: VkPoster

  import vkPoster._

  def vkRoutes: Route =
    pathPrefix("vk") {
      path("url") {
        complete(authUrl())
      } ~ path("post") {
        postVk("lalala")
        complete("ok")
      } ~ pathPrefix("auth") {
        parameter('access_token, 'user_id.as[Int]) { (access_token, userId) =>
          user = new UserActor(userId, access_token)
          complete("privet")
        }
      }
    }

}
