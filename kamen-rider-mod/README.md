# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> modular form play -> finisher** gameplay.

## v0.9 highlights

### First-person Henshin presentation
v0.9 adds a real client presentation layer instead of relying only on particles around the player.

- Driver items move toward the center of view during Henshin.
- Gaia Memories use an insertion-style hand motion during W form changes.
- Raise Buckles use a lock-in motion during Geats form changes.
- Rider-specific hand roll gives Kuuga / Decade / W / Geats slightly different first-person identities.
- FOV narrows briefly during Henshin/form assembly and returns smoothly.
- A very small camera roll pulse is used for impact without making normal gameplay feel like permanent camera shake.

All first-person effects are presentation-only. Damage, cooldowns, transformation state and hit detection remain server authoritative.

### Cinematic finisher cues
A new `RiderPresentationPayload` is sent from server to tracking clients when an important finisher happens. The local first-person view uses it for a short hand recoil + FOV impact pulse.

Current cinematic cues cover:

- shared Rider Kick
- Kuuga Dragon Rod / Titan Sword finishers
- W Maximum Drive
- Geats Raise Buckle finishers
- Decade Final Attack Ride

### Client/server separation
`RiderStatePayload` still owns transformation/form rendering state. `RiderPresentationPayload` is intentionally separate and carries only short visual cue IDs. This makes it possible to expand future Driver insertion, weapon recoil and final-form camera effects without changing gameplay authority.

## Riders currently implemented

### Kuuga
- Mighty / Dragon / Pegasus / Titan
- dedicated staged suit
- Dragon Rod + normal technique + Shift finisher
- Titan Sword + normal technique + Shift finisher
- first-person Driver/finisher presentation

### Decade
- dedicated staged suit
- Ride Booker Sword/Gun modes
- Attack Ride: Slash
- Attack Ride: Blast
- Final Attack Ride: Decade with cinematic cue

### W
- full 3x3 Gaia Memory matrix (9 forms)
- dedicated vertical split suit
- Metal Shaft
- Trigger Magnum
- six Gaia Memory Maximum Drives
- Gaia Memory insertion-style first-person motion

### Geats
- MagnumBoost / MagnumZombie / NinjaBoost / NinjaZombie
- dedicated modular fox suit
- Magnum Shooter
- Ninja Dualer
- Zombie Breaker
- four equipped-Buckle finishers
- Raise Buckle lock-in first-person motion

## Controls

1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click a Driver: **Henshin**.
3. Shift + right-click the Driver while transformed: **Rider Kick**.
4. Right-click the same Driver again: de-henshin.
5. Kuuga: right-click its matching weapon for the normal technique; Shift + right-click the weapon for the finisher.
6. Decade: use Ride Booker and Rider Cards; Final Attack Ride is its dedicated finisher path.
7. W: right-click a Gaia Memory to replace only that side's Memory; Shift + right-click an already inserted Memory for Maximum Drive.
8. Geats: right-click a Raise Buckle to replace only its upper/lower slot; Shift + right-click an equipped Buckle for its finisher.
9. Geats weapons remain Buckle-gated: Magnum Shooter needs Magnum, Ninja Dualer needs Ninja, Zombie Breaker needs Zombie.

## Architecture

- `RiderForm`: gameplay and visual metadata.
- `RiderTransformation`: shared transformation/effect service.
- `RiderFinisher`: shared finisher targeting, damage, movement, particles, sound and cooldown behavior.
- `RiderStatePayload`: multiplayer transformation/form snapshot.
- `RiderPresentationPayload`: short server -> client cinematic cue.
- `RiderNetworking`: registration + Rider state/presentation sync.
- `RiderClientState`: client form, Henshin timing and presentation timing cache.
- `RiderFirstPersonPresentation`: first-person hand transforms, FOV pulse and subtle camera roll.
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
- `KamenRiderClient`: wide/slim suit registration + first-person event registration.

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

- in-game QA for first-person hand positions at different FOV settings
- true Driver-on-waist / Memory / Buckle insertion animation using dedicated rendered equipment transforms
- original texture sheets for all four dedicated Riders
- custom distributable sound set for Henshin and finishers
- more Decade Kamen Ride / Form Ride cards
- Pegasus Bowgun and additional Kuuga equipment
- enemy mobs and bosses designed around finisher gameplay
- unlock/progression loop instead of creative-only equipment access
- configurable keybinds and accessibility options, including camera-effect intensity

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source NeoForge/Minecraft projects, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
