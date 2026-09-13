# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod prototype focused on **henshin -> Rider powers -> finisher** gameplay.

## Included in v0.1.0

- Kuuga Arcle -> Kamen Rider Kuuga
- Decade Driver -> Kamen Rider Decade
- Double Driver -> Kamen Rider W
- Desire Driver -> Kamen Rider Geats
- Right-click a Driver: transform / release transformation
- Shift + right-click while transformed: Rider Kick / finisher
- Form-specific Speed, Strength, Resistance and Jump Boost
- Five-second finisher cooldown
- English + Vietnamese localization
- Vanilla placeholder icons so the project contains no ripped copyrighted textures/audio

## Controls

1. Give yourself a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click: **Henshin**.
3. Shift + right-click with the same Driver: **Rider Kick**.
4. Right-click again: **de-henshin**.

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

Jar output: `build/libs/kamenrider-0.1.0.jar`.

## Next milestones

- Proper 3D Driver models made in Blockbench
- Full Rider suit renderer / armor models
- Transformation animation timeline and camera effects
- Original/remade sound effects that are safe to distribute
- Form-change items (cards, Gaia Memories, Raise Buckles, etc.)
- Rider-specific weapons and bosses
- Configurable keybinds and multiplayer synchronization

## Source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. Gameplay ideas were also cross-checked against open-source Kamen Rider mod projects on GitHub, but this repository does not copy their copyrighted assets.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
