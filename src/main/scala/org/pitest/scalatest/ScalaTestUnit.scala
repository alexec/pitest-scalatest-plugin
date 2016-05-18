package org.pitest.scalatest

import org.pitest.testapi.{AbstractTestUnit, Description, ResultCollector}
import org.pitest.util.IsolationUtils
import org.scalatest._
import org.scalatest.events._

class ScalaTestUnit(clazz: Class[_]) extends AbstractTestUnit(new Description(clazz.getName, clazz)) {
  override def execute(loader: ClassLoader, rc: ResultCollector) = {
    val suite = clazz.newInstance().asInstanceOf[Suite]

    assert(!IsolationUtils.loaderDetectionStrategy.fromDifferentLoader(clazz, loader))

    rc.notifyStart(getDescription)
    try {
      suite.run(
        None,
        new Reporter {
          override def apply(event: Event): Unit = {
            event match {
              case e: TestFailed => throw new AssertionError(event.toString)
              case e: TestCanceled => throw new AssertionError(event.toString)
              case e: SuiteAborted => throw new AssertionError(event.toString)
              case _ =>
            }
          }
        },
        Stopper.default,
        Filter(),
        Map(),
        Option.empty,
        new Tracker()
      )
      rc.notifyEnd(getDescription)
    } catch {
      case t: Throwable => rc.notifyEnd(getDescription, t)
    }
  }
}
