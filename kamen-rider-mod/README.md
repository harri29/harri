# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod prototype focused on **henshin -> form change -> Rider powers -> finisher** gameplay.

## v0.2 gameplay

### Riders / Drivers
- Kuuga Arcle -> Kuuga Mighty Form
- Decade Driver -> Kamen Rider Decade
- Double Driver -> W CycloneJoker
- Desire Driver -> Geats MagnumBoost

### Form changes
- Kuuga: Mighty -> Dragon -> Pegasus -> Titan
- W: CycloneJoker -> HeatMetal -> LunaTrigger
- Geats: MagnumBoost -> Ninja -> Zombie
- Form stats and Rider Kick damage change with the active form

### Controls
1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click the Driver: **Henshin**.
3. Use the matching Form Changer item to cycle forms.
4. Shift + right-click the Driver while transformed: **Rider Kick**.
5. Right-click the same Driver again: de-henshin.

## Architecture

`RiderTransformation` is the shared transformation service. Drivers and form-changing items no longer duplicate transformation state/effect logic. `RiderForm` acts as the gameplay definition table for Rider series, stats, finisher damage and sound pitch. This structure is intended to support future data-driven forms, armor renderers, networking and animation hooks.

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

## Planned v0.3+

- Proper 3D Driver models made in Blockbench
- Full Rider suit / armor renderer
- Henshin animation timeline, particles and camera effects
- Original/remade distributable sound effects
- Rider-specific weapons and weapon finishers
- Additional Rider generations and upgrade/final forms
- Enemy mobs, bosses and unlock/progression loop
- Configurable keybinds and explicit multiplayer synchronization

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. Gameplay ideas were cross-checked against open-source Kamen Rider projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Placeholder item models use vanilla Minecraft textures until original assets are produced.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
