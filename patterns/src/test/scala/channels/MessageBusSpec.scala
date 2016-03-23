package channels

import akka.actor.ActorSystem
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.FlatSpecLike

class MessageBusSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike {

  "MessageBus" should "filter events" in {
    val probe = TestProbe()

    val bus = new OrderMessageBus
    bus.subscribe(probe.ref, false)

    val msg = Order(1)

    bus.publish(msg)

    probe.expectMsg(msg)
  }

}
