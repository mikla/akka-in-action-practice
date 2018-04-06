package reference.api.basic

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.{Done, NotUsed}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

object RunnableGraphApp extends App {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val runnable: RunnableGraph[Future[Int]] = source.toMat(sumSink)(Keep.right)

  val sum = runnable.run()
  val sum1 = runnable.run() // I can run it multiple times
  val sum2 = source.runWith(sumSink)

  println(Await.result(sum, Duration.Inf))
  println(Await.result(sum2, Duration.Inf))

  val zeros = source.map(_ => 0)
  val zeroSum = zeros.runWith(sumSink)
  println(Await.result(zeroSum, Duration.Inf))

  // defining sources, sinks and flows

  val sourceFromFuture: Source[String, NotUsed] = Source.fromFuture(Future.successful("Single element"))
  val singleElementSource = Source.single("only element")
  Source.empty

  Sink.head
  Sink.ignore
  Sink.last

  val printSink: Sink[Int, Future[Done]] = Sink.foreach(e => print(e + " "))
  Await.result(source.runWith(printSink), Duration.Inf)

  // all we need - just run
  val mapped: RunnableGraph[NotUsed] = Source(1 to 6).via(Flow[Int].map(_ * 2)).to(printSink)

  val otherSink = Flow[Int].alsoTo(printSink).to(Sink.ignore)

  source.runForeach(_ => ())

  // parallel processing

  val prallel = source
    .map(_ + 1)
    .async
    .map(_ * 2)

  println()

  Await.result(prallel.runWith(printSink), Duration.Inf)

  // combining materialized views

  val maybeSource: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]
  val throttledFlow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ + 1)
  val headSink: Sink[Int, Future[Int]] = Sink.head[Int]

  val r1: RunnableGraph[Promise[Option[Int]]] = maybeSource.via(throttledFlow).to(headSink)

  val r8: RunnableGraph[(Promise[Option[Int]], NotUsed)] = maybeSource.viaMat(throttledFlow)(Keep.both).to(headSink)
  val r9: RunnableGraph[(Promise[Option[Int]], Future[Int])] = maybeSource.via(throttledFlow).toMat(headSink)(Keep.both)
  val r10: RunnableGraph[((Promise[Option[Int]], NotUsed), Future[Int])] = maybeSource.viaMat(throttledFlow)(Keep.both).toMat(headSink)(Keep.both)
  val r11: RunnableGraph[(NotUsed, Future[Int])] = maybeSource.viaMat(throttledFlow)(Keep.right).toMat(headSink)(Keep.both)

  // mapping materialized values

  val mappedR9: RunnableGraph[(Promise[Option[Int]], Future[Int])] = r9.mapMaterializedValue {
    case ((promise, value)) =>
      (promise, value)
  }

  val (prom, r9value) = mappedR9.run()
  r9value.onComplete(println(_))
  prom.success(Some(2))

  // the same but with graph dsl
  val r12: RunnableGraph[(Promise[Option[Int]], NotUsed, Future[Int])] =
    RunnableGraph.fromGraph(GraphDSL.create(maybeSource, throttledFlow, headSink)((_, _, _)) {
      implicit builder =>
        (src, f, dst) =>
          import GraphDSL.Implicits._
          src ~> f ~> dst
          ClosedShape
    })

}
