package model;

import util.SimConstants;

/**
 * Vehicle (Araç) sınıfı, trafik simülasyonundaki her bir aracın
 * yön, hız, konum ve fiziksel özelliklerini tanımlar.
 * Ayrıca bu sınıf, aracın hareketini ve kavşağa yaklaşma davranışını da yönetir.
 */
public class Vehicle {
    private double x, y;                // Aracın ekrandaki konumu
    private double speed;              // Aracın hızı
    private Direction direction;       // Aracın hareket yönü (NORTH, EAST, vs.)
    private CarModel model;            // Aracın tipi (sedan, suv vs.)
    private boolean active = true;     // Araç aktif mi? (ekran dışına çıktıysa false yapılır)
    private final double width;        // Araç genişliği (modelden alınır)
    private final double height;       // Araç yüksekliği (modelden alınır)

    /**
     * Araç nesnesi oluşturur.
     *
     * @param direction Aracın yönü
     * @param speed     Aracın hızı
     * @param x         Başlangıç X koordinatı
     * @param y         Başlangıç Y koordinatı
     * @param model     Araç modeli
     */
    public Vehicle(Direction direction, double speed, double x, double y, CarModel model) {
        this.direction = direction;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.model = model;
        this.height = model.getHeight();
        this.width = model.getWidth();
    }

    /**
     * Aracın konumunu günceller.
     * Hareket edebilme izni varsa, yönüne göre konumu değiştirir.
     * Eğer araç sahneden çıkmışsa, pasif hale getirilir.
     *
     * @param canMove Bu araç hareket edebilir mi?
     * @param now     Şu anki zaman (nanoseconds)
     */
    public void updatePosition(boolean canMove, long now) {
        if (!active || !canMove) return;

        switch (direction) {
            case EAST:  x += speed; break;
            case WEST:  x -= speed; break;
            case NORTH: y -= speed; break;
            case SOUTH: y += speed; break;
        }

        // Araç ekran dışına çıkarsa aktiflik durumu pasif yapılır
        if (x < -150 || x > 950 || y < -150 || y > 750) {
            active = false;
        }
    }

    // === Get metodları ===

    public boolean isActive() {
        return active;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Direction getDirection() { return direction; }
    public CarModel getModel() { return model; }

    /**
     * Bu araç ile öndeki araç arasındaki mesafeyi hesaplar.
     * Aynı yönde değillerse sonsuz mesafe döner.
     *
     * @param other Öndeki diğer araç
     * @return Mesafe değeri
     */
    public double distanceTo(Vehicle other) {
        if (this.direction != other.direction) return Double.MAX_VALUE;

        switch (direction) {
            case EAST:  return other.x - (this.x + width);
            case WEST:  return this.x - (other.x + width);
            case NORTH: return this.y - (other.y + height);
            case SOUTH: return other.y - (this.y + height);
        }
        return Double.MAX_VALUE;
    }

    /**
     * Araç kavşağa yaklaşmakta mı?
     * Işık konumuna göre tespit yapılır.
     *
     * @return true → yaklaşmakta, false → değil
     */
    public boolean approachingIntersection() {
        Double boundary = SimConstants.LIGHT_BOUNDARIES.get(direction);
        switch (direction) {
            case EAST:  return x + width >= boundary && x < boundary + 10;
            case WEST:  return x <= boundary && x > boundary - 10;
            case NORTH: return y <= boundary && y > boundary - 10;
            case SOUTH: return y + height >= boundary && y < boundary + 10;
        }
        return false;
    }
}
