package com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation;

import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;

public class Average<N extends Number> extends NumericColumn<N> {
    public static <N extends Number> Average<N> of(NumericColumn<N> col) {
        return new Average<>(col);
    }

    private Average(NumericColumn<N> col) {
        super("", "avg(%s)".formatted(col.column()));
    }
}
