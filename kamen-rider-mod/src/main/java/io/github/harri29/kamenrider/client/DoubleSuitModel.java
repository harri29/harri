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

/** Dedicated vertically split armor geometry for Kamen Rider W. */
public final class DoubleSuitModel extends PlayerModel<AbstractClientPlayer> {
    public enum Pass { LEFT, RIGHT, ACCENT, CORE }

    private final ModelPart helmetLeft;
    private final ModelPart helmetRight;
    private final ModelPart eyeLeft;
    private final ModelPart eyeRight;
    private final ModelPart chestLeft;
    private final ModelPart chestRight;
    private final ModelPart centerSeam;
    private final ModelPart beltFrame;
    private final ModelPart beltLeft;
    private final ModelPart beltRight;
    private final ModelPart leftShoulder;
    private final ModelPart rightShoulder;
    private final ModelPart leftForearm;
    private final ModelPart rightForearm;
    private final ModelPart leftShin;
    private final ModelPart rightShin;

    public DoubleSuitModel(ModelPart root, boolean slim) {
        super(root, slim);
        helmetLeft = head.getChild("double_helmet_left");
        helmetRight = head.getChild("double_helmet_right");
        eyeLeft = head.getChild("double_eye_left");
        eyeRight = head.getChild("double_eye_right");
        chestLeft = body.getChild("double_chest_left");
        chestRight = body.getChild("double_chest_right");
        centerSeam = body.getChild("double_center_seam");
        beltFrame = body.getChild("double_belt_frame");
        beltLeft = body.getChild("double_belt_left");
        beltRight = body.getChild("double_belt_right");
        leftShoulder = leftArm.getChild("double_shoulder_left");
        rightShoulder = rightArm.getChild("double_shoulder_right");
        leftForearm = leftArm.getChild("double_forearm_left");
        rightForearm = rightArm.getChild("double_forearm_right");
        leftShin = leftLeg.getChild("double_shin_left");
        rightShin = rightLeg.getChild("double_shin_right");

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

        head.addOrReplaceChild("double_helmet_left",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.35F, -8.35F, -4.35F, 4.35F, 8.60F, 8.70F, new CubeDeformation(0.06F)),
                PartPose.ZERO);
        head.addOrReplaceChild("double_helmet_right",
                CubeListBuilder.create().texOffs(18, 0)
                        .addBox(0.0F, -8.35F, -4.35F, 4.35F, 8.60F, 8.70F, new CubeDeformation(0.06F)),
                PartPose.ZERO);
        head.addOrReplaceChild("double_eye_left",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-3.55F, -5.30F, -4.76F, 3.45F, 2.10F, 0.58F, CubeDeformation.NONE),
                PartPose.ZERO);
        head.addOrReplaceChild("double_eye_right",
                CubeListBuilder.create().texOffs(8, 18)
                        .addBox(0.10F, -5.30F, -4.76F, 3.45F, 2.10F, 0.58F, CubeDeformation.NONE),
                PartPose.ZERO);

        body.addOrReplaceChild("double_chest_left",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-4.48F, -0.28F, -2.36F, 4.48F, 7.10F, 4.72F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        body.addOrReplaceChild("double_chest_right",
                CubeListBuilder.create().texOffs(38, 20)
                        .addBox(0.0F, -0.28F, -2.36F, 4.48F, 7.10F, 4.72F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        body.addOrReplaceChild("double_center_seam",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(-0.24F, -0.10F, -2.72F, 0.48F, 7.20F, 0.42F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("double_belt_frame",
                CubeListBuilder.create().texOffs(20, 34)
                        .addBox(-4.65F, 9.25F, -2.46F, 9.30F, 2.20F, 4.92F, CubeDeformation.NONE),
                PartPose.ZERO);
        body.addOrReplaceChild("double_belt_left",
                CubeListBuilder.create().texOffs(20, 42)
                        .addBox(-3.25F, 9.00F, -3.02F, 2.70F, 2.70F, 0.76F, new CubeDeformation(0.02F)),
                PartPose.ZERO);
        body.addOrReplaceChild("double_belt_right",
                CubeListBuilder.create().texOffs(28, 42)
                        .addBox(0.55F, 9.00F, -3.02F, 2.70F, 2.70F, 0.76F, new CubeDeformation(0.02F)),
                PartPose.ZERO);

        float armWidth = slim ? 3.0F : 4.0F;
        float armMinX = -armWidth / 2.0F;
        leftArm.addOrReplaceChild("double_shoulder_left",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(armMinX - 0.62F, -2.45F, -2.62F, armWidth + 1.24F, 3.20F, 5.24F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("double_shoulder_right",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(armMinX - 0.62F, -2.45F, -2.62F, armWidth + 1.24F, 3.20F, 5.24F, new CubeDeformation(0.04F)),
                PartPose.ZERO);
        leftArm.addOrReplaceChild("double_forearm_left",
                CubeListBuilder.create().texOffs(0, 42)
                        .addBox(armMinX - 0.25F, 5.45F, -2.27F, armWidth + 0.50F, 4.72F, 4.54F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightArm.addOrReplaceChild("double_forearm_right",
                CubeListBuilder.create().texOffs(0, 42)
                        .addBox(armMinX - 0.25F, 5.45F, -2.27F, armWidth + 0.50F, 4.72F, 4.54F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        leftLeg.addOrReplaceChild("double_shin_left",
                CubeListBuilder.create().texOffs(38, 42)
                        .addBox(-2.27F, 5.85F, -2.29F, 4.54F, 6.25F, 4.58F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        rightLeg.addOrReplaceChild("double_shin_right",
                CubeListBuilder.create().texOffs(38, 42)
                        .addBox(-2.27F, 5.85F, -2.29F, 4.54F, 6.25F, 4.58F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void configure(Pass pass, float progress) {
        float leftReveal = stage(progress, 0.06F, 0.46F);
        float rightReveal = stage(progress, 0.16F, 0.56F);

        configurePart(chestLeft, pass == Pass.LEFT, leftReveal);
        configurePart(leftShoulder, pass == Pass.LEFT, stage(progress, 0.12F, 0.50F));
        configurePart(leftForearm, pass == Pass.LEFT, stage(progress, 0.24F, 0.58F));
        configurePart(leftShin, pass == Pass.LEFT, stage(progress, 0.28F, 0.62F));
        configurePart(helmetLeft, pass == Pass.LEFT, stage(progress, 0.50F, 0.82F));
        configurePart(beltLeft, pass == Pass.LEFT, stage(progress, 0.42F, 0.70F));

        configurePart(chestRight, pass == Pass.RIGHT, rightReveal);
        configurePart(rightShoulder, pass == Pass.RIGHT, stage(progress, 0.20F, 0.56F));
        configurePart(rightForearm, pass == Pass.RIGHT, stage(progress, 0.30F, 0.64F));
        configurePart(rightShin, pass == Pass.RIGHT, stage(progress, 0.32F, 0.66F));
        configurePart(helmetRight, pass == Pass.RIGHT, stage(progress, 0.56F, 0.86F));
        configurePart(beltRight, pass == Pass.RIGHT, stage(progress, 0.46F, 0.74F));

        configurePart(centerSeam, pass == Pass.ACCENT, stage(progress, 0.34F, 0.68F));
        configurePart(beltFrame, pass == Pass.ACCENT, stage(progress, 0.38F, 0.72F));
        configurePart(eyeLeft, pass == Pass.CORE, stage(progress, 0.76F, 1.00F));
        configurePart(eyeRight, pass == Pass.CORE, stage(progress, 0.76F, 1.00F));
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
