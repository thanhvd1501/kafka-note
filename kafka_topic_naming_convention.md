# Kafka Topics Naming Convention

## 📌 Tại sao cần đặt tên topic có quy ước?
- Giúp **dễ quản lý và tìm kiếm** trong các hệ thống lớn có hàng trăm topics.
- Tránh trùng lặp hoặc sai mục đích sử dụng topic.
- Hỗ trợ **monitoring**, **alerting** và **phân quyền** hiệu quả.
- Làm việc nhóm hoặc đa team dễ dàng vì ai cũng hiểu ý nghĩa topic.

## 📐 Quy ước đặt tên phổ biến

Một tên topic nên tuân thủ mẫu:

```
<domain>.<subdomain>.<entity>.<action>.<environment>
```

### 🧩 Các thành phần

| Thành phần      | Ý nghĩa                                                    | Ví dụ                      |
|------------------|-------------------------------------------------------------|-----------------------------|
| `domain`         | Hệ thống / dịch vụ chính                                     | `order`, `user`, `payment` |
| `subdomain`      | Dịch vụ phụ, hoặc phân khu chức năng                         | `billing`, `profile`       |
| `entity`         | Đối tượng nghiệp vụ cụ thể                                   | `invoice`, `user`, `cart`  |
| `action`         | Loại sự kiện hoặc hành động                                  | `created`, `updated`, `log`|
| `environment`    | Môi trường (optional nếu có phân vùng cluster)              | `dev`, `test`, `prod`      |

### 🧾 Ví dụ

- `order.payment.invoice.created.prod`
- `user.profile.updated.dev`
- `log.system.error`

## ✅ Quy tắc bổ sung
- Dùng chữ **thường**, phân cách bằng dấu **chấm (`.`)**.
- Tránh viết tắt khó hiểu, nếu viết tắt thì cần **có tài liệu nội bộ**.
- Không dùng ký tự đặc biệt (`@`, `#`, `!`, `?`, etc).
- Nếu sử dụng namespace (ví dụ như `companyX.`) thì thống nhất toàn hệ thống.
- Không nên encode ý nghĩa version vào tên topic (nên version ở schema, không ở tên).

## ❓ Khi nào nên tạo topic mới?
- Khi **ngữ nghĩa khác biệt hoàn toàn** so với topic hiện tại.
- Khi bạn cần **phân quyền khác nhau**.
- Khi cần **retention policy riêng**, ví dụ:
  - Topic log giữ 7 ngày.
  - Topic giao dịch giữ 90 ngày.
