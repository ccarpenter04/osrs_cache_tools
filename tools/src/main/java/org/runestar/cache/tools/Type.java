package org.runestar.cache.tools;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Type {
    AREA('R'),
    BOOLEAN('1'),
    CATEGORY('y'),
    CHAR('z'),
    COMPONENT('I'),
    COORD('c'),
    ENUM('g'),
    FONTMETRICS('f'),
    GRAPHIC('d'),
    INT('i'),
    INTERFACE('a'),
    INV('v'),
    LOC('l'),
    LOCSHAPE('H'),
    MAPAREA('`'),
    MAPELEMENT('µ'),
    MODEL('m'),
    NAMEDOBJ('O'),
    NEWVAR('-'),
    NPC('n'),
    NPC_UID('u'),
    OBJ('o'),
    OVERLAYINTERFACE('L'),
    PARAM,
    PLAYER_UID('p'),
    SEQ('A'),
    SPOTANIM('t'),
    STAT('S'),
    STRING('s'),
    STRUCT('J'),
    SYNTH('P'),
    TOPLEVELINTERFACE('F'),
    TYPE,
    ;

    private static final Map<Byte, Type> VALUES = Arrays.stream(values())
            .filter(type -> type.getDesc() != 0)
            .collect(Collectors.toMap(Type::getDesc, Function.identity()));

    private final byte desc;
    private final String literal;

    Type() {
        this('\0');
    }

    Type(char desc) {
        this.desc = toCp1252Byte(desc);
        literal = name().toLowerCase();
    }

    public byte getDesc() {
        return desc;
    }

    public String getLiteral() {
        return literal;
    }

    public static Type ofAuto(byte desc) {
        if (desc == 0) return INT;
        Type value = VALUES.get(desc);
        if (value == null) {
            throw new IllegalStateException();
        }
        return value;
    }

    private static byte toCp1252Byte(char ch) {
        final Charset CP1252 = Charset.forName("windows-1252");
        CharBuffer buffer = CharBuffer.allocate(1).append(ch).flip();
        ByteBuffer encoded = CP1252.encode(buffer);
        return encoded.get();
    }
}