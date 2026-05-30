package me.alfie.alfinolib.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Sealed union representing how items are specified in an {@link ItemCost}: a single item,
 * a list of items, or an item tag. Exactly one variant must be present in the JSON.
 */
public sealed interface ItemCostIngredient
        permits ItemCostIngredient.SingleItem, ItemCostIngredient.ItemList, ItemCostIngredient.TagIngredient {

    record SingleItem(Item item) implements ItemCostIngredient {}

    record ItemList(List<Item> items) implements ItemCostIngredient {}

    record TagIngredient(TagKey<Item> tag) implements ItemCostIngredient {}

    // ---- codecs ----

    Codec<Item> ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec();
    Codec<TagKey<Item>> TAG_KEY_CODEC = Identifier.CODEC
            .xmap(rl -> TagKey.create(Registries.ITEM, rl), TagKey::location);

    /**
     * Inlinable map codec — the three keys ("item", "items", "tag") are written directly
     * into the parent JSON object rather than under a nested field.
     */
    MapCodec<ItemCostIngredient> MAP_CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of("item", "items", "tag").map(ops::createString);
        }

        @Override
        public <T> DataResult<ItemCostIngredient> decode(DynamicOps<T> ops, MapLike<T> input) {
            T itemVal  = input.get("item");
            T itemsVal = input.get("items");
            T tagVal   = input.get("tag");

            int present = (itemVal != null ? 1 : 0) + (itemsVal != null ? 1 : 0) + (tagVal != null ? 1 : 0);
            if (present != 1) {
                return DataResult.error(() -> "Exactly one of 'item', 'items', or 'tag' must be present");
            }

            if (itemVal  != null) return ITEM_CODEC.parse(ops, itemVal).map(SingleItem::new);
            if (itemsVal != null) return ITEM_CODEC.listOf().parse(ops, itemsVal).map(ItemList::new);
            return TAG_KEY_CODEC.parse(ops, tagVal).map(TagIngredient::new);
        }

        @Override
        public <T> RecordBuilder<T> encode(ItemCostIngredient input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            if (input instanceof SingleItem s) {
                return prefix.add("item", ITEM_CODEC.encodeStart(ops, s.item()));
            } else if (input instanceof ItemList l) {
                return prefix.add("items", ITEM_CODEC.listOf().encodeStart(ops, l.items()));
            } else if (input instanceof TagIngredient t) {
                return prefix.add("tag", TAG_KEY_CODEC.encodeStart(ops, t.tag()));
            }
            throw new IllegalStateException("Unknown ItemCostIngredient type: " + input);
        }
    };

    StreamCodec<FriendlyByteBuf, ItemCostIngredient> STREAM_CODEC = StreamCodec.of(
            (buf, ingredient) -> {
                if (ingredient instanceof SingleItem s) {
                    buf.writeByte(0);
                    buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(s.item()));
                } else if (ingredient instanceof ItemList l) {
                    buf.writeByte(1);
                    buf.writeVarInt(l.items().size());
                    for (Item item : l.items()) {
                        buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(item));
                    }
                } else if (ingredient instanceof TagIngredient t) {
                    buf.writeByte(2);
                    buf.writeIdentifier(t.tag().location());
                }
            },
            buf -> {
                byte type = buf.readByte();
                if (type == 0) {
                    return new SingleItem(BuiltInRegistries.ITEM.get(buf.readIdentifier()).map(Holder.Reference::value).orElse(Items.AIR));
                } else if (type == 1) {
                    int size = buf.readVarInt();
                    List<Item> items = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        items.add(BuiltInRegistries.ITEM.get(buf.readIdentifier()).map(Holder.Reference::value).orElse(Items.AIR));
                    }
                    return new ItemList(items);
                } else if (type == 2) {
                    return new TagIngredient(TagKey.create(Registries.ITEM, buf.readIdentifier()));
                }
                throw new IllegalStateException("Unknown ingredient type byte: " + type);
            }
    );
}
