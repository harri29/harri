# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod prototype focused on **henshin -> form change -> Rider powers -> finisher** gameplay.

## v0.3 highlights

### Riders / Drivers
- Kuuga Arcle -> Kuuga Mighty Form
- Decade Driver -> Kamen Rider Decade
- Double Driver -> W CycloneJoker
- Desire Driver -> Geats MagnumBoost

All four Drivers now use multi-part 3D item geometry instead of flat placeholder icons. The models intentionally use vanilla Minecraft textures so the repository contains no ripped Kamen Rider artwork.

### Form changes
- Kuuga: Mighty -> Dragon -> Pegasus -> Titan
- W: CycloneJoker -> HeatMetal -> LunaTrigger
- Geats: MagnumBoost -> Ninja -> Zombie
- Form stats, suit color and Rider Kick damage change with the active form

### Rider suit renderer
v0.3 adds a client-side Rider suit layer around the player model. It supports both wide and slim player skins, uses a separate outer-shell model, changes color per Rider form and performs a short Henshin expansion pulse when a new transformation state arrives.

This energy-suit layer is deliberately a copyright-safe placeholder architecture. Original Blockbench Rider armor can replace the shell later without changing transformation gameplay or multiplayer state handling.

### Multiplayer synchronization
Rider state is now synchronized with a dedicated NeoForge payload. Henshin, form changes and de-henshin broadcast the active form to tracking clients, while `PlayerEvent.StartTracking` sends the current state when another player comes into view. This gives the renderer a proper multiplayer-safe source of truth instead of relying only on server scoreboard tags.

### Controls
1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click the Driver: **Henshin**.
3. Use the matching Form Changer item to cycle forms.
4. Shift + right-click the Driver while transformed: **Rider Kick**.
5. Right-click the same Driver again: de-henshin.

## Architecture

- `RiderForm`: gameplay and visual metadata for each form.
- `RiderTransformation`: shared transformation/effect service.
- `RiderStatePayload` + `RiderNetworking`: client/server form synchronization.
- `RiderClientState`: client presentation cache and Henshin animation timing.
- `RiderSuitLayer`: player render layer for the current Rider form.
- `KamenRiderClient`: client model-layer registration.

## Development

Requirements: JDK 21, Gradle 9.2.1.

```bash
cd kamen-rider-mod
gradle runClient
```

Build:

```bash
gradle build
```

Jar output is written to `build/libs/`.

## Next milestones

- Original Blockbench suit geometry and textures
- More detailed 3D Driver models and Driver animations
- Henshin timeline with staged armor assembly, particles and camera effects
- Original/remade distributable sound effects
- Rider-specific weapons and weapon finishers
- Additional Rider generations, upgrade/final forms and Decade cards
- Enemy mobs, bosses and unlock/progression loop
- Configurable keybinds

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. Gameplay and API patterns were cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current visual placeholders use vanilla Minecraft resources until original assets are produced.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
