package io.github.harri29.kamenrider.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.harri29.kamenrider.GeatsBuckleItem;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Dedicated modular renderer for Kamen Rider Geats. */
public final class GeatsSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SOLID_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/block/white_concrete.png"
    );

    private final GeatsSuitModel armorModel;

    public GeatsSuitLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        this.armorModel = new GeatsSuitModel(root, slim);
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
        if (form == null || !form.seriesId().equals("geats") || player.isInvisible()) return;

        getParentModel().copyPropertiesTo(armorModel);
        armorModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        armorModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float progress = RiderClientState.henshinProgress(player.getId());

        armorModel.configure(GeatsSuitModel.Pass.BASE, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, 0xFFF1F1F1);

        armorModel.configure(GeatsSuitModel.Pass.UPPER, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, upperColor(form));

        armorModel.configure(GeatsSuitModel.Pass.LOWER, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, lowerColor(form));

        armorModel.configure(GeatsSuitModel.Pass.CORE, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, LightTexture.FULL_BRIGHT, player, 0xFFFF3348);
    }

    private static int upperColor(RiderForm form) {
        return GeatsBuckleItem.hasUpper(form, GeatsBuckleItem.Buckle.NINJA)
                ? 0xFF00A98F
                : 0xFFE53935;
    }

    private static int lowerColor(RiderForm form) {
        return GeatsBuckleItem.hasLower(form, GeatsBuckleItem.Buckle.ZOMBIE)
                ? 0xFF6C3C9A
                : 0xFFF05A34;
    }
}
