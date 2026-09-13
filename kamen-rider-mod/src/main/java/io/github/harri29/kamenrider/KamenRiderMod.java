package io.github.harri29.kamenrider;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(KamenRiderMod.MODID)
public final class KamenRiderMod {
    public static final String MODID = "kamenrider";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<DriverItem> KUUGA_ARCLE = ITEMS.register("kuuga_arcle",
            () -> new DriverItem(RiderForm.KUUGA, driverProperties()));
    public static final DeferredItem<DriverItem> DECADE_DRIVER = ITEMS.register("decade_driver",
            () -> new DriverItem(RiderForm.DECADE, driverProperties()));
    public static final DeferredItem<DriverItem> DOUBLE_DRIVER = ITEMS.register("double_driver",
            () -> new DriverItem(RiderForm.DOUBLE, driverProperties()));
    public static final DeferredItem<DriverItem> DESIRE_DRIVER = ITEMS.register("desire_driver",
            () -> new DriverItem(RiderForm.GEATS, driverProperties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RIDER_TAB = TABS.register("rider_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kamenrider"))
                    .icon(() -> KUUGA_ARCLE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(KUUGA_ARCLE.get());
                        output.accept(DECADE_DRIVER.get());
                        output.accept(DOUBLE_DRIVER.get());
                        output.accept(DESIRE_DRIVER.get());
                    })
                    .build());

    public KamenRiderMod(IEventBus modBus) {
        ITEMS.register(modBus);
        TABS.register(modBus);
    }

    private static Item.Properties driverProperties() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC);
    }
}
