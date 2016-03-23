package channels

import akka.actor.ActorRef
import akka.event.{ActorEventBus, LookupClassification, EventBus}

class OrderMessageBus
  extends EventBus
    with LookupClassification
    with ActorEventBus {

  override type Event = Order

  override type Classifier = Boolean

  override protected def mapSize(): Int = 2
  override protected def classify(event: OrderMessageBus#Event): Boolean = event.order > 1

  override protected def publish(event: Order, subscriber: ActorRef): Unit = {
    subscriber ! event
  }

}
