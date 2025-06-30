# Kafka Streams - Tổng Hợp Dễ Hiểu Nhất

Kafka Streams là một thư viện Java được Apache Kafka cung cấp để xử lý dữ liệu streaming trực tiếp trên Kafka. Dưới đây là tóm tắt ngắn gọn nhưng đủ ý nghĩa để hiểu rõ Kafka Streams.

---

## 1. Kafka Streams là gì?

- **Là thư viện Java** (không phải framework) để xử lý dòng dữ liệu streaming.
- Đọc từ Kafka topic, xử lý, rồi ghi ngược lại Kafka hoặc output khác.
- Có hỗ trợ **stateless** (xử lý từng tin nhắn) và **stateful** (count, join, window\...).

## 2. So sánh với Kafka Consumer

| Tiêu chí            | Kafka Consumer | Kafka Streams                      |
| ------------------- | -------------- | ---------------------------------- |
| Mục đích            | Đọc dữ liệu    | Đọc + xử lý + ghi                  |
| Quản lý offset      | Tự làm         | Tự động                            |
| Retry/lỗi           | Tự viết        | Built-in                           |
| Stateful processing | Khó            | Có hỗ trợ sẵn                      |
| DSL xử lý           | Không          | Có (map, filter, join, window\...) |

## 3. Các thành phần chính trong Kafka Streams

- `KStream`: Dòng dữ liệu chuồi (stream)
- `KTable`: Bảng (giá trị cuối cùng theo key)
- `GlobalKTable`: Giống KTable nhưng truy cập được từ mọi partition
- `Topology`: Đồ thị xử lý (pipeline)

## 4. Các toán tử DSL phổ biến

- `map()`, `filter()`, `flatMap()`
- `groupByKey()`, `reduce()`, `count()`
- `join()`, `leftJoin()`, `outerJoin()`
- `windowedBy()` – chia cửa sổ theo thời gian

## 5. Cấu hình tối thiểu

```java
Properties props = new Properties();
props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-app");
props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
```

## 6. Tính năng nổi bật

- **Exactly-once processing** (v2)
- **State Store** để lưu trạng thái local
- **Fault-tolerance** (khi crash có thể khôi phục)
- **Repartition & Rebalancing** tự động

## 7. Khi nào dùng Kafka Streams?

- Cần xử lý dòng dữ liệu Kafka theo pipeline
- Cần join/cộng/truy vến bảng tạm theo key
- Muốn xử lý phân tán và có state recovery

## 8. Khi KHÔNG nên dùng Kafka Streams?

- Các tác vụ external như call API, ghi DB... → nên dùng Kafka Consumer
- Cần xử lý bằng non-Java (Python, Go...)

## 9. Dạng có sẵn trong Spring Boot

Spring Boot hỗ trợ `spring-cloud-stream` kết hợp Kafka Streams DSL dễ triển khai:

```java
@Bean
public Function<KStream<String, String>, KStream<String, String>> process() {
    return input -> input.mapValues(val -> val.toUpperCase());
}
```

---

Kafka Streams giúc việc xử lý dữ liệu Kafka trở nên gọn gàn, an toàn, phân tán và linh hoạt. Nó thay thế nhiều logic phức tạp bằng cách viết pipeline cực ngắn gọn, dễ đọc.

