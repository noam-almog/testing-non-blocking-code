package com.wix.test

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Await, Future}
import scala.concurrent.duration._

class InTheWildTest extends SpecificationWithJUnit {

  trait ctx extends Scope {

    val fileServer = new {

      private val files = mutable.LinkedHashSet.empty[(String, String)]

      private def matchFilesWith(regex: String) =
        files.map(_._1)
             .filter( _.matches(regex) )
             .toSet

      def getFiles(count: Int, regex: String): Set[String] =
        if (matchFilesWith(regex).size < count) {
          Thread.sleep(100)
          getFiles(count, regex)
        } else
          matchFilesWith(regex).take(count)

      def storeFile(file: String, content: String) {
        files += file -> content
      }
    }
  }


  "InTheWild" should {

    "continue only after it got the files" in new ctx {
      import ExecutionContext.Implicits.global

      val futureFileNames = Future {
        fileServer.getFiles(2, "file.*")
      }

      Future {
        fileServer.storeFile("folder1", "content")
        Thread.sleep(100)
        fileServer.storeFile("file1", "content")
        Thread.sleep(100)
        fileServer.storeFile("folder2", "content")
        Thread.sleep(100)
        fileServer.storeFile("file2", "content")
      }


      Await.result(futureFileNames, atMost = 5.seconds) must containAllOf(Seq("file1", "file2"))
    }
  }
}