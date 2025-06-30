
# Kafka Topic Replication – Tăng độ tin cậy và tính sẵn sàng

## 1. Replication là gì?

**Replication** trong Kafka là cơ chế **sao chép dữ liệu** giữa các broker để đảm bảo **tính sẵn sàng (availability)** và **khả năng chịu lỗi (fault tolerance)**.

- Mỗi **partition** của một topic có thể có **n bản sao (replicas)**.
- Một bản sao được chỉ định là **leader**, còn lại là **follower**.
- **Producer và Consumer chỉ tương tác với leader** của partition.

## 2. Cấu hình replication

- Khi tạo topic, có thể chỉ định số **replication factor**:

```bash
kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --topic demo-topic \
  --replication-factor 3 \
  --partitions 2
```

- Điều này nghĩa là:
  - Mỗi partition sẽ có 1 leader và 2 follower.
  - Dữ liệu sẽ được sao chép sang 3 broker (nếu có đủ).

## 3. Lợi ích của replication

| Lợi ích                         | Mô tả                                                                 |
|--------------------------------|----------------------------------------------------------------------|
| **Chịu lỗi**                   | Nếu một broker chết, follower khác có thể lên làm leader.           |
| **Tính sẵn sàng cao**          | Hệ thống vẫn tiếp tục hoạt động ngay cả khi mất một số broker.      |
| **Không mất dữ liệu**          | Dữ liệu được nhân bản ở nhiều nơi → giảm rủi ro mất mát.            |

## 4. Vai trò của Leader và Follower

- **Leader partition**: Nhận ghi dữ liệu từ producer và trả dữ liệu cho consumer.
- **Follower partition**: Sao chép dữ liệu từ leader và luôn theo kịp (in-sync).

```plaintext
Partition 0 (replication factor = 3)
- Broker 1: Leader
- Broker 2: Follower
- Broker 3: Follower
```

## 5. In-Sync Replica (ISR)

- Tập hợp các follower đang **đồng bộ hoàn toàn với leader** được gọi là **In-Sync Replicas (ISR)**.
- Kafka đảm bảo chỉ commit dữ liệu khi được ghi thành công vào tất cả ISR.

Nếu follower chậm hoặc bị lỗi → bị loại khỏi ISR.

## 6. Cấu hình liên quan

- **`replication.factor`** – số lượng bản sao khi tạo topic.
- **`min.insync.replicas`** – số lượng replica tối thiểu phải sync trước khi cho phép ghi.
- **`acks`** – cấu hình từ phía producer:
  - `acks=1`: thành công khi leader nhận được.
  - `acks=all`: thành công khi toàn bộ ISR nhận được.

## 7. Leader election (Bầu lại leader)

Khi leader bị lỗi:

- Kafka sẽ **tự động chọn 1 follower trong ISR** lên làm leader mới.
- Việc này giúp cluster **tự phục hồi nhanh chóng mà không dừng dịch vụ**.

## 8. Thực tế triển khai

- Trong hệ thống sản xuất, replication-factor thường đặt là **3**.
- Các broker thường được đặt ở **nhiều máy khác nhau** hoặc **multi-zone**.
- Cần đảm bảo follower luôn theo kịp leader và dung lượng đĩa đủ lớn.

## 9. Mối liên hệ giữa partition, replication, và high availability

| Partition | Replication | Đảm bảo |
|-----------|-------------|---------|
| Nhiều     | Không có    | Không chống lỗi nếu broker chết |
| Nhiều     | Có          | Có thể chịu lỗi, nâng cao tính sẵn sàng |
| Ít        | Có          | Có thể phục hồi, nhưng giới hạn throughput |

## 10. Tổng kết

- Kafka Topic Replication là **cơ chế bắt buộc** trong hệ thống thực tế để bảo vệ dữ liệu.
- Để an toàn:
  - `replication-factor ≥ 3`
  - `acks=all`, `min.insync.replicas ≥ 2`
  - Theo dõi ISR và cảnh báo nếu có replica bị out-of-sync
