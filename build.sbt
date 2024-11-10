import Dependencies._

ThisBuild / scalaVersion     := "3.5.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.kompreneble"
ThisBuild / organizationName := "kompreneble"
ThisBuild / libraryDependencySchemes += "com.github.luben" % "zstd-jni" % "early-semver"

lazy val root = (project in file("."))
  .settings(
    name := "scala-parquet-example",
  )
  .aggregate(genJson, jsonToParquet, parquetToDb)

lazy val core = project
  .in(file("core"))
  .settings(
    libraryDependencies ++= Seq(
      catsEffect,
      fs2kafka,
      jsonniter,
      "org.legogroup" %% "woof-core" % "0.7.0",
      "org.legogroup" %% "woof-slf4j-2" % "0.7.0",
        jsonniterMacros % Compile,
      "org.scalacheck" %% "scalacheck" % "1.17.1",
      munit % Test,
    )
  )

lazy val genJson = project
  .dependsOn(core)
  .in(file("gen"))
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.magnolia1_3" %% "magnolia" % "1.3.7",
    )
  )

lazy val jsonToParquet = project
  .dependsOn(core)
  .in(file("json-to-parquet"))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.parquet" % "parquet-hadoop" % "1.14.1",
      "org.apache.parquet" % "parquet-column" % "1.14.1",
      "org.apache.hadoop" % "hadoop-client" % "3.4.0",
      "com.github.mjakubowski84" %% "parquet4s-fs2" % "2.19.0",
      "com.github.mjakubowski84" %% "parquet4s-core" % "2.19.0",
    )
  )

lazy val parquetToDb = project
  .dependsOn(core)
  .in(file("parquet-to-db"))
  .settings(
    libraryDependencies ++= Seq(

    )
  )
