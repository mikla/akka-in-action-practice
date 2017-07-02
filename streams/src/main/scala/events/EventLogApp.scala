package events

import java.nio.file.Paths
import java.nio.file.StandardOpenOption._

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorAttributes, ActorMaterializer, IOResult, Supervision}
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future

object EventLogApp extends App {

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val decider: Supervision.Decider = {
    case _: LogParseException => Supervision.Resume
    case _ => Supervision.Stop
  }

  val maxLine = 255
  val inputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/input-stream/events.log")
  val outputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/input-stream/events-processed.txt")

  val source: Source[ByteString, Future[IOResult]] =
    FileIO.fromPath(inputFile)

  val sink: Sink[ByteString, Future[IOResult]] =
    FileIO.toPath(outputFile, Set(CREATE, WRITE, APPEND))

  val frame: Flow[ByteString, String, NotUsed] =
    Framing.delimiter(ByteString("\n"), maxLine)
      .map(_.decodeString("UTF8"))

  val parse: Flow[String, Event, NotUsed] =
    Flow[String].map(LogLineProcessor.parseLineEx)
      .collect { case Some(e) => e }
    .withAttributes(ActorAttributes.supervisionStrategy(decider))

  val filter: Flow[Event, Event, NotUsed] =
    Flow[Event].filter(_.state.state == "error")

  val serialize: Flow[Event, ByteString, NotUsed] =
    Flow[Event].map(event => ByteString(event.toString))

  val composedFlow: Flow[ByteString, ByteString, NotUsed] =
    frame.via(parse).via(filter).via(serialize)

  val runnableGraph: RunnableGraph[Future[IOResult]] =
    source.via(composedFlow).toMat(sink)(Keep.right)

  runnableGraph.run().foreach { res =>
    println(res)
    system.terminate()
  }

}
