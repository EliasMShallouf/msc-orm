package com.eliasshallouf.msc.seminar2.service.utils.functions;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Concat implements Function<String> {
    @SafeVarargs
    public static ColumnInfo<String> of(ColumnInfo<String> ...cols) {
        return new Concat(cols).result();
    }

    private ColumnInfo<String>[] cols;

    private Concat(ColumnInfo<String> ...cols) {
        this.cols = cols;
    }

    @Override
    public ColumnInfo<String> result() {
        return new ColumnInfo<>(
            ColumnInfo.NO_TABLE,
            "concat(%s)".formatted(
                Arrays
                    .stream(cols)
                    .map(ColumnInfo::column)
                    .collect(Collectors.joining(", "))
            )
        ) {};
    }
}
