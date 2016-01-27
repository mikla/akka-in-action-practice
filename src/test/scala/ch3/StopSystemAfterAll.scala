package ch3

import akka.testkit.TestKit
import org.scalatest.{Suite, BeforeAndAfterAll}

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit
    with Suite =>

  override protected def afterAll() = {
    super.afterAll()
    system.shutdown()
  }

}
