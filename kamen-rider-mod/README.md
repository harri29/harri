# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> form/ability play -> finisher** gameplay.

## v0.6 highlights

### Kamen Rider W is now the third dedicated full-suit Rider
W no longer uses the generic colored Rider shell. The new dedicated model is vertically split into two independently colored halves on the vanilla player skeleton:

- left half: Cyclone / Heat / Luna
- right half: Joker / Metal / Trigger
- silver center seam and Double Driver frame
- red full-bright compound eyes
- wide and slim player skin support
- staged Henshin where the two halves assemble with slightly offset timing

The editable art source is included at `assets-src/blockbench/double_suit.bbmodel`.

### Real Gaia Memory pairing
The old `Double Form Changer` flow has been replaced in the creative tab by six individual Gaia Memory items:

- Cyclone Memory
- Heat Memory
- Luna Memory
- Joker Memory
- Metal Memory
- Trigger Memory

Each Memory changes only its own side of W. This produces the complete 3x3 matrix of nine implemented forms:

- CycloneJoker
- CycloneMetal
- CycloneTrigger
- HeatJoker
- HeatMetal
- HeatTrigger
- LunaJoker
- LunaMetal
- LunaTrigger

Example: starting in CycloneJoker, inserting Metal changes only the right side -> CycloneMetal. Inserting Heat after that changes only the left side -> HeatMetal.

The legacy `double_form_changer` item remains registered for old-world save compatibility but is hidden from the creative tab.

### W weapons
- **Metal Shaft**: works with any W form whose right-side Memory is Metal. Right-click performs a wide heavy sweep with strong knockback.
- **Trigger Magnum**: works with any W form whose right-side Memory is Trigger. Right-click fires a focused long-range attack trace with electric projectile FX.

Both use dedicated 3D item models and Memory-aware form checks rather than exact-form hardcoding.

### 3D Gaia Memories
All six Gaia Memories inherit from one reusable 3D base model and swap their own vanilla-material color set. This keeps proportions consistent and makes later model refinement easy.

## Riders currently implemented

### Kuuga
- Mighty / Dragon / Pegasus / Titan
- dedicated staged suit
- Dragon Rod
- Titan Sword

### Decade
- dedicated staged suit
- Ride Booker Sword/Gun modes
- Attack Ride: Slash
- Attack Ride: Blast
- Final Attack Ride: Decade

### W
- nine Gaia Memory combinations
- dedicated split suit
- Metal Shaft
- Trigger Magnum

### Geats
- MagnumBoost / Ninja / Zombie
- still uses the shared fallback shell until its dedicated model pass

## Controls

1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click a Driver: **Henshin**.
3. Shift + right-click the Driver while transformed: **Rider Kick**.
4. Right-click the Driver again: de-henshin.
5. Kuuga: use its form changer and matching form weapons.
6. Decade: use Ride Booker and Rider Cards while transformed.
7. W: right-click any Gaia Memory while transformed to replace that side's Memory and immediately resolve the new form.
8. W: use Metal Shaft with Metal on the right side, or Trigger Magnum with Trigger on the right side.

## Architecture

- `RiderForm`: gameplay and visual metadata for every form.
- `RiderTransformation`: shared transformation/effect service.
- `RiderStatePayload` + `RiderNetworking`: multiplayer Rider-state synchronization.
- `RiderClientState`: client presentation cache and Henshin timing.
- `RiderSuitLayer`: shared undersuit/fallback shell.
- `KuugaSuitModel` + `KuugaSuitLayer`: dedicated Kuuga armor.
- `DecadeSuitModel` + `DecadeSuitLayer`: dedicated Decade armor.
- `DoubleSuitModel` + `DoubleSuitLayer`: dedicated left/right W armor renderer.
- `DoubleMemoryItem`: independent Gaia Memory slot replacement and 3x3 form resolution.
- `DoubleWeaponItem`: Memory-aware Metal Shaft / Trigger Magnum abilities.
- `KamenRiderClient`: wide/slim model-layer registration.

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

- in-game visual QA for W's center split and eye proportions
- original texture sheets for Kuuga / Decade / W
- Memory insertion / Double Driver activation animation
- Joker-side finisher presentation and Maximum Drive framework
- dedicated Geats suit geometry and Raise Buckle system
- Pegasus Bowgun and additional Kuuga techniques
- more Decade Kamen Ride / Form Ride cards
- enemy mobs, bosses and unlock/progression loop
- configurable keybinds

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
