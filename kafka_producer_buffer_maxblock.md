# Kafka Producer Settings: `max.block.ms` and `buffer.memory`

## 🧩 `max.block.ms` – Thời gian chờ tối đa khi Producer bị block

### 📝 Ý nghĩa:
- Đây là thời gian tối đa (mili giây) mà Kafka Producer sẽ **chờ đợi** khi không thể ghi thêm message vào buffer (do đầy hoặc metadata chưa có).
- Sau thời gian này, nếu vẫn không gửi được → **ném lỗi `TimeoutException`**.

### ⚙️ Khi nào bị block?
- Khi **buffer.memory** đầy và Producer không thể ghi thêm.
- Khi chưa nhận được metadata từ broker (sau khi khởi tạo Producer).

### 🔍 Ví dụ:
```properties
max.block.ms=5000
```
→ Kafka sẽ chờ tối đa 5 giây nếu buffer đầy. Nếu sau 5 giây vẫn chưa có chỗ trống → ném lỗi.

---

## 🧩 `buffer.memory` – Dung lượng bộ nhớ đệm của Producer

### 📝 Ý nghĩa:
- Tổng dung lượng RAM (byte) Kafka Producer được phép sử dụng để **lưu trữ tạm thời các message** trước khi gửi lên broker.

### ⚙️ Cơ chế hoạt động:
- Khi gọi `send()`, message được ghi vào buffer.
- Worker thread sẽ đọc message từ buffer và gửi đến broker (theo batch).
- Nếu buffer đầy, Producer sẽ **chặn (block)** cho đến khi có chỗ trống, **hoặc** vượt quá `max.block.ms` thì lỗi.

### 🔍 Ví dụ:
```properties
buffer.memory=33554432 # 32MB
```
→ Kafka producer sẽ có 32MB RAM để tạm lưu message trước khi gửi.

---

## ✅ Tương quan giữa `buffer.memory` và `max.block.ms`

| Thuộc tính      | Mục đích                         | Ghi chú |
|-----------------|----------------------------------|--------|
| `buffer.memory` | Giới hạn tổng RAM cho message   | Khi đầy → block |
| `max.block.ms`  | Thời gian block tối đa cho `send()` | Sau thời gian này → lỗi |

---

## 🛠 Khi nào cần chỉnh?
- **Tăng `buffer.memory`** nếu producer gửi message nhanh và thường bị đầy.
- **Giảm `max.block.ms`** nếu bạn muốn nhanh chóng phát hiện sự cố (ví dụ: broker không phản hồi).

---

## 🧪 Ví dụ cấu hình thực tế:
```yaml
buffer.memory: 67108864   # 64MB
max.block.ms: 10000       # 10 giây
```

→ Tối ưu cho hệ thống tải cao, cho phép chờ lâu hơn trước khi ném lỗi.

---

## 📌 Kết luận:
- `buffer.memory` ảnh hưởng đến khả năng xử lý nhanh của producer.
- `max.block.ms` giúp giới hạn thời gian đợi khi buffer đầy hoặc chưa có metadata.
- Cần cấu hình cân đối để tránh mất dữ liệu hoặc chặn hệ thống không cần thiết.