package logapp

import java.nio.file.Path

import events.{Event, LogLineProcessor, LogReceipt, ParseError}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{BidiFlow, FileIO, Flow, Framing, Keep}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import akka.{Done, NotUsed}

import events.Codecs._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import akka.http.scaladsl.server.Route

class LogsApi(
  val logsDir: Path,
  val maxLine: Int)(
  implicit val executionContext: ExecutionContext,
  val materializer: ActorMaterializer) {

  def logFile(id: String) = logsDir.resolve(id)

  import java.nio.file.StandardOpenOption._

  val inFlow: Flow[ByteString, Event, NotUsed] = Framing.delimiter(ByteString("\n"), maxLine)
    .map(_.decodeString("UTF8"))
    .map(LogLineProcessor.parseLineEx)
    .collect { case Some(e) => e }
  val outFlow: Flow[Event, ByteString, NotUsed] = Flow[Event].map { event =>
    ByteString(event.asJson.noSpaces)
  }

  // The bidirectional flow is joined with a flow that passes every event through unchanged.
  val bidiFlow: BidiFlow[ByteString, Event, Event, ByteString, NotUsed] = BidiFlow.fromFlows(inFlow, outFlow)

  val logToJsonFlow = bidiFlow.join(Flow[Event])

  def logFileSink(logId: String) =
    FileIO.toPath(logFile(logId), Set(CREATE, WRITE, APPEND))
  def logFileSource(logId: String) = FileIO.fromPath(logFile(logId))

  def routes: Route = postRoute

  def postRoute = pathPrefix("logs" / Segment) { logId =>
    pathEndOrSingleSlash {
      post {
        entity(as[HttpEntity]) { entity =>
          onComplete(
            entity
              .dataBytes
              .via(logToJsonFlow)
              .toMat(logFileSink(logId))(Keep.right)
              .run()
          ) {
            case Success(IOResult(count, Success(Done))) =>
              complete(StatusCodes.OK, LogReceipt(logId, count).asJson.noSpaces)
            case Success(IOResult(_, Failure(e))) =>
              complete(StatusCodes.BadRequest, ParseError(logId, e.getMessage).asJson.noSpaces)
            case Failure(e) =>
              complete(StatusCodes.BadRequest, ParseError(logId, e.getMessage).asJson.noSpaces)
          }
        }
      }
    }
  }

}

