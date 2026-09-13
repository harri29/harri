# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> modular form play -> finisher** gameplay.

## v0.7 highlights

### Geats is now the fourth dedicated full-suit Rider
Geats no longer uses the generic fallback shell. The new suit has original Minecraft-style geometry attached to the vanilla player skeleton:

- white fox helmet shell
- tall fox ears
- full-bright red eye visor
- white base chest shell
- independent upper Raise Buckle armor module
- independent lower Raise Buckle armor module
- Desire Driver frame and glowing core
- wide and slim player skin support
- staged Henshin: fox shell -> upper module -> lower module -> Driver/visor finish

Editable source: `assets-src/blockbench/geats_suit.bbmodel`.

### Two-slot Raise Buckle loadouts
The old Raise Buckle Case is replaced in the creative tab by four real Buckle items:

- Magnum Raise Buckle — upper slot
- Ninja Raise Buckle — upper slot
- Boost Raise Buckle — lower slot
- Zombie Raise Buckle — lower slot

This produces four clean modular loadouts:

- MagnumBoost
- MagnumZombie
- NinjaBoost
- NinjaZombie

Using a Buckle replaces only its slot. Example: MagnumBoost + Ninja Buckle -> NinjaBoost; NinjaBoost + Zombie Buckle -> NinjaZombie.

Legacy standalone Ninja/Zombie forms and `geats_form_changer` remain registered for older-world save compatibility but are hidden from the main creative flow.

### Geats weapons
- **Magnum Shooter**: requires Magnum in the upper slot; focused long-range shot trace with electric FX.
- **Ninja Dualer**: requires Ninja in the upper slot; fast forward rush + sweep attack.
- **Zombie Breaker**: requires Zombie in the lower slot; slower heavy cone smash with high damage and knockback.

Boost is intentionally mobility/finisher-oriented instead of adding another weapon item.

### 3D Buckles and equipment
All four Raise Buckles inherit from one reusable 3D base model and swap their own material colors. Magnum Shooter, Ninja Dualer and Zombie Breaker each use a dedicated multi-part 3D item model.

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
- full 3x3 Gaia Memory matrix (9 forms)
- dedicated vertical split suit
- Metal Shaft
- Trigger Magnum

### Geats
- MagnumBoost / MagnumZombie / NinjaBoost / NinjaZombie
- dedicated modular fox suit
- Magnum Shooter
- Ninja Dualer
- Zombie Breaker

## Controls

1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click a Driver: **Henshin**.
3. Shift + right-click the Driver while transformed: **Rider Kick**.
4. Right-click the same Driver again: de-henshin.
5. Kuuga: use its form changer and matching weapons.
6. Decade: use Ride Booker and Rider Cards.
7. W: right-click a Gaia Memory to replace only that side's Memory.
8. Geats: right-click a Raise Buckle to replace only its upper/lower slot.
9. Geats: use Magnum Shooter with Magnum, Ninja Dualer with Ninja, or Zombie Breaker with Zombie.

## Architecture

- `RiderForm`: gameplay and visual metadata.
- `RiderTransformation`: shared transformation/effect service.
- `RiderStatePayload` + `RiderNetworking`: multiplayer Rider-state synchronization.
- `RiderClientState`: client presentation cache and Henshin timing.
- `RiderSuitLayer`: shared undersuit/fallback layer.
- `KuugaSuitModel` + `KuugaSuitLayer`: Kuuga armor.
- `DecadeSuitModel` + `DecadeSuitLayer`: Decade armor.
- `DoubleSuitModel` + `DoubleSuitLayer`: W split armor.
- `DoubleMemoryItem`: independent Gaia Memory side replacement.
- `GeatsSuitModel` + `GeatsSuitLayer`: modular Geats fox armor.
- `GeatsBuckleItem`: upper/lower Raise Buckle slot replacement.
- `GeatsWeaponItem`: Buckle-aware weapon abilities.
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

- in-game visual QA for fox helmet/ears and Raise Buckle proportions
- original texture sheets for all four dedicated Riders
- belt-on-waist and Driver/Buckle insertion animation
- Geats finisher presentation and Boost-specific movement burst
- W Maximum Drive framework
- additional Decade Kamen Ride / Form Ride cards
- Pegasus Bowgun and additional Kuuga techniques
- enemy mobs, bosses and unlock/progression loop
- configurable keybinds

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
