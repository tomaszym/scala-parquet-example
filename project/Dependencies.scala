import sbt._

object Dependencies {
  lazy val munit = "org.scalameta" %% "munit" % "1.0.0"
  lazy val fs2kafka =  "com.github.fd4s" %% "fs2-kafka" % "3.5.1"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.4"
  lazy val jsonniter = "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.31.0"
  lazy val jsonniterMacros = "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.31.0"
}
