package com.wix.test

import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class Example1Test extends SpecificationWithJUnit with Mockito {

  trait UnitUnderTest {
    def doSomething(msg: String): Future[String]
  }

  trait ctx extends Scope {
    val someStr = "someRandomString"

    val unitUnderTest = mock[UnitUnderTest]

    def givenEchoUnitUnderTestWith(echo: String) {
      import ExecutionContext.Implicits.global
      unitUnderTest.doSomething(echo) returns Future(echo)
    }
  }


  "Example1" should {

    "match against a future value" in new ctx with ExecutionEnvSupport {
      givenEchoUnitUnderTestWith(echo = someStr)

      Await.result( unitUnderTest.doSomething(someStr), 5.seconds) must beTypedEqualTo(someStr)

      unitUnderTest.doSomething(someStr) must beTypedEqualTo(someStr).await
    }
  }
}

