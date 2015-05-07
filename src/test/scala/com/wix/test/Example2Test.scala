package com.wix.test

import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

import scala.concurrent.Future


class Example2Test extends SpecificationWithJUnit with Mockito {

  val Pong = "pong"
  
  trait CallableForTest {
    def ping: String
    def sample: Future[String]
  }

  trait ctx extends Scope with ExecutionEnvSupport {

    val callable = mock[CallableForTest]

    val unitUnderTest = new {

      def triggerSomething() {
        Future {
          Thread.sleep(50)
          callable.ping
        }
      }

      def triggerSomethingElse: Future[String] = callable.sample
    }


  }


  "Example2" should {

    "test another async code" in new ctx {

      unitUnderTest.triggerSomething()

      got {
        one(callable).ping
      }
    }

    "testing sample" in new ctx {
      callable.sample returns Future("something") thenReturns Future(Pong)

      unitUnderTest.triggerSomethingElse must beTypedEqualTo(Pong).await
    }
  }
}

