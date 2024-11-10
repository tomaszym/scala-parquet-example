package com.kompreneble.parquetexample.gen

import com.kompreneble.parquetexample.core.{Device, RootDevice, SubDevice}
import org.scalacheck.{Arbitrary, Gen}
import cats.effect.IO
import com.kompreneble.parquetexample.core.Device.DeviceMeasurements
import com.kompreneble.parquetexample.core.RootDevice.RootDeviceMeasurements
import com.kompreneble.parquetexample.core.SubDevice.SubDeviceMeasurements
import fs2.Chunk
import org.scalacheck.rng.Seed

object GenerateMeasurements {

  import Arbitrary.arbUuid
  import Arbitrary.arbDouble
  import Arbitrary.arbInt
  import Arbitrary.arbLong


  import ArbitraryProductDerivation.given

  lazy val rootGen = for {
    id <- Gen.uuid
    measurements <- summon[Arbitrary[RootDeviceMeasurements]].arbitrary
    number <- Gen.chooseNum(4, 30)
    dev <- Gen.listOfN(number, devGen)
  } yield RootDevice(id, measurements = measurements, devices = dev)

  lazy val devGen = for {
    id <- Gen.uuid
    measurements <- summon[Arbitrary[DeviceMeasurements]].arbitrary
    number <- Gen.chooseNum(4, 30)
    sub <- Gen.listOfN(number, subGen)
  } yield Device(id, measurements, sub)

  lazy val subGen = for {
    id <- Gen.uuid
    measurements <- summon[Arbitrary[SubDeviceMeasurements]].arbitrary
  } yield SubDevice(id, measurements)


  private val first: (Seed, RootDevice) = Seed(1L) -> rootGen.pureApply(Gen.Parameters.default, Seed(1L))

  def rootDevice: fs2.Stream[IO, RootDevice] = fs2.Stream.iterate(first) { case (seed, chunks) =>
    val nextSeed = seed.next
    rootGen.pureApply(Gen.Parameters.default, nextSeed)
    nextSeed -> rootGen.pureApply(Gen.Parameters.default, nextSeed)
  }.map(_._2)


}
