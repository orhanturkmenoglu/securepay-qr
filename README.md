# 🚀 SecurePay QR - Modern & Güvenli Ödeme Platformu

**Stripe ile entegre, gerçek zamanlı ödeme takibi ve otomatik QR kod üretimi sağlayan, güvenli ve ölçeklenebilir Spring Boot tabanlı ödeme platformu.**

---

## 🛠 Proje Amacı

Bu proje, Stripe ödeme altyapısını kullanarak Spring Boot üzerinde güvenli, hızlı ve dinamik bir ödeme sistemi sunmayı amaçlar.  
Kullanıcılar için oluşturulan dinamik QR kodlar ile kolay ödeme imkanı sağlar.  
Ödeme durumları anlık olarak Stripe’dan çekilir, güncellenir ve veritabanında kayıt altına alınır.  
Böylece hem müşteriler hem de işletmeler için şeffaf ve sorunsuz bir ödeme deneyimi oluşturulur.


## ⚡ Kullanım Akışı

- **Ödeme Oluşturma:** Kullanıcı ödeme detaylarını gönderir, Stripe üzerinde ödeme oturumu oluşturulur ve buna özel QR kodu üretilir.  
- **QR Kod ile Ödeme:** Kullanıcı QR kodu taratarak hızlıca ödemeyi gerçekleştirir.  
- **Ödeme Takibi:** Sistem düzenli aralıklarla Stripe API’si ile ödeme durumlarını kontrol eder ve günceller.  
- **API Erişimi:** REST API üzerinden ödeme oluşturma ve QR kod görseline erişim sağlanabilir.


## 🌟 Özellikler

- 🔐 **Güvenli Ödeme:** Stripe Checkout Session ile güvenli ödeme oturumu oluşturma  
- 🖼️ **QR Kod Üretimi:** Ödeme URL'sine özel dinamik QR kodlar  
- ⏱️ **Otomatik Güncelleme:** 30 saniyede bir ödeme durumlarını kontrol edip güncelleme  
- 🗂️ **Veritabanı Entegrasyonu:** Ödemeler ve durumları kalıcı olarak saklanır  
- 📊 **Kolay Yönetim:** Basit loglama ile ödeme süreci takibi
- 💱 **Çoklu Para Birimi Desteği:** USD, EUR gibi farklı para birimleriyle ödeme alınabilir

---

## 🛠️ Teknolojiler

- ☕ **Java 17+**  
- 🌱 **Spring Boot (Data JPA, Scheduling)**  
- 💳 **Stripe Java SDK**  
- 📱 **ZXing - QR Code Generator**  
- 📦 **Lombok**  
- 📝 **Slf4j Logging**  

---


   ## 🔌 API Endpointleri

| HTTP Metodu | URL               | Açıklama                          | Request Body         | Response                  |
|-------------|-------------------|---------------------------------|---------------------|---------------------------|
| POST        | `/api/payments`   | Yeni ödeme oluşturur, QR kod ve ödeme linki döner | `Payment` JSON objesi | `PaymentResponseDTO` (checkout URL + QR kod base64) |
| GET         | `/api/payments/qr/{token}` | Belirtilen token'a ait QR kod görselini PNG olarak döner | -                   | PNG formatında QR kod byte dizisi  |

---

### Örnek `createPayment` isteği

```json
POST /api/payments
Content-Type: application/json

{
  "amount": 10000,
  "currency": "usd",
  "description": "IPHONE 13"
}


## ⚙️ Kurulum & Çalıştırma

1. **Depoyu klonla:**  
   ```bash
   git clone https://github.com/orhanturkmenoglu/securepay-qr.git
   cd securepay-qr
