package com.kompreneble.parquetexample.jsontoparquet

import fs2.Stream
import cats.effect.IO
import cats.effect.std.Dispatcher
import org.legogroup.woof.{*, given}
import org.legogroup.woof.slf4j2.*


object logging {

  given Filter =
    Filter.regexFilter("com.kompreneble.parquetexample.*".r).and(Filter.atLeastLevel(LogLevel.Debug))
    .or(
      Filter.regexFilter("org.apache.kafka.*".r).and(Filter.atLeastLevel(LogLevel.Warn))
    )

  given Printer = ColorPrinter()

  def slf4jWoofLogger: Stream[IO, DefaultLogger[IO]] = for {

    logger <- Stream.eval(DefaultLogger.makeIo(Output.fromConsole[IO]))

    dispatcher <- Stream.resource(Dispatcher.sequential[IO])

    _ <- Stream.eval(logger.registerSlf4j(using dispatcher))

  } yield logger
}
