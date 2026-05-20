// package src.main.java;
// Note: Trong file ParallelSeminarDemo.java, vì để file ngay trong thư mục java mà không tạo thêm thư mục con (như com/example/...), nên dòng đầu tiên của file không được có lệnh package ...;.
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.hadoop.fs.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.security.MessageDigest;

public class ParallelSeminarDemo {
    public static void main(String[] args) throws Exception{
        System.setProperty("hadoop.home.dir", "/");
        String filePath = "review_philadelphia_500rows.parquet";

        System.out.println("Loading data from Parquet file");
        List<String> reviews = loadParquet(filePath);
        System.out.println("Number of reviews: " + reviews.size());

        runTest(reviews, false); // Sequential
        runTest(reviews, true); // Parallel
    }

    public static void runTest(List<String> data, boolean isParallel){
        System.out.println("Running: " + (isParallel ? "PARALLEL" : "SEQUENTIAL"));
        Map<String, LongAdder> threadMetrics = new ConcurrentHashMap<>();
        long startTime = System.currentTimeMillis();

        var stream = isParallel ? data.parallelStream() : data.stream();

        stream.forEach(review -> {
            // Take current running thread
            String tName = Thread.currentThread().getName();
            threadMetrics.computeIfAbsent(tName, k -> new LongAdder()).increment();

            // job 01 + 02: normalize word and count word
            String clean = review.toLowerCase().replaceAll("[^a-z ]", "");
            int count = clean.split("\\s+").length;

            // job 03: hash review
            calculateSHA256(review);
        });
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // results
        System.out.println("1. Execution Time: " + duration + " ms");
        // System.out.println("2. Throughput: " + (data.size() * 1000L / duration) + " reviews/giây");
        if(duration > 0){
            System.out.println("2. Throughput: " + (data.size() * 1000L / duration) + " reviews/giây");
        }else{
            System.out.println("2. Throughput: duration quá nhỏ (<1ms)");
        }
        System.out.println("3. Thread Metrics (Số dòng mỗi thread xử lý):");
        threadMetrics.forEach((name, adder) -> System.out.println("   - [" + name + "]: " + adder.sum()));

    }

    private static void calculateSHA256(String input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.digest(input.getBytes());
        }catch(Exception e){}
    }
    private static List<String> loadParquet(String path) throws Exception{
        List<String> list = new ArrayList<>();
        try(var reader = AvroParquetReader.<GenericRecord>builder(new Path(path)).build()){
            GenericRecord record;
            while((record = reader.read()) != null){
                list.add(record.get("text").toString());
            }
            // boolean printed = false;
            // while((record = reader.read()) != null){

            //     if(!printed){
            //         System.out.println(record.getSchema());
            //         printed = true;
            //     }

            //     break;
            // }
        }
        return list;
    }
}
