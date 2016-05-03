import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.camel.CamelExtension
import akka.testkit.{TestKit, TestProbe}
import org.apache.commons.io.FileUtils
import org.scalatest.FlatSpecLike

import scala.concurrent.Await
import scala.concurrent.duration._

class OrderConsumerSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike {

  "order consumer" should "read messages from file system" in {
    val probe = TestProbe()
    val camelUri = "file:d:/tmp/messages"
    val consumer = system.actorOf(Props(new OrderConsumerXml(camelUri, probe.ref)))

    // waiting for Camel initialization

    val camelExt = CamelExtension(system)
    val activated = camelExt.activationFutureFor(consumer)(timeout = 10.seconds, executor = system.dispatcher)
    Await.result(activated, 5.seconds)

    val msg = new Order("me", "akka in action", 20)
    val xml = <order>
      <customerId>{msg.customerId}</customerId>
      <productId>{msg.productId}</productId>
      <number>{msg.number}</number>
    </order>

    val msgFile = new File("d:/tmp/messages", "msg1.xml")
    FileUtils.writeStringToFile(msgFile, xml.toString())

    probe.expectMsg(msg)

    system.terminate()

  }

}
