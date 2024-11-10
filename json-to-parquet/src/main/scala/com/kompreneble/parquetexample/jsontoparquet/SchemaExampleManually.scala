package com.kompreneble.parquetexample.jsontoparquet

import org.apache.parquet.schema.{LogicalTypeAnnotation, MessageType, Types}
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition.REQUIRED

object SchemaExampleManually {

  LogicalTypeAnnotation.uuidType()
  val schema: MessageType =
    Types.buildMessage()
      .addField(Types.primitive(PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY, REQUIRED).length(16).as(LogicalTypeAnnotation.uuidType()).named("id"))
      .addField(Types.primitive(PrimitiveTypeName.INT32, REQUIRED).named("one"))
      .addField(Types.primitive(PrimitiveTypeName.DOUBLE, REQUIRED).named("two"))
      .named("A")

}

