package com.eliasshallouf.msc.seminar2.service.utils.query;

import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.OrderColumn;
import com.eliasshallouf.msc.seminar2.service.utils.paging.Paging;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectQuery<T> {
    private final ConnectionManager manager;
    private final SubQuery<T> query = new SubQuery<>();
    private final List<UnionQuery<?>> unionQueries = new ArrayList<>();

    public SelectQuery(ConnectionManager m) {
        this.manager = m;
    }

    public SelectQuery<T> table(EntityModel<T> table) {
        this.query.table(table);
        return this;
    }

    public SelectQuery<T> join(EntityModel<?> another, Query on) {
        this.query.join(another, on);
        return this;
    }

    public SelectQuery<T> select(ColumnInfo<?> ...cols) {
        this.query.select(cols);
        return this;
    }

    public SelectQuery<T> select(EntityModel<?> entity) {
        this.query.select(entity);
        return this;
    }

    public SelectQuery<T> where(Query q) {
        this.query.where(q);
        return this;
    }

    public SelectQuery<T> groupBy(ColumnInfo<?> ...cols) {
        this.query.groupBy(cols);
        return this;
    }

    public SelectQuery<T> having(Query q) {
        this.query.having(q);
        return this;
    }

    public SelectQuery<T> orderBy(OrderColumn<?> ...cols) {
        this.query.orderBy(cols);
        return this;
    }

    public SelectQuery<T> limit(long limit) {
        this.query.limit(limit);
        return this;
    }

    public SelectQuery<T> skip(long skip) {
        this.query.skip(skip);
        return this;
    }

    public SelectQuery<T> union(SelectQuery<?> other) {
        this.unionQueries.add(new UnionQuery<>(other, UnionQuery.UnionType.DEFAULT));
        return this;
    }

    public SelectQuery<T> unionAll(SelectQuery<?> other) {
        this.unionQueries.add(new UnionQuery<>(other, UnionQuery.UnionType.ALL));
        return this;
    }

    public String queryBuilder() {
        StringBuilder builder = new StringBuilder();

        if(unionQueries.isEmpty())
            builder.append(this.query.query());
        else
            builder
                .append("(")
                .append(this.query.query())
                .append(") ")
                .append(unionQueries.stream().map(
                        unionQuery -> new StringBuilder()
                            .append(
                                unionQuery.getType() == UnionQuery.UnionType.DEFAULT ? "UNION" : "UNION ALL"
                            )
                            .append(" (")
                            .append(unionQuery.getQuery().query.query())
                            .append(")")
                    ).collect(Collectors.joining(" "))
                );

        return builder.toString();
    }

    public <R> List<R> list(Class<R> resultClass) {
        return this.manager.rawQuery(
            queryBuilder(),
            resultClass
        );
    }

    public List<T> list() {
        return list(query.table.getClazz());
    }

    public <R> R find(Class<R> resultClass) {
        long tmp = query.limit;

        this.query.limit(1);

        List<R> list = list(resultClass);

        this.query.limit(tmp);

        return list.size() == 1 ? list.get(0) : null;
    }

    public T find() {
        return find(query.table.getClazz());
    }

    public <R> Paging<R> paging(int pageSize, boolean lazyFetch, Class<R> clazz) {
        return new Paging<>(
            manager,
            clazz,
            queryBuilder(),
            pageSize,
            lazyFetch
        );
    }

    public Paging<T> paging(int pageSize, boolean lazyFetch) {
        return paging(pageSize, lazyFetch, this.query.table.getClazz());
    }
}
