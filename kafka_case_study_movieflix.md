# Case Study: MovieFlix – Real-Time Streaming Platform with Kafka

## 🎯 Mục tiêu hệ thống

MovieFlix là một nền tảng phát video theo yêu cầu như Netflix. Để cải thiện khả năng **theo dõi hành vi người dùng**, **cá nhân hóa nội dung**, và **phát hiện gian lận**, MovieFlix ứng dụng **Apache Kafka** để thu thập và xử lý dữ liệu người dùng theo thời gian thực.

---

## 🏗️ Kiến trúc tổng thể

```
User Devices ───► API Gateway ───► Kafka Topics ───► Kafka Consumers/Streams ───► DBs, Alerting, Recommendation Engine
```

### Các thành phần chính:

| Thành phần | Mô tả |
|-----------|------|
| **User Devices** | Thiết bị người dùng gửi sự kiện như: play, pause, stop, search |
| **API Gateway** | Chuyển đổi request HTTP thành message Kafka |
| **Kafka Topics** | Ghi nhận sự kiện (ví dụ: `user-actions`, `search-events`, `watch-history`) |
| **Kafka Consumers** | Gửi dữ liệu vào hệ thống phân tích, DB, hoặc hệ thống cảnh báo |
| **Kafka Streams** | Xử lý luồng dữ liệu: phân tích thời gian thực, tạo khuyến nghị |
| **Recommendation Engine** | Đề xuất nội dung dựa vào hành vi người dùng |
| **ElasticSearch / Cassandra / PostgreSQL** | Lưu lịch sử xem, hành vi, logs |

---

## 🧩 Các Kafka Topic sử dụng

| Tên Topic | Mục đích |
|-----------|----------|
| `user-interactions` | Ghi lại tất cả tương tác: play, pause, rate |
| `watch-history` | Ghi nhận nội dung đã xem của người dùng |
| `search-events` | Dữ liệu từ chức năng tìm kiếm |
| `fraud-signals` | Phát hiện hoạt động bất thường |

### Thiết kế topic:
- Theo **event-type**
- Dùng **key = userId** để giữ locality
- Topic như `watch-history` có thể bật **log compaction** nếu cần lưu trạng thái cuối

---

## 🔄 Kafka Streams trong MovieFlix

- Tính thời gian trung bình xem mỗi thể loại
- Đếm số lượt tìm kiếm trong 30 phút
- Gửi alert khi login bất thường

```java
KStream<String, WatchEvent> stream = builder.stream("watch-history");
stream.groupBy((key, value) -> value.getGenre())
      .windowedBy(TimeWindows.of(Duration.ofMinutes(30)))
      .count();
```

---

## 🕵️ Phát hiện gian lận với Kafka

- Topic `fraud-signals` phát hiện:
  - Một người xem 100 phim trong 5 phút
  - Một IP đăng nhập 50 tài khoản khác nhau
- Kafka Streams hoặc Flink + ML model giúp cảnh báo tức thì

---

## ⚙️ Khả năng mở rộng và độ bền

- Kafka giúp:
  - Xử lý hàng **triệu sự kiện mỗi ngày**
  - Chống nghẽn cổ chai
  - Dễ mở rộng: thêm consumer, stream mới không ảnh hưởng hệ thống cũ

---

## ✅ Bài học từ MovieFlix

**Ưu điểm:**
- Kafka giảm độ trễ xử lý
- Kafka Streams thay thế batch jobs phức tạp
- Đơn giản hóa kiến trúc – event-driven

**Thách thức:**
- Cần quản lý schema nghiêm ngặt (Avro, Protobuf)
- Monitoring offsets, consumer lag quan trọng
- Thiết kế topic/partition đúng là tối quan trọng

---

Kafka không chỉ phù hợp với fintech, mà còn là **trái tim** của những nền tảng giải trí quy mô lớn như MovieFlix.