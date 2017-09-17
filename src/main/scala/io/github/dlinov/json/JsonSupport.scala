package io.github.dlinov.json

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.DateTime
import io.github.dlinov.model._
import io.github.dlinov.model.ui._
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  object HLCJsonProtocol extends DefaultJsonProtocol {
    implicit object UuidJsonFormat extends JsonFormat[UUID] {
      def write(x: UUID) = JsString(x.toString)

      def read(value: JsValue): UUID = value match {
        case JsString(x) ⇒
          UUID.fromString(x)
        case x ⇒
          throw DeserializationException("Expected UUID as JsString, but got " + x)
      }
    }

    implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
      override def write(obj: DateTime) = JsString(obj.toIsoDateTimeString())

      override def read(json: JsValue): DateTime = json match {
        case JsString(value) ⇒
          DateTime.fromIsoDateTimeString(value) match {
            case Some(date) ⇒
              date
            case _ ⇒
              throw DeserializationException("Failed to parse date time [" + value + "].")
          }
        case _ ⇒
          throw DeserializationException("Failed to parse json string [" + json + "].")
      }
    }

    implicit val uiUserJsonFormat: RootJsonFormat[UiUser] = jsonFormat4(UiUser)
    implicit val uiNewUserFormat: RootJsonFormat[UiNewUser] = jsonFormat4(UiNewUser)
    implicit val uiProjectFormat: RootJsonFormat[UiProject] = jsonFormat7(UiProject)
    implicit val uiNewProjectFormat: RootJsonFormat[UiNewProject] = jsonFormat5(UiNewProject)
    implicit val newRewardFormat: RootJsonFormat[UiNewReward] = jsonFormat3(UiNewReward)
    implicit val rewardFormat: RootJsonFormat[UiReward] = jsonFormat4(UiReward.apply)
    implicit val sponsorFormat: RootJsonFormat[UiSponsor] = jsonFormat5(UiSponsor.apply)
    implicit val newSponsorFormat: RootJsonFormat[UiNewSponsor] = jsonFormat3(UiNewSponsor)
    implicit val newOrganizationFormat: RootJsonFormat[UiNewOrganization] = jsonFormat3(UiNewOrganization)
    implicit val organizationFormat: RootJsonFormat[UiOrganization] = jsonFormat4(UiOrganization)
    implicit val applicationFormat: RootJsonFormat[UiVolunteerApplication] = jsonFormat2(UiVolunteerApplication)
    implicit val volunteerFormat: RootJsonFormat[UiVolunteer] = jsonFormat7(UiVolunteer)
  }
}
