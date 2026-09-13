package io.github.harri29.kamenrider;

/**
 * Gameplay definitions for Rider forms.
 * Stats are intentionally data-like so more forms can be added without
 * duplicating transformation logic.
 */
public enum RiderForm {
    KUUGA_MIGHTY("kuuga_mighty", "kuuga", "rider.kamenrider.kuuga_mighty", 1, 2, 1, 1, 24.0F, 1.00F),
    KUUGA_DRAGON("kuuga_dragon", "kuuga", "rider.kamenrider.kuuga_dragon", 2, 1, 0, 3, 20.0F, 1.08F),
    KUUGA_PEGASUS("kuuga_pegasus", "kuuga", "rider.kamenrider.kuuga_pegasus", 2, 1, 0, 2, 22.0F, 1.16F),
    KUUGA_TITAN("kuuga_titan", "kuuga", "rider.kamenrider.kuuga_titan", 0, 3, 2, 0, 30.0F, 0.92F),

    DECADE("decade", "decade", "rider.kamenrider.decade", 1, 2, 1, 0, 28.0F, 1.12F),

    DOUBLE_CYCLONE_JOKER("double_cyclone_joker", "double", "rider.kamenrider.double_cyclone_joker", 2, 1, 0, 1, 22.0F, 1.24F),
    DOUBLE_HEAT_METAL("double_heat_metal", "double", "rider.kamenrider.double_heat_metal", 0, 3, 1, 0, 29.0F, 1.16F),
    DOUBLE_LUNA_TRIGGER("double_luna_trigger", "double", "rider.kamenrider.double_luna_trigger", 2, 1, 0, 2, 23.0F, 1.32F),

    GEATS_MAGNUM_BOOST("geats_magnum_boost", "geats", "rider.kamenrider.geats_magnum_boost", 2, 2, 0, 1, 26.0F, 1.36F),
    GEATS_NINJA("geats_ninja", "geats", "rider.kamenrider.geats_ninja", 3, 1, 0, 2, 24.0F, 1.44F),
    GEATS_ZOMBIE("geats_zombie", "geats", "rider.kamenrider.geats_zombie", 0, 3, 2, 0, 31.0F, 1.04F);

    private final String id;
    private final String seriesId;
    private final String translationKey;
    private final int speedAmplifier;
    private final int strengthAmplifier;
    private final int resistanceAmplifier;
    private final int jumpAmplifier;
    private final float kickDamage;
    private final float soundPitch;

    RiderForm(String id, String seriesId, String translationKey, int speedAmplifier, int strengthAmplifier,
              int resistanceAmplifier, int jumpAmplifier, float kickDamage, float soundPitch) {
        this.id = id;
        this.seriesId = seriesId;
        this.translationKey = translationKey;
        this.speedAmplifier = speedAmplifier;
        this.strengthAmplifier = strengthAmplifier;
        this.resistanceAmplifier = resistanceAmplifier;
        this.jumpAmplifier = jumpAmplifier;
        this.kickDamage = kickDamage;
        this.soundPitch = soundPitch;
    }

    public String id() { return id; }
    public String seriesId() { return seriesId; }
    public String translationKey() { return translationKey; }
    public int speedAmplifier() { return speedAmplifier; }
    public int strengthAmplifier() { return strengthAmplifier; }
    public int resistanceAmplifier() { return resistanceAmplifier; }
    public int jumpAmplifier() { return jumpAmplifier; }
    public float kickDamage() { return kickDamage; }
    public float soundPitch() { return soundPitch; }
}
