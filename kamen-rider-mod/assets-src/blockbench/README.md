# Blockbench source assets

This directory contains editable Blockbench 5.x source projects for the dedicated Rider suits.

## Kuuga

`kuuga_suit.bbmodel` mirrors the runtime geometry in `client/KuugaSuitModel.java`.

Render passes:
- Base: helmet shell, chest armor, gauntlets and shins; tinted by active Kuuga form.
- Accent: horns, shoulder/crest/belt details; warm metallic gold.
- Core: eye band and Arcle core; full-bright.

Henshin assembly: torso -> shoulders -> arms/legs -> belt/crest -> helmet -> horns/core.

## Decade

`decade_suit.bbmodel` mirrors `client/DecadeSuitModel.java`.

Render passes:
- Base: black helmet/chest/limb armor.
- Accent: magenta shoulders and chest stripe.
- Card: pale card rails and Decadriver frame.
- Core: cyan eyes and Driver core; full-bright.

## Kamen Rider W

`double_suit.bbmodel` is the editable source for the split-body W pipeline in `client/DoubleSuitModel.java`.

The runtime suit is deliberately separated into left/right geometry rather than recoloring one shell:
- Left half: Cyclone / Heat / Luna.
- Right half: Joker / Metal / Trigger.
- Accent: silver center seam and Double Driver frame.
- Core: red compound-eye pass rendered full-bright.

The six Gaia Memories can be changed independently, producing all nine currently implemented combinations. Keep left/right armor pieces on their matching vanilla player bones when refining the model.

## Asset policy

Runtime geometry is kept dependency-light in Java while `.bbmodel` files remain the editable art source. Do not add ripped TV/movie textures, audio or proprietary models here. Original/remade distributable assets are preferred.
