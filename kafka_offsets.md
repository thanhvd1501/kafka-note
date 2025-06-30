# Kafka Offsets – Khái niệm, Cách Hoạt Động và Ứng Dụng

## 1. Offset là gì?

Trong Kafka, mỗi **message** trong một **partition** được gán một **số nguyên duy nhất, tăng dần** gọi là **offset**. Offset đại diện cho **vị trí của message** trong partition đó.

> Offset **chỉ có ý nghĩa trong phạm vi 1 partition** (không toàn topic).

---

## 2. Vai trò của Offset

- Giúp Kafka **xác định message đã đọc đến đâu**.
- Consumer có thể sử dụng offset để:
  - Tiếp tục đọc từ nơi dừng lại.
  - Đọc lại một phần dữ liệu cũ (replay).
- Cung cấp khả năng **chịu lỗi** (fault-tolerance) cao cho hệ thống phân tán.

---

## 3. Kafka Consumer sử dụng offset như thế nào?

- Mỗi consumer đọc dữ liệu từ topic partition **theo thứ tự offset**.
- Sau khi xử lý xong một message, Kafka consumer **commit offset** để đánh dấu đã xử lý đến vị trí đó.

### Có 2 kiểu commit offset:

| Kiểu Commit     | Mô tả                                                                 |
|-----------------|----------------------------------------------------------------------|
| **Tự động (auto)** | Kafka tự commit định kỳ (default mỗi 5s). Có thể **mất message** nếu crash. |
| **Thủ công (manual)** | Developer chủ động commit offset sau khi xử lý xong → **an toàn hơn**.     |

---

## 4. Quản lý offset bằng Kafka

- Kafka lưu offset trong một **internal topic** tên là `__consumer_offsets`.
- Offset được lưu theo **(consumer group, topic, partition)**.

---

## 5. Ảnh hưởng của offset đến độ tin cậy

### Trường hợp mất kết nối

Nếu consumer xử lý xong message nhưng **chưa commit offset**, rồi bị crash → message đó sẽ **bị đọc lại** sau khi restart.

✅ Tốt cho **idempotent processing** (xử lý lặp vẫn đúng).

❌ Nếu không idempotent (ví dụ gửi email) → có thể **bị lặp lại**.

---

## 6. Lấy offset theo nhu cầu

Kafka cho phép consumer điều chỉnh lại offset:

| Cách | Lệnh hoặc API |
|------|---------------|
| Đọc từ đầu topic | `seekToBeginning()` |
| Đọc từ cuối topic | `seekToEnd()` |
| Đọc từ offset cụ thể | `seek(partition, offset)` |

---

## 7. Ví dụ thực tế

```java
@KafkaListener(topics = "account-topic")
public void listen(ConsumerRecord<String, String> record) {
    log.info("Offset: {}, Key: {}, Value: {}", 
             record.offset(), 
             record.key(), 
             record.value());
}
```

---

## 8. Tóm tắt kiến thức

| Khái niệm | Ý nghĩa |
|----------|---------|
| **Offset** | Vị trí của message trong partition |
| **Consumer** | Dùng offset để đọc đúng dữ liệu |
| **Commit offset** | Ghi nhớ đã xử lý tới đâu |
| **Auto vs Manual** | Tự động dễ dùng nhưng kém an toàn |
| **Replay** | Có thể đọc lại bằng cách chỉnh offset |