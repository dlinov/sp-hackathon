package io.github.dlinov

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.github.swagger.akka.SwaggerHttpService
import io.github.dlinov.db.mongo.ModelMongoCodecs
import io.github.dlinov.route._
import org.mongodb.scala.MongoClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Boot extends App with AppSettings with UserRoutes with ProjectRoutes with SponsorRoutes with OrganizationRoutes
  with VolunteerRoutes {
  implicit val system: ActorSystem = ActorSystem("sp-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit lazy val timeout: Timeout = Timeout(20.seconds)

  val host = "0.0.0.0"
  val dbName = mongodbUri.split("/").last
  val mongoClient: MongoClient = MongoClient(mongodbUri)
  override val db = mongoClient.getDatabase(dbName).withCodecRegistry(ModelMongoCodecs.codecRegistry)

  val SwaggerDocService = new SwaggerHttpService {
    override val apiClasses: Set[Class[_]] = Set(
      classOf[SponsorRoutes],
      classOf[UserRoutes],
      classOf[ProjectRoutes]
    )
    override val apiDocsPath = "swagger" //where you want the swagger-json endpoint exposed
  }

  lazy val routess: Route = concat(userRoutes, projectRoutes, sponsorRoutes, organizationRoutes, volunteerRoutes,
    SwaggerDocService.routes)

  val serverBindingFuture: Future[ServerBinding] = Http().bindAndHandle(routess, host, port)
  serverBindingFuture.onComplete {
    case Success(ok) ⇒ println(s"Successfully binded: $ok")
    case Failure(ex) ⇒ println(s"Couldn't bind: $ex")
  }

}
