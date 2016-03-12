package routing_slip

import akka.actor.ActorRef

trait RouteSlip {

  def sendMessageToNextBox(routeSlip: Seq[ActorRef], message: AnyRef) = {
    val nextBox = routeSlip.head
    val newBoxes = routeSlip.tail
    if (newBoxes.isEmpty) {
      nextBox ! message
    } else {
      nextBox ! RouteSlipMessage(newBoxes, message)
    }
  }

}
