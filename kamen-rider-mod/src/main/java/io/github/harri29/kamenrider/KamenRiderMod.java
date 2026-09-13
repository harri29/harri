package io.github.harri29.kamenrider;

import io.github.harri29.kamenrider.network.RiderNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

@Mod(KamenRiderMod.MODID)
public final class KamenRiderMod {
    public static final String MODID = "kamenrider";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<DriverItem> KUUGA_ARCLE = ITEMS.register("kuuga_arcle", () -> new DriverItem(RiderForm.KUUGA_MIGHTY, driverProperties()));
    public static final DeferredItem<DriverItem> DECADE_DRIVER = ITEMS.register("decade_driver", () -> new DriverItem(RiderForm.DECADE, driverProperties()));
    public static final DeferredItem<DriverItem> DOUBLE_DRIVER = ITEMS.register("double_driver", () -> new DriverItem(RiderForm.DOUBLE_CYCLONE_JOKER, driverProperties()));
    public static final DeferredItem<DriverItem> DESIRE_DRIVER = ITEMS.register("desire_driver", () -> new DriverItem(RiderForm.GEATS_MAGNUM_BOOST, driverProperties()));

    public static final DeferredItem<RiderTrialItem> KUUGA_TRIAL_SIGIL = ITEMS.register("kuuga_trial_sigil",
            () -> new RiderTrialItem(RiderTrialItem.Trial.KUUGA, trialProperties()));
    public static final DeferredItem<RiderTrialItem> DECADE_TRIAL_SIGIL = ITEMS.register("decade_trial_sigil",
            () -> new RiderTrialItem(RiderTrialItem.Trial.DECADE, trialProperties()));
    public static final DeferredItem<RiderTrialItem> DOUBLE_TRIAL_SIGIL = ITEMS.register("double_trial_sigil",
            () -> new RiderTrialItem(RiderTrialItem.Trial.DOUBLE, trialProperties()));
    public static final DeferredItem<RiderTrialItem> GEATS_TRIAL_SIGIL = ITEMS.register("geats_trial_sigil",
            () -> new RiderTrialItem(RiderTrialItem.Trial.GEATS, trialProperties()));

    public static final DeferredItem<FormChangeItem> KUUGA_FORM_CHANGER = ITEMS.register("kuuga_form_changer",
            () -> new FormChangeItem("kuuga", List.of(RiderForm.KUUGA_MIGHTY, RiderForm.KUUGA_DRAGON, RiderForm.KUUGA_PEGASUS, RiderForm.KUUGA_TITAN), formItemProperties()));
    public static final DeferredItem<KuugaWeaponItem> KUUGA_DRAGON_ROD = ITEMS.register("kuuga_dragon_rod",
            () -> new KuugaWeaponItem(RiderForm.KUUGA_DRAGON, KuugaWeaponItem.Style.DRAGON_ROD,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4.0F, -2.2F))));
    public static final DeferredItem<KuugaWeaponItem> KUUGA_TITAN_SWORD = ITEMS.register("kuuga_titan_sword",
            () -> new KuugaWeaponItem(RiderForm.KUUGA_TITAN, KuugaWeaponItem.Style.TITAN_SWORD,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 7.0F, -2.8F))));

    public static final DeferredItem<DecadeWeaponItem> RIDE_BOOKER = ITEMS.register("ride_booker",
            () -> new DecadeWeaponItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 5.0F, -2.4F))));
    public static final DeferredItem<DecadeCardItem> ATTACK_RIDE_SLASH = ITEMS.register("attack_ride_slash",
            () -> new DecadeCardItem(DecadeCardItem.CardAction.ATTACK_RIDE_SLASH, cardProperties()));
    public static final DeferredItem<DecadeCardItem> ATTACK_RIDE_BLAST = ITEMS.register("attack_ride_blast",
            () -> new DecadeCardItem(DecadeCardItem.CardAction.ATTACK_RIDE_BLAST, cardProperties()));
    public static final DeferredItem<DecadeCardItem> FINAL_ATTACK_RIDE = ITEMS.register("final_attack_ride_decade",
            () -> new DecadeCardItem(DecadeCardItem.CardAction.FINAL_ATTACK_RIDE, cardProperties()));

    public static final DeferredItem<DoubleMemoryItem> CYCLONE_MEMORY = ITEMS.register("cyclone_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.CYCLONE, memoryProperties()));
    public static final DeferredItem<DoubleMemoryItem> HEAT_MEMORY = ITEMS.register("heat_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.HEAT, memoryProperties()));
    public static final DeferredItem<DoubleMemoryItem> LUNA_MEMORY = ITEMS.register("luna_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.LUNA, memoryProperties()));
    public static final DeferredItem<DoubleMemoryItem> JOKER_MEMORY = ITEMS.register("joker_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.JOKER, memoryProperties()));
    public static final DeferredItem<DoubleMemoryItem> METAL_MEMORY = ITEMS.register("metal_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.METAL, memoryProperties()));
    public static final DeferredItem<DoubleMemoryItem> TRIGGER_MEMORY = ITEMS.register("trigger_memory", () -> new DoubleMemoryItem(DoubleMemoryItem.Memory.TRIGGER, memoryProperties()));
    public static final DeferredItem<DoubleWeaponItem> METAL_SHAFT = ITEMS.register("metal_shaft",
            () -> new DoubleWeaponItem(DoubleWeaponItem.Style.METAL_SHAFT,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4.5F, -2.2F))));
    public static final DeferredItem<DoubleWeaponItem> TRIGGER_MAGNUM = ITEMS.register("trigger_magnum",
            () -> new DoubleWeaponItem(DoubleWeaponItem.Style.TRIGGER_MAGNUM, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<FormChangeItem> DOUBLE_FORM_CHANGER = ITEMS.register("double_form_changer",
            () -> new FormChangeItem("double", List.of(RiderForm.DOUBLE_CYCLONE_JOKER, RiderForm.DOUBLE_HEAT_METAL, RiderForm.DOUBLE_LUNA_TRIGGER), formItemProperties()));

    public static final DeferredItem<GeatsBuckleItem> MAGNUM_BUCKLE = ITEMS.register("magnum_buckle",
            () -> new GeatsBuckleItem(GeatsBuckleItem.Buckle.MAGNUM, buckleProperties()));
    public static final DeferredItem<GeatsBuckleItem> NINJA_BUCKLE = ITEMS.register("ninja_buckle",
            () -> new GeatsBuckleItem(GeatsBuckleItem.Buckle.NINJA, buckleProperties()));
    public static final DeferredItem<GeatsBuckleItem> BOOST_BUCKLE = ITEMS.register("boost_buckle",
            () -> new GeatsBuckleItem(GeatsBuckleItem.Buckle.BOOST, buckleProperties()));
    public static final DeferredItem<GeatsBuckleItem> ZOMBIE_BUCKLE = ITEMS.register("zombie_buckle",
            () -> new GeatsBuckleItem(GeatsBuckleItem.Buckle.ZOMBIE, buckleProperties()));

    public static final DeferredItem<GeatsWeaponItem> MAGNUM_SHOOTER = ITEMS.register("magnum_shooter",
            () -> new GeatsWeaponItem(GeatsWeaponItem.Style.MAGNUM_SHOOTER, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredItem<GeatsWeaponItem> NINJA_DUALER = ITEMS.register("ninja_dualer",
            () -> new GeatsWeaponItem(GeatsWeaponItem.Style.NINJA_DUALER,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4.0F, -2.0F))));
    public static final DeferredItem<GeatsWeaponItem> ZOMBIE_BREAKER = ITEMS.register("zombie_breaker",
            () -> new GeatsWeaponItem(GeatsWeaponItem.Style.ZOMBIE_BREAKER,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 7.5F, -2.9F))));

    public static final DeferredItem<FormChangeItem> GEATS_FORM_CHANGER = ITEMS.register("geats_form_changer",
            () -> new FormChangeItem("geats", List.of(RiderForm.GEATS_MAGNUM_BOOST, RiderForm.GEATS_NINJA, RiderForm.GEATS_ZOMBIE), formItemProperties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RIDER_TAB = TABS.register("rider_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kamenrider"))
                    .icon(() -> KUUGA_ARCLE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(KUUGA_TRIAL_SIGIL.get());
                        output.accept(DECADE_TRIAL_SIGIL.get());
                        output.accept(DOUBLE_TRIAL_SIGIL.get());
                        output.accept(GEATS_TRIAL_SIGIL.get());

                        output.accept(KUUGA_ARCLE.get());
                        output.accept(KUUGA_FORM_CHANGER.get());
                        output.accept(KUUGA_DRAGON_ROD.get());
                        output.accept(KUUGA_TITAN_SWORD.get());

                        output.accept(DECADE_DRIVER.get());
                        output.accept(RIDE_BOOKER.get());
                        output.accept(ATTACK_RIDE_SLASH.get());
                        output.accept(ATTACK_RIDE_BLAST.get());
                        output.accept(FINAL_ATTACK_RIDE.get());

                        output.accept(DOUBLE_DRIVER.get());
                        output.accept(CYCLONE_MEMORY.get());
                        output.accept(JOKER_MEMORY.get());
                        output.accept(HEAT_MEMORY.get());
                        output.accept(METAL_MEMORY.get());
                        output.accept(LUNA_MEMORY.get());
                        output.accept(TRIGGER_MEMORY.get());
                        output.accept(METAL_SHAFT.get());
                        output.accept(TRIGGER_MAGNUM.get());

                        output.accept(DESIRE_DRIVER.get());
                        output.accept(MAGNUM_BUCKLE.get());
                        output.accept(BOOST_BUCKLE.get());
                        output.accept(NINJA_BUCKLE.get());
                        output.accept(ZOMBIE_BUCKLE.get());
                        output.accept(MAGNUM_SHOOTER.get());
                        output.accept(NINJA_DUALER.get());
                        output.accept(ZOMBIE_BREAKER.get());
                    })
                    .build());

    public KamenRiderMod(IEventBus modBus) {
        ITEMS.register(modBus);
        TABS.register(modBus);
        modBus.addListener(RiderNetworking::registerPayloadHandlers);
        NeoForge.EVENT_BUS.addListener(RiderNetworking::onStartTracking);
        NeoForge.EVENT_BUS.addListener(RiderTrialSystem::onLivingDeath);
    }

    private static Item.Properties driverProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC); }
    private static Item.Properties formItemProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.RARE); }
    private static Item.Properties cardProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC); }
    private static Item.Properties memoryProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.RARE); }
    private static Item.Properties buckleProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.RARE); }
    private static Item.Properties trialProperties() { return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC); }
}
