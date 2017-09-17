package io.github.dlinov.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, concat, entity, pathEnd, pathPrefix, rejectEmptyResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import io.github.dlinov.db.mongo.{OrganizationsMongoDao, ProjectsMongoDao, UsersMongoDao}
import io.github.dlinov.json.JsonSupport
import io.github.dlinov.model.{UiNewOrganization, User}
import org.bson.types.ObjectId
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
  private lazy val projectsDao = new ProjectsMongoDao(db)
  private lazy val usersDao = new UsersMongoDao(db)

  lazy val organizationRoutes: Route =
    pathPrefix("organizations") {
      concat(
        pathEnd {
          concat(
            get {
              rejectEmptyResponse {
                complete {
                  for {
                    organizations ← organizationDao.findAll
                    projects ← projectsDao.findAll
                  } yield {
                    organizations.map(org ⇒ {
                      val orgProjects = projects.collect {
                        case p if org.taskIds.contains(p._id) ⇒ p.asUI
                      }
                      org.asUI(orgProjects)
                    })
                  }
                }
              }
            },
            post {
              entity(as[UiNewOrganization]) { newOrg ⇒
                complete {
                  val userId = ObjectId.get()
                  for {
                    _ ← usersDao.createUser(User(userId, newOrg.email, newOrg.password, newOrg.title, lastName = ""))
                    organization ← organizationDao.registerOrganization(newOrg.toOrganization(userId))
                  } yield organization.map(_.asUI(Seq.empty)) // new organization cannot contain projects
                }
              }
            }
          )
        }
      )
    }

}
