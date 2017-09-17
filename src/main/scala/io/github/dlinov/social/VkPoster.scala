package io.github.dlinov.social

import akka.actor.ActorSystem
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.{GroupActor, ServiceActor, UserActor}
import com.vk.api.sdk.httpclient.HttpTransportClient
import io.github.dlinov.model.Project

import scala.concurrent.ExecutionContext

class VkPoster(
  implicit system: ActorSystem,
  executionContext: ExecutionContext) {

  import scala.collection.JavaConversions._

  val vk = new VkApiClient(new HttpTransportClient())
  val apiAccessToken = "daa29725daa29725daa297251ddafcfef3ddaa2daa29725831cc52b78bdba9c2ad5cfdf"
  val clientSecret = "JY1zsxaFtJpwuTfmbvQu"
  val appId = 6187478
  val sActor = new ServiceActor(appId, clientSecret, apiAccessToken)
  val groupId = 146097403

  val actor = new GroupActor(groupId, apiAccessToken)

  var user: UserActor = null

  val appHost = "http://02bc1c3a.ngrok.io"
  def authUrl() = {
    s"https://oauth.vk.com/authorize?" +
      s"client_id=$appId&display=page&" +
      "redirect_uri=" + "https://oauth.vk.com/blank.html" + "/vk/auth&scope=wall,friends&response_type=token&v=5.63&state=123456&revoke=1"
  }


  def postVk(text: String) = {
    vk.wall().post(user).ownerId(-153608838).fromGroup(true).message(text).execute()
  }

  def postVk(project: Project) = {
    import project._
    val text =
      s"""
        |Требуются волонтеры!
        |
        |Проект: $title
        |
        |$description
        |
        |Количество поинтов: $price
        |
        |Ждем ваших откликов!
      """.stripMargin
    vk.wall().post(user).ownerId(-153608838).fromGroup(true).message(text).execute()
  }

  def getTopic(groupId: Integer, id: Int): String = {
    vk.board().getTopics(sActor, groupId).topicIds(id).execute().getItems.head.getTitle
  }

}
