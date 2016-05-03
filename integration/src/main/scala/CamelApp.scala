import akka.actor.ActorSystem
import akka.camel.CamelExtension

object CamelApp extends App {

  val system = ActorSystem("system")
  val camelExt = CamelExtension(system)

	
}