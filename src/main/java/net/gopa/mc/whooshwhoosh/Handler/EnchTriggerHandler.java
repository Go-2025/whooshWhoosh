package net.gopa.mc.whooshwhoosh.Handler;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.mixin.accessors.EnchantmentAccessorMixin;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerHandler;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.gopa.mc.whooshwhoosh.util.EnchantmentUtil;
import net.gopa.mc.whooshwhoosh.util.EntityUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchTriggerHandler extends TriggerHandler {

    private static final Map<TriggerPoint, EquipmentSlot[]> TRIGGER_TO_SLOTS_MAP = createTriggerSlotMapping();

    public EnchTriggerHandler(TriggerPoint point) {
        super(point);
    }

    public EnchTriggerHandler handleStack(List<ItemStack> stacks, BiFunction<Triggerable, NbtCompound, ActionResult> processor) {
        boolean flag = isOtherPoint();

        return this.handle(stacks, (ench, comp) -> {
            if (!flag && ench instanceof Triggerable triggerable && point.hasTriggerPoint(triggerable)) {
                return processor.apply(triggerable, comp);
            }
            return ActionResult.PASS;
        });
    }

    public <T extends Enchantment> EnchTriggerHandler handleStack(List<ItemStack> stacks, T enchantment, BiFunction<T, NbtCompound, ActionResult> processor) {
        ActionResult res = ActionResult.PASS;
        for (ItemStack stack : stacks) {
            NbtCompound enchNbt = EnchantmentUtil.getEnchNbt(stack.getEnchantments(), enchantment);
            if (enchNbt != null) {
                res = processor.apply(enchantment, enchNbt);
                if (res != ActionResult.PASS) break;
            }
        }
        return addResult(res);
    }

    public EnchTriggerHandler handleEntity(Entity entity, BiFunction<Triggerable, Integer, ActionResult> processor) {
        boolean flag = isOtherPoint();

        List<ItemStack> stacks = getEquippedItems(entity);
        return this.handleEntity(stacks, (ench, lvl) -> {
            if (!flag && ench instanceof Triggerable triggerable && point.hasTriggerPoint(triggerable)) {
                return processor.apply(triggerable, lvl);
            }
            return ActionResult.PASS;
        });
    }

    public <T extends Enchantment> EnchTriggerHandler handleEntity(Entity entity, T enchantment, BiFunction<T, Integer, ActionResult> processor) {
        EnchantmentAccessorMixin accessor = (EnchantmentAccessorMixin) enchantment;
        List<ItemStack> stacks = EntityUtils.getEquippedItems(entity, accessor.getSlotTypes());

        return handle(stacks, enchantment, processor);
    }

    private EnchTriggerHandler handle(List<ItemStack> stacks, BiFunction<Enchantment, NbtCompound, ActionResult> processor) {
        ActionResult res = this.processEnch(stacks, comp -> processor.apply(EnchantmentUtil.getEnchByNbt(comp), comp));
        return addResult(res);
    }

    private EnchTriggerHandler handleEntity(List<ItemStack> stacks, BiFunction<Enchantment, Integer, ActionResult> processor) {
        ActionResult res = this.processEnch(stacks, processor);
        return addResult(res);
    }

    private <T extends Enchantment> EnchTriggerHandler handle(List<ItemStack> stacks, T enchantment, BiFunction<T, Integer, ActionResult> processor) {
        ActionResult res = ActionResult.PASS;
        for (ItemStack stack : stacks) {
            int level = EnchantmentHelper.getLevel(enchantment, stack);
            if (level > 0) {
                res = processor.apply(enchantment, level);
                if (res.isAccepted()) break;
            }
        }
        return addResult(res);
    }

    private ActionResult processEnch(List<ItemStack> stacks, Function<NbtCompound, ActionResult> processor) {
        for (ItemStack stack : stacks) {
            List<NbtCompound> enchNbtList = this.filterAndSortList(stack.getEnchantments());
            WhooshwhooshMod.LOGGER.debug("processEnch: {}", enchNbtList);
            for (int i = enchNbtList.size() - 1; i >= 0; i--) {
                NbtCompound nbt = enchNbtList.get(i);
                ActionResult res = processor.apply(nbt);
                if (res.isAccepted()) return res;
            }
        }
        return ActionResult.PASS;
    }

    private ActionResult processEnch(List<ItemStack> stacks, BiFunction<Enchantment, Integer, ActionResult> processor) {
        return processEnch(stacks, (nbt) -> {
            Enchantment ench = EnchantmentUtil.getEnchByNbt(nbt);

            if (ench != null) return processor.apply(ench, nbt.getInt("lvl"));
            return ActionResult.PASS;
        });
    }

    /**
     * 筛选并排序
     * @return 筛选并排序后的列表
     */
    private List<NbtCompound> filterAndSortList(List<NbtElement> targetList) {
        if (targetList.isEmpty()) return Collections.emptyList();

        List<Class<?>> sortedList = TRIGGERS;
        // 1. 创建元素->索引的映射（记录首次出现位置）
        Map<Class<?>, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sortedList.size(); i++) {
            indexMap.putIfAbsent(sortedList.get(i), i);
        }

        // 2. 创建分组桶（每个桶对应sortedList的一个位置）
        List<List<NbtCompound>> buckets = new ArrayList<>();
        for (int i = 0; i < sortedList.size(); i++) {
            buckets.add(new ArrayList<>());
        }

        // 3. 将targetList元素分配到对应桶中
        for (NbtElement v : targetList) {
            NbtCompound nbt = (NbtCompound) v;
            Enchantment ench = EnchantmentUtil.getEnchByNbt(nbt);
            if (indexMap.containsKey(ench.getClass())) {
                int index = indexMap.get(ench.getClass());
                buckets.get(index).add(nbt);
            }
        }

        // 4. 合并所有桶（按sortedList顺序）
        List<NbtCompound> result = new ArrayList<>();
        for (List<NbtCompound> bucket : buckets) {
            result.addAll(bucket);
        }
        return result;
    }

//    private <T> List<T> filterAndSortList(List<T> targetList) {
//        Set<Class<?>> validItems = new HashSet<>(TRIGGERS);
//        List<T> result = new ArrayList<>();
//        for (T item : targetList) {
//            if (validItems.contains(item.getClass())) {
//                result.add(item);
//            }
//        }
//        return result;
//    }

    private static Map<TriggerPoint, EquipmentSlot[]> createTriggerSlotMapping() {
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

    private List<ItemStack> getEquippedItems(Entity entity) {
        EquipmentSlot[] relevantSlots = TRIGGER_TO_SLOTS_MAP.getOrDefault(point, new EquipmentSlot[0]);
        return EntityUtils.getEquippedItems(entity, relevantSlots);
    }
}
