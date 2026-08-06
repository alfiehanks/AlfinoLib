package me.alfie.alfinolib.debug.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.alfie.alfinolib.networking.codec.CommonCodecs;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.networking.codec.StreamCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Test data example that holds a string and an int
 */
public record TestData(String testString, int testInt) {

    static final Codec<TestData> CODEC = RecordCodecBuilder.create(
            testDataInstance -> testDataInstance.group(
                    Codec.STRING.fieldOf("testString").forGetter(TestData::testString),
                    Codec.INT.fieldOf("testInt").forGetter(TestData::testInt)
            ).apply(testDataInstance, TestData::new)
    );

    static final StreamCodec<RegistryFriendlyByteBuf, TestData> STREAM_CODEC = StreamCodecBuilder.<RegistryFriendlyByteBuf, TestData>create()
            .add(CommonCodecs.STRING, TestData::testString)
            .add(CommonCodecs.VAR_INT, TestData::testInt)
            .build(TestData::new);

}
