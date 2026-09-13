# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> modular form play -> finisher** gameplay.

## v0.8 highlights

### Rider-specific finisher layer
v0.8 adds a reusable `RiderFinisher` service instead of making every finisher reimplement targeting, damage, movement and FX. Normal techniques remain available; Shift activates the stronger form/equipment finisher where supported.

### Kuuga weapon finishers
Kuuga keeps the existing Dragon Rod and Titan Sword techniques, but both now have a second finisher input:

- Dragon Rod: right-click = normal technique; Shift + right-click = long-reach high-damage finisher with electric/end-rod FX and a forward burst.
- Titan Sword: right-click = normal heavy technique; Shift + right-click = high-damage heavy finisher with large knockback, crit and impact-smoke FX.

The required Kuuga form is still enforced before either technique can activate.

### W Maximum Drive
All six Gaia Memories can now trigger a Maximum Drive when that Memory is actually inserted in the current W form:

- Cyclone: radial wind burst and knockback.
- Heat: high-damage flame cone.
- Luna: long-range energy trace.
- Joker: forward burst finisher with heavy impact.
- Metal: short-range heavy smash.
- Trigger: focused long-range Maximum Drive shot.

Right-click a Gaia Memory normally to change that side of W. **Shift + right-click an already equipped Memory** activates its Maximum Drive. An unequipped Memory cannot trigger a finisher just because it is held in the hand.

### Geats Raise Buckle finishers
The two-slot Raise Buckle system now drives four distinct finishers:

- Magnum Raise Buckle: focused long-range Magnum Strike.
- Ninja Raise Buckle: high-speed rush and sweep.
- Boost Raise Buckle: Boost Grand Strike mobility burst with the highest forward-impact damage.
- Zombie Raise Buckle: radial heavy smash with large knockback.

Right-click a Buckle normally to replace its upper/lower slot. **Shift + right-click an equipped Buckle** activates its finisher. A Buckle that is not currently equipped cannot activate the finisher.

### Existing Decade finisher flow retained
Decade already has its Rider Card finisher architecture, including Final Attack Ride: Decade, so v0.8 keeps that path intact rather than duplicating another activation system.

## Riders currently implemented

### Kuuga
- Mighty / Dragon / Pegasus / Titan
- dedicated staged suit
- Dragon Rod + normal technique + Shift finisher
- Titan Sword + normal technique + Shift finisher

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
- six Gaia Memory Maximum Drives

### Geats
- MagnumBoost / MagnumZombie / NinjaBoost / NinjaZombie
- dedicated modular fox suit
- Magnum Shooter
- Ninja Dualer
- Zombie Breaker
- four equipped-Buckle finishers

## Controls

1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click a Driver: **Henshin**.
3. Shift + right-click the Driver while transformed: **Rider Kick**.
4. Right-click the same Driver again: de-henshin.
5. Kuuga: right-click its matching weapon for the normal technique; Shift + right-click the weapon for the finisher.
6. Decade: use Ride Booker and Rider Cards; Final Attack Ride remains its dedicated finisher path.
7. W: right-click a Gaia Memory to replace only that side's Memory; Shift + right-click an already inserted Memory for Maximum Drive.
8. Geats: right-click a Raise Buckle to replace only its upper/lower slot; Shift + right-click an equipped Buckle for its finisher.
9. Geats weapons remain Buckle-gated: Magnum Shooter needs Magnum, Ninja Dualer needs Ninja, Zombie Breaker needs Zombie.

## Architecture

- `RiderForm`: gameplay and visual metadata.
- `RiderTransformation`: shared transformation/effect service.
- `RiderFinisher`: shared finisher targeting, damage, movement, particles, sound and cooldown behavior.
- `RiderStatePayload` + `RiderNetworking`: multiplayer Rider-state synchronization.
- `RiderClientState`: client presentation cache and Henshin timing.
- `RiderSuitLayer`: shared undersuit/fallback layer.
- `KuugaSuitModel` + `KuugaSuitLayer`: Kuuga armor.
- `KuugaWeaponItem`: form-locked normal techniques + Shift finishers.
- `DecadeSuitModel` + `DecadeSuitLayer`: Decade armor.
- `DecadeWeaponItem` + `DecadeCardItem`: Ride Booker and Rider Card actions.
- `DoubleSuitModel` + `DoubleSuitLayer`: W split armor.
- `DoubleMemoryItem`: independent Gaia Memory side replacement + Maximum Drive activation.
- `DoubleWeaponItem`: Memory-aware Metal Shaft / Trigger Magnum abilities.
- `GeatsSuitModel` + `GeatsSuitLayer`: modular Geats fox armor.
- `GeatsBuckleItem`: upper/lower Raise Buckle replacement + equipped-Buckle finisher activation.
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

- in-game visual and balance QA for the new finisher damage/ranges
- first-person Driver / Gaia Memory / Raise Buckle activation animation
- original texture sheets for all four dedicated Riders
- custom distributable sound set for Henshin and finishers
- more Decade Kamen Ride / Form Ride cards
- Pegasus Bowgun and additional Kuuga equipment
- enemy mobs and bosses designed around the stronger finisher layer
- unlock/progression loop instead of creative-only equipment access
- configurable keybinds and accessibility options

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
