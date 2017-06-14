import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object MainApp extends App {

  implicit val system = ActorSystem("MyActorSystem")
  implicit val materializer = ActorMaterializer()

  // Sources
  val sourceFromRange = Source(1 to 10)
  val sourceFromIterable = Source(List(1, 2, 3))
  val sourceFromFuture = Source(List(Future.successful("hello")))
  val sourceWithSingleElement = Source.single("just one")
  val sourceEmittingTheSameElement = Source.repeat("again and again")
  val emptySource = Source.empty

  // Sinks

  val sinkPrintingOutElements = Sink.foreach[String](println(_))
  val sinkCalculatingASumOfElements = Sink.fold[Int, Int](0)(_ + _)
  val sinkReturningTheFirstElement = Sink.head
  val sinkNoop = Sink.ignore

  // Flows

  val flowDoublingElement = Flow[Int].map(_ * 2)
  val flowFilteringOddElements = Flow[Int].filter(_ % 2 == 0)
  val flowBatchingElements = Flow[Int].grouped(10)
  val flowBufferingElements = Flow[String].buffer(1000, OverflowStrategy.backpressure) // back-

  // Defining a stream

  val streamCalculatingSumOfElements: RunnableGraph[Future[Int]] =
    sourceFromIterable.toMat(sinkCalculatingASumOfElements)(Keep.right)

  // adding flow

  val streamCalculatingSumOfDoubledElements: RunnableGraph[Future[Int]] =
    sourceFromIterable
      .via(flowDoublingElement)
      .toMat(sinkCalculatingASumOfElements)(Keep.right)

  streamCalculatingSumOfDoubledElements.run()

}