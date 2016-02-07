import akka.actor._
import com.typesafe.config._

object Frontend extends App {

  val system = ActorSystem("frontend", config)

  val simpleRef = system.actorSelection("akka.tcp://backend@0.0.0.0:2551/user/simple")

  simpleRef ! "message"

  private lazy val config = ConfigFactory.load("frontend")

}

