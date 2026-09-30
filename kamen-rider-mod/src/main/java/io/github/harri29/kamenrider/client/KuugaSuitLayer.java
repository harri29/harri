package io.github.harri29.kamenrider.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Dedicated staged armor renderer for the Kuuga family of forms. */
public final class KuugaSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SOLID_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/block/white_concrete.png"
    );
    private static final int GOLD = 0xFFD8B24B;

    private final KuugaSuitModel armorModel;

    public KuugaSuitLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        this.armorModel = new KuugaSuitModel(root, slim);
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
        if (form == null || !form.seriesId().equals("kuuga") || player.isInvisible()) {
            return;
        }

        getParentModel().copyPropertiesTo(armorModel);
        armorModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        armorModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float progress = RiderClientState.henshinProgress(player.getId());

        armorModel.configure(KuugaSuitModel.Pass.BASE, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                packedLight,
                player,
                0xFF000000 | form.suitColor()
        );

        armorModel.configure(KuugaSuitModel.Pass.ACCENT, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                packedLight,
                player,
                GOLD
        );

        armorModel.configure(KuugaSuitModel.Pass.CORE, progress);
        renderColoredCutoutModel(
                armorModel,
                SOLID_TEXTURE,
                poseStack,
                bufferSource,
                LightTexture.FULL_BRIGHT,
                player,
                coreColor(form)
        );
    }

    private static int coreColor(RiderForm form) {
        return switch (form) {
            case KUUGA_DRAGON -> 0xFF4F9DFF;
            case KUUGA_PEGASUS -> 0xFF47E18B;
            case KUUGA_TITAN -> 0xFFC067F2;
            default -> 0xFFFF3348;
        };
    }
}
