package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.NativeConnectionHelper;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.Mapper;
import com.eliasshallouf.msc.seminar2.service.utils.naming.Naming;
import com.eliasshallouf.msc.seminar2.service.utils.query.Query;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

import java.time.temporal.Temporal;

public abstract class ColumnInfo<E> {
    public static <E> ColumnInfo<E> defineColumn(EntityModel<?> model, String column) {
        return new ColumnInfo<>(model.columns(), column) { };
    }

    public static <E> ColumnInfo<E> defineColumn(String table, String column) {
        return new ColumnInfo<>(table, column) { };
    }

    public static final String NO_TABLE = ""; //use to create functions, aggregation columns and operators like (+, - ...)

    private final static Naming namingActions = Naming.defaults();

    protected String table;
    protected final String column;
    protected String alias;

    protected boolean distinct = false;

    public ColumnInfo(String table, String column) {
        this.table = table;
        this.column = column;
        this.alias = "";
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ColumnInfo(Class<?> table, String column) {
        this(NativeConnectionHelper.ClassParsingHelper.getTableName(table), column);
    }

    public ColumnInfo(TableColumns<?> table, String column) {
        this(table.getModel().toString(), column);
        table.addColumn(this);
    }

    public Query equal(ColumnInfo<E> e) {
        return () -> column() + " = " + e.column();
    }

    public Query notEqual(ColumnInfo<E> e) {
        return () -> column() + " != " + e.column();
    }

    public Query isNull() {
        return () -> column() + " is null";
    }

    public Query isNotNull() {
        return () -> column() + " is not null";
    }

    public <CI extends ColumnInfo<E>> Query in(CI ci) {
        return () -> column() + " in (" + ci.column() +")";
    }

    public <CI extends ColumnInfo<E>> Query notIn(CI ci) {
        return () -> column() + " not in (" + ci.column() +")";
    }

    public <CI extends ColumnInfo<E>> CI as(String alias) {
        this.alias = alias;
        return (CI) this;
    }

    public <CI extends ColumnInfo<E>> CI distinct() {
        this.distinct = true;
        return (CI) this;
    }

    public String column() {
        return LogicalStream
            .of(table)
            .ifTrue(t -> !t.isEmpty())
            .then(t -> namingActions.doChange(t) + "." + namingActions.doChange(alias.isEmpty() ? column : alias))
            .otherwise(t -> column)
            .get();
    }

    public SetColumn<E> setTo(E value) {
        return new SetColumn<>() {
            @Override
            public ColumnInfo<E> column() {
                return ColumnInfo.this;
            }

            @Override
            public E value() {
                return value;
            }
        };
    }

    private OrderColumn<E> order(OrderColumn.OrderBy order) {
        return new OrderColumn<E>() {
            @Override
            public ColumnInfo<E> column() {
                return ColumnInfo.this;
            }

            @Override
            public OrderBy type() {
                return order;
            }
        };
    }

    public OrderColumn<E> ascendingOrder() {
        return order(OrderColumn.OrderBy.ASC);
    }

    public OrderColumn<E> descendingOrder() {
        return order(OrderColumn.OrderBy.DESC);
    }

    public String groupNaming() {
        return LogicalStream
            .of(alias)
            .ifTrue(t -> !t.isEmpty())
            .thenReturn(a -> namingActions.doChange(alias))
            .otherwise(a ->
                LogicalStream
                .of(table)
                .ifTrue(t -> !t.isEmpty())
                .then(t -> namingActions.doChange(t) + "." + namingActions.doChange(column))
                .otherwise(t -> column)
                .get()
            ).get();
    }

    @Override
    public String toString() {
        return
            (distinct ? "DISTINCT " : "") +
            LogicalStream
                .of(table)
                .ifTrue(t -> !t.isEmpty())
                .then(t -> namingActions.doChange(t) + "." + namingActions.doChange(column))
                .otherwise(t -> column)
                .get() +
            LogicalStream
                .of(alias)
                .ifTrue(a -> !a.isEmpty())
                .then(a -> " as " + namingActions.doChange(a))
                .otherwise(a -> "")
                .get();
    }

    public static <E> Value<E> valueOf(E e) {
        return new Value<>(e);
    }

    public <N extends Number> NumericColumn<N> asNumber() {
        return new NumericColumn<>("", column());
    }

    public <D extends Temporal> DateColumn<D> asDate() {
        return new DateColumn<>("", column());
    }

    public BlobColumn asBlob() {
        return new BlobColumn("", column());
    }

    public TextColumn asText() {
        return new TextColumn("", column());
    }

    public static ColumnInfo<String> all(Class<?> table) {
        return new ColumnInfo<>(table, "*") {};
    }

    public static ColumnInfo<String> all(Object table) {
        return new ColumnInfo<>(table.getClass(), "*") {};
    }

    public static ColumnInfo<String> all(EntityModel<?> entity) {
        return new ColumnInfo<>(entity.toString(), "*") {};
    }
}
