package io.github.harri29.kamenrider.client;

import io.github.harri29.kamenrider.RiderForm;
import io.github.harri29.kamenrider.network.RiderPresentationPayload;
import io.github.harri29.kamenrider.network.RiderStatePayload;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Client-only presentation state for Rider suits, Henshin and cinematic cues. */
public final class RiderClientState {
    private static final Map<Integer, Entry> STATES = new ConcurrentHashMap<>();
    private static final Map<Integer, PresentationEntry> PRESENTATIONS = new ConcurrentHashMap<>();
    private static final double HENSHIN_DURATION_SECONDS = 1.20D;
    private static final double PRESENTATION_DURATION_SECONDS = 0.72D;

    private RiderClientState() {
    }

    public static void accept(RiderStatePayload payload) {
        RiderForm.byId(payload.formId()).ifPresentOrElse(
                form -> STATES.put(
                        payload.entityId(),
                        new Entry(form, payload.animate() ? System.nanoTime() : Long.MIN_VALUE)
                ),
                () -> STATES.remove(payload.entityId())
        );
    }

    public static void accept(RiderPresentationPayload payload) {
        if (payload.effectId() == null || payload.effectId().isBlank()) {
            PRESENTATIONS.remove(payload.entityId());
            return;
        }
        PRESENTATIONS.put(payload.entityId(), new PresentationEntry(payload.effectId(), System.nanoTime()));
    }

    public static Optional<RiderForm> form(int entityId) {
        Entry entry = STATES.get(entityId);
        return entry == null ? Optional.empty() : Optional.of(entry.form());
    }

    public static float henshinProgress(int entityId) {
        Entry entry = STATES.get(entityId);
        if (entry == null || entry.changedAtNanos() == Long.MIN_VALUE) {
            return 1.0F;
        }
        double elapsedSeconds = (System.nanoTime() - entry.changedAtNanos()) / 1_000_000_000.0D;
        return (float) Math.min(1.0D, Math.max(0.0D, elapsedSeconds / HENSHIN_DURATION_SECONDS));
    }

    public static Optional<String> presentationEffect(int entityId) {
        PresentationEntry entry = PRESENTATIONS.get(entityId);
        if (entry == null || presentationProgress(entityId) >= 1.0F) {
            PRESENTATIONS.remove(entityId);
            return Optional.empty();
        }
        return Optional.of(entry.effectId());
    }

    public static float presentationProgress(int entityId) {
        PresentationEntry entry = PRESENTATIONS.get(entityId);
        if (entry == null) {
            return 1.0F;
        }
        double elapsedSeconds = (System.nanoTime() - entry.changedAtNanos()) / 1_000_000_000.0D;
        return (float) Math.min(1.0D, Math.max(0.0D, elapsedSeconds / PRESENTATION_DURATION_SECONDS));
    }

    private record Entry(RiderForm form, long changedAtNanos) {
    }

    private record PresentationEntry(String effectId, long changedAtNanos) {
    }
}
