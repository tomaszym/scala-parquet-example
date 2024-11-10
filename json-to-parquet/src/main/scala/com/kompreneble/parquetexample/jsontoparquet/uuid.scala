package com.kompreneble.parquetexample.jsontoparquet

import com.github.mjakubowski84.parquet4s.{BinaryValue, ParquetWriter, SchemaDef, TypedSchemaDef, Value, ValueCodec, ValueCodecConfiguration}
import com.kompreneble.parquetexample.core.SubDevice
import org.apache.parquet.schema.{LogicalTypeAnnotation, PrimitiveType}

import java.nio.ByteBuffer
import java.util.UUID

object uuid {
  given uuidCodec: ValueCodec[UUID] =
    new ValueCodec[UUID] {
      override def decode(value: Value, configuration: ValueCodecConfiguration): UUID =
        value match {
          case binary: BinaryValue => uuidFromBytes(binary.value.getBytes)
        }

      override def encode(data: UUID, configuration: ValueCodecConfiguration): Value =
        BinaryValue(uuidToBytes(data))
    }

  given uuidSchema: TypedSchemaDef[UUID] =
    SchemaDef.primitive(
      primitiveType = PrimitiveType.PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY,
      length = Some(16),
      required = false,
      logicalTypeAnnotation = Option(LogicalTypeAnnotation.uuidType()),
    ).typed[UUID]

  def uuidFromBytes(bytes: Array[Byte]): UUID = {
    require(bytes.length == 16, "A UUID requires a 16-byte array")

    // Wrap the byte array in a ByteBuffer to read two longs
    val buffer = ByteBuffer.wrap(bytes)
    val mostSignificantBits = buffer.getLong
    val leastSignificantBits = buffer.getLong

    // Construct the UUID
    new UUID(mostSignificantBits, leastSignificantBits)
  }

  def uuidToBytes(uuid: UUID): Array[Byte] = {
    val buffer = ByteBuffer.allocate(16) // Allocate 16 bytes for the UUID
    buffer.putLong(uuid.getMostSignificantBits) // Write the most significant bits
    buffer.putLong(uuid.getLeastSignificantBits) // Write the least significant bits
    buffer.array() // Convert ByteBuffer to Array[Byte]
  }
}
