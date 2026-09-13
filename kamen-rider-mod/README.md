# Kamen Rider Craft (NeoForge 1.21.1)

Fan-made Minecraft Java mod focused on **henshin -> Rider-specific equipment -> form/ability play -> finisher** gameplay.

## v0.5 highlights

### Decade is the second dedicated full-suit Rider
Decade now has original Minecraft-style armor geometry instead of the generic colored Rider shell:

- dark undersuit
- black helmet shell
- cyan full-bright eye bar
- four card-like face/crown rails
- black chest armor
- magenta chest stripe and shoulder accents
- forearm and shin armor
- Decadriver frame and glowing core

The armor is attached to the vanilla player skeleton, so normal walk, sprint, crouch and arm animation still drive the custom pieces. Henshin assembly is staged rather than appearing all at once.

Editable Blockbench source is included at `assets-src/blockbench/decade_suit.bbmodel`; runtime geometry mirrors it in `client/DecadeSuitModel.java` without adding an external animation dependency.

### Ride Booker
The **Ride Booker** is Decade's dedicated weapon item.

- Right-click: Sword Mode, a forward melee sweep.
- Shift + right-click: Gun Mode, a focused long-range shot trace.
- Both modes require Kamen Rider Decade, have independent behavior, particles, knockback and cooldown handling.
- A 3D handheld item model is included using vanilla Minecraft materials.

### Rider Card foundation
`DecadeCardItem` is a reusable card-action class for future Decade systems. v0.5 includes:

- **Attack Ride: Slash** — stronger wide melee technique.
- **Attack Ride: Blast** — long-range multi-target beam/cone attack.
- **Final Attack Ride: Decade** — forward burst with high-damage finisher hit cone.

All three cards now have thin 3D card models instead of flat paper icons. This structure is intended to support later `Kamen Ride`, `Form Ride` and additional `Attack Ride` cards without duplicating item logic.

### Kuuga remains fully implemented
- dedicated Kuuga suit geometry
- Mighty / Dragon / Pegasus / Titan armor colors
- staged Henshin
- Dragon Rod
- Titan Sword
- editable Blockbench source at `assets-src/blockbench/kuuga_suit.bbmodel`

### Other Riders already present
- W: CycloneJoker / HeatMetal / LunaTrigger
- Geats: MagnumBoost / Ninja / Zombie

W and Geats still use the shared Rider shell until their dedicated model passes are implemented.

## Controls

1. Take a Driver from the **Kamen Rider Craft** creative tab.
2. Right-click a Driver: **Henshin**.
3. Use a matching form changer where available.
4. Shift + right-click the Driver while transformed: **Rider Kick**.
5. Right-click the Driver again: de-henshin.
6. Kuuga: use Dragon Rod/Titan Sword in the required form.
7. Decade: right-click Ride Booker for Sword Mode; Shift + right-click for Gun Mode.
8. Decade: right-click an Attack Ride / Final Attack Ride card while transformed.

## Architecture

- `RiderForm`: gameplay and visual metadata.
- `RiderTransformation`: shared transformation/effect service.
- `RiderStatePayload` + `RiderNetworking`: multiplayer Rider-state synchronization.
- `RiderClientState`: client presentation cache and Henshin timing.
- `RiderSuitLayer`: shared undersuit/fallback shell.
- `KuugaSuitModel` + `KuugaSuitLayer`: dedicated Kuuga armor.
- `DecadeSuitModel` + `DecadeSuitLayer`: dedicated Decade armor with four render passes.
- `KuugaWeaponItem`: form-locked Kuuga weapons.
- `DecadeWeaponItem`: dual-mode Ride Booker.
- `DecadeCardItem`: reusable Rider Card action foundation.
- `KamenRiderClient`: wide/slim player model-layer registration.

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

- in-game visual QA for Decade proportions and card rails
- original Kuuga/Decade texture sheets authored from the Blockbench sources
- belt-on-waist and Driver activation animation
- Kamen Ride / Form Ride card-state architecture
- dedicated W and Geats suit geometry
- Pegasus Bowgun and more Kuuga techniques
- original/remade distributable sound effects
- enemy mobs, bosses and unlock/progression loop
- configurable keybinds

## Assets and source references

The project structure follows the NeoForge 1.21.1 ModDevGradle MDK. API patterns are cross-checked against open-source Minecraft projects on GitHub, but copyrighted textures, ripped audio and proprietary models are not copied into this repository. Current original geometry uses vanilla Minecraft resources for runtime material sampling until original texture sheets are authored.

Kamen Rider names/marks belong to their respective rights holders. This project is an unofficial fan work.
