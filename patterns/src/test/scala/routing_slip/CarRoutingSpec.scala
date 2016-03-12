package routing_slip

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.FlatSpecLike

class CarRoutingSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike {

  "car routing" should "produce nice car" in {
    val probe = TestProbe()
    val router = system.actorOf(Props(new Router(probe.ref)), "Router")

    val order = new Order(Seq(CarOptions.CarColorGray, CarOptions.Navigation))
    router ! order

    val expectedCar = new Car("gray", true, false)
    probe.expectMsg(expectedCar)
  }

}
