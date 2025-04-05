package com.eliasshallouf.msc.seminar2.service.utils;

import com.eliasshallouf.msc.seminar2.service.utils.dialects.Dialect;
import com.eliasshallouf.msc.seminar2.service.utils.query.SelectQuery;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class ConnectionManager {
    private final Connection connection;
    private final Statement statement;
    private final Dialect dialect = Dialect.defaultDialect();

    public ConnectionManager(
        String driver,
        String url,
        String user,
        String password
    ) {
        try {
            this.connection = NativeConnectionHelper.createConnection(driver, url, user, password);
            this.statement = this.connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <E> List<E> rawQuery(String qry, Class<E> clazz) {
        try {
            return NativeConnectionHelper.fetch(statement.executeQuery(qry), clazz);
        } catch (Exception e) {
            throw new RuntimeException(new Exception("Error in execute raw query \"%s\"".formatted(qry) + ",\n" + e.getMessage()));
        }
    }

    public <E> E findOne(String qry, Class<E> clazz) {
        try {
            return NativeConnectionHelper.fetchOne(statement.executeQuery(qry), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <E> SelectQuery<E> query() {
        return new SelectQuery<>(this);
    }

    public <E> EntityManager<E> createEntityManager(EntityModel<E> model) {
        return new EntityManager<>(this, model);
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public String tableCountRowsQuery(String table) {
        return dialect.tableCountRowsQuery(table);
    }
}
