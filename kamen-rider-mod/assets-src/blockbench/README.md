# Blockbench source assets

This folder contains editable Blockbench 5.x source projects for the dedicated Rider suits.

- `kuuga_suit.bbmodel` -> runtime geometry in `client/KuugaSuitModel.java`
- `decade_suit.bbmodel` -> runtime geometry in `client/DecadeSuitModel.java`

The NeoForge runtime models mirror these designs directly in Java so the mod stays dependency-light. Keep Helmet, Torso and Shoulders aligned to the vanilla player skeleton when editing.

## Kuuga render passes

- Base: helmet shell, chest armor, gauntlets and shins; tint follows the active Kuuga form.
- Accent: horns, shoulder/crest/belt details; warm metallic gold.
- Core: eye band and Arcle core; full-bright.

## Decade render passes

- Base: black helmet/body armor, forearms and shins.
- Accent: magenta shoulders and chest stripe.
- Card: white card rails and Decadriver frame.
- Core: cyan eye bar and Driver core; full-bright.

## Henshin timeline

Both dedicated suits assemble in stages rather than appearing instantly. Kuuga builds torso -> shoulders -> limbs -> belt/crest -> helmet -> horns/core. Decade builds torso -> shoulders -> limbs -> Driver/chest stripe -> helmet -> card rails -> eyes/core.

Do not add ripped TV/movie textures, models or audio to this directory. Original/remade assets are preferred so the repository stays distributable.
