package com.wix.test

import org.specs2.concurrent.ExecutionEnv

trait ExecutionEnvSupport {
  implicit val executionEnv = ExecutionEnv.fromGlobalExecutionContext
}
