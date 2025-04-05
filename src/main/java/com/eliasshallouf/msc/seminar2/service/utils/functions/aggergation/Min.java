package com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;

public class Min<E> extends ColumnInfo<E> {
    public static <E> Min<E> of(ColumnInfo<E> col) {
        return new Min<>(col);
    }

    private Min(ColumnInfo<E> col) {
        super("", "min(%s)".formatted(col.column()));
    }
}
