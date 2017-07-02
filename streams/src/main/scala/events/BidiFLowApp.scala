package events

import akka.NotUsed
import akka.stream.scaladsl.{BidiFlow, Flow}
import akka.util.ByteString

object BidiFLowApp {

  val inFlow: Flow[ByteString, Event, NotUsed] = ???
  val outFlow: Flow[Event, ByteString, NotUsed] = ???

  val filter: Flow[Event, Event, NotUsed] = ???

  val bidiFlow: BidiFlow[ByteString, Event, Event, ByteString, NotUsed] = BidiFlow.fromFlows(inFlow, outFlow)

  val flow: Flow[ByteString, ByteString, NotUsed] = bidiFlow.join(filter)

}
