# Báo cáo phân tích — Cache không hoạt động

## a) Nguyên nhân

### Annotation bị thiếu

**`@EnableCaching`** trên class cấu hình chính (`Application`) hoặc `@Configuration` riêng.

### Vì sao chỉ `@Cacheable` chưa đủ?

Spring Cache dựa trên **AOP Proxy**:

1. `@EnableCaching` đăng ký `CacheInterceptor` + advisor.
2. Bean `UserService` được bọc bởi proxy.
3. Khi gọi `getUserById` qua proxy:
   - Cache hit → trả từ cache, **không** vào method.
   - Cache miss → gọi method thật → ghi cache.

Không có `@EnableCaching` → không có interceptor → lời gọi đi thẳng vào method gốc → luôn in `>>> Truy vấn Database...`.

### Vì sao mỗi lần vẫn truy xuất DB?

`@Cacheable` chỉ là metadata. Không bật caching infrastructure thì annotation **không có hiệu lực runtime**.

---

## b) Cách sửa

1. Thêm `@EnableCaching` vào `Application`.
2. Khai báo `CacheManager` (ví dụ `ConcurrentMapCacheManager("users")`).
3. `UserService`:
   - Fail-fast nếu `userId` null/blank.
   - `@Cacheable(..., condition = "#userId != null and #userId != ''", unless = "#result == null")`.

---

## c) Test hit/miss

- `@SpringBootTest` + `@MockBean UserRepository`.
- Lần 1: `verify(repo, times(1)).findById(...)`.
- Lần 2 cùng `userId`: `times(1)` vẫn giữ nguyên (không tăng) → cache hit.

---

## d) Tình huống đặc biệt

### userId null / rỗng

- **Fail-fast:** ném `IllegalArgumentException` trước khi cache.
- **condition:** `#userId != null and #userId != ''` → không tạo key rác.

### Kết quả null

| Có cache null | Không cache null (`unless` / `disableCachingNullValues`) |
|---------------|----------------------------------------------------------|
| Giảm spam DB khi user thật sự không tồn tại | User vừa được tạo sau đó có thể bị “che” bởi null đã cache |
| Rủi ro stale “không có user” | An toàn hơn khi data thay đổi nhanh |

**Đề xuất Fintech tra cứu user:** dùng `unless = "#result == null"` và/hoặc `cacheManager.setAllowNullValues(false)` — không cache null; chấp nhận miss thêm vài lần thay vì trả null giả lâu dài.

Cấu hình tương đương:
```java
@Cacheable(value = "users", key = "#userId",
           condition = "#userId != null and !#userId.isBlank()",
           unless = "#result == null")
```
