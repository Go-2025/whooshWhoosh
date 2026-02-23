package net.gopa.mc.whooshwhoosh.Handler;

import net.gopa.mc.whooshwhoosh.mixin.accessors.EnchantmentAccessorMixin;
import net.gopa.mc.whooshwhoosh.toolkit.registry.RegistryEnchantment;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.gopa.mc.whooshwhoosh.toolkit.util.EnchantmentUtil;
import net.gopa.mc.whooshwhoosh.toolkit.util.EntityUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchTriggerHandler extends Handler {

    @NotNull
    private final TriggerPoint point;
    public static final List<Triggerable> TRIGGERS = createTriggerList();
    public static final Map<TriggerPoint, HashSet<Triggerable>> POINT_MAP = createTriggerPointMap();
    private static final Map<TriggerPoint, EquipmentSlot[]> TRIGGER_TO_SLOTS_MAP = createTriggerSlotMapping();


    public EnchTriggerHandler(@NotNull TriggerPoint point) {
        this.point = point;
    }

    private static List<Triggerable> createTriggerList() {
        List<Triggerable> lst = new ArrayList<>();
        for (Enchantment ench : RegistryEnchantment.getEnchantments()) {
            if (!isTriggered(ench.getClass())) continue;
            Triggerable trigger = (Triggerable) ench;
            lst.add(trigger);
        }
        lst.sort(Comparator.comparingInt(EnchTriggerHandler::getOrder));
        return lst;
    }

    private static int getOrder(Triggerable trigger) {
        return trigger.getOrder();
    }

    private static Map<TriggerPoint, HashSet<Triggerable>> createTriggerPointMap() {
        Map<TriggerPoint, HashSet<Triggerable>> map = new EnumMap<>(TriggerPoint.class);

        for (Triggerable item : TRIGGERS) {
            List<TriggerPoint> points = Arrays.asList(item.getClass().getAnnotation(Trigger.class).value());
            points.remove(TriggerPoint.OTHER);
            for (TriggerPoint point : points) {
                map.put(point, new HashSet<>());
                map.get(point).add(item);
            }
        }
        return map;
    }

    protected static boolean isTriggered(Class<?> cls) {
        return Triggerable.class.isAssignableFrom(cls) && cls.isAnnotationPresent(Trigger.class);
    }

    protected boolean isOtherPoint() {
        return point == TriggerPoint.OTHER;
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
            NbtList enchantments = stack.getEnchantments();
            for (int i = enchantments.size() - 1; i >= 0; i--) {
                NbtCompound nbt = enchantments.getCompound(i);
                ActionResult res = processor.apply(nbt);
                if (res.isAccepted()) return res;
            }

            // 有优先级的 ↓↓↓

//            List<NbtCompound> enchNbtList = this.filterAndSortList(stack.getEnchantments());
//            WhooshwhooshMod.LOGGER.debug("processEnch: {}", enchNbtList);
//            for (int i = enchNbtList.size() - 1; i >= 0; i--) {
//                NbtCompound nbt = enchNbtList.get(i);
//                ActionResult res = processor.apply(nbt);
//                if (res.isAccepted()) return res;
//            }
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

        List<Triggerable> sortedList = TRIGGERS;

        Map<Triggerable, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sortedList.size(); i++) {
            indexMap.putIfAbsent(sortedList.get(i), i);
        }

        List<List<NbtCompound>> buckets = new ArrayList<>();
        for (int i = 0; i < sortedList.size(); i++) {
            buckets.add(new ArrayList<>());
        }

        for (NbtElement v : targetList) {
            NbtCompound nbt = (NbtCompound) v;
            Enchantment ench = EnchantmentUtil.getEnchByNbt(nbt);
            if (indexMap.containsKey(ench)) {
                int index = indexMap.get(ench);
                buckets.get(index).add(nbt);
            }
        }

        List<NbtCompound> result = new ArrayList<>();
        for (List<NbtCompound> bucket : buckets) {
            result.addAll(bucket);
        }
        return result;
    }

    private static Map<TriggerPoint, EquipmentSlot[]> createTriggerSlotMapping() {
        Map<TriggerPoint, EquipmentSlot[]> map = new HashMap<>();

        for (Map.Entry<TriggerPoint, HashSet<Triggerable>> entry : POINT_MAP.entrySet()) {
            TriggerPoint point = entry.getKey();
            HashSet<Triggerable> triggers = entry.getValue();

            EquipmentSlot[] slots = triggers.stream()
                    .map(t -> (EnchantmentAccessorMixin) t)
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
