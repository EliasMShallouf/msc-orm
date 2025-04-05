package com.eliasshallouf.msc.seminar2.service.utils.table;

import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.NativeConnectionHelper;
import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.naming.Naming;

public abstract class EntityModel<T> {
    public static <E> EntityModel<E> defineEntity(String table, Class<E> clazz) {
        return new EntityModel<>(clazz) {
            @Override
            public TableColumns<E> columns() {
                return new TableColumns<>(this) { };
            }
        }.setTable(table);
    }

    public static EntityModel<Void> defineEntity(String table) {
        return defineEntity(table, null);
    }

    private Class<T> clazz;
    private String table;
    private String alias;

    public EntityModel(Class<T> clazz) {
        this.clazz = clazz;
        this.table = clazz != null ? NativeConnectionHelper.ClassParsingHelper.getTableName(clazz) : "";
        this.alias = "";
    }

    public <E extends EntityModel<T>> E setTable(String table) {
        this.table = table;
        return (E) this;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public <E extends EntityModel<T>> E setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return (E) this;
    }

    public String getAlias() {
        return alias;
    }

    public <E extends EntityModel<T>> E aliased(String alias) {
        this.alias = alias;
        refreshAlias();
        return (E) this;
    }

    public void refreshAlias() {
        this.columns().getColumns().forEach(columnInfo -> columnInfo.setTable(toString()));
    }

    public String getTable() {
        return table;
    }

    public abstract TableColumns<?> columns();

    @Override
    public String toString() {
        return alias.isEmpty() ? table : alias;
    }

    public String getTableName() {
        return Naming.defaults().doChange(table);
    }

    public String sql() {
        return getTableName() + LogicalStream
                .of(alias)
                .ifTrue(a -> !a.isEmpty())
                .then(a -> " as " + Naming.defaults().doChange(a))
                .otherwise(a -> "")
                .get();
    }

    public EntityManager<T> manager(ConnectionManager manager) {
        return manager.createEntityManager(this);
    }

    public ColumnInfo<String> allColumns() {
        return ColumnInfo.all(this);
    }
}
