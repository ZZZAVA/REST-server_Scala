name := "REST-server_Scala"

version := "0.1"

scalaVersion := "2.13.6"


val Http4sVersion = "0.23.16"
val CirceVersion = "0.14.3"
// https://mvnrepository.com/artifact/org.http4s/http4s-blaze-server
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.23.11"
libraryDependencies += "org.http4s" %% "http4s-core" % "0.23.11"
// https://mvnrepository.com/artifact/org.http4s/http4s-circe
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.23.11"
// https://mvnrepository.com/artifact/org.http4s/http4s-blaze-client
libraryDependencies += "org.http4s" %% "http4s-blaze-client" % "0.23.11"
// https://mvnrepository.com/artifact/org.http4s/http4s-blaze-server
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.23.11"
// https://mvnrepository.com/artifact/org.http4s/http4s-client
libraryDependencies += "org.http4s" %% "http4s-client" % "0.23.11"
// https://mvnrepository.com/artifact/org.http4s/http4s-server
libraryDependencies += "org.http4s" %% "http4s-server" % "0.23.11"
// https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-http4s-server
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.20.2"
// https://mvnrepository.com/artifact/io.janstenpickle/trace4cats-sttp-client3
libraryDependencies += "io.janstenpickle" %% "trace4cats-sttp-client3" % "0.14.0"
libraryDependencies += "io.circe"        %% "circe-generic" %"0.14.3"

/*
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.20"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.20"
libraryDependencies += "com.fizzed" % "blaze-core" % "0.21.0" % "provided"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.21.20"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.21.20"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % "0.20.2"*/
