# Kafka Producer Settings: `linger.ms` and `batch.size`

## 🧩 `linger.ms` – Độ trễ tối đa trước khi gửi một batch

### 📝 Ý nghĩa:
- `linger.ms` (mặc định: 0) là **thời gian tối đa (tính bằng mili giây)** mà Producer sẽ **chờ đợi** trước khi gửi batch message, **dù batch chưa đầy**.

### ⚙️ Cơ chế hoạt động:
- Khi Producer ghi nhận một message, nó **sẽ không gửi ngay**, mà **đưa vào batch**.
- Nếu batch đầy → gửi ngay.
- Nếu batch chưa đầy, thì Kafka Producer **chờ tối đa `linger.ms` mili giây**, rồi gửi batch đó, **ngay cả khi chưa đầy**.

### 🟩 Mục đích:
- Giúp **tăng hiệu suất** bằng cách gom nhiều messages lại trong một request gửi đi → giảm chi phí mạng.
- Nhưng đồng thời cũng **tăng latency**.

### 🔍 Ví dụ:
```properties
linger.ms=5
```
→ Producer sẽ chờ tối đa 5ms trước khi gửi batch, nhằm gom thêm message khác nếu có.

---

## 🧩 `batch.size` – Kích thước tối đa của batch (bytes)

### 📝 Ý nghĩa:
- `batch.size` (mặc định: 16KB) là **kích thước tối đa tính theo byte** của một batch các message sẽ được gửi đến cùng một partition.

### ⚙️ Cơ chế hoạt động:
- Mỗi partition có **một batch buffer riêng**.
- Khi buffer đầy → gửi batch đi, **bỏ qua `linger.ms`**.
- Nếu batch không đầy → chờ `linger.ms` trước khi gửi.

### 🟩 Mục đích:
- Giới hạn kích thước payload gửi đi.
- Tối ưu dung lượng gửi theo mạng.

### 🔍 Ví dụ:
```properties
batch.size=32768 # 32KB
```
→ Một batch sẽ tối đa là 32KB dữ liệu. Khi đầy, Producer sẽ gửi đi mà không chờ `linger.ms`.

---

## ✅ Tóm tắt mối quan hệ

| Thuộc tính     | Kiểu       | Mục đích                            | Ảnh hưởng |
|----------------|------------|-------------------------------------|-----------|
| `linger.ms`    | Thời gian  | Chờ thêm message vào batch         | Tăng throughput, tăng latency |
| `batch.size`   | Kích thước | Giới hạn dung lượng của mỗi batch  | Tối ưu tài nguyên mạng |

### 🎯 Cấu hình tối ưu điển hình:
```yaml
linger.ms: 5
batch.size: 32768
```
→ Gửi batch mỗi 5ms hoặc khi batch đạt 32KB – giúp tăng throughput trong hệ thống lớn.