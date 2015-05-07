package com.wix.test

import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

import scala.concurrent.Future


class Example2Test extends SpecificationWithJUnit with Mockito {

  val Pong = "pong"
  
  trait CallableForTest {
    def ping: Future[String]
  }



  trait ctx extends Scope with ExecutionEnvSupport {

    val callable = mock[CallableForTest]

    val unitUnderTest = new {

      def triggerSomething: Future[String] = callable.ping
      def triggerSomethingElse() {
        Future {
          Thread.sleep(50)
          triggerSomething
        }
      }
    }


    def givenCallablePonging() {
      callable.ping returns Future.successful( Pong )
    }
  }


  "Example2" should {

    "test another async code" in new ctx {
      unitUnderTest.triggerSomethingElse()

      got {
        one(callable).ping
      }
    }
  }
}

