package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.db.mongo.OrganizationsMongoDao
import io.github.dlinov.json.JsonSupport
import org.mongodb.scala.MongoDatabase

import scala.concurrent.ExecutionContext

trait OrganizationRoutes extends JsonSupport {
  import HLCJsonProtocol._
  import io.github.dlinov.model.Implicits._

  implicit def system: ActorSystem
  implicit def executionContext: ExecutionContext
  implicit def timeout: Timeout

  val db: MongoDatabase
  private lazy val organizationDao = new OrganizationsMongoDao(db)

  lazy val organizationRoutes: Route =
    pathPrefix("organizations") {
      concat(
        pathEnd {
          concat(
            get {
              rejectEmptyResponse {
                complete {
                  organizationDao.findAll.map(_.map(_.asUI))
                }
              }
            },
            post {
              entity(as[UiNewOrganization]) { organization ⇒
                complete {
                  organizationDao.registerOrganization(organization.toOrganization).map(_.asUI)
                }
              }
            }
          )
        }
      )
    }

}
