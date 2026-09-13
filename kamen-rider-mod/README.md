# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> form change -> Rider powers -> finisher** gameplay.

## v0.4 highlights

### Kuuga is now the reference full-suit Rider
v0.4 replaces Kuuga's generic colored shell with dedicated armor geometry on the vanilla player skeleton:

- dark undersuit
- helmet shell
- glowing eye band
- two articulated horns
- chest armor and center crest
- shoulder armor
- forearm gauntlets
- shin armor
- Arcle belt frame and glowing core

The armor follows normal Minecraft player animation because every piece is parented to the corresponding head/body/arm/leg bone.

### Four Kuuga forms
- Mighty: red armor
- Dragon: blue armor
- Pegasus: green armor
- Titan: purple armor

Armor color changes with the active form while metallic details stay gold and the eye/belt-core pass renders full-bright.

### Staged Henshin animation
The client presentation timeline is now 1.20 seconds instead of a single scale pulse. Kuuga armor assembles in stages:

`torso -> shoulders -> arms/legs -> belt/crest -> helmet -> horns/core`

Each part eases from a small scale to full size, so Henshin reads as armor assembly rather than an instant model swap.

### Better multiplayer synchronization
`RiderStatePayload` now includes an animation flag and networking protocol revision `2`.

Real Henshin/form changes animate for tracking clients. A player who merely starts tracking an already-transformed Rider receives an immediate snapshot instead of replaying a fake Henshin animation.

### Editable Blockbench source
The first editable source project is included at:

`assets-src/blockbench/kuuga_suit.bbmodel`

It uses Blockbench 5.x project format. Runtime geometry currently mirrors the same design in `client/KuugaSuitModel.java`, keeping the mod dependency-light while preserving an editable art source. See `assets-src/blockbench/README.md` for the source-asset workflow.

### Drivers and Riders already present
- Kuuga Arcle -> Kuuga Mighty / Dragon / Pegasus / Titan
- Decade Driver -> Kamen Rider Decade
- Double Driver -> W CycloneJoker / HeatMetal / LunaTrigger
- Desire Driver -> Geats MagnumBoost / Ninja / Zombie

All four Drivers use multi-part 3D item geometry instead of flat placeholder icons.

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
- `RiderClientState`: client presentation cache and Henshin timing.
- `RiderSuitLayer`: shared undersuit/fallback shell.
- `KuugaSuitModel` + `KuugaSuitLayer`: dedicated Kuuga armor, staged assembly and render passes.
- `KamenRiderClient`: model-layer registration for wide/slim player skins.

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

- In-game visual QA and refinement of Kuuga proportions
- Original texture sheet authored from the Blockbench source
- Driver activation animation and belt-on-waist rendering
- Kuuga-specific weapon/item gameplay
- Dedicated Decade, W and Geats suit geometry using the Kuuga pipeline
- Original/remade distributable sound effects
- Enemy mobs, bosses and unlock/progression loop
- Configurable keybinds

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until an original texture sheet is authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
