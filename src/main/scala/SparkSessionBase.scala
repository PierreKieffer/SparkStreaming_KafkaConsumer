import org.apache.spark.streaming.{Seconds, StreamingContext}

trait SparkSessionBase {

  val sparkSession = org.apache.spark.sql.SparkSession.builder
    .master("local[*]")
    .appName("DirectStreamKafka")

    .getOrCreate()



  /** Create spark streaming context with 2 seconds batch interval*/
  val sc = sparkSession.sparkContext
  val ssc = new StreamingContext(sc, Seconds(2))

}
