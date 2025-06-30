
# Kafka Consumer Internal Threads

Trong Kafka, mỗi consumer không chỉ đơn giản là một thread lắng nghe và xử lý dữ liệu – nó còn có **nhiều thread nội bộ** hoạt động song song để đảm bảo việc tiêu thụ message diễn ra chính xác, hiệu quả và an toàn. Dưới đây là các luồng nội bộ chính của một Kafka Consumer:

---

## 1. Main Thread (Application Thread)
- Đây là thread thực thi hàm bạn khai báo, ví dụ:
  ```java
  @KafkaListener
  public void listen(String message) {
      // logic xử lý message
  }
  ```
- Thực hiện công việc nghiệp vụ chính.
- Luồng này nhận message đã được Kafka poll từ broker, sau đó gọi logic người dùng để xử lý.

---

## 2. Consumer Poll Thread
- Là thread **ẩn** hoặc quản lý bên trong framework (ví dụ Spring Kafka) – chịu trách nhiệm gọi `poll()` đến Kafka broker.
- Định kỳ fetch các message từ topic đã đăng ký.
- Nếu bạn sử dụng Spring Kafka, nó nằm bên trong `KafkaMessageListenerContainer`.

---

## 3. Heartbeat Thread
- Kafka sử dụng heartbeat để giữ consumer còn sống trong group.
- Thread này gửi định kỳ heartbeat đến coordinator (Kafka broker) để tránh bị coi là "dead" và bị kích hoạt rebalance.

---

## 4. Rebalance Thread
- Khi xảy ra các sự kiện như: consumer mới join, một consumer bị crash, hoặc topic thay đổi partition, Kafka sẽ kích hoạt **rebalance**.
- Rebalance Thread xử lý việc phân phối lại partition cho các consumer.

---

## 5. Commit Thread
- Dù bạn gọi commit thủ công hay sử dụng auto commit, Kafka sẽ có logic nội bộ commit offset – lưu thông tin đã xử lý xong.
- Trong Spring Kafka, commit có thể chạy cùng thread với poll, nhưng framework vẫn hỗ trợ commit async hoặc sync bằng các thread riêng.

---

## Cách Thực Tế Nó Hoạt Động trong Spring Kafka

Spring Kafka dùng:
- **KafkaMessageListenerContainer**: Là nơi vận hành Poll Thread + các consumer logic.
- Dưới hood, nó dùng executor (thread pool) để điều phối các thread (poll, listener, ack...).
- Bạn có thể cấu hình concurrency để chạy nhiều thread (consumer) trong 1 ứng dụng.

---

## Tóm Tắt

| Thread              | Vai trò                             |
|---------------------|--------------------------------------|
| Main/Application Thread | Xử lý logic nghiệp vụ             |
| Poll Thread         | Lấy message từ Kafka broker         |
| Heartbeat Thread    | Giữ session với broker              |
| Rebalance Thread    | Xử lý tái phân vùng khi cần         |
| Commit Thread       | Ghi nhận offset đã xử lý            |
