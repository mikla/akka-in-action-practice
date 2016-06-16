import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.FlatSpecLike

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Success

class CalculatorSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike {

  implicit val timeout = Timeout(5 seconds)

  "The Calculator" should "recover after crash" in {

    val calc = system.actorOf(Props[Calculator], "calc")
    calc ! Add(1)
    calc ! Add(1)
    calc ! Add(1)

    (calc ask GetResult).andThen {
      case Success(v) => println(s"calc: $v")
      case _ => println("err from calc")
    } andThen {
      case _ =>
        system.stop(calc)
    }

    val recoveredCalc = system.actorOf(Props[Calculator], "calc2")
    recoveredCalc ! Multiply(2)

    (recoveredCalc ask GetResult).andThen {
      case Success(v) => println(s"recovered $v")
      case _ => println("err2")
    }

  }

  it should "recover from snapshot after clear command" in {
    val calc = system.actorOf(Props[Calculator], "calc-snapshot")
    calc ! Add(1)
    calc ! Add(1)
    calc ! Add(1)
    calc ! Clear
    calc ! Add(2)

    Thread.sleep(1000)

    system.stop(calc)

    val snapshotCalc = system.actorOf(Props[Calculator], "calc-snapshot2")

    val res = snapshotCalc ? GetResult

    res.andThen {
      case Success(v) => println(v)
      case _ => println("err")
    }

    Await.result(res, Duration(30, "seconds"))
  }

}
