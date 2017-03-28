package demo.spark;

import java.util.Arrays;

import junit.framework.Test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class HelloSpark {

	public static void test() {
		// 创建一个Java版本的Spark Context
		SparkConf conf = new SparkConf().setMaster("local").setAppName(
				"wordCount");
		JavaSparkContext sc = new JavaSparkContext(conf);
		// 读取我们的输入数据
		JavaRDD<String> input = sc.textFile("README.md");
		// 切分为单词
		JavaRDD<String> words = input
				.flatMap(new FlatMapFunction<String, String>() {
					public Iterable<String> call(String x) {
						return Arrays.asList(x.split(" "));
					}
				});
		// 转换为键值对并计数
		JavaPairRDD<String, Integer> counts = words.mapToPair(
				new PairFunction<String, String, Integer>() {
					public Tuple2<String, Integer> call(String x) {
						return new Tuple2(x, 1);
					}
				}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer x, Integer y) {
				return x + y;
			}
		});
		// 将统计出来的单词总数存入一个文本文件，引发求值
		counts.saveAsTextFile("E:\\READMECount.md");
	}

	public static void main(String[] args) {
		test();
	}
}
