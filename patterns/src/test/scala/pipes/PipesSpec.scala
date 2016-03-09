package pipes

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.FlatSpecLike

import scala.concurrent.duration._

class PipesSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike {

  "actors" should "be filters" in {
    val endProbe = TestProbe()
    val speedFilterRef = system.actorOf(Props(new SpeedFilter(50, endProbe.ref)))
    val licenceFilterRef = system.actorOf(Props(new LicenseFilter(speedFilterRef)))
    val msg = new Photo("licence", 51)

    speedFilterRef ! msg
    endProbe.expectMsg(msg)

    licenceFilterRef ! msg
    endProbe.expectMsg(msg)

    licenceFilterRef ! Photo("", 30)
    endProbe.expectNoMsg(1 second)

  }

}
