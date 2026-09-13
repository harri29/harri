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
 * Small dynamic waist-equipment overlay shared by every Rider.
 * Dedicated suit models already contain the belt body; this layer only renders
 * the active modules/core so the equipment shown at the waist matches gameplay.
 */
public final class RiderWaistModel extends PlayerModel<AbstractClientPlayer> {
    public enum Pass { FRAME, LEFT, RIGHT, CORE }

    private final ModelPart frame;
    private final ModelPart leftModule;
    private final ModelPart rightModule;
    private final ModelPart core;

    public RiderWaistModel(ModelPart root, boolean slim) {
        super(root, slim);
        this.frame = this.body.getChild("rider_driver_frame");
        this.leftModule = this.body.getChild("rider_driver_left");
        this.rightModule = this.body.getChild("rider_driver_right");
        this.core = this.body.getChild("rider_driver_core");

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
        PartDefinition body = root.getChild("body");

        body.addOrReplaceChild("rider_driver_frame",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.45F, 9.20F, -3.00F, 6.90F, 2.05F, 0.55F, new CubeDeformation(0.03F)),
                PartPose.ZERO);
        body.addOrReplaceChild("rider_driver_left",
                CubeListBuilder.create().texOffs(0, 4)
                        .addBox(-3.10F, 9.34F, -3.58F, 2.25F, 1.75F, 0.78F, new CubeDeformation(0.02F)),
                PartPose.ZERO);
        body.addOrReplaceChild("rider_driver_right",
                CubeListBuilder.create().texOffs(7, 4)
                        .addBox(0.85F, 9.34F, -3.58F, 2.25F, 1.75F, 0.78F, new CubeDeformation(0.02F)),
                PartPose.ZERO);
        body.addOrReplaceChild("rider_driver_core",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(-0.82F, 9.08F, -3.78F, 1.64F, 2.18F, 0.92F, new CubeDeformation(0.03F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 16);
    }

    public void configure(Pass pass, float progress, String seriesId) {
        boolean dual = seriesId.equals("double") || seriesId.equals("geats");
        frame.visible = pass == Pass.FRAME && progress > 0.01F;
        leftModule.visible = pass == Pass.LEFT && dual && progress > 0.01F;
        rightModule.visible = pass == Pass.RIGHT && dual && progress > 0.01F;
        core.visible = pass == Pass.CORE && progress > 0.01F;

        float eased = progress * progress * (3.0F - 2.0F * progress);
        float scale = 0.45F + 0.55F * eased;
        frame.xScale = frame.yScale = frame.zScale = scale;
        leftModule.xScale = leftModule.yScale = leftModule.zScale = scale;
        rightModule.xScale = rightModule.yScale = rightModule.zScale = scale;
        core.xScale = core.yScale = core.zScale = scale;
    }
}
