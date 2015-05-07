package com.wix.test

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Example1Test extends SpecificationWithJUnit {

  trait UnitUnderTest {
    def doSomething(msg: String): Future[String]
  }

  trait ctx extends Scope {
    val Ping = "ping"

    val unitUnderTest = new {
      def doSomething(echo: String): Future[String] = Future(echo) 
    }
  }


  "Example1" should {

    "match against a future value" in new ctx with ExecutionEnvSupport {
      unitUnderTest.doSomething(Ping) must beTypedEqualTo(Ping)
    }
  }
}

