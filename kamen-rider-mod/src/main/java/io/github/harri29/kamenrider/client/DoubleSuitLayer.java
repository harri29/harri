package io.github.harri29.kamenrider.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.harri29.kamenrider.DoubleMemoryItem;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Dedicated split-color renderer for all nine W memory combinations. */
public final class DoubleSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SOLID_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/block/white_concrete.png"
    );
    private static final int SILVER = 0xFFD5D8DC;
    private static final int EYE_RED = 0xFFFF3344;

    private final DoubleSuitModel armorModel;

    public DoubleSuitLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        this.armorModel = new DoubleSuitModel(root, slim);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        RiderForm form = RiderClientState.form(player.getId()).orElse(null);
        if (form == null || !form.seriesId().equals("double") || player.isInvisible()) {
            return;
        }

        getParentModel().copyPropertiesTo(armorModel);
        armorModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        armorModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float progress = RiderClientState.henshinProgress(player.getId());

        armorModel.configure(DoubleSuitModel.Pass.LEFT, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                packedLight,
                player,
                leftColor(DoubleMemoryItem.leftMemory(form))
        );

        armorModel.configure(DoubleSuitModel.Pass.RIGHT, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                packedLight,
                player,
                rightColor(DoubleMemoryItem.rightMemory(form))
        );

        armorModel.configure(DoubleSuitModel.Pass.ACCENT, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                packedLight,
                player,
                SILVER
        );

        armorModel.configure(DoubleSuitModel.Pass.CORE, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                LightTexture.FULL_BRIGHT,
                player,
                EYE_RED
        );
    }

    private static int leftColor(DoubleMemoryItem.Memory memory) {
        return switch (memory) {
            case HEAT -> 0xFFD84535;
            case LUNA -> 0xFFE3C339;
            default -> 0xFF45B85A;
        };
    }

    private static int rightColor(DoubleMemoryItem.Memory memory) {
        return switch (memory) {
            case METAL -> 0xFFB9C0C8;
            case TRIGGER -> 0xFF2E70D1;
            default -> 0xFF21192A;
        };
    }
}
