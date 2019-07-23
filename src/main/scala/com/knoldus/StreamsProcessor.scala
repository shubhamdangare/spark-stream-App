package com.knoldus

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{Dataset, SparkSession}

object StreamsProcessor {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("Spark-Kafka-Integration")
      .master("local")
      .getOrCreate()

    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9093")
      .option("subscribe", "test-sqlite-jdbc-accounts")
      .option("startingOffsets", "earliest")
      .load()
    val dataSet: Dataset[(String, String)] = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]

    df.printSchema()

    val ds = df
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9093")
      .option("truncate", false)
      .option("checkpointLocation", "/tmp/checkpoint")
      .option("topic", "write-topic")
      .start()
      .awaitTermination()

    val query: StreamingQuery = dataSet.writeStream
      .outputMode("append")
      .format("console")
      .start()
    query.awaitTermination()
    spark.stop()

  }
}