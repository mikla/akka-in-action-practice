package routing_slip

import akka.actor.{Actor, ActorRef, Props}

class PainterBox(color: String) extends Actor with RouteSlip {
  override def receive: Receive = {
    case RouteSlipMessage(routingSlip, car: Car) =>
      sendMessageToNextBox(routingSlip, car.copy(color = color))
  }
}

class NavigationBox extends Actor with RouteSlip {
  override def receive: Actor.Receive = {
    case RouteSlipMessage(rs, car: Car) =>
      sendMessageToNextBox(rs, car.copy(hasNavigation = true))
  }
}

class ParkingBox extends Actor with RouteSlip {
  override def receive: Actor.Receive = {
    case RouteSlipMessage(rs, car: Car) =>
      sendMessageToNextBox(rs, car.copy(hasParkingSensors = true))
  }
}

class Router(endStep: ActorRef) extends Actor with RouteSlip {

  val painterBox = context.actorOf(Props(new PainterBox("gray")))
  val parkingBox = context.actorOf(Props[ParkingBox])
  val navigationBox = context.actorOf(Props[NavigationBox])

  override def receive: Actor.Receive = {
    case order: Order =>
      val route = createRoute(order.options)
      sendMessageToNextBox(route, new Car)
  }

  private def createRoute(orders: Seq[CarOptions.Value]): Seq[ActorRef] = {
    orders.map {
      case CarOptions.CarColorGray => painterBox
      case CarOptions.Navigation => navigationBox
      case CarOptions.ParkingSensors => painterBox
    } :+ endStep
  }

}
