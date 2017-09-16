package io.github.dlinov.json

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.DateTime
import io.github.dlinov.model.ui._
import spray.json.{ DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat }

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

//    implicit object CurrencyJsonFormat extends JsonFormat[Currency] {
//      def write(x: Currency) = JsString(x.toString)
//
//      def read(value: JsValue): Currency = value match {
//        case JsString("EUR") ⇒
//          Currency.EUR
//        case JsString("USD") ⇒
//          Currency.USD
//        case x ⇒
//          throw DeserializationException("Expected currency code as JsString, but got " + x)
//      }
//    }

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
    implicit val uiNewUserFormat: RootJsonFormat[UiNewUser] = jsonFormat3(UiNewUser)
  }
}
