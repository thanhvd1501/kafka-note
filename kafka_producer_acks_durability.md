
# Kafka Producer Acknowledgements & Topic Durability

## 1. Acknowledgements (acks) – Producer xác nhận ghi

Khi Producer gửi một message tới Kafka, nó cần biết **Kafka đã xử lý thành công chưa**. Tham số `acks` quyết định cách Producer nhận phản hồi từ Kafka.

### acks có 3 giá trị chính:

| Giá trị | Ý nghĩa | Độ an toàn |
|--------|---------|------------|
| `acks=0` | Producer **không chờ phản hồi** từ Kafka → nhanh nhất, **dễ mất dữ liệu** | ❌ Rất rủi ro |
| `acks=1` | Kafka leader **xác nhận đã ghi message** → nếu leader chết sau đó, **data có thể mất** | ⚠️ Trung bình |
| `acks=all` hoặc `-1` | Kafka chỉ xác nhận khi **tất cả các replica** (ISR - In-Sync Replicas) đã ghi xong | ✅ An toàn nhất |

> ISR (In-Sync Replica): Các replica **cập nhật kịp thời** với leader, sẵn sàng takeover nếu leader chết.

## 2. min.insync.replicas – Đảm bảo tối thiểu số bản sao được ghi

Đây là cấu hình **broker-level hoặc topic-level**, giúp **kết hợp với acks=all** để tăng tính an toàn.

- Nếu `acks=all` mà số replica ghi thành công < `min.insync.replicas` → Kafka sẽ từ chối ghi.
- Điều này ngăn ghi dữ liệu khi cluster không đủ tính ổn định.

> Ví dụ: `min.insync.replicas=2` và có 3 replica → nếu chỉ 1 replica online thì message sẽ **không được ghi**.

## 3. Topic Durability – Độ bền dữ liệu của Topic

Topic durability là khả năng **giữ message an toàn và tồn tại sau lỗi hệ thống**.

### Cách đảm bảo durability:

1. **Replication**: Luôn cấu hình topic có **replication-factor ≥ 2** để tránh mất dữ liệu khi 1 node chết.
2. **Log retention**: Thiết lập thời gian giữ log phù hợp (`log.retention.hours`, `log.retention.bytes`, v.v.).
3. **acks=all + min.insync.replicas ≥ 2**: Đảm bảo producer ghi chỉ thành công khi dữ liệu được replicate đủ.

## 4. Message Loss Scenarios & Durability Solutions

| Tình huống | Giải pháp |
|-----------|-----------|
| Node leader bị crash | Replication + ISR |
| Network partition | Kafka ISR loại bỏ replica lỗi thời |
| Broker chưa ghi xong đã ack | Dùng `acks=all`, không dùng `acks=1` |
| Producer gửi nhanh quá | Sử dụng `linger.ms`, `batch.size` để tối ưu |

## 5. Ví dụ cấu hình Producer an toàn

```properties
acks=all
retries=5
enable.idempotence=true
```

- `acks=all`: Chờ tất cả replica xác nhận.
- `retries=5`: Thử lại nếu gửi thất bại.
- `enable.idempotence=true`: Tránh gửi trùng khi retry → đảm bảo **exactly-once** gửi.

## 6. Tóm tắt

| Khái niệm | Ý nghĩa |
|----------|---------|
| `acks=0` | Không xác nhận → nhanh, dễ mất dữ liệu |
| `acks=1` | Leader xác nhận → vẫn có rủi ro |
| `acks=all` | Ghi đủ replica mới xác nhận → an toàn |
| `min.insync.replicas` | Yêu cầu tối thiểu số replica online để ghi |
| Replication | Dữ liệu tồn tại trên nhiều broker |
| Idempotent Producer | Tránh gửi trùng khi retry |

