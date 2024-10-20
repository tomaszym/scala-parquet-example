package com.kompreneble.parquetexample.gen

import magnolia1.{AutoDerivation, CaseClass, Monadic, ProductDerivation, SealedTrait}
import org.scalacheck.{Arbitrary, Gen}

object ArbitraryProductDerivation extends ProductDerivation[Arbitrary] {

  given genMonadic: Monadic[Gen] = new Monadic[Gen] {

    override def point[A](value: A): Gen[A] = Gen.const(value)

    override def map[A, B](from: Gen[A])(fn: A => B): Gen[B] = from.map(fn)

    override def flatMap[A, B](from: Gen[A])(fn: A => Gen[B]): Gen[B] = from.flatMap(fn)
  }

  override def join[T](
    caseClass: CaseClass[ArbitraryProductDerivation.Typeclass, T]
  ): ArbitraryProductDerivation.Typeclass[T] = Arbitrary {
    caseClass.constructMonadic { param =>
      param.typeclass.arbitrary
    }
  }
}