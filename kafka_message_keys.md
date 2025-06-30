# Kafka Message Keys – Tổng Quan và Ứng Dụng

## 1. Message Key là gì?

Mỗi message (record) gửi vào Kafka có thể gồm:

- `key` (khóa)
- `value` (giá trị)
- `headers` (tuỳ chọn)
- `timestamp` (tuỳ chọn)

Ví dụ:

```java
kafkaTemplate.send("account-topic", "user-123", "Balance updated");
```

- `"user-123"` là **message key**
- `"Balance updated"` là **message value**

---

## 2. Tác dụng của Message Key

### a. Điều hướng đến phân vùng (Partitioning)

Kafka dùng key để xác định message sẽ được ghi vào partition nào trong topic:

- **Key = null**: phân phối round-robin.
- **Key ≠ null**: Kafka dùng hash(key) để chọn partition.

Điều này đảm bảo:

- Các message cùng key luôn vào **cùng một partition**.
- **Giữ được thứ tự xử lý** với cùng một key.

### b. Hỗ trợ xử lý có trạng thái (Stateful Processing)

Nếu xử lý dữ liệu theo key (ví dụ tổng số lần login theo user), tất cả message cùng key phải đi về **một partition**, để xử lý logic liên tục và chính xác.

---

## 3. Ví dụ thực tế

```java
Account account = new Account("user-123", 50000L);
kafkaTemplate.send("account-topic", account.getId(), account);
```

➡ Tất cả message của `user-123` vào cùng 1 partition.

---

## 4. Lưu ý khi dùng Message Key

| Tình huống                          | Nên làm gì                         |
|------------------------------------|------------------------------------|
| Cần giữ thứ tự theo thực thể       | Gửi message với key là ID thực thể |
| Không cần thứ tự                   | Có thể dùng key = null             |
| Cần cân bằng tải                   | Tránh quá ít key → phân phối kém   |
| Dùng Kafka Streams / KTable        | **Phải có key** để group/join      |

---

## 5. Kafka Consumer nhận key như thế nào?

```java
@KafkaListener(topics = "account-topic")
public void listen(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                   AccountMsgDto msg) {
    log.info("Received key={}, value={}", key, msg);
}
```

---

## 6. Tóm tắt

| Thuộc tính             | Ý nghĩa                                         |
|------------------------|--------------------------------------------------|
| **Key**                | Định tuyến partition và xử lý theo nhóm         |
| **Cùng key → 1 partition** | Đảm bảo thứ tự và state                       |
| **Key null**           | Phân phối round-robin                           |
| **Key quan trọng**     | Đặc biệt trong group, join, aggregation         |