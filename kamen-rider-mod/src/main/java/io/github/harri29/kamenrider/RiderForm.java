package io.github.harri29.kamenrider;

public enum RiderForm {
    KUUGA("kuuga", "rider.kamenrider.kuuga", 1, 2, 1, 1, 24.0F, 1.00F),
    DECADE("decade", "rider.kamenrider.decade", 1, 2, 1, 0, 28.0F, 1.12F),
    DOUBLE("double", "rider.kamenrider.double", 2, 1, 0, 1, 22.0F, 1.24F),
    GEATS("geats", "rider.kamenrider.geats", 2, 2, 0, 1, 26.0F, 1.36F);

    private final String id;
    private final String translationKey;
    private final int speedAmplifier;
    private final int strengthAmplifier;
    private final int resistanceAmplifier;
    private final int jumpAmplifier;
    private final float kickDamage;
    private final float soundPitch;

    RiderForm(String id, String translationKey, int speedAmplifier, int strengthAmplifier,
              int resistanceAmplifier, int jumpAmplifier, float kickDamage, float soundPitch) {
        this.id = id;
        this.translationKey = translationKey;
        this.speedAmplifier = speedAmplifier;
        this.strengthAmplifier = strengthAmplifier;
        this.resistanceAmplifier = resistanceAmplifier;
        this.jumpAmplifier = jumpAmplifier;
        this.kickDamage = kickDamage;
        this.soundPitch = soundPitch;
    }

    public String id() { return id; }
    public String translationKey() { return translationKey; }
    public int speedAmplifier() { return speedAmplifier; }
    public int strengthAmplifier() { return strengthAmplifier; }
    public int resistanceAmplifier() { return resistanceAmplifier; }
    public int jumpAmplifier() { return jumpAmplifier; }
    public float kickDamage() { return kickDamage; }
    public float soundPitch() { return soundPitch; }
}
