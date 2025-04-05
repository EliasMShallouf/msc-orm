package com.eliasshallouf.msc.seminar2.service.utils.table;

import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.NativeConnectionHelper;
import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.OrderColumn;
import com.eliasshallouf.msc.seminar2.service.utils.columns.SetColumn;
import com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation.Count;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.join.Join;
import com.eliasshallouf.msc.seminar2.service.utils.paging.Paging;
import com.eliasshallouf.msc.seminar2.service.utils.query.Query;
import com.eliasshallouf.msc.seminar2.service.utils.query.SelectQuery;
import java.util.ArrayList;
import java.util.List;

public class EntityManager<E> {
    private final ConnectionManager connectionManager;
    private final EntityModel<E> table;
    
    public EntityManager(ConnectionManager connectionManager, EntityModel<E> table) {
        this.connectionManager = connectionManager;
        this.table = table;
    }

    public List<E> getAll() {
        try {
            return NativeConnectionHelper.getAll(this.connectionManager.getStatement(), this.table);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Paging<E> pages(int pageSize, boolean lazyFetch) {
        return pages(query().select(table.allColumns()).queryBuilder(), pageSize, lazyFetch);
    }

    public Paging<E> pages(String qry, int pageSize, boolean lazyFetch) {
        return new Paging<>(
            connectionManager,
            this.table.getClazz(),
            qry,
            pageSize,
            lazyFetch
        );
    }

    public long count() {
        return LogicalStream
            .of(connectionManager.tableCountRowsQuery(this.table.getTable()))
            .ifTrue(q -> q != null && !q.isEmpty())
            .thenReturn(q -> connectionManager.findOne(q, Long.class))
            .otherwise(q -> query().select(Count.all()).find(Long.class))
            .get();
    }

    public <Id> boolean contains(Id id) {
        return findById(id) != null;
    }

    public <Id> E findById(Id id) {
        try {
            return NativeConnectionHelper.findById(this.connectionManager.getConnection(), this.table, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public final void save(E... items) {
        for(E e : items) {
            try {
                NativeConnectionHelper.insert(this.connectionManager.getConnection(), e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public int deleteAll() {
        try {
            return NativeConnectionHelper.deleteAll(this.connectionManager.getStatement(), this.table);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <Id> int deleteById(Id id) {
        try {
            return NativeConnectionHelper.deleteById(this.connectionManager.getConnection(), this.table, id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public int delete(Query where) {
        try {
            return NativeConnectionHelper.delete(this.connectionManager.getConnection(), this.table, () -> where);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> List<T> rawQuery(String qry, Class<T> clazz) {
        try {
            return NativeConnectionHelper.fetch(this.connectionManager.getStatement().executeQuery(qry), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<E> list(String qry) {
        try {
            return NativeConnectionHelper.fetch(this.connectionManager.getStatement().executeQuery(qry), this.table.getClazz());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SelectQuery<E> query() {
        return new SelectQuery<E>(connectionManager).table(this.table);
    }

    public List<E> select(Query where) {
        try {
            return NativeConnectionHelper.select(this.connectionManager.getStatement(), this.table, () -> where);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<E> select(ColumnInfo<?>[] cols, Query where) {
        return select(cols, where, this.table.getClazz());
    }

    public <R> List<R> select(ColumnInfo<?>[] cols, Query where, Class<R> resultClazz) {
        try {
            return NativeConnectionHelper.select(this.connectionManager.getStatement(), this.table, cols, () -> where, resultClazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <R> List<R> select(
        Join<?, ?> join, //joining
        ColumnInfo<?>[] cols, //the target columns to select
        Query where, //where clause,
        ColumnInfo<?>[] groupCols,
        Query having,
        OrderColumn<?>[] orderByCols,
        long limit,
        long skip,
        Class<R> resultClazz
    ) {
        try {
            return NativeConnectionHelper.select(this.connectionManager.getStatement(), this.table, join, cols, () -> where, groupCols, () -> having, orderByCols, limit, skip, resultClazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E findOne(String qry) {
        try {
            return NativeConnectionHelper.fetchOne(this.connectionManager.getStatement().executeQuery(qry), this.table.getClazz());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <R> R findOne(
        Join<?, ?> join, //joining
        ColumnInfo<?>[] cols, //the target columns to select
        Query where, //where clause,
        ColumnInfo<?>[] groupCols,
        Query having,
        OrderColumn<?>[] orderByCols,
        Class<R> resultClazz
    ) {
        try {
            List<R> res = NativeConnectionHelper.select(this.connectionManager.getStatement(), this.table, join, cols, () -> where, groupCols, () -> having, orderByCols, 1, 0, resultClazz);

            if(res.isEmpty())
                return null;

            return res.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Query where, SetColumn<?>...cols) {
        try {
            return NativeConnectionHelper.update(this.connectionManager.getConnection(), this.table, cols, () -> where);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int update(E e) {
        try {
            return NativeConnectionHelper.update(this.connectionManager.getConnection(), e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public int saveOrUpdate(E e) {
        if(update(e) == 0) {
            save(e);
            return 0;
        }

        return 1;
    }
}
