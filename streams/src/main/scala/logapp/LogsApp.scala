package logapp

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import events.LogParseException

import scala.concurrent.Future
import scala.util.{Failure, Success}

object LogsApp extends App {

  lazy val host = "localhost"
  lazy val port = 8000

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val decider: Supervision.Decider = {
    case _: LogParseException => Supervision.Stop
    case _ => Supervision.Stop
  }

  implicit val materializer = ActorMaterializer(
    ActorMaterializerSettings(system)
      .withSupervisionStrategy(decider)
  )

  val api = new LogsApi(
    Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/input-stream/logs"),
    256).routes

  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api, host, port)

  val log = Logging(system.eventStream, "logs")
  bindingFuture.onComplete {
    case Success(serverBinding) =>
      log.info(s"Bound to ${serverBinding.localAddress} ")
    case Failure(ex) =>
      log.error(ex, "Failed to bind to {}:{}!", host, port)
      system.terminate()

  }

}
