package reference.api.quick

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, OverflowStrategy}
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import reference.api.quick.domain.{Author, Hashtag, Tweet}

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TweetApp extends App {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  /**
    * Source[Out, M1] => Flow[In, Out, M2] => Sink[In, M3]
    */
  val tweets: Source[Tweet, NotUsed] = Source(
    List(
      Tweet(Author("me"), 1L, "body #akka"),
      Tweet(Author("odersky"), 1L, "Scala 2.12.5 is out! #scala"),
      Tweet(Author("me"), 1L, "0_o #shapeless")
    )
  ).buffer(10, OverflowStrategy.dropHead)

  val authors: Source[Author, NotUsed] =
    tweets
      .filter(_.hashtags.contains(domain.akkaTag))
      .map(_.author)

  authors.runWith(Sink.foreach(println))

  // flatMap called mapConcat

  val hashTags = tweets.mapConcat(_.hashtags.toList)

  // storing authors and hashtags to diff files

  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val writeAuthors =
    Flow[Author]
      .map(a => ByteString(s"$a\n"))
      .toMat(FileIO.toPath(Paths.get("tweet-authors")))(Keep.right)

  val writeTags = Flow[Hashtag]
    .map(a => ByteString(s"$a\n"))
    .toMat(FileIO.toPath(Paths.get("tweet-tags")))(Keep.right)

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val bcast = b.add(Broadcast[Tweet](3))
    tweets ~> bcast.in

    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeTags
    bcast.out(2) ~> count ~> sumSink // how to return value from graph, currently is NotUsed

    ClosedShape
  })

  g.run()

  val counterGraphFuture =
    tweets
      .via(count)
      .toMat(sumSink)(Keep.right)
      .run()
      .map(println)

  Await.result(counterGraphFuture, Duration.Inf)

}
