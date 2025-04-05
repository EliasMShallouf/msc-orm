package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

public class BlobColumn extends ColumnInfo<byte[]> {
    public BlobColumn(String table, String column) {
        super(table, column);
    }

    public BlobColumn(Class<?> table, String column) {
        super(table, column);
    }

    public BlobColumn(TableColumns<?> table, String column) {
        super(table, column);
    }
}
