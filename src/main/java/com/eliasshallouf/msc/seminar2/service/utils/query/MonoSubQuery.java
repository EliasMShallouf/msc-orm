package com.eliasshallouf.msc.seminar2.service.utils.query;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.OrderColumn;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;

public class MonoSubQuery<T, C> extends ColumnInfo<C> {
    private final SubQuery<T> query = new SubQuery<>();
    
    public MonoSubQuery(EntityModel<T> table, ColumnInfo<C> target) {
        super("", "");

        this.query.limit(1);
        this.query.table(table);
        this.query.select(target);
    }

    public MonoSubQuery<T, C> join(EntityModel<?> another, Query on) {
        this.query.join(another, on);
        return this;
    }

    public MonoSubQuery<T, C> where(Query q) {
        this.query.where(q);
        return this;
    }

    public MonoSubQuery<T, C> groupBy(ColumnInfo<?> ...cols) {
        this.query.groupBy(cols);
        return this;
    }

    public MonoSubQuery<T, C> having(Query q) {
        this.query.having(q);
        return this;
    }

    public MonoSubQuery<T, C> orderBy(OrderColumn<?>...cols) {
        this.query.orderBy(cols);
        return this;
    }

    public MonoSubQuery<T, C> skip(long skip) {
        this.query.skip(skip);
        return this;
    }

    @Override
    public String column() {
        return "(" + query.query() + ")";
    }

    public Query exists() {
        return () -> "exists " + column();
    }

    public Query notExists() {
        return () -> "not exists " + column();
    }
}
