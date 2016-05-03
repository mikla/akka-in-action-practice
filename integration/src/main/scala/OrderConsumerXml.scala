import akka.actor.ActorRef
import akka.camel.CamelMessage
import scala.xml._

class OrderConsumerXml(uri: String, next: ActorRef) extends akka.camel.Consumer {

  override def endpointUri: String = uri

  override def receive: Receive = {
    case msg: CamelMessage =>
      val content = msg.bodyAs[String]
      val xml = XML.loadString(content)
      val order = xml \\ "order"
      val customerId = (order \\ "customerId").text
      val productId = (order \\ "productId").text
      val number = (order \\ "number").text.toInt
      next ! Order(customerId, productId, number)
  }

}
