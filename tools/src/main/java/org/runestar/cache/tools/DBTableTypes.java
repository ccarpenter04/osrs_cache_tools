package org.runestar.cache.tools;

import org.runestar.cache.content.Type;
import org.runestar.cache.content.config.ConfigType;
import org.runestar.cache.content.config.DBTableType;
import org.runestar.cache.format.MemCache;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DBTableTypes {

    private DBTableTypes() {}

    public static SortedMap<Integer, String> get(MemCache cache) {
        var dbtableTypes = new TreeMap<Integer, String>();
        for (var file : cache.archive(ConfigType.ARCHIVE).group(DBTableType.GROUP).files()) {
            var dbtable = new DBTableType();
            dbtable.decode(file.data());
            for (int column = 0; column < dbtable.types.length; column++) {
                Type[] columnTypes = dbtable.types[column];
                if (columnTypes == null) {
                    continue;
                }

                String joined = Stream.of(columnTypes)
                        .map(Type::getLiteral)
                        .collect(Collectors.joining(","));
                dbtableTypes.put(file.id() << 12 | column << 4, joined);
            }
        }
        return dbtableTypes;
    }

}
