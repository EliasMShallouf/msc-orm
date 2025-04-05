package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

import java.util.Date;

public class BooleanColumn extends ColumnInfo<Date> {
    public BooleanColumn(String table, String column) {
        super(table, column);
    }

    public BooleanColumn(Class<?> table, String column) {
        super(table, column);
    }

    public BooleanColumn(TableColumns<?> table, String column) {
        super(table, column);
    }
}
