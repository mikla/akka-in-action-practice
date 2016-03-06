import akka.actor.{Actor, ActorLogging}

class HelloWorld extends Actor with ActorLogging {

  override def receive: Receive = {
    case msg: String =>
      val hello = s"Hello $msg"
      sender() ! hello
      log.info("Sent response {}", hello)
  }

}
