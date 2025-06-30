# Kafka Consumers in a Group

## 🧠 Giới thiệu

Trong Apache Kafka, **Consumer Group** là cơ chế cốt lõi giúp **cân bằng tải**, **tăng khả năng mở rộng**, và **bảo đảm mỗi message chỉ được xử lý một lần trong một group**. Khi nhiều consumer cùng tham gia một nhóm (group), Kafka tự động **phân chia các partition** của topic cho từng consumer trong nhóm đó.

---

## 🔁 Cách hoạt động của Consumer Group

- Mỗi **consumer group** có một **group.id** duy nhất.
- Một **topic** có thể có nhiều **partition**.
- Kafka đảm bảo **mỗi partition chỉ được xử lý bởi đúng 1 consumer trong group**, tại một thời điểm.
- **Các group khác nhau có thể cùng đọc từ một topic** mà không ảnh hưởng đến nhau.

### Ví dụ:

Giả sử:

- Topic `orders` có 3 partitions: P0, P1, P2.
- Bạn có Consumer Group `order-group` với 3 consumer: C1, C2, C3.

Kết quả:
- P0 → C1  
- P1 → C2  
- P2 → C3

Nếu bạn thêm C4 vào nhóm, một trong các consumer sẽ idle, vì Kafka chỉ có 3 partitions và 3 consumer đang sử dụng hết.

---

## 📦 Ưu điểm của Consumer Group

- ✅ **Scalability**: Có thể thêm nhiều consumer để xử lý dữ liệu song song.
- ✅ **Fault-tolerance**: Nếu một consumer bị lỗi, Kafka sẽ tự động **rebalance** và gán lại partition cho các consumer còn lại.
- ✅ **Load Balancing**: Kafka tự phân phối workload giữa các consumer.

---

## 📚 Một số khái niệm liên quan

| Khái niệm | Giải thích |
|----------|------------|
| `group.id` | ID duy nhất xác định một group |
| Offset | Vị trí của consumer trong partition (được lưu cho từng group) |
| Rebalance | Quá trình Kafka phân phối lại partitions khi số lượng consumer thay đổi |

---

## 🔄 Quá trình Rebalance

Khi có sự thay đổi trong nhóm consumer (một consumer rời đi hoặc mới được thêm), Kafka sẽ **rebalance** để đảm bảo mỗi partition vẫn được gán cho một consumer duy nhất trong nhóm.

Trong quá trình này:
- Consumer cũ dừng lại.
- Kafka sử dụng **Kafka Coordinator** để phân phối lại partition.
- Các consumer nhận nhiệm vụ mới và bắt đầu lại từ offset đã lưu.

---

## 🛑 Lưu ý khi sử dụng

- Mỗi partition chỉ được một consumer trong group xử lý. Nếu có nhiều consumer hơn partition, một số consumer sẽ **không nhận dữ liệu**.
- Quá trình **rebalance có thể gây trễ tạm thời** trong xử lý.
- Offset được lưu riêng cho từng **group-topic-partition** (cho phép nhiều group cùng đọc topic khác nhau).

---

## ✅ Tóm tắt

| Thuộc tính | Mô tả |
|------------|-------|
| Một group | Nhiều consumer |
| Một partition | Chỉ được đọc bởi 1 consumer trong group |
| Offset | Quản lý riêng theo group |
| Rebalance | Tự động xảy ra khi group thay đổi |
| Group độc lập | Không ảnh hưởng nhau khi cùng đọc một topic |