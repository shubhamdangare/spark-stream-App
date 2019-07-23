name := "Stream-App"

version := "0.1"

scalaVersion := "2.11.0"


lazy val root = (project in file("."))
  .settings(
    name := "root",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "2.4.3",
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.3" % "provided",
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.3",
      "org.apache.kafka" % "kafka-clients" % "2.3.0"
    )
  )