package org.runestar.cache.content.config;

import org.runestar.cache.content.Type;
import org.runestar.cache.content.io.Input;

public final class DBTableType extends ConfigType {

    public static final int GROUP = 39;

    public Type[][] types;

    public Object[][] defaultValues;

    @Override protected void decode0(Input in) {
        while (true) {
            int code = in.g1();

            switch (code) {
                case 0:
                    return;
                case 1:
                    int numColumns = in.g1();
                    if (types == null) {
                        types = new Type[numColumns][];
                    }
                    for (int setting = in.g1(); setting != 255; setting = in.g1()) {
                        int column = setting & 0x7F;
                        boolean hasDefault = (setting & 0x80) != 0;
                        Type[] columnTypes = new Type[in.g1()];
                        for (int i = 0; i < columnTypes.length; i++) {
                            columnTypes[i] = Type.ofAuto(in.gSmart1or2());
                        }
                        types[column] = columnTypes;

                        if (hasDefault) {
                            if (defaultValues == null) {
                                defaultValues = new Object[types.length][];
                            }

                            defaultValues[column] = decodeValues(in, columnTypes);
                        }
                    }
                    break;
                default:
                    unrecognisedCode(code);
            }
        }
    }

    public static Object[] decodeValues(Input in, Type[] types) {
        int fieldCount = in.gSmart1or2();
        Object[] values = new Object[fieldCount * types.length];

        for (int fieldId = 0; fieldId < fieldCount; fieldId++) {
            for (int typeId = 0; typeId < types.length; typeId++) {
                Type type = types[typeId];
                int index = fieldId * types.length + typeId;

                if (type == Type.STRING) {
                    values[index] = in.gjstr();
                } else {
                    values[index] = in.g4s();
                }
            }
        }

        return values;
    }

}
