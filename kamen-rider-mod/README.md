# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> modular form play -> finisher -> Survival progression**.

## v1.0 highlights

### Survival Rider Trials
Kamen Rider Craft is no longer Creative-only. Four craftable Trial Sigils provide a Survival progression path:

- Kuuga Trial Sigil
- Decade Trial Sigil
- W Trial Sigil
- Geats Trial Sigil

Using a Sigil summons a powered-up Trial Guardian based on a vanilla mob, with increased health, combat buffs, a visible custom name and persistent trial tags. Defeating the Guardian personally unlocks that Rider series for the player and grants its Driver plus starter equipment.

Current rewards:

- Kuuga: Kuuga Arcle + Kuuga Form Changer + Dragon Rod
- Decade: Decade Driver + Ride Booker + Attack Ride: Slash
- W: Double Driver + Cyclone Memory + Joker Memory
- Geats: Desire Driver + Magnum Raise Buckle + Boost Raise Buckle

Creative inventory is still available for testing and building, but Survival now has a real acquisition loop.

### Dynamic waist equipment
Dedicated Rider suits already include their belt body. v1.0 adds a separate dynamic waist-module render layer so the equipment visible on the belt changes with gameplay state instead of remaining a static decoration.

- Kuuga: Arcle core emphasis
- Decade: active Driver plate/core
- W: left/right Gaia Memory modules reflect the current 3x3 Memory combination
- Geats: upper/lower Raise Buckle modules reflect the current two-slot loadout
- Belt core renders full-bright for a stronger Driver read at normal gameplay distance

### Four-stage first-person Henshin
The first-person Henshin presentation is extended to roughly 1.65 seconds and split into four phases:

1. prepare the hand/device
2. present or insert the Driver/Memory/Buckle
3. lock the equipment into place
4. completion flash / settle

Driver items, Gaia Memories and Raise Buckles now use different staged transforms. Henshin FOV and camera roll also follow the phase timing instead of one continuous sine motion.

### Existing cinematic finishers retained
- shared Rider Kick
- Kuuga Dragon Rod / Titan Sword finishers
- W six-Memory Maximum Drive framework
- Geats Magnum / Ninja / Boost / Zombie Buckle finishers
- Decade Final Attack Ride

All damage, cooldowns and transformation state remain server authoritative. First-person hand transforms and camera/FOV effects are presentation-only.

## Riders currently implemented

### Kuuga
- Mighty / Dragon / Pegasus / Titan
- dedicated staged suit
- Dragon Rod + normal technique + Shift finisher
- Titan Sword + normal technique + Shift finisher
- dynamic Arcle waist core
- Survival Trial path

### Decade
- dedicated staged suit
- Ride Booker Sword/Gun modes
- Attack Ride: Slash
- Attack Ride: Blast
- Final Attack Ride: Decade
- dynamic waist Driver plate/core
- Survival Trial path

### W
- full 3x3 Gaia Memory matrix (9 forms)
- dedicated vertical split suit
- dynamic left/right Gaia Memory waist modules
- Metal Shaft
- Trigger Magnum
- six Gaia Memory Maximum Drives
- Survival Trial path

### Geats
- MagnumBoost / MagnumZombie / NinjaBoost / NinjaZombie
- dedicated modular fox suit
- dynamic upper/lower Raise Buckle waist modules
- Magnum Shooter
- Ninja Dualer
- Zombie Breaker
- four equipped-Buckle finishers
- Survival Trial path

## Survival progression

Craft a Trial Sigil with vanilla materials, use it in a safe combat area, defeat the summoned Trial Guardian and receive that Rider's Driver + starter kit. The clear is stored as a persistent player scoreboard tag (`kamenrider.unlock.<series>`), giving later progression systems a stable unlock foundation.

## Controls

1. Right-click a Driver: **Henshin**.
2. Shift + right-click the Driver while transformed: **Rider Kick**.
3. Right-click the same Driver again: de-henshin.
4. Kuuga: right-click the matching weapon for its normal technique; Shift + right-click for the weapon finisher.
5. Decade: use Ride Booker and Rider Cards; Final Attack Ride is its dedicated finisher path.
6. W: right-click a Gaia Memory to replace only that side; Shift + right-click an already inserted Memory for Maximum Drive.
7. Geats: right-click a Raise Buckle to replace only its upper/lower slot; Shift + right-click an equipped Buckle for its finisher.
8. Geats weapons remain Buckle-gated: Magnum Shooter needs Magnum, Ninja Dualer needs Ninja, Zombie Breaker needs Zombie.
9. In Survival, craft and use a Rider Trial Sigil to earn the corresponding Driver and starter gear.

## Architecture

- `RiderForm`: gameplay and visual metadata.
- `RiderTransformation`: shared transformation/effect service.
- `RiderFinisher`: shared finisher targeting, damage, movement, particles, sound and cooldown behavior.
- `RiderStatePayload`: multiplayer transformation/form snapshot.
- `RiderPresentationPayload`: short server -> client cinematic cue.
- `RiderNetworking`: state/presentation synchronization.
- `RiderClientState`: form, Henshin timing and presentation timing cache.
- `RiderFirstPersonPresentation`: staged first-person hand transforms, FOV pulse and camera roll.
- `RiderSuitLayer`: shared undersuit/fallback layer.
- `RiderWaistModel` + `RiderWaistLayer`: dynamic Driver/Memory/Buckle waist equipment.
- `RiderTrialItem`: craftable Survival challenge summoner.
- `RiderTrialSystem`: trial completion detection and Driver/starter-kit rewards.
- Rider-specific suit, equipment and weapon classes remain separated by series.

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

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source NeoForge/Minecraft projects, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
