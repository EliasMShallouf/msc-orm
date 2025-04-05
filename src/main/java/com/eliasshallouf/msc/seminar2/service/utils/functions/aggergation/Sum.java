package com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation;

import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;

public class Sum<N extends Number> extends NumericColumn<N> {
    public static <N extends Number> Sum<N> of(NumericColumn<N> col) {
        return new Sum<>(col);
    }

    private Sum(NumericColumn<N> col) {
        super("", "sum(%s)".formatted(col.column()));
    }
}
