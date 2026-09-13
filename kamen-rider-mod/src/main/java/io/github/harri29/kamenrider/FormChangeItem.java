package io.github.harri29.kamenrider;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * Cycles through a predefined list of forms for one Rider series.
 */
public class FormChangeItem extends Item {
    private final String seriesId;
    private final List<RiderForm> forms;

    public FormChangeItem(String seriesId, List<RiderForm> forms, Properties properties) {
        super(properties);
        if (forms.isEmpty()) {
            throw new IllegalArgumentException("A form changer must contain at least one form");
        }
        this.seriesId = seriesId;
        this.forms = List.copyOf(forms);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            Optional<RiderForm> current = RiderTransformation.currentForm(player);
            if (current.isEmpty() || !current.get().seriesId().equals(seriesId)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.form_required"), true);
                return InteractionResultHolder.fail(stack);
            }

            int currentIndex = forms.indexOf(current.get());
            int nextIndex = currentIndex < 0 ? 0 : (currentIndex + 1) % forms.size();
            RiderTransformation.changeForm(serverLevel, player, forms.get(nextIndex));
        }

        return InteractionResultHolder.success(stack);
    }
}
