import org.typelevel.sbt.tpolecat.*

ThisBuild / organization := "hotwheels"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
  name := "hotwheels",
  Docker / packageName := "hotwheels-app",
  dockerBaseImage := "eclipse-temurin:11-jre-jammy",
  dockerExposedPorts ++= Seq(8080),
  dockerUpdateLatest := true,
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % "3.5.3",
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % "3.5.3",
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % "3.5.3",
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test,

    // Compiler plugins
    "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full,
    "com.olegpy" %% "better-monadic-for" % "0.3.1",
//    "org.scalameta" % "semanticdb-scalac" % "4.5.8" cross CrossVersion.full,

//    // Functional libraries
//    //    "org.typelevel" %% "cats-core" % "2.7.0",
//    //    "org.typelevel" %% "cats-effect" % "3.3.12",
//
    // Circe JSON
    "io.circe" %% "circe-core" % "0.14.2",
    "io.circe" %% "circe-generic" % "0.14.2",
    "io.circe" %% "circe-parser" % "0.14.2",
    "io.circe" %% "circe-refined" % "0.14.2",

    // Config
    "is.cir" %% "ciris" % "2.3.2",
    "is.cir" %% "ciris-enumeratum" % "2.3.2",
    "is.cir" %% "ciris-refined" % "2.3.2",

//    // Typeclass derivation
//    //    "tf.tofu" %% "derevo-core" % "0.13.0",
//    //    "tf.tofu" %% "derevo-cats" % "0.13.0",
//    //    "tf.tofu" %% "derevo-circe" % "0.13.0",
//
//    // Streaming
//    //    "co.fs2" %% "fs2-core" % "3.1.3",
//
    // HTTP4s
    "org.http4s" %% "http4s-dsl" % "0.23.1",
    "org.http4s" %% "http4s-ember-server" % "0.23.1",
    "org.http4s" %% "http4s-ember-client" % "0.23.1",
    "org.http4s" %% "http4s-circe" % "0.23.1",
    "dev.profunktor" %% "http4s-jwt-auth" % "1.0.0",

    // Logging
    "org.typelevel" %% "log4cats-core" % "2.3.1",
    "ch.qos.logback" % "logback-classic" % "1.2.11" % Runtime,

    // Optics
//    "dev.optics" %% "monocle-core" % "3.1.0",
//    "dev.optics" %% "monocle-law" % "3.1.0" % Test,

    // Newtype
    //    "io.estatico" %% "newtype" % "0.4.4",

    // Refined
    //    "eu.timepit" %% "refined" % "0.9.29",
    //    "eu.timepit" %% "refined-cats" % "0.9.29",
    //    "eu.timepit" %% "refined-scalacheck" % "0.9.29" % Test,

    // Database
    "org.tpolecat" %% "skunk-core" % "0.3.1",
    "org.tpolecat" %% "skunk-circe" % "0.3.1",

    // Units
    "org.typelevel" %% "squants" % "1.8.3",

    // Testing
    "org.typelevel" %% "cats-laws" % "2.7.0" % Test,
    "org.typelevel" %% "log4cats-noop" % "2.3.1" % Test,
    "com.disneystreaming" %% "weaver-cats" % "0.7.12" % Test,
    "com.disneystreaming" %% "weaver-discipline" % "0.7.12" % Test,
    "com.disneystreaming" %% "weaver-scalacheck" % "0.7.12" % Test
  ),
  scalacOptions += "-Ymacro-annotations")
