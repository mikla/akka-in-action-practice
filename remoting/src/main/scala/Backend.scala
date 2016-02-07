import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

object Backend extends App {

  val system = ActorSystem("backend", config)

  val simpleRef = system.actorOf(Props[SimpleActor], "simple")

  private lazy val config = ConfigFactory.load("backend")

}
