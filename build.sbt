import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

enablePlugins(JavaAppPackaging)

lazy val akkaHttpVersion = "10.0.9"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "io.github.dlinov",
      scalaVersion    := "2.12.3"
    )),
    name := "sp-hackathon",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"      %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka"      %% "akka-http-spray-json" % akkaHttpVersion,
      "org.mongodb.scala"      %% "mongo-scala-driver"   % "2.1.0",
      "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0",
      "org.scalaz" %% "scalaz-core" % "7.2.15",
      "com.typesafe.akka"      %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "org.scalatest"          %% "scalatest"            % "3.0.1"         % Test
    )
  )
