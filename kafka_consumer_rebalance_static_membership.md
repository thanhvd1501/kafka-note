# Kafka Consumer: Incremental Cooperative Rebalance & Static Group Membership

Trong hệ thống Kafka, khi một **consumer group** xảy ra thay đổi (consumer join/leave), **rebalance** sẽ được kích hoạt để phân phối lại các partition. Tuy nhiên, việc này thường gây **tạm dừng tiêu thụ dữ liệu**. Để tối ưu hóa quá trình đó, Kafka cung cấp hai cơ chế quan trọng:

---

## 🔁 1. Incremental Cooperative Rebalance

### ✅ Mục tiêu
Giảm độ gián đoạn (downtime) trong quá trình rebalance bằng cách **chuyển giao dần dần các partition** giữa các consumer.

### 🧠 Cách hoạt động
Khác với cơ chế rebalance mặc định (Eager Rebalance), trong đó tất cả consumer phải **release tất cả partition** trước khi phân phối lại, **Incremental Rebalance** chỉ yêu cầu release **những partition thực sự cần chuyển giao**.

### 📌 Ưu điểm:
- **Ít gián đoạn**: Không cần ngừng hoàn toàn việc tiêu thụ dữ liệu.
- **Tăng hiệu suất**: Giảm lượng dữ liệu chưa xử lý do dừng đột ngột.
- **Thân thiện với các ứng dụng stateful** (ví dụ: Flink, Kafka Streams).

### ⚙️ Cấu hình sử dụng:

```yaml
spring:
  kafka:
    consumer:
      group-id: my-group
      properties:
        partition.assignment.strategy: org.apache.kafka.clients.consumer.CooperativeStickyAssignor
```

> `CooperativeStickyAssignor` là phiên bản hỗ trợ incremental rebalance.

---

## 🧷 2. Static Group Membership

### ✅ Mục tiêu
Giữ nguyên danh tính của consumer trong group dù có ngắt kết nối tạm thời → **tránh rebalance không cần thiết**.

### 🧠 Cách hoạt động
Mỗi consumer khi khởi động có thể cung cấp một **group.instance.id** cố định. Nếu consumer bị ngắt kết nối (ví dụ: restart ngắn), Kafka sẽ giữ lại partition assignment của nó **trong thời gian timeout**, tránh kick out khỏi group.

### 📌 Ưu điểm:
- Giảm số lần rebalance khi consumer **tái kết nối nhanh chóng**.
- **Tối ưu cho container orchestration** (Docker, K8s) – khi container restart nhưng vẫn là cùng một ứng dụng.

### ⚙️ Cấu hình sử dụng:

```yaml
spring:
  kafka:
    consumer:
      properties:
        group.instance.id: instance-01
```

> Mỗi consumer nên có `group.instance.id` **duy nhất** để tránh lỗi khởi động.

---

## 🔄 Tương quan giữa 2 cơ chế

| Cơ chế | Mục tiêu chính | Giảm downtime | Tránh rebalance không cần thiết |
|--------|----------------|----------------|----------------------------|
| Incremental Rebalance | Giảm độ gián đoạn khi thêm/bớt consumer | ✅ | ❌ |
| Static Group Membership | Giữ consumer trong group khi restart | ✅ | ✅ |

📝 Thường dùng **cả hai cùng lúc** để có hiệu quả tối ưu trong các hệ thống yêu cầu **real-time**, **tối thiểu downtime**, như:
- Hệ thống thanh toán
- Streaming analytics
- Microservices Kafka consumers

---

## 🔚 Kết luận

- `Incremental Rebalance` giúp Kafka chuyển đổi consumer nhanh hơn, không ảnh hưởng đến toàn bộ group.
- `Static Membership` giúp consumer reconnect mà không mất trạng thái.
- Kết hợp cả hai giúp **giảm độ trễ**, **tăng độ ổn định**, và **tránh tắc nghẽn dữ liệu** trong môi trường sản xuất.