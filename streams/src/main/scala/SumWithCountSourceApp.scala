import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Broadcast, Concat, Flow, GraphDSL, Keep, Merge, Sink, Source, Zip}

import scala.concurrent.ExecutionContext.Implicits.global


object SumWithCountSourceApp extends App {

  implicit val system = ActorSystem("MyActorSystem")
  implicit val materializer = ActorMaterializer()

  val source = Source(List(1,2,3,4,5))

  val countFlow = Flow[Int].fold(0)((c, _) => c + 1)
  val sumFlow = Flow[Int].fold(0)((s, e) => s + e)

  val graph = Source.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val fanOut = builder.add(Broadcast[Int](2))
    val merge = builder.add(Zip[Int, Int])

    source ~> fanOut ~> countFlow ~> merge.in0
              fanOut ~> sumFlow ~> merge.in1

    SourceShape(merge.out)
  })


  graph.runWith(Sink.last).onComplete(println)

}
