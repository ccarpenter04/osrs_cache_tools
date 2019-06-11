package org.runestar.cache.content;

import java.nio.ByteBuffer;

import static org.runestar.cache.content.Buf.*;

public final class EnumType extends ConfigType {

    public byte inputtype = 0;

    public byte outputtype = 0;

    public int outputcount = 0;

    public int[] keys = null;

    public int intdefault = 0;

    public String stringdefault = null;

    public int[] intvals = null;

    public String[] stringvals = null;

    @Override protected void decode0(ByteBuffer buffer) {
        while (true) {
            int opcode = getUnsignedByte(buffer);
            switch (opcode) {
                case 0:
                    return;
                case 1:
                    inputtype = buffer.get();
                    break;
                case 2:
                    outputtype = buffer.get();
                    break;
                case 3:
                    stringdefault = Buf.getString(buffer);
                    break;
                case 4:
                    intdefault = buffer.getInt();
                    break;
                case 5:
                    outputcount = getUnsignedShort(buffer);
                    keys = new int[outputcount];
                    stringvals = new String[outputcount];
                    for (int i = 0; i < outputcount; i++) {
                        keys[i] = buffer.getInt();
                        stringvals[i] = Buf.getString(buffer);
                    }
                    break;
                case 6:
                    outputcount = getUnsignedShort(buffer);
                    keys = new int[outputcount];
                    intvals = new int[outputcount];
                    for (int i = 0; i < outputcount; i++) {
                        keys[i] = buffer.getInt();
                        intvals[i] = buffer.getInt();
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(Integer.toString(opcode));
            }
        }
    }

    public int getInt(int key) {
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == key) return intvals[i];
        }
        return intdefault;
    }

    public String getString(int key) {
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == key) return stringvals[i];
        }
        return stringdefault;
    }
}
