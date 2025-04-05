package com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;

import java.util.Objects;

public class Count extends NumericColumn<Long> {
    public static Count all() {
        return new Count();
    }

    public static Count of(ColumnInfo<?> col) {
        if(Objects.equals(col.column(), "*"))
            return all();

        return new Count(col);
    }

    private Count(ColumnInfo<?> col) {
        super("", "count(%s)".formatted(col.column()));
    }

    private Count() { this(ColumnInfo.all(EntityModel.defineEntity(""))); }
}
