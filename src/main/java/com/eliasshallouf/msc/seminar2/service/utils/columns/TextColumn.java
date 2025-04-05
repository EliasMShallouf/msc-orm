package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.query.Query;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

public class TextColumn extends ColumnInfo<String> {
    public TextColumn(String table, String column) {
        super(table, column);
    }

    public TextColumn(Class<?> table, String column) {
        super(table, column);
    }

    public TextColumn(TableColumns<?> table, String column) {
        super(table, column);
    }

    public <CI extends ColumnInfo<String>> Query like(CI c) {
        return () -> column() + " like " + c.column();
    }

    public Query like(String s) {
        return () -> column() + " like '" + s + "'";
    }

    public Query startsWith(String prefix) {
        return like(prefix + "%");
    }

    public Query endsWith(String suffix) {
        return like("%" + suffix);
    }
}
