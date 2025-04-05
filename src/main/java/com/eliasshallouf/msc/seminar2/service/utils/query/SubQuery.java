package com.eliasshallouf.msc.seminar2.service.utils.query;

import com.eliasshallouf.msc.seminar2.service.utils.NativeConnectionHelper;
import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.OrderColumn;
import com.eliasshallouf.msc.seminar2.service.utils.join.Join;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.eliasshallouf.msc.seminar2.service.utils.helpers.ListHelper.arrayOf;

public class SubQuery<T> extends EntityModel<T> {
    protected EntityModel<T> table;
    protected Join<?, ?> join;
    protected List<ColumnInfo> selectColumns;
    protected Query where;
    protected List<ColumnInfo> groupColumns;
    protected Query having;
    protected List<OrderColumn> orderColumns;
    protected long limit = -1;
    protected long skip = -1;
    private String sql = "";

    public SubQuery() {
        super(null);
    }

    public SubQuery<T> table(EntityModel<T> table) {
        this.table = table;
        setClazz(table.getClazz());
        return rebuildQuery();
    }

    public SubQuery<T> join(EntityModel<?> another, Query on) {
        if(join == null) {
            join = new Join<>(table, another, (l, r) -> on);
        } else {
            join = new Join<>(join, another, (l, r) -> on);
        }

        return rebuildQuery();
    }

    public SubQuery<T> select(ColumnInfo<?> ...cols) {
        this.selectColumns = Arrays.asList(cols);
        return rebuildQuery();
    }

    public SubQuery<T> select(EntityModel<?> entity) {
        this.selectColumns = entity.columns().getColumns();
        return rebuildQuery();
    }

    public SubQuery<T> where(Query q) {
        this.where = q;
        return rebuildQuery();
    }

    public SubQuery<T> groupBy(ColumnInfo<?> ...cols) {
        this.groupColumns = Arrays.asList(cols);
        return rebuildQuery();
    }

    public SubQuery<T> having(Query q) {
        this.having = q;
        return rebuildQuery();
    }

    public SubQuery<T> orderBy(OrderColumn<?> ...cols) {
        this.orderColumns = Arrays.asList(cols);
        return rebuildQuery();
    }

    public SubQuery<T> limit(long limit) {
        this.limit = limit;
        return rebuildQuery();
    }

    public SubQuery<T> skip(long skip) {
        this.skip = skip;
        return rebuildQuery();
    }

    @Override
    public TableColumns<T> columns() {
        return new TableColumns<>(this) {
            @Override
            public <CI extends ColumnInfo<?>> void addColumn(CI c) { }

            @Override
            public List<ColumnInfo> getColumns() {
                return selectColumns;
            }
        };
    }

    public String query() {
        return sql;
    }
    
    private SubQuery<T> rebuildQuery() {
        this.sql = NativeConnectionHelper.buildSelectQuery(
            table,
            join,
            arrayOf(selectColumns, ColumnInfo.class),
            () -> where,
            arrayOf(groupColumns, ColumnInfo.class),
            () -> having,
            arrayOf(orderColumns, OrderColumn.class),
            limit,
            skip
        );
        
        return this;
    }

    @Override
    public String getTableName() {
        return "(" + query() + ")";
    }
}
