package model;

/**
 * Araç tiplerini temsil eden enum sınıfıdır.
 * Her modelin görsel dosyası, boyutları, dönüş açısı ve hız aralığı tanımlanır.
 */
public enum CarModel {

    // 🚗 Temel binek araçlar
    SEDAN("sedan_Car.png", 24, 53, 90,     1.4, 2.2),
    SUV("suv_Car.png", 34, 58, 90,         1.1, 1.8),
    SPORT("sport_Car.png", 54, 27, 180,    1.5, 2.5),
    COUPE("coupe_Car.png", 27, 50, -90,    1.3, 2.1);

    // (İsteğe bağlı olarak ileride eklenebilir):
    // F1("f1_Car.png", 18, 57, -90, 3.5, 5.2),
    // BUS("bus_Car.png", 80, 22, 0, 0.8, 1.4);

    // === Özellikler ===
    private final String imageFile;      // Araca ait resim dosyası
    private final double width;          // Araç genişliği (pixel)
    private final double height;         // Araç yüksekliği (pixel)
    private final double baseRotation;   // Aracın kendi yönüne göre varsayılan dönüşü
    private final double minSpeed;       // Min hız (birim/frame)
    private final double maxSpeed;       // Max hız (birim/frame)

    /**
     * Enum sabiti oluşturucu.
     */
    CarModel(String imageFile, double width, double height, double baseRotation,
             double minSpeed, double maxSpeed) {
        this.imageFile = imageFile;
        this.width = width;
        this.height = height;
        this.baseRotation = baseRotation;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    // === Getter Metotları ===

    public String getImageFile() {
        return imageFile;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getBaseRotation() {
        return baseRotation;
    }

    /**
     * Belirtilen hız aralığında rastgele bir hız döndürür.
     */
    public double getRandomSpeed() {
        return minSpeed + Math.random() * (maxSpeed - minSpeed);
    }
}
