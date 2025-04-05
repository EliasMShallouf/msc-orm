package com.eliasshallouf.msc.seminar2.service.utils.functions;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.query.MonoSubQuery;

public class Any<T> implements Function<T> {
    public static <T> ColumnInfo<T> of(MonoSubQuery<?, T> q) {
        return new Any<>(q).result();
    }

    private final MonoSubQuery<?, T> query;

    private Any(MonoSubQuery<?, T> q) {
        this.query = q;
    }

    @Override
    public ColumnInfo<T> result() {
        return new ColumnInfo<>(
            ColumnInfo.NO_TABLE,
            "any " + this.query.column()
        ) {};
    }
}
