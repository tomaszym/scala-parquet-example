package com.kompreneble.parquetexample.jsontoparquet

import cats.effect.IO
import com.kompreneble.parquetexample.core.RootDevice
import fs2.{Chunk, Stream}
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow
import fs2.kafka.{AutoOffsetReset, ConsumerRecord, ConsumerSettings, KafkaConsumer}
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*

object kafka {
  val topic = "scala-parquet-example"
  private val consumerSettings = ConsumerSettings[IO, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withBootstrapServers("localhost:9092")
      .withGroupId("json-to-parquet")
//      .withMaxPollRecords(50000)

  def consumer: fs2.Stream[IO, KafkaConsumer[IO, String, String]] =  fs2.Stream.resource(
    KafkaConsumer
      .resource[IO, String, String](consumerSettings)
      .evalTap(_.subscribeTo(topic))
  )

//  def consumeChunk(
//    process: Chunk[RootDevice] => IO[CommitNow]
//  ): fs2.Stream[IO, KafkaConsumer[IO, String, String]] = for {
//    consumer <- consumer
//
//    records <- Stream.eval(consumer.consumeChunk(
//      records =>
//        process(
//          records.map { record =>
////            println(record)
//            readFromString[RootDevice](record.value)
//          }
//        )
//    ))
//
//    records <- consumer.partitionedStream
//      .map { partitionStream =>
//        partitionStream.chunks.evalMap { chunk =>
//          chunk.map(record => readFromString[RootDevice](record.record.value) -> record.offset)
//        }
//      }
//      .parJoinUnbounded
//
//  } yield consumer

}
