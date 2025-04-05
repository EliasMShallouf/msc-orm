package com.eliasshallouf.msc.seminar2.service.utils.functions;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.query.MonoSubQuery;

import java.util.Arrays;
import java.util.stream.Collectors;

public class All<T> implements Function<T> {
    public static <T> ColumnInfo<T> of(MonoSubQuery<?, T> q) {
        return new All<>(q).result();
    }

    private final MonoSubQuery<?, T> query;

    private All(MonoSubQuery<?, T> q) {
        this.query = q;
    }

    @Override
    public ColumnInfo<T> result() {
        return new ColumnInfo<>(
            ColumnInfo.NO_TABLE,
            "all " + this.query.column()
        ) {};
    }
}
