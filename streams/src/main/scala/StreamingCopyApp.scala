import java.nio.file.{Path, Paths, StandardOpenOption}

import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, RunnableGraph, Sink, Source}
import akka.util.ByteString
import java.nio.file.StandardOpenOption._

import akka.actor.ActorSystem

import scala.concurrent.Future

object StreamingCopyApp extends App {

  val inputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/input-stream/1.txt")
  val outputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/output-stream/1.txt")

  val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(inputFile)
  val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(outputFile, Set(CREATE, WRITE, APPEND))

  val runnableGraph: RunnableGraph[Future[IOResult]] = source.to(sink)

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  runnableGraph.run().foreach { result =>
    println(s"${result.status}, ${result.count} bytes read.")
    system.terminate()
  }

}
