package io.github.harri29.kamenrider.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;

/**
 * Original, Blockbench-friendly Kuuga armor geometry built on the vanilla player skeleton.
 * Vanilla body cubes are skipped while their transforms still drive the custom armor children.
 */
public final class KuugaSuitModel extends PlayerModel<AbstractClientPlayer> {
    public enum Pass { BASE, ACCENT, CORE }

    private final ModelPart helmet;
    private final ModelPart eyes;
    private final ModelPart hornLeft;
    private final ModelPart hornRight;
    private final ModelPart chest;
    private final ModelPart chestCrest;
    private final ModelPart belt;
    private final ModelPart beltCore;
    private final ModelPart shoulderLeft;
    private final ModelPart shoulderRight;
    private final ModelPart gauntletLeft;
    private final ModelPart gauntletRight;
    private final ModelPart shinLeft;
    private final ModelPart shinRight;

    public KuugaSuitModel(ModelPart root, boolean slim) {
        super(root, slim);

        this.helmet = this.head.getChild("kuuga_helmet");
        this.eyes = this.head.getChild("kuuga_eyes");
        this.hornLeft = this.head.getChild("kuuga_horn_left");
        this.hornRight = this.head.getChild("kuuga_horn_right");
        this.chest = this.body.getChild("kuuga_chest");
        this.chestCrest = this.body.getChild("kuuga_chest_crest");
        this.belt = this.body.getChild("kuuga_belt");
        this.beltCore = this.body.getChild("kuuga_belt_core");
        this.shoulderLeft = this.leftArm.getChild("kuuga_shoulder_left");
        this.shoulderRight = this.rightArm.getChild("kuuga_shoulder_right");
        this.gauntletLeft = this.leftArm.getChild("kuuga_gauntlet_left");
        this.gauntletRight = this.rightArm.getChild("kuuga_gauntlet_right");
        this.shinLeft = this.leftLeg.getChild("kuuga_shin_left");
        this.shinRight = this.rightLeg.getChild("kuuga_shin_right");

        // Keep the vanilla skeleton/animation, but render only our custom children.
        this.head.skipDraw = true;
        this.hat.visible = false;
        this.body.skipDraw = true;
        this.leftArm.skipDraw = true;
        this.rightArm.skipDraw = true;
        this.leftLeg.skipDraw = true;
        this.rightLeg.skipDraw = true;
        this.leftSleeve.visible = false;
        this.rightSleeve.visible = false;
        this.leftPants.visible = false;
        this.rightPants.visible = false;
        this.jacket.visible = false;
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition leftArm = root.getChild("left_arm");
        PartDefinition rightArm = root.getChild("right_arm");
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightLeg = root.getChild("right_leg");

        head.addOrReplaceChild("kuuga_helmet",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.35F, -8.35F, -4.35F, 8.70F, 8.55F, 8.70F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        head.addOrReplaceChild("kuuga_eyes",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-3.55F, -5.25F, -4.72F, 7.10F, 2.15F, 0.55F, CubeDeformation.NONE),
                PartPose.ZERO);
        head.addOrReplaceChild("kuuga_horn_left",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-0.45F, -4.70F, -0.45F, 0.90F, 5.20F, 0.90F, new CubeDeformation(0.08F)),
                PartPose.offsetAndRotation(-2.15F, -7.65F, -2.45F, -0.16F, 0.0F, -0.25F));
        head.addOrReplaceChild("kuuga_horn_right",
                CubeListBuilder.create().texOffs(4, 22)
                        .addBox(-0.45F, -4.70F, -0.45F, 0.90F, 5.20F, 0.90F, new CubeDeformation(0.08F)),
                PartPose.offsetAndRotation(2.15F, -7.65F, -2.45F, -0.16F, 0.0F, 0.25F));

        body.addOrReplaceChild("kuuga_chest",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-4.45F, -0.25F, -2.35F, 8.90F, 6.10F, 4.70F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        body.addOrReplaceChild("kuuga_chest_crest",
                CubeListBuilder.create().texOffs(20, 12)
                        .addBox(-1.05F, 0.30F, -2.78F, 2.10F, 5.10F, 0.55F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("kuuga_belt",
                CubeListBuilder.create().texOffs(20, 18)
                        .addBox(-4.60F, 9.35F, -2.42F, 9.20F, 2.10F, 4.84F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("kuuga_belt_core",
                CubeListBuilder.create().texOffs(20, 26)
                        .addBox(-1.55F, 9.05F, -3.05F, 3.10F, 2.70F, 0.80F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        float armWidth = slim ? 3.0F : 4.0F;
        float armMinX = -armWidth / 2.0F;
        leftArm.addOrReplaceChild("kuuga_shoulder_left",
                CubeListBuilder.create().texOffs(38, 18)
                        .addBox(armMinX - 0.65F, -2.45F, -2.65F, armWidth + 1.30F, 3.35F, 5.30F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("kuuga_shoulder_right",
                CubeListBuilder.create().texOffs(38, 18)
                        .addBox(armMinX - 0.65F, -2.45F, -2.65F, armWidth + 1.30F, 3.35F, 5.30F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        leftArm.addOrReplaceChild("kuuga_gauntlet_left",
                CubeListBuilder.create().texOffs(38, 28)
                        .addBox(armMinX - 0.28F, 5.65F, -2.28F, armWidth + 0.56F, 4.55F, 4.56F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("kuuga_gauntlet_right",
                CubeListBuilder.create().texOffs(38, 28)
                        .addBox(armMinX - 0.28F, 5.65F, -2.28F, armWidth + 0.56F, 4.55F, 4.56F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        leftLeg.addOrReplaceChild("kuuga_shin_left",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-2.28F, 6.15F, -2.30F, 4.56F, 5.95F, 4.60F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightLeg.addOrReplaceChild("kuuga_shin_right",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-2.28F, 6.15F, -2.30F, 4.56F, 5.95F, 4.60F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void configure(Pass pass, float progress) {
        configurePart(chest, pass == Pass.BASE, stage(progress, 0.08F, 0.34F));
        configurePart(shoulderLeft, pass == Pass.ACCENT, stage(progress, 0.16F, 0.42F));
        configurePart(shoulderRight, pass == Pass.ACCENT, stage(progress, 0.16F, 0.42F));
        configurePart(gauntletLeft, pass == Pass.BASE, stage(progress, 0.28F, 0.58F));
        configurePart(gauntletRight, pass == Pass.BASE, stage(progress, 0.28F, 0.58F));
        configurePart(shinLeft, pass == Pass.BASE, stage(progress, 0.30F, 0.60F));
        configurePart(shinRight, pass == Pass.BASE, stage(progress, 0.30F, 0.60F));
        configurePart(belt, pass == Pass.ACCENT, stage(progress, 0.38F, 0.66F));
        configurePart(chestCrest, pass == Pass.ACCENT, stage(progress, 0.44F, 0.70F));
        configurePart(helmet, pass == Pass.BASE, stage(progress, 0.56F, 0.82F));
        configurePart(hornLeft, pass == Pass.ACCENT, stage(progress, 0.72F, 0.96F));
        configurePart(hornRight, pass == Pass.ACCENT, stage(progress, 0.72F, 0.96F));
        configurePart(eyes, pass == Pass.CORE, stage(progress, 0.82F, 1.00F));
        configurePart(beltCore, pass == Pass.CORE, stage(progress, 0.76F, 0.98F));
    }

    private static void configurePart(ModelPart part, boolean activePass, float reveal) {
        part.visible = activePass && reveal > 0.01F;
        float eased = reveal * reveal * (3.0F - 2.0F * reveal);
        float scale = 0.30F + 0.70F * eased;
        part.xScale = scale;
        part.yScale = scale;
        part.zScale = scale;
    }

    private static float stage(float progress, float start, float end) {
        if (progress <= start) {
            return 0.0F;
        }
        if (progress >= end) {
            return 1.0F;
        }
        return (progress - start) / (end - start);
    }
}
