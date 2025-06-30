# Kafka Producer Callbacks

## 🎯 Mục tiêu

Trong Apache Kafka, khi một producer gửi message đến broker, có thể xảy ra một trong ba trường hợp:

1. Gửi thành công (message được ghi vào topic-partition).
2. Gửi thất bại do lỗi mạng, lỗi serialization, thiếu broker, v.v.
3. Gửi chưa chắc chắn – nếu không xử lý callback thì ứng dụng **không biết** kết quả gửi.

**Callbacks** giúp xử lý các tình huống này một cách **chủ động và an toàn**, từ đó tăng độ tin cậy và debug dễ hơn.

---

## 🔁 Cách dùng callback trong Kafka Producer

Kafka sử dụng interface `org.apache.kafka.clients.producer.Callback`, thường truyền vào phương thức `send()` như sau:

```java
producer.send(record, new Callback() {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            // Gửi thành công
            System.out.println("Message sent successfully to topic: " + metadata.topic());
        } else {
            // Có lỗi xảy ra
            exception.printStackTrace();
        }
    }
});
```

✅ Ghi nhớ:
- `metadata` chứa thông tin như topic, partition, offset.
- `exception` là `null` nếu gửi thành công, ngược lại chứa nguyên nhân lỗi.

---

## 📦 Lợi ích của callback

| Lợi ích | Giải thích |
|--------|------------|
| ✅ Kiểm soát kết quả gửi | Biết được message đã ghi thành công hay chưa. |
| ✅ Logging chi tiết | Ghi log theo từng message để dễ debug. |
| ✅ Retry có điều kiện | Có thể thực hiện retry thủ công trong callback khi gặp lỗi tạm thời. |
| ✅ Thống kê và giám sát | Thu thập thông tin gửi để phân tích hiệu năng hoặc cảnh báo lỗi. |

---

## ⚠️ Lưu ý khi dùng callback

- **Không chặn luồng callback quá lâu** (blocking I/O, sleep...) vì sẽ ảnh hưởng đến throughput.
- Nếu gửi hàng ngàn message/giây, hãy dùng **batch processing** để giảm log hoặc thống kê gộp.
- Không throw exception trong `onCompletion()` – nếu có lỗi, nên log và xử lý nội bộ.

---

## 🧪 Ví dụ thực tế dùng lambda

```java
producer.send(record, (metadata, exception) -> {
    if (exception != null) {
        log.error("Failed to send message", exception);
    } else {
        log.info("Message sent to topic {} at offset {}", metadata.topic(), metadata.offset());
    }
});
```

---

## 🧠 Tóm tắt

| Yếu tố | Mô tả |
|--------|------|
| Callback là gì? | Là hàm được gọi khi Kafka producer gửi message xong (thành công hoặc thất bại). |
| Khi nào nên dùng? | Khi bạn cần biết rõ kết quả gửi để log, thống kê, retry... |
| Có thể dùng lambda? | ✅ Có, rất phổ biến với Java 8+. |
| Callback blocking có ổn không? | ❌ Không nên chặn lâu, tránh làm chậm producer. |
