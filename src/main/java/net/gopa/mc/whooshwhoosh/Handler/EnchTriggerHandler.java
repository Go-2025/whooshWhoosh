package net.gopa.mc.whooshwhoosh.Handler;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.gopa.mc.whooshwhoosh.mixin.accessors.EnchantmentAccessorMixin;
import net.gopa.mc.whooshwhoosh.util.EnchantmentUtil;
import net.gopa.mc.whooshwhoosh.util.EntityUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class EnchTriggerHandler extends TriggerHandler {

    private static final Map<TriggerPoint, EquipmentSlot[]> EQUIPMENT_SLOT_MAP = createEquipmentSlotMap();
    private final TriggerPoint point;

    private final List<ActionResult> results = new ArrayList<>();

    public EnchTriggerHandler(TriggerPoint point) {
        this.point = point;
    }

    private EnchTriggerHandler handle(List<ItemStack> stacks, BiFunction<Enchantment, Integer, ActionResult> processor) {
        ActionResult res = EnchantmentUtil.processEnch(stacks, (processor));

        results.add(res);
        return this;
    }

    public EnchTriggerHandler handle(
            Entity entity, BiFunction<Triggerable, Integer, ActionResult> processor
    ) throws IllegalArgumentException {
        if (this.point.equals(TriggerPoint.OTHER)) throw new IllegalArgumentException("Cannot handle 'OTHER' trigger point");

        EquipmentSlot[] slots = EQUIPMENT_SLOT_MAP.get(point);
        List<ItemStack> stacks = EntityUtils.getEquippedItems(entity, slots);

        return this.handle(stacks, (ench, lvl) -> {
            if (ench instanceof Triggerable triggerable && point.hasTriggerPoint(triggerable)) {
                return processor.apply(triggerable, lvl);
            }
            return ActionResult.PASS;
        });
    }

    public <T extends Enchantment> EnchTriggerHandler handle(
            Entity entity, T enchantment, BiFunction<T, Integer, ActionResult> processor
    ) {
        EnchantmentAccessorMixin accessor = (EnchantmentAccessorMixin) enchantment;
        List<ItemStack> stacks = EntityUtils.getEquippedItems(entity, accessor.getSlotTypes());

        ActionResult res = ActionResult.PASS;
        for (ItemStack stack : stacks) {
            int level = EnchantmentHelper.getLevel(enchantment, stack);
            if (level > 0) {
                res = processor.apply(enchantment, level);
                if (res != ActionResult.PASS) break;
            }
        }

        results.add(res);
        return this;
    }

    public ActionResult result() {
        return results.stream()
                .filter(ActionResult::isAccepted)
                .findFirst()
                .orElse(ActionResult.PASS);
    }

    private static Map<TriggerPoint, EquipmentSlot[]> createEquipmentSlotMap() {
        Map<TriggerPoint, EquipmentSlot[]> map = new HashMap<>();

        for (Map.Entry<TriggerPoint, Class<?>[]> entry : POINT_MAP.entrySet()) {
            TriggerPoint point = entry.getKey();
            Class<?>[] classes = entry.getValue();

            EquipmentSlot[] slots = Arrays.stream(classes)
                    .map(c -> {
                        Enchantment e = EnchantmentUtil.getEnchByClass(c);
                        return e == null ? null : (EnchantmentAccessorMixin) e;
                    })
                    .filter(Objects::nonNull)
                    .flatMap(accessor -> Arrays.stream(accessor.getSlotTypes()))
                    .collect(Collectors.toSet())
                    .toArray(EquipmentSlot[]::new);
            map.put(point, slots);
        }
        return map;
    }
}
