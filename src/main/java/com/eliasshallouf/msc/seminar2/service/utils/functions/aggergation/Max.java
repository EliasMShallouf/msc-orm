package com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;

public class Max<E> extends ColumnInfo<E> {
    public static <E> Max<E> of(ColumnInfo<E> col) {
        return new Max<>(col);
    }

    private Max(ColumnInfo<E> col) {
        super("", "max(%s)".formatted(col.column()));
    }
}
