# NO23 Sports Club Backend

**NO23 Sports Club** için mikroservis tabanlı backend. Antrenman, beslenme (NO23 Kitchen), topluluk ve yaşam tarzını tek bir üyelik sistemi altında birleştiren dijital ekosistemin sunucu tarafıdır.

Bu depo, ürün vizyonunda (`Specs.md`) tanımlanan platformun backend servislerini içerir.

---

## Mimari Genel Bakış

Sistem **Spring Cloud mikroservis** mimarisi ile kurulmuştur:

| Bileşen | Rol | Port (dahili) |
|---------|-----|---------------|
| **ApiGateway** | Tek genel giriş noktası, CORS, yönlendirme, birleşik Swagger UI | `8080` |
| **ServiceRegistry** | Netflix Eureka servis keşfi | `8761` |
| **AuthService** | Kayıt, giriş (JWT), kullanıcı yönetimi, roller | `8001` |
| **UserProfileService** | Üye profilleri, vücut ölçüleri, beslenme hedefleri | — |
| **LessonService** | Grup dersleri (Bootcamp, Pilates vb.) | — |
| **MembershipService** | Üyelik paketleri (START / PLUS / PRO / ELITE) ve abonelikler | — |
| **InstructorService** | Eğitmenler, uzmanlık alanları, verdikleri dersler | — |
| **ReservationService** | Ders / kişisel antrenman rezervasyonları | — |
| **KitchenMenuService** | Menü ürünleri, makrolar, diyet etiketleri, malzemeler | — |
| **KitchenSubscriptionService** | Kitchen yemek planı abonelikleri (5/10/20 günlük, aylık) | — |
| **MealPlanService** | Hedef ve makrolara göre kişiselleştirilmiş beslenme planları | — |
| **CalorieTrackingService** | Günlük yemek/su kayıtları, kalori ve makro takip paneli | — |
| **PaymentService** | Iyzico ile ödemeler (checkout form + callback) | — |
| **BlogService** | Blog yazıları (antrenman, beslenme, motivasyon…) | — |
| **FaqService** | Sık sorulan sorular | — |
| **SuccessStoryService** | Üye dönüşüm hikâyeleri | — |

Tüm iş servisleri Eureka’ya kaydolur ve **API Gateway** üzerinden `http://localhost:8080` adresiyle erişilir.

**Veritabanı düzeni**: Tek bir PostgreSQL instance, **servis başına ayrı veritabanı** (`infra/init-databases.sh` ile otomatik oluşturulur).

---

## Teknoloji Yığını

- **Java 25**
- **Spring Boot 4.x** + Spring Cloud (Gateway, Eureka)
- **PostgreSQL 17**
- **Spring Data JPA** + Hibernate
- **JWT** kimlik doğrulama (tüm servislerde ortak gizli anahtar)
- **Springdoc OpenAPI** (Gateway üzerinde birleşik Swagger UI)
- **Iyzico** ödeme altyapısı
- **Docker** & **Docker Compose** ile yerel orkestrasyon

---

## Gereksinimler

- Docker ve Docker Compose (v2+)
- (İsteğe bağlı) Servisleri Docker dışında çalıştırmak için JDK 25 + Maven
- Ödeme testleri için Iyzico sandbox hesabı

---

## Hızlı Başlangıç

### 1. Projeyi açın / çıkarın

```bash
# Arşivi aldıysanız:
tar -xf "NO23 Sports Club Backend.tar.xz"
cd "NO23 Sports Club Backend"
```

### 2. Ortam değişkenlerini yapılandırın

```bash
cp .envexample .env
```

`.env` dosyasını düzenleyip gerçek değerleri girin:

```env
POSTGRES_USER=no23
POSTGRES_PASSWORD=<güçlü-bir-şifre>

# Bir kez üretin ve tüm servislerde aynı tutun
JWT_SECRET=$(openssl rand -base64 32)

# CORS için frontend origin
ALLOWED_ORIGIN=http://localhost:3000

# Iyzico sandbox bilgileri
IYZICO_API_KEY=<sandbox-api-anahtarınız>
IYZICO_SECRET_KEY=<sandbox-gizli-anahtarınız>
IYZICO_BASE_URL=https://sandbox-api.iyzipay.com
IYZICO_CALLBACK_URL=http://localhost:8080/payments/callback

FRONTEND_PAYMENT_SUCCESS_URL=http://localhost:3000/payment/success
FRONTEND_PAYMENT_FAILURE_URL=http://localhost:3000/payment/failure
```

> **`.env` dosyasını asla commit etmeyin.** Gitignore ile dışlanmıştır.

### 3. Stack’i başlatın

```bash
docker compose up --build -d
```

Bu komut şunları yapar:

1. PostgreSQL’i başlatır ve her servis için ayrı veritabanı oluşturur
2. Eureka’yı (`service-registry`) ayağa kaldırır
3. Tüm mikroservisleri derleyip başlatır
4. API Gateway’i **8080** portunda dışarı açar

### 4. Kontrol edin

- Eureka paneli: http://localhost:8761  
- Birleşik Swagger UI: http://localhost:8080/swagger-ui.html  
- Gateway: http://localhost:8080  

Servis bazlı OpenAPI dokümanlarına örnek yollar:

- `/auth/v3/api-docs`
- `/lessons/v3/api-docs`
- `/membership/v3/api-docs`
- `/kitchen/menu/v3/api-docs`
- `/kitchen/tracking/v3/api-docs`
- … (tam liste gateway `application.properties` içinde)

### 5. Durdurma

```bash
docker compose down
# Postgres volume’ünü de silmek için:
docker compose down -v
```

---

## Geliştirme Akışı

### Tek bir servisi yerelde çalıştırma

1. Sadece altyapıyı başlatın:

```bash
docker compose up postgres service-registry -d
```

2. Gerekli ortam değişkenlerini export edin (`DB_HOST=localhost`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `EUREKA_URL=http://localhost:8761/eureka`) veya `.env` değerlerini kullanın.

3. İlgili servis dizininde:

```bash
./mvnw spring-boot:run
```

### Sık kullanılan endpoint’ler (Gateway üzerinden)

| Alan | Örnek yollar |
|------|--------------|
| Auth | `POST /auth/register`, `POST /auth/login` |
| Kullanıcılar (admin) | `GET /auth/users`, `PUT /auth/users/{id}/role` |
| Üyelik | `GET /membership/packages`, `POST /membership` |
| Dersler | `GET /lessons`, … |
| Rezervasyonlar | `POST /reservations`, … |
| Kitchen Menü | `GET /kitchen/menu`, `GET /kitchen/menu?category=…` |
| Beslenme Planları | `POST /kitchen/meal-plans` |
| Kalori Takibi | `POST /kitchen/tracking/meals`, `GET /kitchen/tracking/summary/user/{userId}` |
| Ödemeler | `/payments` altında Iyzico checkout + callback |
| Blog / SSS / Başarı Hikâyeleri / Eğitmenler | ilgili önekler |

Korumalı endpoint’ler `Bearer <JWT>` header’ı bekler (token AuthService tarafından verilir).

---

## Proje Yapısı

```
NO23 Sports Club Backend/
├── docker-compose.yml          # Tam yerel stack
├── .envexample                 # Ortam şablonu
├── infra/
│   └── init-databases.sh       # Servis başına veritabanı oluşturur
├── ApiGateway/
├── ServiceRegistry/
├── AuthService/
├── UserProfileService/
├── LessonService/
├── MembershipService/
├── InstructorService/
├── ReservationService/
├── KitchenMenuService/
├── KitchenSubscriptionService/
├── MealPlanService/
├── CalorieTrackingService/
├── PaymentService/
├── BlogService/
├── FaqService/
└── SuccessStoryService/
```

Her servis standart Spring Boot düzenini takip eder (`src/main/java/...`, `application.properties`, Dockerfile, Maven wrapper).

---

## Ürün Spesifikasyonuna Uyum ve Eksik Kalanlar

Mevcut backend, yol haritasının **Faz 1–2**’si için sağlam bir temel sunar (kurumsal site desteği, üyelik paketleri, Kitchen tanıtımı, online başvuru, temel ödeme, üye paneli yapı taşları, rezervasyon, kalori takibi, beslenme planları).

Aşağıda `Specs.md` ile karşılaştırıldığında eksik / tamamlanmamış parçalar listelenmiştir.

### Uygulanmış olanlar (backend desteği mevcut)

- Kimlik doğrulama ve rol bazlı erişim (üye / admin)
- Üyelik paketleri ve abonelik yaşam döngüsü (abone ol / dondur / devam ettir / iptal)
- Dersler / grup dersleri
- Eğitmenler
- Rezervasyonlar
- NO23 Kitchen menü (ürünler, makrolar, diyet etiketleri, kategoriler)
- Kitchen abonelikleri
- Kişiselleştirilmiş beslenme planları
- Kalori ve su takibi + günlük özet (üye paneli “Kalori Takip Paneli”)
- Kullanıcı profilleri ve beslenme hedefi hesaplama
- Ödemeler (Iyzico)
- Blog, SSS, Başarı Hikâyeleri
- Paketler, menü öğeleri, kullanıcılar üzerinde temel admin işlemleri

### Eksik veya Eksik Tamamlanmış Olanlar (Specs’e göre)

#### 1. NO23 Shop (tamamen eksik)
- Ürün kataloğu (tişört, hoodie, shaker, direnç bandı, foam roller…)
- Stok yönetimi
- Sepet ve sipariş akışı
- Kampanyalar / indirim kodları
- Sipariş takibi

→ Yeni bir `ShopService` (veya benzeri) + mevcut ödeme entegrasyonunun yeniden kullanımı gerekir.

#### 2. NO23 Community
- Challenge’lar
- Etkinlikler / workshop’lar / outdoor aktiviteler
- Koşu / bisiklet grupları
- Üye başarı hikâyelerinin daha derin entegrasyonu ve sosyal özellikler

→ `CommunityService` (etkinlikler, challenge’lar, RSVP vb.) gerekir.

#### 3. Tam Kitchen Online Sipariş ve Lojistik
- Tek seferlik sipariş için sepet
- Teslimat adresi yönetimi
- Teslim günü ve saat dilimi seçimi
- Günlük üretim / mutfak sipariş ekranı
- Teslimat planlama ve durum takibi

Mevcut servisler menü + abonelik + beslenme planını kapsar; ancak tam sipariş → üretim → teslimat hattı henüz yoktur.

#### 4. Gelişmiş Üye Paneli özellikleri
- Vücut ölçüm geçmişi ve ilerleme grafikleri (profil + kalori takibi ile kısmen mevcut)
- Faturalar ve tam ödeme geçmişi
- Sadakat puanları / kazanılan ödüller
- Kampanya görünürlüğü
- Atanan eğitmenle doğrudan mesajlaşma
- Aktif Kitchen aboneliğinin tek yerden özeti

#### 5. CRM ve Pazarlama (Admin)
- Push / uygulama içi bildirimler
- E-posta ve SMS kampanyaları
- Sadakat / puan motoru
- Hediye çekleri
- Referans programı
- Satış ve performans panoları / raporlar

#### 6. Herkese açık Kalori Hesaplayıcı
Specs, bağımsız bir hesaplayıcı tanımlar (boy, kilo, yaş, cinsiyet, aktivite, hedef → günlük makrolar + paket önerileri).  
Beslenme hedefleri UserProfile / MealPlan akışlarında mevcuttur; ancak ayrı bir public endpoint ve öneri mantığı hâlâ gerekebilir.

#### 7. Alana özel içerik
- Kids Club detayları
- Atletik Performans takip özellikleri
- Daha zengin ders meta verisi (süre, zorluk, ortalama yakılan kalori, kimler için uygun…) — Lesson modeline göre kısmen mevcut olabilir

#### 8. Frontend / İstemci
Bu depo **yalnızca backend**’dir. Tam web sitesi (video arka planlı ana sayfa, üyelik karşılaştırma, Kitchen arayüzü, üye paneli, admin paneli) ve ilerideki mobil uygulamalar burada yer almaz.

#### 9. Uzun vadeli yol haritası (Faz 3)
- Yerel mobil uygulamalar (iOS / Android)
- Akıllı saat entegrasyonları
- Yapay zekâ destekli antrenman ve beslenme önerileri
- Franchise / çok şubeli yapı desteği

---

## Önerilen Sonraki Adımlar

1. **ShopService** + sepet/sipariş modeli  
2. **CommunityService** (etkinlikler, challenge’lar)  
3. Kitchen tarafını sepet → sipariş → üretim → teslimat ile genişletme  
4. Sadakat / puan + referans temel yapıları  
5. Bildirim servisi (e-posta / SMS / push)  
6. Daha zengin admin raporlama endpoint’leri  
7. Üyelik / Kitchen paketlerini de öneren public kalori hesaplayıcı endpoint’i  
8. Gateway’i tüketen frontend uygulaması  

---

## API Dokümantasyonu

Stack çalışırken şu adresi açın:

**http://localhost:8080/swagger-ui.html**

Tüm kayıtlı servislerin birleşik görünümünü göreceksiniz. “Authorize” butonu ile `/auth/login` veya `/auth/register` üzerinden aldığınız JWT’yi kullanın.

---

## Lisans ve Sahiplik

Tescilli — NO23 Sports Club.  
Tüm hakları saklıdır.

---

*Bu README, mevcut backend’i belgelemek ve ürün spesifikasyonuna göre eksik kalan parçaları net bir şekilde ortaya koymak amacıyla hazırlanmıştır; böylece ekip bir sonraki geliştirme fazlarını önceliklendirebilir.*
