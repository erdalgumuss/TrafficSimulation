# 📐 PROJECT_STRUCTURE.md
## Trafik Işığı Simülasyonu | Teknik Yapı ve Mimarisi

### 📁 Dizin Yapısı ve Açıklamaları

```
TrafficSimulation/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/          → Simülasyon mantığı ve yöneticiler
│       │   ├── model/               → Veri sınıfları ve enum'lar
│       │   ├── util/                → Sabitler, yardımcı fonksiyonlar
│       │   ├── view/                → JavaFX render ve UI sınıfları
│       │   ├── png/                 → Araç görselleri
│       │   └── Main.java            → Ana uygulama girişi
│       └── resources/
│           └── app.css              → Stil dosyası
│
├── README.md                        → Genel açıklama, kullanım, kurulum
├── PROJECT_STRUCTURE.md             → Bu dosya: mimari ve sınıf açıklamaları
└── pom.xml                          → Maven yapılandırması
```

---

## 🎯 Uygulama Katmanları (Katmanlı Mimari)

Bu proje klasik MVC yapısı temel alınarak, yöneticilerle (manager/controller ayrımı) genişletilmiştir.

### 1. `model/` → Veri ve Davranış Katmanı

| Sınıf / Enum | Açıklama |
|--------------|----------|
| `Vehicle`     | Araç nesnesi: yön, hız, konum bilgisi taşır, `updatePosition()` ile hareket eder |
| `TrafficLight`| Işık nesnesi: yön, ışık durumu (`LightState`), zaman bilgisi içerir |
| `Direction`   | Dört yön: `NORTH`, `SOUTH`, `EAST`, `WEST` |
| `LightState`  | Trafik ışığı renk durumu: `RED`, `YELLOW`, `GREEN` |
| `CarModel`    | Araç tipi (`sedan`, `coupe`, `f1` vb.) – görselle bağlantılı |

### 2. `controller/` → Mantık ve Yönetici Katmanı

| Sınıf | Sorumluluk |
|-------|------------|
| `TrafficSimulationManager`   | Tüm sistemi yöneten ana sınıf |
| `VehicleSimulationManager`   | Araç üretimi ve zamanlamasını kontrol eder |
| `VehicleController`          | Araç hareketleri, ışık durumu ve çarpışma kontrolü |
| `TrafficLightController`     | 120 sn döngüye göre ışık geçişlerini hesaplar |
| `VehicleFactory`             | Yeni araç oluşturur (`generateSingleVehicle`) |

### 3. `util/` → Sabitler ve Yardımcılar

| Sınıf | Açıklama |
|-------|----------|
| `SimConstants`              | Sabitler: ışık konumları, kavşak sınırları, sahne boyutları |
| `TrafficSignalCalculator`  | Yoğunluğa göre yeşil süre hesaplaması (min/max sınırlı) |

### 4. `view/` → Kullanıcı Arayüzü ve Çizim

| Sınıf | Sorumluluk |
|-------|------------|
| `MainScene`             | Ana canvas sahnesi – ışıklar, araçlar ve kavşak burada çizilir |
| `VehicleRenderer`       | Tüm araçları çizer |
| `TrafficLightRenderer`  | Işıkları fiziksel olarak sahneye yerleştirir |
| `IntersectionRenderer`  | Yol ve kavşak arka planını çizer |
| `SimulationControlPanel`| Kullanıcıdan yoğunluk verisi alır, simülasyonu başlatır |
| `TrafficObserverPanel`  | Anlık yoğunluk/durum göstergesi (devam ediyor) |

### 5. `png/` → Araç Görselleri

Görsel tanımlı araçlar için:

```
bus_Car.png
coupe_Car.png
f1_Car.png
sedan_Car.png
sport_Car.png
suv_Car.png
```

> Bu görseller `CarModel` enum’una bağlanarak araçlara atanacaktır.

---

## 🧪 Simülasyon Akışı

1. Kullanıcı, `SimulationControlPanel` üzerinden her yön için araç sayısı girer
2. `TrafficSimulationManager` başlatılır → ışık süreleri hesaplanır
3. Her saniyede bir yönden bir araç üretilir (`VehicleSimulationManager`)
4. Araç:
   - Öndeki araçla mesafesi yeterliyse
   - Işık **yeşilse** geçer, **kırmızı/sarıysa** durur
5. `MainScene` her karede (`frame`) sahneyi günceller:
   - Kavşak, ışıklar ve araçlar yeniden çizilir

---

## 🔜 Geliştirme Listesi (To-Do)

| Geliştirme | Durum |
|------------|-------|
| Araçlara görsel atanması (`CarModel`) | ⏳ Planlandı |
| Arka plan geometrisinin görselleştirilmesi | ⏳ Planlandı |
| Işık geçiş efektleri (animasyon) | ⏳ Planlandı |
| `TrafficObserverPanel`: canlı yoğunluk ve bekleme sayısı | 🔄 Geliştiriliyor |
| Simülasyon loglama (CSV, JSON) | 🔲 Planlandı |
| Test modülü (JUnit) | 🔲 Planlandı |
| Performans ölçümü (FPS, araç bekleme kuyruğu) | 🔲 Geliştirilebilir |

---

## 📌 Teknik Notlar

- Tüm zaman hesaplamaları `System.nanoTime()` üzerinden yapılır
- Her araç yönü için üretim limiti, kullanıcıdan alınan yoğunlukla sınırlıdır
- Işıklar sırayla `GREEN → YELLOW → RED` olarak döner, 120 saniyelik çevrim içinde
- Yönler sırayla kontrol edilir, eş zamanlı üretim yapılmaz