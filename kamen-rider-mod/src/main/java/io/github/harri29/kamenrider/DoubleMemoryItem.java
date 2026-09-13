package io.github.harri29.kamenrider;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * One physical Gaia Memory item. The left and right halves of W are selected
 * independently, producing the full 3x3 matrix of implemented combinations.
 */
public final class DoubleMemoryItem extends Item {
    public enum Side { LEFT, RIGHT }

    public enum Memory {
        CYCLONE("cyclone", Side.LEFT),
        HEAT("heat", Side.LEFT),
        LUNA("luna", Side.LEFT),
        JOKER("joker", Side.RIGHT),
        METAL("metal", Side.RIGHT),
        TRIGGER("trigger", Side.RIGHT);

        private final String id;
        private final Side side;

        Memory(String id, Side side) {
            this.id = id;
            this.side = side;
        }

        public String id() { return id; }
        public Side side() { return side; }
        public String translationKey() { return "memory.kamenrider." + id; }
    }

    private final Memory memory;

    public DoubleMemoryItem(Memory memory, Properties properties) {
        super(properties);
        this.memory = memory;
    }

    public Memory memory() {
        return memory;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RiderForm current = RiderTransformation.currentForm(player).orElse(null);
            if (current == null || !current.seriesId().equals("double")) {
                player.displayClientMessage(Component.translatable("message.kamenrider.memory_requires_double"), true);
                return InteractionResultHolder.success(stack);
            }

            Memory left = leftMemory(current);
            Memory right = rightMemory(current);

            if (player.isShiftKeyDown()) {
                boolean activeMemory = memory.side() == Side.LEFT ? left == memory : right == memory;
                if (!activeMemory) {
                    player.displayClientMessage(Component.translatable("message.kamenrider.maximum_drive_memory_inactive"), true);
                    return InteractionResultHolder.success(stack);
                }
                RiderFinisher.maximumDrive(serverLevel, player, current, memory, this);
                return InteractionResultHolder.success(stack);
            }

            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.success(stack);
            }

            if (memory.side() == Side.LEFT) {
                left = memory;
            } else {
                right = memory;
            }

            RiderForm target = resolve(left, right);
            RiderTransformation.changeForm(serverLevel, player, target);
            player.getCooldowns().addCooldown(this, 8);
            player.displayClientMessage(
                    Component.translatable(
                            "message.kamenrider.memory_insert",
                            Component.translatable(memory.translationKey()),
                            Component.translatable(target.translationKey())
                    ),
                    true
            );
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 0.85F, memory.side() == Side.LEFT ? 1.32F : 0.96F);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    player.getX(), player.getY() + 1.0D, player.getZ(),
                    24, 0.45D, 0.75D, 0.45D, 0.06D);
        }
        return InteractionResultHolder.success(stack);
    }

    public static Memory leftMemory(RiderForm form) {
        return switch (form) {
            case DOUBLE_HEAT_JOKER, DOUBLE_HEAT_METAL, DOUBLE_HEAT_TRIGGER -> Memory.HEAT;
            case DOUBLE_LUNA_JOKER, DOUBLE_LUNA_METAL, DOUBLE_LUNA_TRIGGER -> Memory.LUNA;
            default -> Memory.CYCLONE;
        };
    }

    public static Memory rightMemory(RiderForm form) {
        return switch (form) {
            case DOUBLE_CYCLONE_METAL, DOUBLE_HEAT_METAL, DOUBLE_LUNA_METAL -> Memory.METAL;
            case DOUBLE_CYCLONE_TRIGGER, DOUBLE_HEAT_TRIGGER, DOUBLE_LUNA_TRIGGER -> Memory.TRIGGER;
            default -> Memory.JOKER;
        };
    }

    public static RiderForm resolve(Memory left, Memory right) {
        if (left == Memory.CYCLONE) {
            return switch (right) {
                case METAL -> RiderForm.DOUBLE_CYCLONE_METAL;
                case TRIGGER -> RiderForm.DOUBLE_CYCLONE_TRIGGER;
                default -> RiderForm.DOUBLE_CYCLONE_JOKER;
            };
        }
        if (left == Memory.HEAT) {
            return switch (right) {
                case METAL -> RiderForm.DOUBLE_HEAT_METAL;
                case TRIGGER -> RiderForm.DOUBLE_HEAT_TRIGGER;
                default -> RiderForm.DOUBLE_HEAT_JOKER;
            };
        }
        return switch (right) {
            case METAL -> RiderForm.DOUBLE_LUNA_METAL;
            case TRIGGER -> RiderForm.DOUBLE_LUNA_TRIGGER;
            default -> RiderForm.DOUBLE_LUNA_JOKER;
        };
    }
}
