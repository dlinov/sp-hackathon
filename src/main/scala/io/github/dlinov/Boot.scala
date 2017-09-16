package io.github.dlinov

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import io.github.dlinov.route.UserRoutes
import org.mongodb.scala.MongoClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Boot extends App with AppSettings with UserRoutes {
  implicit val system: ActorSystem = ActorSystem("sp-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit lazy val timeout: Timeout = Timeout(20.seconds)

  val host = "0.0.0.0"
  val dbName = mongodbUri.split("/").last
  val mongoClient: MongoClient = MongoClient(mongodbUri)

  lazy val routes: Route = concat(userRoutes)

  val serverBindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)
  serverBindingFuture.onComplete {
    case Success(ok) ⇒ println(s"Successfully binded: $ok")
    case Failure(ex) ⇒ println(s"Couldn't bind: $ex")
  }

}
