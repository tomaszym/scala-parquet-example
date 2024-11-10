package com.kompreneble.parquetexample.jsontoparquet

import cats.effect.{IO, IOApp}
import fs2.{Pipe, Stream}
import com.github.mjakubowski84.parquet4s.{BinaryValue, InMemoryOutputFile, LogicalTypes, ParquetSchemaResolver, ParquetWriter, SchemaDef, TypedSchemaDef, Value, ValueCodec, ValueCodecConfiguration}
import com.github.mjakubowski84.parquet4s.parquet.*
import com.github.plokhotnyuk.jsoniter_scala.core.readFromString
import com.kompreneble.parquetexample.core.{RootDevice, SubDevice}
import com.kompreneble.parquetexample.core.SubDevice.SubDeviceMeasurements
import fs2.kafka.KafkaConsumer
import org.apache.parquet.schema.{LogicalTypeAnnotation, PrimitiveType}
import uuid.given
import java.nio.ByteBuffer
import java.util.UUID
import org.legogroup.woof.{*, given}
import scala.concurrent.duration.given
import cats.effect.std.Dispatcher

object JsonToParquetMain extends IOApp.Simple {
  override def run: IO[Unit] = {

    val S: fs2.Stream[IO, Unit] = for {

      logger <- logging.slf4jWoofLogger

      consumer <- kafka.consumer
      assigned <- consumer.assignmentStream

      _ <- Stream.eval(logger.debug(s"consumer created with topics ${assigned}"))

      files = consumer.partitionedStream.map { partitionStream =>
        partitionStream.map(r => readFromString[RootDevice](r.record.value)).through(
          pipeToBytes
        )
      }.parJoinUnbounded

      _ <- files.evalMap(bytes => IO.println(bytes.length))

    } yield ()

    S.compile.drain
  }


  def pipeToBytes: Pipe[IO, RootDevice, Array[Byte]] = { (in: Stream[IO, RootDevice]) =>
    val buffer = InMemoryOutputFile(4000*1000)
    println("buffer ready")
    in
      .chunkN(80)
//      .prefetchN(2)
      .evalMap { bigChunk =>
        Stream.chunk(bigChunk).through(
          writeSingleFile[IO].of[RootDevice].write(buffer)
        ).compile.drain *> IO.println("flushing") *> IO.delay(buffer.take())
      }
    }

}
