package com.kompreneble.parquetexample.gen

import cats.effect.{IO, IOApp}
import com.kompreneble.parquetexample.core.RootDevice
import fs2.*
import fs2.kafka.*
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow
import com.github.plokhotnyuk.jsoniter_scala.core.writeToString

object GenerateMeasurementsMain extends IOApp.Simple {

  val run: IO[Unit] = {

    val topic = "scala-parquet-example.source"

    val producerSettings =
      ProducerSettings[IO, String, String].withBootstrapServers("localhost:9092")

    def processRecords(
      producer: KafkaProducer[IO, String, String]
    )(records: Chunk[ConsumerRecord[String, String]]): IO[CommitNow] = {
      val producerRecords = records
        .map(consumerRecord => ProducerRecord("topic", consumerRecord.key, consumerRecord.value))
      producer.produce(producerRecords).flatten.as(CommitNow)
    }

    val S = for {
      producer <- KafkaProducer
        .stream(producerSettings)

      _ <- Stream.eval(IO.println(s"producer created"))

      partitions <- Stream.eval(producer.partitionsFor(topic))

      _ <- Stream.eval(IO.println(s"partitions: $partitions"))

      _ <- GenerateMeasurements.rootDevice
//        .metered(1.millisecond)
        .take(100)
        .chunks
        .evalMap { chunk =>
         producer.produce(ProducerRecords(
           chunk.map { root =>
             ProducerRecord(topic, root.rootId.toString, writeToString(root))
           }
         ))
      }

    } yield ()

    S.compile.drain
  }


}
