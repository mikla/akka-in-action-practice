package routing_slip

import akka.actor.ActorRef

case class Order(options: Seq[CarOptions.Value])

case class Car(color: String = "",
               hasNavigation: Boolean = false,
               hasParkingSensors: Boolean = false)

case class RouteSlipMessage(routingSlip: Seq[ActorRef],
                            message: AnyRef)