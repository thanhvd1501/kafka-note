# 💡 Câu hỏi và trả lời chuyên sâu về Apache Kafka

## I. Kiến trúc và nguyên lý hoạt động

### 1. Kafka là message queue hay message log?
- Kafka là **distributed append-only log** (khác với message queue).
- **Queue**: mỗi message chỉ xử lý 1 lần rồi mất.
- **Log**: lưu trữ toàn bộ, **consumer tự quản lý offset** đọc.

### 2. Tại sao Kafka nhanh?
- Sử dụng **zero-copy I/O** (sendfile syscall).
- Ghi tuần tự (sequential write) vào ổ đĩa, tận dụng OS page cache.
- **Batch**, **compression**, và **NIO**.

### 3. So sánh Kafka vs RabbitMQ:
| Tiêu chí | Kafka | RabbitMQ |
|---------|-------|----------|
| Mô hình | Log/Event Streaming | Queue-based |
| Throughput | Rất cao | Trung bình |
| Ordering | Partition-level | Queue-level |
| Storage | Persistent log | FIFO queue |

### 4. Cơ chế routing theo key
- Nếu có `key`, Kafka dùng **hash(key) % num_partitions`**.
- Nếu không có key → **round-robin**.

### 5. Kafka có đảm bảo ordering?
- Có, nhưng **chỉ trong cùng một partition**.
- Nếu cần strict ordering toàn topic → dùng 1 partition (hy sinh scale).

### 6. Consumer group hoạt động ra sao?
- Kafka chia các partition trong topic cho các consumer cùng group.
- Mỗi partition **tối đa chỉ có 1 consumer xử lý** → load balancing.

### 7. ACK và delivery semantics:
- **At most once**: commit offset trước khi xử lý → có thể mất message.
- **At least once**: xử lý xong mới commit → có thể trùng message.
- **Exactly once**: dùng transactional producer + idempotent consumer.

---

## II. Cơ chế an toàn và production

### 8. Kafka bảo vệ dữ liệu như thế nào?
- Ghi vào nhiều **replica**.
- Chỉ khi **leader + ISR (in-sync replica)** ghi thành công mới ACK.

### 9. Tại sao nên dùng số lẻ broker/replica?
- Để đảm bảo **quorum**.
- Ví dụ: 3 broker → cần 2 OK để commit → nếu mất 1 vẫn hoạt động.

### 10. Quorum hoạt động ra sao?
- Khi ghi dữ liệu: Kafka cần **đa số (n/2 + 1)** ISR thành công → mới coi là ghi ổn định.

### 11. Có cần Zookeeper không?
- Trước Kafka 2.x → cần Zookeeper.
- Kafka 3.x+ có thể dùng **KRaft mode (Kafka Raft)** để bỏ Zookeeper.

### 12. Chọn partition theo gì?
- Cần **parallelism** → nhiều partition.
- Cần **ordering** → ít partition hoặc key phù hợp.
- Tùy theo **consumer scale** và **data volume**.

### 13. Kafka transaction hoạt động ra sao?
- Sử dụng **transactional producer** + `_transaction_state` topic.
- Cho phép gửi nhiều message **atomically** đến nhiều topic/partition.

### 14. Offset lưu ở đâu?
- Kafka lưu offset ở topic nội bộ `__consumer_offsets`.
- Consumer chỉ commit sau khi xử lý xong.

### 15. Khi nào cần DLQ (Dead Letter Queue)?
- Khi consumer gặp message lỗi không thể xử lý.
- Gửi message lỗi vào DLQ để phân tích sau.

---

## III. Tối ưu và vận hành

### 16. Kafka scale thế nào?
- Scale **horizontal** bằng broker, partition, consumer.
- Có thể xử lý hàng triệu message/sec nếu setup đúng.

### 17. Monitor Kafka bằng gì?
- Dùng **Prometheus + Grafana**, hoặc Kafka UI, Confluent Control Center.
- Monitor **lag**, **disk**, **throughput**, **partition skew**.

### 18. Partition hot → xử lý?
- Sử dụng **key phân tán đều**.
- Dùng thêm partition hoặc routing key phụ.

### 19. Lag cao → debug?
- Kiểm tra `lag`, `consumer throughput`, `rebalance logs`.
- Có thể do xử lý chậm, batch lớn, hoặc consumer chết.

### 20. Auto-rebalance rủi ro gì?
- Mất thời gian, có thể **duplicate** message nếu chưa commit.
- Gây **downtime tạm thời**.

### 21. Kafka Connect vs Streams?
- Connect: import/export từ DB, S3, etc.
- Streams: xử lý dữ liệu realtime trong app Java.

### 22. Kafka trên K8s?
- Dùng Helm chart (`bitnami/kafka`, `strimzi`).
- Dùng StatefulSet + PV + Headless service.

---

## IV. Dự án thực tế

### 23. E-commerce sẽ chia gì?
- **OrderService** → gửi `order.created`
- **InventoryService** → nhận `order.created`, cập nhật tồn kho
- **EmailService** → nhận sự kiện gửi mail

### 24. Duplicate → xử lý?
- Dùng **idempotent logic** (ví dụ: check orderId trước khi insert)
- Sử dụng **transactional producer** nếu cần

### 25. Sự cố gặp phải?
- Consumer lag cao do commit offset quá chậm.
- Kafka rebalance liên tục do crash.
- Producer thiếu key → gây hot partition.

### 26. Scale app từ 1 → 50 instance?
- Cần tăng partition → để mỗi consumer có thể nhận riêng.
- Kafka chỉ cho **1 consumer / partition** trong 1 group.

### 27. Kafka cho command hay event?
- Kafka lý tưởng cho **event**.
- Command → nên dùng HTTP / synchronous request.

---

## V. Câu hỏi cực khó (chuyên sâu kiến trúc)

### 28. Làm sao chống mất dữ liệu khi consumer chết?
- Dùng **manual commit offset sau khi xử lý thành công**.
- Dùng retry + DLQ.

### 29. Versioning topic schema?
- Dùng **Schema Registry** (Avro/Protobuf).
- Chọn strategy: backward / forward compatible.

### 30. Đồng bộ Kafka giữa 2 DC?
- Dùng **MirrorMaker 2.0**, hoặc Confluent Replicator.
- Cần config replication + topic whitelist.

### 31. Kafka + CQRS/Event Sourcing?
- Kafka đóng vai trò **event store**.
- Command → ghi vào Kafka.
- Read model cập nhật thông qua consumer.

### 32. Gossip protocol và ISR shrinkage?
- Kafka không dùng gossip, nhưng ISR sẽ bị shrink nếu broker chậm quá `replica.lag.time.max.ms`
- Nếu leader không thấy follower trong ISR → xóa nó khỏi ISR

---

## ✅ Tổng kết
Kafka là nền tảng mạnh mẽ, nhưng để khai thác tối đa, bạn cần:
- Hiểu rõ partition, consumer group, commit
- Thiết kế topic, schema, và consumer logic đúng chuẩn
- Theo dõi hiệu năng và scale hợp lý

👉 Khi cần tư vấn thiết kế thực tế, sơ đồ kiến trúc hoặc migration từ MQ khác → cứ hỏi nhé!

