# Case Study – GetTaxi: Real-Time Event Streaming với Apache Kafka

## 1. Giới thiệu

GetTaxi (giống như Uber hoặc Grab) là một nền tảng gọi xe theo yêu cầu. Để đảm bảo tốc độ xử lý, khả năng mở rộng và độ tin cậy cao, GetTaxi đã ứng dụng Apache Kafka làm hệ thống backbone cho xử lý sự kiện thời gian thực.

---

## 2. Các Thách Thức Kỹ Thuật

- Xử lý hàng ngàn sự kiện mỗi giây (đặt xe, GPS, cập nhật trạng thái).
- Phải có kiến trúc chịu lỗi và dễ mở rộng.
- Tách biệt các dịch vụ: bản đồ, thanh toán, đơn hàng...

---

## 3. Kiến trúc hệ thống

```
[Rider App]           [Driver App]
    |                      |
    +----> Kafka Topics <----+
                |
       -----------------------
      |         |            |
 [Trip Service][Payment][Analytics]
```

---

## 4. Kafka Topics

| Topic             | Mô tả                                         |
|-------------------|-----------------------------------------------|
| `trip-requests`   | Người dùng đặt xe                             |
| `trip-updates`    | Cập nhật trạng thái chuyến đi                 |
| `driver-location` | Tài xế gửi vị trí định kỳ                     |
| `payments`        | Thông tin giao dịch khi chuyến đi hoàn thành |

---

## 5. Kafka Producers & Consumers

- **Producers**:
  - Rider App: gửi request đặt xe
  - Driver App: gửi vị trí, trạng thái
- **Consumers**:
  - Trip Management: nhận request và phân tài xế
  - Payment Service: xử lý thanh toán
  - Analytics: tính toán thống kê thời gian thực

---

## 6. Kafka Streams

- Dùng để:
  - Tính số chuyến theo từng khu vực mỗi 5 phút
  - Dự đoán nhu cầu cao
  - Matching tài xế theo thời gian gần nhất

---

## 7. Độ bền và độ tin cậy

- Topic replication factor = 3
- Producer dùng `acks=all`
- Consumer manual commit offset
- Sử dụng schema registry để đồng bộ dữ liệu

---

## 8. Lợi ích

- Độ trễ thấp <500ms cho matching tài xế
- Hệ thống mở rộng linh hoạt theo load
- Thêm consumer không ảnh hưởng hệ thống
- Dễ debug, dễ giám sát

---

## 9. Kết luận

GetTaxi là ví dụ điển hình của kiến trúc hướng sự kiện (event-driven) với Kafka. Nó giúp phân tách các dịch vụ lớn nhỏ và tăng hiệu năng toàn hệ thống theo thời gian thực.