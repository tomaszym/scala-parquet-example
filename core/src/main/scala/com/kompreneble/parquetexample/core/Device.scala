package com.kompreneble.parquetexample.core

import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.kompreneble.parquetexample.core.Device.DeviceMeasurements

import java.util.UUID

case class Device(
  deviceId: UUID,
  measurements: DeviceMeasurements,
  sub: Seq[SubDevice],
)
object Device {

  case class DeviceMeasurements(
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

  given codec: JsonValueCodec[Device] = JsonCodecMaker.make
  given measurementsCodec: JsonValueCodec[DeviceMeasurements] = JsonCodecMaker.make

}
