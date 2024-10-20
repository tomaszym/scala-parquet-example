package com.kompreneble.parquetexample.core

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import com.kompreneble.parquetexample.core.SubDevice.SubDeviceMeasurements

import java.util.UUID

case class SubDevice(
  subDeviceId: UUID,
  measurements: SubDeviceMeasurements,
)


object SubDevice {

  case class SubDeviceMeasurements(
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
  
  given codec: JsonValueCodec[SubDevice] = JsonCodecMaker.make
  given measurementsCodec: JsonValueCodec[SubDeviceMeasurements] = JsonCodecMaker.make
}
