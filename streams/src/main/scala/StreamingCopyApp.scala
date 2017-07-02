import java.nio.file.Paths
import java.nio.file.StandardOpenOption._

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object StreamingCopyApp extends App {

  val inputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/input-stream/1.txt")
  val outputFile = Paths.get("/Users/mikla/projects/akka-in-action-practice/streams/output-stream/1.txt")

  val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(inputFile)
  val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(outputFile, Set(CREATE, WRITE, APPEND))

  val runnableGraph: RunnableGraph[Future[IOResult]] = source.to(sink)

  val runnableGraphBoth = source.toMat(sink)(Keep.both)

  val runnableGraphCustomFunction = source.toMat(sink) { (left, right) =>
    Future.sequence(List(left, right)).map(_ => Done)
  }

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  runnableGraph.run().foreach { result =>
    println(s"${result.status}, ${result.count} bytes read.")
    system.terminate()
  }

}
