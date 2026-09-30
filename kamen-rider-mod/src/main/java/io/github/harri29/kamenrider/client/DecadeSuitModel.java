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

/** Original Minecraft-style Decade armor geometry on the vanilla player skeleton. */
public final class DecadeSuitModel extends PlayerModel<AbstractClientPlayer> {
    public enum Pass { BASE, ACCENT, CARD, CORE }

    private final ModelPart helmet;
    private final ModelPart eyeBar;
    private final ModelPart faceRailLeft;
    private final ModelPart faceRailRight;
    private final ModelPart crownRailLeft;
    private final ModelPart crownRailRight;
    private final ModelPart chest;
    private final ModelPart chestStripe;
    private final ModelPart belt;
    private final ModelPart beltCore;
    private final ModelPart leftShoulder;
    private final ModelPart rightShoulder;
    private final ModelPart leftForearm;
    private final ModelPart rightForearm;
    private final ModelPart leftShin;
    private final ModelPart rightShin;

    public DecadeSuitModel(ModelPart root, boolean slim) {
        super(root, slim);
        helmet = head.getChild("decade_helmet");
        eyeBar = head.getChild("decade_eye_bar");
        faceRailLeft = head.getChild("decade_face_rail_left");
        faceRailRight = head.getChild("decade_face_rail_right");
        crownRailLeft = head.getChild("decade_crown_rail_left");
        crownRailRight = head.getChild("decade_crown_rail_right");
        chest = body.getChild("decade_chest");
        chestStripe = body.getChild("decade_chest_stripe");
        belt = body.getChild("decade_belt");
        beltCore = body.getChild("decade_belt_core");
        leftShoulder = leftArm.getChild("decade_shoulder_left");
        rightShoulder = rightArm.getChild("decade_shoulder_right");
        leftForearm = leftArm.getChild("decade_forearm_left");
        rightForearm = rightArm.getChild("decade_forearm_right");
        leftShin = leftLeg.getChild("decade_shin_left");
        rightShin = rightLeg.getChild("decade_shin_right");

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

        head.addOrReplaceChild("decade_helmet",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.30F, -8.30F, -4.30F, 8.60F, 8.55F, 8.60F, new CubeDeformation(0.06F)),
                PartPose.ZERO);
        head.addOrReplaceChild("decade_eye_bar",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-3.50F, -5.15F, -4.72F, 7.0F, 2.05F, 0.58F, CubeDeformation.NONE),
                PartPose.ZERO);
        head.addOrReplaceChild("decade_face_rail_left",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-0.42F, -3.80F, -0.45F, 0.84F, 7.1F, 0.90F, new CubeDeformation(0.03F)),
                PartPose.offsetAndRotation(-2.25F, -4.55F, -4.42F, 0.0F, 0.0F, -0.10F));
        head.addOrReplaceChild("decade_face_rail_right",
                CubeListBuilder.create().texOffs(4, 22)
                        .addBox(-0.42F, -3.80F, -0.45F, 0.84F, 7.1F, 0.90F, new CubeDeformation(0.03F)),
                PartPose.offsetAndRotation(2.25F, -4.55F, -4.42F, 0.0F, 0.0F, 0.10F));
        head.addOrReplaceChild("decade_crown_rail_left",
                CubeListBuilder.create().texOffs(8, 22)
                        .addBox(-0.38F, -3.20F, -0.40F, 0.76F, 4.2F, 0.80F, new CubeDeformation(0.03F)),
                PartPose.offsetAndRotation(-1.25F, -8.15F, -2.3F, -0.12F, 0.0F, -0.08F));
        head.addOrReplaceChild("decade_crown_rail_right",
                CubeListBuilder.create().texOffs(12, 22)
                        .addBox(-0.38F, -3.20F, -0.40F, 0.76F, 4.2F, 0.80F, new CubeDeformation(0.03F)),
                PartPose.offsetAndRotation(1.25F, -8.15F, -2.3F, -0.12F, 0.0F, 0.08F));

        body.addOrReplaceChild("decade_chest",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-4.45F, -0.30F, -2.35F, 8.90F, 6.80F, 4.70F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        body.addOrReplaceChild("decade_chest_stripe",
                CubeListBuilder.create().texOffs(20, 14)
                        .addBox(-1.00F, 0.10F, -2.80F, 2.0F, 6.2F, 0.55F, CubeDeformation.NONE),
                PartPose.rotation(0.0F, 0.0F, -0.42F));
        body.addOrReplaceChild("decade_belt",
                CubeListBuilder.create().texOffs(20, 22)
                        .addBox(-4.65F, 9.25F, -2.45F, 9.30F, 2.20F, 4.90F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("decade_belt_core",
                CubeListBuilder.create().texOffs(20, 30)
                        .addBox(-1.75F, 8.95F, -3.05F, 3.50F, 2.75F, 0.82F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        float armWidth = slim ? 3.0F : 4.0F;
        float armMinX = -armWidth / 2.0F;
        leftArm.addOrReplaceChild("decade_shoulder_left",
                CubeListBuilder.create().texOffs(40, 16)
                        .addBox(armMinX - 0.72F, -2.45F, -2.60F, armWidth + 1.44F, 3.0F, 5.20F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("decade_shoulder_right",
                CubeListBuilder.create().texOffs(40, 16)
                        .addBox(armMinX - 0.72F, -2.45F, -2.60F, armWidth + 1.44F, 3.0F, 5.20F, new CubeDeformation(0.05F)),
                PartPose.ZERO);
        leftArm.addOrReplaceChild("decade_forearm_left",
                CubeListBuilder.create().texOffs(40, 26)
                        .addBox(armMinX - 0.25F, 5.45F, -2.26F, armWidth + 0.50F, 4.7F, 4.52F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("decade_forearm_right",
                CubeListBuilder.create().texOffs(40, 26)
                        .addBox(armMinX - 0.25F, 5.45F, -2.26F, armWidth + 0.50F, 4.7F, 4.52F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        leftLeg.addOrReplaceChild("decade_shin_left",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-2.26F, 5.85F, -2.28F, 4.52F, 6.25F, 4.56F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightLeg.addOrReplaceChild("decade_shin_right",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-2.26F, 5.85F, -2.28F, 4.52F, 6.25F, 4.56F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void configure(Pass pass, float progress) {
        configurePart(chest, pass == Pass.BASE, stage(progress, 0.05F, 0.30F));
        configurePart(leftShoulder, pass == Pass.ACCENT, stage(progress, 0.12F, 0.38F));
        configurePart(rightShoulder, pass == Pass.ACCENT, stage(progress, 0.12F, 0.38F));
        configurePart(leftForearm, pass == Pass.BASE, stage(progress, 0.24F, 0.52F));
        configurePart(rightForearm, pass == Pass.BASE, stage(progress, 0.24F, 0.52F));
        configurePart(leftShin, pass == Pass.BASE, stage(progress, 0.28F, 0.56F));
        configurePart(rightShin, pass == Pass.BASE, stage(progress, 0.28F, 0.56F));
        configurePart(belt, pass == Pass.CARD, stage(progress, 0.36F, 0.62F));
        configurePart(chestStripe, pass == Pass.ACCENT, stage(progress, 0.42F, 0.70F));
        configurePart(helmet, pass == Pass.BASE, stage(progress, 0.54F, 0.80F));
        configurePart(faceRailLeft, pass == Pass.CARD, stage(progress, 0.64F, 0.88F));
        configurePart(faceRailRight, pass == Pass.CARD, stage(progress, 0.64F, 0.88F));
        configurePart(crownRailLeft, pass == Pass.CARD, stage(progress, 0.68F, 0.92F));
        configurePart(crownRailRight, pass == Pass.CARD, stage(progress, 0.68F, 0.92F));
        configurePart(eyeBar, pass == Pass.CORE, stage(progress, 0.78F, 1.00F));
        configurePart(beltCore, pass == Pass.CORE, stage(progress, 0.76F, 0.98F));
    }

    private static void configurePart(ModelPart part, boolean activePass, float reveal) {
        part.visible = activePass && reveal > 0.01F;
        float eased = reveal * reveal * (3.0F - 2.0F * reveal);
        float scale = 0.26F + 0.74F * eased;
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
