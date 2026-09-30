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

/** Original Minecraft-style Geats armor with separate upper/lower Raise Buckle modules. */
public final class GeatsSuitModel extends PlayerModel<AbstractClientPlayer> {
    public enum Pass { BASE, UPPER, LOWER, CORE }

    private final ModelPart helmet;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart eyeVisor;
    private final ModelPart chest;
    private final ModelPart upperChest;
    private final ModelPart shoulderLeft;
    private final ModelPart shoulderRight;
    private final ModelPart belt;
    private final ModelPart beltCore;
    private final ModelPart lowerLeft;
    private final ModelPart lowerRight;
    private final ModelPart shinLeft;
    private final ModelPart shinRight;

    public GeatsSuitModel(ModelPart root, boolean slim) {
        super(root, slim);
        helmet = head.getChild("geats_helmet");
        earLeft = head.getChild("geats_ear_left");
        earRight = head.getChild("geats_ear_right");
        eyeVisor = head.getChild("geats_eye_visor");
        chest = body.getChild("geats_chest");
        upperChest = body.getChild("geats_upper_chest");
        belt = body.getChild("geats_belt");
        beltCore = body.getChild("geats_belt_core");
        shoulderLeft = leftArm.getChild("geats_shoulder_left");
        shoulderRight = rightArm.getChild("geats_shoulder_right");
        lowerLeft = leftLeg.getChild("geats_lower_left");
        lowerRight = rightLeg.getChild("geats_lower_right");
        shinLeft = leftLeg.getChild("geats_shin_left");
        shinRight = rightLeg.getChild("geats_shin_right");

        head.skipDraw = true;
        hat.visible = false;
        body.skipDraw = true;
        leftArm.skipDraw = true;
        rightArm.skipDraw = true;
        leftLeg.skipDraw = true;
        rightLeg.skipDraw = true;
        leftSleeve.visible = false;
        rightSleeve.visible = false;
        leftPants.visible = false;
        rightPants.visible = false;
        jacket.visible = false;
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

        head.addOrReplaceChild("geats_helmet",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.35F, -8.35F, -4.35F, 8.70F, 8.60F, 8.70F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        head.addOrReplaceChild("geats_eye_visor",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-3.55F, -5.45F, -4.74F, 7.10F, 2.15F, 0.58F, CubeDeformation.NONE),
                PartPose.ZERO);
        head.addOrReplaceChild("geats_ear_left",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-0.65F, -4.60F, -0.65F, 1.30F, 5.20F, 1.30F, new CubeDeformation(0.04F)),
                PartPose.offsetAndRotation(-2.25F, -7.85F, -0.6F, -0.10F, 0.0F, -0.30F));
        head.addOrReplaceChild("geats_ear_right",
                CubeListBuilder.create().texOffs(6, 22)
                        .addBox(-0.65F, -4.60F, -0.65F, 1.30F, 5.20F, 1.30F, new CubeDeformation(0.04F)),
                PartPose.offsetAndRotation(2.25F, -7.85F, -0.6F, -0.10F, 0.0F, 0.30F));

        body.addOrReplaceChild("geats_chest",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-4.45F, -0.25F, -2.35F, 8.90F, 7.00F, 4.70F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        body.addOrReplaceChild("geats_upper_chest",
                CubeListBuilder.create().texOffs(20, 15)
                        .addBox(-4.65F, 0.15F, -2.78F, 9.30F, 3.85F, 0.70F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        body.addOrReplaceChild("geats_belt",
                CubeListBuilder.create().texOffs(20, 22)
                        .addBox(-4.65F, 9.20F, -2.45F, 9.30F, 2.20F, 4.90F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("geats_belt_core",
                CubeListBuilder.create().texOffs(20, 30)
                        .addBox(-2.05F, 8.90F, -3.08F, 4.10F, 2.85F, 0.82F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        float armWidth = slim ? 3.0F : 4.0F;
        float armMinX = -armWidth / 2.0F;
        leftArm.addOrReplaceChild("geats_shoulder_left",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(armMinX - 0.70F, -2.45F, -2.62F, armWidth + 1.40F, 3.20F, 5.24F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("geats_shoulder_right",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(armMinX - 0.70F, -2.45F, -2.62F, armWidth + 1.40F, 3.20F, 5.24F, new CubeDeformation(0.05F)),
                PartPose.ZERO);

        leftLeg.addOrReplaceChild("geats_lower_left",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-2.30F, -0.15F, -2.36F, 4.60F, 5.60F, 4.72F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightLeg.addOrReplaceChild("geats_lower_right",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-2.30F, -0.15F, -2.36F, 4.60F, 5.60F, 4.72F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        leftLeg.addOrReplaceChild("geats_shin_left",
                CubeListBuilder.create().texOffs(20, 36)
                        .addBox(-2.25F, 5.45F, -2.30F, 4.50F, 6.65F, 4.60F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightLeg.addOrReplaceChild("geats_shin_right",
                CubeListBuilder.create().texOffs(20, 36)
                        .addBox(-2.25F, 5.45F, -2.30F, 4.50F, 6.65F, 4.60F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void configure(Pass pass, float progress) {
        configurePart(chest, pass == Pass.BASE, stage(progress, 0.04F, 0.28F));
        configurePart(helmet, pass == Pass.BASE, stage(progress, 0.18F, 0.48F));
        configurePart(earLeft, pass == Pass.BASE, stage(progress, 0.34F, 0.58F));
        configurePart(earRight, pass == Pass.BASE, stage(progress, 0.34F, 0.58F));
        configurePart(upperChest, pass == Pass.UPPER, stage(progress, 0.42F, 0.70F));
        configurePart(shoulderLeft, pass == Pass.UPPER, stage(progress, 0.46F, 0.74F));
        configurePart(shoulderRight, pass == Pass.UPPER, stage(progress, 0.46F, 0.74F));
        configurePart(lowerLeft, pass == Pass.LOWER, stage(progress, 0.52F, 0.80F));
        configurePart(lowerRight, pass == Pass.LOWER, stage(progress, 0.52F, 0.80F));
        configurePart(shinLeft, pass == Pass.LOWER, stage(progress, 0.58F, 0.84F));
        configurePart(shinRight, pass == Pass.LOWER, stage(progress, 0.58F, 0.84F));
        configurePart(belt, pass == Pass.BASE, stage(progress, 0.64F, 0.88F));
        configurePart(eyeVisor, pass == Pass.CORE, stage(progress, 0.78F, 1.00F));
        configurePart(beltCore, pass == Pass.CORE, stage(progress, 0.74F, 0.98F));
    }

    private static void configurePart(ModelPart part, boolean activePass, float reveal) {
        part.visible = activePass && reveal > 0.01F;
        float eased = reveal * reveal * (3.0F - 2.0F * reveal);
        float scale = 0.28F + 0.72F * eased;
        part.xScale = scale;
        part.yScale = scale;
        part.zScale = scale;
    }

    private static float stage(float progress, float start, float end) {
        if (progress <= start) return 0.0F;
        if (progress >= end) return 1.0F;
        return (progress - start) / (end - start);
    }
}
