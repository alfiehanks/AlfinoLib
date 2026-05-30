package me.alfie.alfinolib.util.codec;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Standardised record and codecs for item costs used in datapack JSON.
 *
 * <p>JSON shape (exactly one of "item", "items", or "tag" must be present):
 * <pre>
 *   {"item":  "minecraft:diamond", "count": 5, "nbt": {...}}
 *   {"items": ["minecraft:diamond", "minecraft:emerald"]}
 *   {"tag":   "minecraft:logs", "count": 3}
 * </pre>
 *
 * <p>On the 1.21.1+ branch "nbt" is replaced by "components" and ItemData wraps DataComponentPatch,
 * but method signatures remain identical.
 */
public record ItemCost(ItemCostIngredient ingredient, int count, Optional<ItemData> data) {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ItemCost(ItemCostIngredient ingredient) {
        this(ingredient, 1, Optional.empty());
    }

    public ItemCost(ItemCostIngredient ingredient, int count) {
        this(ingredient, count, Optional.empty());
    }

    public ItemCost(ItemCostIngredient ingredient, ItemData data) {
        this(ingredient, 1, Optional.of(data));
    }

    public ItemCost(ItemCostIngredient ingredient, int count, ItemData data) {
        this(ingredient, count, Optional.of(data));
    }

    public static final ItemCost EMPTY = new ItemCost(new ItemCostIngredient.SingleItem(Items.AIR));

    /** Returns all items this cost can match. For tags, resolves against the current registry. */
    public List<Item> getItems() {
        if (ingredient instanceof ItemCostIngredient.SingleItem s) {
            return List.of(s.item());
        } else if (ingredient instanceof ItemCostIngredient.ItemList l) {
            return List.copyOf(l.items());
        } else if (ingredient instanceof ItemCostIngredient.TagIngredient t) {
            Optional<HolderSet.Named<Item>> holders = BuiltInRegistries.ITEM.getTag(t.tag());
            if (holders.isEmpty()) {
                LOGGER.warn("ItemCost: tag '{}' could not be resolved - please wait for TagsUpdated before getting items, returning empty list", t.tag().location());
            }
            return holders.map(named -> named.stream().map(Holder::value).toList()).orElse(List.of());
        }
        return List.of();
    }

    /** Returns true if the stack matches this cost: correct item, count >= cost count, and matching NBT if specified. */
    public boolean isValid(ItemStack stack) {
        if (!getItems().contains(stack.getItem())) return false;
        if (stack.getCount() < count()) return false;
        return data().map(d -> Objects.equals(stack.getComponentsPatch(), d.components())).orElse(true);
    }

    /** Shrinks the stack by cost count if valid. Returns true if consumed, false if not. */
    public boolean tryConsume(ItemStack stack) {
        if (!isValid(stack)) return false;
        stack.shrink(count());
        return true;
    }

    public List<ItemStack> getItemStacks() {
        return getItems().stream()
                .map(item -> {
                    return data()
                            .map(d -> new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(item), count(), d.components()))
                            .orElseGet(() -> new ItemStack(item, count()));
                })
                .toList();
    }

    // ---- codecs ----

    public static final Codec<ItemCost> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemCostIngredient.MAP_CODEC.forGetter(ItemCost::ingredient),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(ItemCost::count),
                    ItemData.CODEC.optionalFieldOf("components").forGetter(ItemCost::data)
            ).apply(instance, ItemCost::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemCost> STREAM_CODEC = StreamCodec.of(
            ItemCost::encode,
            ItemCost::decode
    );

    private static void encode(RegistryFriendlyByteBuf buf, ItemCost cost) {
        ItemCostIngredient.STREAM_CODEC.encode(buf, cost.ingredient());
        buf.writeVarInt(cost.count());
        buf.writeBoolean(cost.data().isPresent());
        cost.data().ifPresent(data -> ItemData.STREAM_CODEC.encode(buf, data));
    }

    private static ItemCost decode(RegistryFriendlyByteBuf buf) {
        ItemCostIngredient ingredient = ItemCostIngredient.STREAM_CODEC.decode(buf);
        int count = buf.readVarInt();
        Optional<ItemData> data = buf.readBoolean()
                ? Optional.of(ItemData.STREAM_CODEC.decode(buf))
                : Optional.empty();
        return new ItemCost(ingredient, count, data);
    }
}
