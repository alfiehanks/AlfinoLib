package me.alfie.alfinolib.networking.codec;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public final class CommonCodecs {

    public static final StreamCodec<ByteBuf, Boolean> BOOL;
    public static final StreamCodec<ByteBuf, Byte>    BYTE;
    public static final StreamCodec<ByteBuf, Short>   SHORT;
    public static final StreamCodec<ByteBuf, Integer> INT;
    public static final StreamCodec<ByteBuf, Long>    LONG;
    public static final StreamCodec<ByteBuf, Float>   FLOAT;
    public static final StreamCodec<ByteBuf, Double>  DOUBLE;
    public static final StreamCodec<ByteBuf, Integer> VAR_INT;
    public static final StreamCodec<ByteBuf, Long>    VAR_LONG;
    public static final StreamCodec<ByteBuf, String>  STRING;

    static {
        BOOL = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Boolean value) { buf.writeBoolean(value); }
            @Override public Boolean decode(ByteBuf buf) { return buf.readBoolean(); }
        };

        BYTE = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Byte value) { buf.writeByte(value); }
            @Override public Byte decode(ByteBuf buf) { return buf.readByte(); }
        };

        SHORT = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Short value) { buf.writeShort(value); }
            @Override public Short decode(ByteBuf buf) { return buf.readShort(); }
        };

        INT = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Integer value) { buf.writeInt(value); }
            @Override public Integer decode(ByteBuf buf) { return buf.readInt(); }
        };

        LONG = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Long value) { buf.writeLong(value); }
            @Override public Long decode(ByteBuf buf) { return buf.readLong(); }
        };

        FLOAT = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Float value) { buf.writeFloat(value); }
            @Override public Float decode(ByteBuf buf) { return buf.readFloat(); }
        };

        DOUBLE = new StreamCodec<>() {
            @Override public void encode(ByteBuf buf, Double value) { buf.writeDouble(value); }
            @Override public Double decode(ByteBuf buf) { return buf.readDouble(); }
        };

        VAR_INT = new StreamCodec<>() {
            @Override
            public void encode(ByteBuf buf, Integer value) {
                while ((value & ~0x7F) != 0) {
                    buf.writeByte((value & 0x7F) | 0x80);
                    value >>>= 7;
                }
                buf.writeByte(value);
            }

            @Override
            public Integer decode(ByteBuf buf) {
                int value = 0, shift = 0;
                byte b;
                do {
                    b = buf.readByte();
                    value |= (b & 0x7F) << shift;
                    shift += 7;
                    if (shift > 35) throw new IllegalStateException("VarInt too large");
                } while ((b & 0x80) != 0);
                return value;
            }
        };

        VAR_LONG = new StreamCodec<>() {
            @Override
            public void encode(ByteBuf buf, Long value) {
                while ((value & ~0x7FL) != 0) {
                    buf.writeByte((int) ((value & 0x7F) | 0x80));
                    value >>>= 7;
                }
                buf.writeByte((int) (long) value);
            }

            @Override
            public Long decode(ByteBuf buf) {
                long value = 0;
                int shift = 0;
                byte b;
                do {
                    b = buf.readByte();
                    value |= (long) (b & 0x7F) << shift;
                    shift += 7;
                    if (shift > 63) throw new IllegalStateException("VarLong too large");
                } while ((b & 0x80) != 0);
                return value;
            }
        };

        STRING = new StreamCodec<>() {
            @Override
            public void encode(ByteBuf buf, String value) {
                byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
                VAR_INT.encode(buf, bytes.length);
                buf.writeBytes(bytes);
            }

            @Override
            public String decode(ByteBuf buf) {
                byte[] bytes = new byte[VAR_INT.decode(buf)];
                buf.readBytes(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        };
    }

    private CommonCodecs() {}
}
