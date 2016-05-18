package org.pitest.scalatest

import org.pitest.testapi.{AbstractTestUnit, Description, ResultCollector}
import org.pitest.util.IsolationUtils
import org.scalatest._

class ScalaTestUnit(clazz: Class[_]) extends AbstractTestUnit(new Description(clazz.getName, clazz)) {
  override def execute(loader: ClassLoader, rc: ResultCollector) = {
    val suite = clazz.newInstance().asInstanceOf[Suite]

    assume(!IsolationUtils.loaderDetectionStrategy.fromDifferentLoader(clazz, loader))

    rc.notifyStart(getDescription)
    try {
      val reporter = new PitReporter()
      val status = suite.run(
        None,
        reporter,
        Stopper.default,
        Filter(),
        Map(),
        Option.empty,
        new Tracker()
      )
      status.waitUntilCompleted()
      if (!status.succeeds()) throw new RuntimeException(reporter.message)
      rc.notifyEnd(getDescription)
    } catch {
      case t: Throwable => rc.notifyEnd(getDescription, t)
    }
  }
}
