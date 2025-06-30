# 🏦 Case Study – MyBank

## 1. Bối cảnh doanh nghiệp
MyBank là một ngân hàng kỹ thuật số với các dịch vụ cốt lõi như:
- Tài khoản thanh toán
- Giao dịch chuyển khoản
- Hệ thống thông báo qua email và SMS
- Phân tích gian lận thời gian thực
- Cung cấp API mở (Open Banking)

Với tốc độ mở rộng và khối lượng giao dịch tăng cao, MyBank cần một giải pháp:
- **Mở rộng theo chiều ngang**
- **Đảm bảo thời gian phản hồi thấp**
- **Xử lý thời gian thực (real-time)**

## 2. Vấn đề gặp phải

### ❌ Trước khi dùng Kafka:
- Giao tiếp giữa các dịch vụ dựa trên HTTP (REST)
- Các service tightly coupled → khó mở rộng
- Dữ liệu giao dịch phân tán, thiếu thời gian thực
- Phân tích gian lận thực hiện theo batch → chậm

### 🔥 Hệ quả:
- Trễ thông báo giao dịch
- Phát hiện gian lận sau khi đã xảy ra
- Căng thẳng hệ thống vào giờ cao điểm

---

## 3. Giải pháp: Apache Kafka

### ✔️ Lý do chọn Kafka:
- Dễ dàng tích hợp nhiều consumer
- Streaming real-time
- Dễ scale theo chiều ngang
- Khả năng lưu trữ và phân vùng dữ liệu hiệu quả

### 🧱 Kiến trúc triển khai:

#### 🔹 Các topic chính:
| Topic | Chức năng |
|-------|-----------|
| `transactions` | Nhận tất cả giao dịch |
| `alerts` | Các cảnh báo gian lận |
| `notifications` | Thông báo SMS / Email |
| `analytics` | Dữ liệu cho phân tích nghiệp vụ |

#### 🔹 Các producer:
- Hệ thống Core Banking: gửi transaction vào `transactions`
- Fraud Detection Service: gửi cảnh báo vào `alerts`

#### 🔹 Các consumer:
- Notification Service: đọc từ `transactions` và `alerts`
- Data Lake Ingestor: ghi vào hệ thống phân tích lớn (BigQuery, Redshift, …)
- Fraud Analyzer (Kafka Streams app): xử lý real-time

---

## 4. Cách Kafka giúp giải quyết vấn đề

| Trước | Sau Kafka |
|-------|-----------|
| HTTP sync giữa service | Pub/Sub bất đồng bộ |
| Batch Fraud Detection | Streaming Fraud Detection |
| Gửi thông báo chậm | Gửi real-time qua `notifications` |
| Căng thẳng hệ thống | Kafka phân tán tải đều |

---

## 5. Lợi ích đạt được

✅ Tăng tốc độ phát hiện gian lận → xử lý trong vòng **<1 giây**

✅ Gửi thông báo tức thì sau mỗi giao dịch

✅ Giảm độ coupling giữa các dịch vụ → dễ mở rộng

✅ Cải thiện trải nghiệm người dùng đáng kể

✅ Có thể audit lại toàn bộ giao dịch nhờ Kafka lưu log

---

## 6. Kết luận

Việc áp dụng Kafka giúp MyBank:
- Chuyển đổi từ kiến trúc hướng request sang event-driven
- Đảm bảo hệ thống mở rộng linh hoạt
- Tăng độ tin cậy và tốc độ phản hồi

Kafka không chỉ là “queue” mà là **xương sống dữ liệu (data backbone)** của MyBank, đóng vai trò chiến lược trong việc vận hành các hệ thống tài chính hiện đại.