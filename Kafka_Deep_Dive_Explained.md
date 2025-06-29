# 📚 Hiểu Tường Tận Apache Kafka – Kiến Thức Cốt Lõi để Xây Dựng Hệ Thống Streaming và Microservices

---

## 🧠 1. Kafka Là Gì?

Apache Kafka là một **nền tảng truyền tải sự kiện phân tán, có tính chịu lỗi cao**, được thiết kế để **xử lý các luồng dữ liệu thời gian thực**.

Kafka hoạt động như một **commit log phân tán** – nơi các producer ghi message vào topic, còn consumer đọc lại message đó theo thứ tự offset.

---

## 🧱 2. Kiến Trúc Thành Phần

| Thành phần | Vai trò |
|------------|--------|
| **Producer** | Gửi message đến Kafka topic |
| **Consumer** | Nhận message từ topic |
| **Broker** | Server Kafka – lưu trữ topic, xử lý request |
| **Topic** | Chủ đề chứa message |
| **Partition** | Chia nhỏ topic để tăng throughput và scale |
| **Consumer Group** | Một nhóm consumer cùng đọc topic và tự động chia việc |

---

## 🔁 3. Cơ Chế Hoạt Động

- Message được ghi vào **topic** (gồm nhiều **partition**)
- Mỗi message có một **offset** (số thứ tự trong partition)
- Consumer đọc message theo thứ tự offset
- **Kafka KHÔNG xóa message sau khi đọc** – khác hoàn toàn queue truyền thống

---

## 📦 4. Ưu Điểm Kafka

- **High Throughput**: scale cao, xử lý hàng triệu event/giây
- **Durability**: lưu trữ message an toàn (ghi đĩa + replica)
- **Scalability**: chia partition và thêm broker dễ dàng
- **Replayability**: consumer có thể đọc lại message cũ
- **Loose Coupling**: giúp các microservices tách biệt nhau (event-driven)

---

## 🧩 5. Cơ Chế Offset

- Mỗi consumer đọc message sẽ **quản lý offset riêng**
- Kafka không "xóa sau khi đọc" → bạn có thể:
  - Auto commit
  - Manual commit
  - Store offset bên ngoài Kafka (vd: DB, Redis...)

---

## ⚙️ 6. Cấu Hình Quan Trọng

| Config | Vai trò |
|--------|--------|
| `acks` | Mức độ xác nhận (0, 1, all) |
| `retention.ms` | Thời gian giữ message |
| `enable.auto.commit` | Có auto commit offset không |
| `group.id` | Định danh nhóm consumer |
| `key.serializer`, `value.serializer` | Cách convert object thành byte |

---

## 📌 7. Use Cases Phổ Biến

### 🏭 Microservices Communication (Event-driven)
- Service A phát `OrderCreatedEvent`
- Service B lắng nghe để gửi email, log...

### 🧾 Log Aggregation
- Tập trung log từ nhiều app → Kafka → Elasticsearch / Hadoop

### 📊 Real-time Analytics
- App đẩy clickstream → Kafka → xử lý / visualization dashboard

### 🔄 Data Pipeline
- CDC (Change Data Capture): DB → Kafka → downstream system

### 🧪 Audit Trail
- Ghi lại lịch sử thay đổi để phục hồi hoặc kiểm toán

---

## 🔐 8. Bảo Mật Kafka

- **TLS Encryption**
- **SASL Authentication**
- **ACL Authorization** theo topic, action, user

---

## 🛠 9. Kafka trong Spring Boot

- `KafkaTemplate` để gửi message
- `@KafkaListener` để nhận message
- Dễ cấu hình, có hỗ trợ converter, error handler, retry

---

## 📊 10. Kafka vs Queue Truyền Thống

| Kafka | RabbitMQ / ActiveMQ |
|-------|----------------------|
| Append-only log | Queue FIFO |
| Replayable | Không replay được |
| Partitioned | Không có khái niệm partition |
| Consumer pull | Consumer push |
| Rất nhanh | Chậm hơn Kafka (thường) |

---

## 🧠 Kết Luận

Kafka là một công cụ nền tảng trong kiến trúc microservices hiện đại và các hệ thống phân tán thời gian thực.

Để dùng hiệu quả Kafka, bạn cần nắm vững:
- Kiến trúc và nguyên lý hoạt động
- Cơ chế offset, consumer group
- Cách scale, bảo mật, tuning
- Thiết kế use case đúng với bất đồng bộ