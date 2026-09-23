# Bài 2 Session 16 — Cache không hoạt động (thiếu @EnableCaching)

## Nguyên nhân ngắn

Thiếu **`@EnableCaching`** → Spring không tạo AOP proxy / `CacheInterceptor` → `@Cacheable` bị bỏ qua → mỗi lần gọi vẫn query DB.

Báo cáo đầy đủ: `BAO_CAO_PHAN_TICH.md`

## Đã sửa

- `@EnableCaching` trên Application
- `ConcurrentMapCacheManager` bean
- `@Cacheable` có `condition` (bỏ qua userId null/blank) + `unless = "#result == null"`
- Test: lần 1 gọi repo, lần 2 không gọi lại
