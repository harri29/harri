# Blockbench source assets

`kuuga_suit.bbmodel` is the editable Blockbench 5.x source for the first dedicated Rider suit.

The runtime NeoForge model currently mirrors the same design in `client/KuugaSuitModel.java` so the mod has no external animation/model dependency. The important parent groups are Helmet, Torso and Shoulders; keep armor pieces aligned to the vanilla player skeleton when editing.

## Current Kuuga render passes

- Base: helmet shell, chest armor, gauntlets and shins. Tint comes from the active Kuuga form.
- Accent: horns, shoulder/crest/belt details. Rendered as warm metallic gold.
- Core: eye band and Arcle core. Rendered full-bright.

## Henshin timeline

Runtime assembly is staged over 1.20 seconds: torso -> shoulders -> arms/legs -> belt/crest -> helmet -> horns/core.

Do not add ripped TV/movie textures or audio to this directory. Original/remade assets are preferred so the repository stays distributable.
