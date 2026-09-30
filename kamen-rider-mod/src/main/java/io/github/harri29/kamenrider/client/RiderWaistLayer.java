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

/** Dynamic waist modules layered over the dedicated Rider belt geometry. */
public final class RiderWaistLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SOLID_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/block/white_concrete.png"
    );
    private static final int FRAME = 0xFF202329;
    private static final int SILVER = 0xFFD5D9DE;

    private final RiderWaistModel waistModel;

    public RiderWaistLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        this.waistModel = new RiderWaistModel(root, slim);
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
        if (form == null || player.isInvisible()) return;

        getParentModel().copyPropertiesTo(waistModel);
        waistModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        waistModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float henshin = RiderClientState.henshinProgress(player.getId());
        float reveal = stage(henshin, 0.34F, 0.78F);
        String series = form.seriesId();

        waistModel.configure(RiderWaistModel.Pass.FRAME, reveal, series);
        renderColoredCutoutModel(waistModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player,
                series.equals("kuuga") ? SILVER : FRAME);

        waistModel.configure(RiderWaistModel.Pass.LEFT, reveal, series);
        renderColoredCutoutModel(waistModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player,
                leftColor(form));

        waistModel.configure(RiderWaistModel.Pass.RIGHT, reveal, series);
        renderColoredCutoutModel(waistModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player,
                rightColor(form));

        waistModel.configure(RiderWaistModel.Pass.CORE, stage(henshin, 0.62F, 0.96F), series);
        renderColoredCutoutModel(waistModel, SOLID_TEXTURE, poseStack, bufferSource, LightTexture.FULL_BRIGHT, player,
                coreColor(form));
    }

    private static int leftColor(RiderForm form) {
        if (form.seriesId().equals("double")) {
            return switch (DoubleMemoryItem.leftMemory(form)) {
                case HEAT -> 0xFFD84535;
                case LUNA -> 0xFFE3C339;
                default -> 0xFF45B85A;
            };
        }
        if (form.seriesId().equals("geats")) {
            return switch (form) {
                case GEATS_NINJA_BOOST, GEATS_NINJA_ZOMBIE, GEATS_NINJA -> 0xFF43B768;
                default -> 0xFFD74435;
            };
        }
        return SILVER;
    }

    private static int rightColor(RiderForm form) {
        if (form.seriesId().equals("double")) {
            return switch (DoubleMemoryItem.rightMemory(form)) {
                case METAL -> 0xFFB9C0C8;
                case TRIGGER -> 0xFF2E70D1;
                default -> 0xFF24192C;
            };
        }
        if (form.seriesId().equals("geats")) {
            return switch (form) {
                case GEATS_MAGNUM_ZOMBIE, GEATS_NINJA_ZOMBIE, GEATS_ZOMBIE -> 0xFF6C4B8C;
                default -> 0xFFF27A2B;
            };
        }
        return SILVER;
    }

    private static int coreColor(RiderForm form) {
        return switch (form.seriesId()) {
            case "kuuga" -> 0xFFFF3B30;
            case "decade" -> 0xFFFF2E88;
            case "double" -> 0xFFFF3344;
            case "geats" -> 0xFFFF4B36;
            default -> 0xFFFFFFFF;
        };
    }

    private static float stage(float progress, float start, float end) {
        if (progress <= start) return 0.0F;
        if (progress >= end) return 1.0F;
        return (progress - start) / (end - start);
    }
}
