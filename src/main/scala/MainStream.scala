import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.functions.{col, split}
import org.apache.spark.streaming.kafka010._

object MainStream extends SparkSessionBase {

  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println(s"""
                            |  <brokers> is a list of one or more Kafka brokers
                            |  <groupId> is a consumer group name to consume from topics
                            |  <topics> is a list of one or more kafka topics to consume from
        """.stripMargin)
      System.exit(1)
    }

    val Array(brokers, groupId, topics) = args


    import sparkSession.implicits._


    /** Create kafka stream*/
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ->  brokers,
      ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG -> "30000",
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false",
      ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG -> "10000",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])


    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

    /** Set the column names*/
    val columns = Array("ROW","VAL1","VAL2","VAL3","VAL4")

    messages.foreachRDD(rdd => {
      if (!rdd.partitions.isEmpty){
        rdd.map( msg => {
          msg.value()
        }).toDF("columns").withColumn("temp",split(col("columns"), ","))
          .select((0 to columns.length -1).map( i => col("temp").getItem(i).as(columns(i))): _*).show()
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
