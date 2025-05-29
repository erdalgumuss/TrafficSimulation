package model;

/**
 * TrafficLight sınıfı, her yön için bir trafik ışığını temsil eder.
 * Işığın yönü, yeşil ışık süresi, döngü içindeki başlangıç zamanı (offset)
 * ve anlık ışık durumu (kırmızı, sarı, yeşil) bilgilerini içerir.
 */
public class TrafficLight {
    private final Direction direction;      // Bu ışığın hangi yönü kontrol ettiği
    private LightState state;               // Anlık ışık durumu (RED, YELLOW, GREEN)
    private int greenDuration;              // Yeşil ışığın süresi (saniye)
    private int phaseOffset;                // Bu ışığın kavşak döngüsündeki başlangıç konumu (saniye cinsinden offset)

    // Sabit süreler
    public static final int YELLOW_DURATION = 3;     // Sarı ışık süresi (saniye)
    public static final int CYCLE_DURATION = 120;    // Tam döngü süresi (her yönün sırasıyla aldığı toplam zaman)

    /**
     * Trafik ışığı nesnesi oluşturur.
     *
     * @param direction     Hangi yön için bu ışık çalışacak
     * @param greenDuration Bu ışığın yeşil süresi
     * @param phaseOffset   Döngü içindeki konumu (başlangıç zamanı)
     */
    public TrafficLight(Direction direction, int greenDuration, int phaseOffset) {
        this.direction = direction;
        this.greenDuration = greenDuration;
        this.phaseOffset = phaseOffset;
        this.state = LightState.RED;
    }

    /**
     * Işığın güncel durumunu hesaplar ve ayarlar.
     * Belirli bir zaman noktasında ışığın RED, GREEN veya YELLOW olması gerekir.
     *
     * @param now        Şu anki zaman (nanoseconds cinsinden)
     * @param startTime  Simülasyonun başladığı zaman
     */
    public void update(long now, long startTime) {
        long elapsedSec = (now - startTime) / 1_000_000_000; // simülasyonun başından geçen süre (saniye)
        long cyclePos = (elapsedSec - phaseOffset + CYCLE_DURATION) % CYCLE_DURATION;

        if (cyclePos < greenDuration) {
            state = LightState.GREEN;
        } else if (cyclePos < greenDuration + YELLOW_DURATION) {
            state = LightState.YELLOW;
        } else {
            state = LightState.RED;
        }
    }

    /**
     * Işığın mevcut döngüde kalan süresini verir.
     * Bu, kullanıcıya ya da görselleştirmeye kalan süreyi göstermek için kullanılabilir.
     *
     * @param now        Şu anki zaman (nanoseconds)
     * @param startTime  Simülasyon başlangıç zamanı
     * @return           Bu ışığın mevcut durumunda kalacağı kalan saniye
     */
    public long getRemainingTime(long now, long startTime) {
        long elapsedSec = (now - startTime) / 1_000_000_000;
        long cyclePos = (elapsedSec - phaseOffset + CYCLE_DURATION) % CYCLE_DURATION;

        long greenStart = 0;
        long yellowStart = greenStart + greenDuration;
        long redStart = yellowStart + YELLOW_DURATION;

        if (cyclePos < greenDuration) {
            return greenDuration - cyclePos;
        } else if (cyclePos < yellowStart + YELLOW_DURATION) {
            return (yellowStart + YELLOW_DURATION) - cyclePos;
        } else {
            return CYCLE_DURATION - cyclePos;
        }
    }


    // === Getter ve Setter metodları ===

    public LightState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public void setGreenDuration(int duration) {
        this.greenDuration = duration;
    }

    public void setPhaseOffset(int offset) {
        this.phaseOffset = offset;
    }
}
