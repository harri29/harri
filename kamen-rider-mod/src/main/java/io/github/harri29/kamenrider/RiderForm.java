package io.github.harri29.kamenrider;

import java.util.Arrays;
import java.util.Optional;

/**
 * Gameplay + visual definitions for Rider forms.
 * Keeping form metadata here lets gameplay, networking and rendering share one source of truth.
 */
public enum RiderForm {
    KUUGA_MIGHTY("kuuga_mighty", "kuuga", "rider.kamenrider.kuuga_mighty", 1, 2, 1, 1, 24.0F, 1.00F, 0xD32F2F),
    KUUGA_DRAGON("kuuga_dragon", "kuuga", "rider.kamenrider.kuuga_dragon", 2, 1, 0, 3, 20.0F, 1.08F, 0x3949AB),
    KUUGA_PEGASUS("kuuga_pegasus", "kuuga", "rider.kamenrider.kuuga_pegasus", 2, 1, 0, 2, 22.0F, 1.16F, 0x2E7D32),
    KUUGA_TITAN("kuuga_titan", "kuuga", "rider.kamenrider.kuuga_titan", 0, 3, 2, 0, 30.0F, 0.92F, 0x7B1FA2),

    DECADE("decade", "decade", "rider.kamenrider.decade", 1, 2, 1, 0, 28.0F, 1.12F, 0xE91E63),

    DOUBLE_CYCLONE_JOKER("double_cyclone_joker", "double", "rider.kamenrider.double_cyclone_joker", 2, 1, 0, 1, 22.0F, 1.24F, 0x43A047),
    DOUBLE_CYCLONE_METAL("double_cyclone_metal", "double", "rider.kamenrider.double_cyclone_metal", 2, 2, 1, 1, 25.0F, 1.20F, 0x66A66A),
    DOUBLE_CYCLONE_TRIGGER("double_cyclone_trigger", "double", "rider.kamenrider.double_cyclone_trigger", 3, 1, 0, 1, 23.0F, 1.30F, 0x2F8B9A),
    DOUBLE_HEAT_JOKER("double_heat_joker", "double", "rider.kamenrider.double_heat_joker", 1, 2, 0, 1, 25.0F, 1.16F, 0xC23D35),
    DOUBLE_HEAT_METAL("double_heat_metal", "double", "rider.kamenrider.double_heat_metal", 0, 3, 1, 0, 29.0F, 1.12F, 0xD06B45),
    DOUBLE_HEAT_TRIGGER("double_heat_trigger", "double", "rider.kamenrider.double_heat_trigger", 1, 2, 0, 0, 27.0F, 1.22F, 0xB44961),
    DOUBLE_LUNA_JOKER("double_luna_joker", "double", "rider.kamenrider.double_luna_joker", 2, 1, 0, 2, 23.0F, 1.28F, 0xC6A733),
    DOUBLE_LUNA_METAL("double_luna_metal", "double", "rider.kamenrider.double_luna_metal", 1, 2, 1, 1, 27.0F, 1.24F, 0xC8BD72),
    DOUBLE_LUNA_TRIGGER("double_luna_trigger", "double", "rider.kamenrider.double_luna_trigger", 2, 1, 0, 2, 24.0F, 1.34F, 0xB5A332),

    GEATS_MAGNUM_BOOST("geats_magnum_boost", "geats", "rider.kamenrider.geats_magnum_boost", 2, 2, 0, 1, 26.0F, 1.36F, 0xF44336),
    GEATS_NINJA("geats_ninja", "geats", "rider.kamenrider.geats_ninja", 3, 1, 0, 2, 24.0F, 1.44F, 0x00897B),
    GEATS_ZOMBIE("geats_zombie", "geats", "rider.kamenrider.geats_zombie", 0, 3, 2, 0, 31.0F, 1.04F, 0x5E35B1);

    private final String id;
    private final String seriesId;
    private final String translationKey;
    private final int speedAmplifier;
    private final int strengthAmplifier;
    private final int resistanceAmplifier;
    private final int jumpAmplifier;
    private final float kickDamage;
    private final float soundPitch;
    private final int suitColor;

    RiderForm(String id, String seriesId, String translationKey, int speedAmplifier, int strengthAmplifier,
              int resistanceAmplifier, int jumpAmplifier, float kickDamage, float soundPitch, int suitColor) {
        this.id = id;
        this.seriesId = seriesId;
        this.translationKey = translationKey;
        this.speedAmplifier = speedAmplifier;
        this.strengthAmplifier = strengthAmplifier;
        this.resistanceAmplifier = resistanceAmplifier;
        this.jumpAmplifier = jumpAmplifier;
        this.kickDamage = kickDamage;
        this.soundPitch = soundPitch;
        this.suitColor = suitColor;
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
    public int suitColor() { return suitColor; }

    public static Optional<RiderForm> byId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values()).filter(form -> form.id.equals(id)).findFirst();
    }
}
