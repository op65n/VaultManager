package op65n.tech.vaultmanager.database.adapter;

import org.apache.commons.lang.NotImplementedException;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public interface ITable {

    int priority();

    @Language("MariaDB")
    String getCreateQuery();

    static List<ITable> sort(final ITable... table) {
        List<ITable> tables = new ArrayList<>();
        Arrays.stream(table).forEach(regTable -> {
            if (regTable.getCreateQuery() == null) throw new NotImplementedException("Received a null table creation statement, aborting!");
            tables.add(regTable);
        });
        tables.sort(Comparator.comparing(ITable::priority));
        return tables;
    }

}
