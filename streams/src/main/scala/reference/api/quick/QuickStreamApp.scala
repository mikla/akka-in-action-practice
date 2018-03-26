package reference.api.quick

import akka.stream._
import akka.stream.scaladsl._

import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object QuickStreamApp extends App {

  val source: Source[Int, NotUsed] = Source(1 to 100)

  implicit val system = ActorSystem("QuickStart")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val done: Future[Done] = source.runForeach(println)(materializer)

  val factorials = source.scan(BigInt(1)) {
    case (acc, next) => acc * next
  }

  val factorialResult = factorials
    .map(s => ByteString(s"$s"))
    .runWith(FileIO.toPath(Paths.get("factorials.txt")))

  Await.result(factorialResult, Duration.Inf)

  def lineSink(fileName: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s"$s\n"))
      .toMat(FileIO.toPath(Paths.get(fileName)))(Keep.right)

  val factorialsResult2 = factorials.map(_.toString).runWith(lineSink("factorials2.txt"))

  Await.result(factorialsResult2, Duration.Inf)

  val factorialsResult3: Future[Done] = factorials
    .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
    .throttle(1, 1.second, 1, ThrottleMode.shaping)
    .runForeach(println)

  Await.result(factorialsResult3, Duration.Inf)

}
