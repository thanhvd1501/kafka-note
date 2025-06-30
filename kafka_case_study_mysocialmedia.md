# Case Study – MySocialMedia: Real-Time Social Platform with Kafka

## 1. Mục tiêu hệ thống

MySocialMedia là một nền tảng mạng xã hội nơi người dùng có thể đăng bài, tương tác (like/comment), và nhận thông báo ngay tức thì. Để đáp ứng hiệu năng cao và xử lý dữ liệu thời gian thực, hệ thống ứng dụng Apache Kafka.

---

## 2. Kiến trúc sử dụng Kafka

### Thành phần chính:

- **Producers**:
  - Gửi dữ liệu khi người dùng tạo bài, like, comment...
- **Kafka Topics**:
  - `post-events`: Bài viết mới
  - `like-events`: Thả tim
  - `comment-events`: Bình luận
  - `notification-events`: Gửi thông báo
- **Kafka Streams**:
  - Gộp dữ liệu tương tác
  - Phát hiện bài viết xu hướng
- **Consumers**:
  - Notification Service
  - Analytics Service
  - Recommendation Service

---

## 3. Luồng dữ liệu ví dụ

### User A đăng bài:
- Event gửi vào `post-events`
- Kafka Streams tạo bản ghi thống kê
- Notification Service gửi alert đến follower

### User B thả tim bài viết:
- Event gửi vào `like-events`
- Kafka Streams cập nhật điểm tương tác
- Notification Service alert cho User A

### User C bình luận:
- Event vào `comment-events`
- Gửi thông báo cho người viết và follower

---

## 4. Lợi ích khi dùng Kafka

- **Tách biệt microservices**
- **Xử lý song song hiệu quả**
- **Thông báo tức thì**
- **Dễ mở rộng**

---

## 5. Lưu ý khi triển khai

- Thiết kế key & partition hợp lý
- Sử dụng consumer groups tránh trùng xử lý
- Tận dụng Kafka Streams cho phân tích nâng cao

---

## 6. Kết luận

MySocialMedia ứng dụng kiến trúc hướng sự kiện (event-driven) với Apache Kafka để cung cấp trải nghiệm người dùng thời gian thực, bền vững và mở rộng theo nhu cầu.