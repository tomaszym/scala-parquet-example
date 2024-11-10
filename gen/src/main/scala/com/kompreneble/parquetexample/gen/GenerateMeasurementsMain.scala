package com.kompreneble.parquetexample.gen

import cats.effect.{IO, IOApp}
import com.kompreneble.parquetexample.core.RootDevice
import fs2.*
import fs2.kafka.*
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow
import com.github.plokhotnyuk.jsoniter_scala.core.writeToString

object GenerateMeasurementsMain extends IOApp.Simple {

  val run: IO[Unit] = {

    val topic = "scala-parquet-example"

    val producerSettings =
      ProducerSettings[IO, String, String].withBootstrapServers("localhost:9092")

    val S = for {
      producer <- KafkaProducer
        .stream(producerSettings)

      _ <- Stream.eval(IO.println(s"producer created"))

      partitions <- Stream.eval(producer.partitionsFor(topic))

      _ <- Stream.eval(IO.println(s"partitions: $partitions"))

      _ <- GenerateMeasurements.rootDevice
//        .metered(1.millisecond)
        .take(10000)
        .chunkN(100)
        .evalMap { chunk =>
         producer.produce(ProducerRecords(
           chunk.map { root =>
             ProducerRecord(topic, root.rootId.toString, writeToString(root))
           }
         )).flatTap(_ => IO.println("chunk!"))
      }

    } yield ()

    S.compile.drain
  }


}
