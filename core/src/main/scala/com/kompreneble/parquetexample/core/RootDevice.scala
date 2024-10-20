package com.kompreneble.parquetexample.core

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import com.kompreneble.parquetexample.core.RootDevice.RootDeviceMeasurements

import java.util.UUID

case class RootDevice(
  rootId: UUID,
  measurements: RootDeviceMeasurements,
  devices: Seq[Device],
)

object RootDevice {

  case class RootDeviceMeasurements(
    a: Double,
    b: Double,
    c: Double,
    d: Double,
    e: Double,
    f: Double,
    g: Int,
    h: Int,
    i: Int,
    j: Int,
    k: Int,
    l: Long,
    m: Long,
    n: Long,
    o: Long,
    p: Long,
  )

  given codec: JsonValueCodec[RootDevice] = JsonCodecMaker.make
  given measurementCodec: JsonValueCodec[RootDeviceMeasurements] = JsonCodecMaker.make
}
