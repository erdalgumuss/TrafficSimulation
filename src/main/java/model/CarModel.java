package model;

public enum CarModel {
    //STANDARD("standard_Car.png", 60, 22, 0,    1.2, 2.0),   // %50 büyütme
    SEDAN("sedan_Car.png", 24, 53, 90,         1.4, 2.2),   // %50 büyütme
    SUV("suv_Car.png", 34, 58, 90,             1.1, 1.8),   // %33 büyütme
    SPORT("sport_Car.png", 54, 27, 180,          1.5, 2.5),   // %50 büyütme
    COUPE("coupe_Car.png", 27, 50, -90,        1.3, 2.1),   // %50 büyütme
    F1("f1_Car.png", 18, 57, -90,               3.5, 5.2),   // %50 büyütme
    BUS("bus_Car.png", 80, 22, 0,              0.8, 1.4);   // Ağır araç

    private final String imageFile;
    private final double width;
    private final double height;
    private final double baseRotation;
    private final double minSpeed;
    private final double maxSpeed;

    CarModel(String imageFile, double width, double height, double baseRotation,
             double minSpeed, double maxSpeed) {
        this.imageFile = imageFile;
        this.width = width;
        this.height = height;
        this.baseRotation = baseRotation;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

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

    public double getRandomSpeed() {
        return minSpeed + Math.random() * (maxSpeed - minSpeed);
    }
}
