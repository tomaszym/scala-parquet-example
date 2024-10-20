import Dependencies._

ThisBuild / scalaVersion     := "3.5.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.kompreneble"
ThisBuild / organizationName := "kompreneble"


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
      jsonniterMacros % Compile,
      "com.softwaremill.magnolia1_3" %% "magnolia" % "1.3.7",
      "org.scalacheck" %% "scalacheck" % "1.17.1",
      munit % Test,
    )
  )

lazy val genJson = project
  .dependsOn(core)
  .in(file("gen"))
  .settings(
    libraryDependencies ++= Seq(

    )
  )

lazy val jsonToParquet = project
  .dependsOn(core)
  .in(file("json-to-parquet"))
  .settings(
    libraryDependencies ++= Seq(

    )
  )

lazy val parquetToDb = project
  .dependsOn(core)
  .in(file("parquet-to-db"))
  .settings(
    libraryDependencies ++= Seq(

    )
  )
