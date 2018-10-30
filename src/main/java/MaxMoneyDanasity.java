import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by yangmingquan on 2018/10/30.
 */
public class MaxMoneyDanasity {

    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args){
        SparkConf sparkConf = new SparkConf().setAppName("MaxMoneyDanasity");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = javaSparkContext.textFile(args[0], 1);

        JavaRDD<String> moneys = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(SPACE.split(s)).iterator();
            }
        });

        JavaPairRDD<String, Integer> ones = moneys.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        List<Tuple2<String, Integer>> output = counts.collect();
        for(Tuple2<String, Integer> tuple2 : output){
            System.out.println(tuple2._1 + ":" + tuple2._2());
        }

        javaSparkContext.stop();
    }
}
