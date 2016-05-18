package org.pitest.scalatest

import org.scalatest.Reporter
import org.scalatest.events.{Event, SuiteAborted, TestCanceled, TestFailed}

class PitReporter extends Reporter {

  var message = ""

  override def apply(event: Event) = {
    event match {
      case e: TestFailed => fail(event)
      case e: TestCanceled => fail(event)
      case e: SuiteAborted => fail(event)
      case _ =>
    }
  }

  private def fail(event: Event) = {
    message = event.toString
  }
}
