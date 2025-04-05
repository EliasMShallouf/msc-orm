package com.eliasshallouf.msc.seminar2.service.utils;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.OrderColumn;
import com.eliasshallouf.msc.seminar2.service.utils.columns.SetColumn;
import com.eliasshallouf.msc.seminar2.service.utils.exceptions.PrimaryKeyNotFoundException;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.ClassHelper;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.join.Join;
import com.eliasshallouf.msc.seminar2.service.utils.query.QueryBuilder;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NativeConnectionHelper {
    public static class ClassParsingHelper {
        public static String getTableName(Class<?> clazz) {
            if(clazz == null)
                return "";

            String table = clazz.getSimpleName();

            if (clazz.isAnnotationPresent(Entity.class)) {
                String entityName = clazz.getAnnotation(Entity.class).name();
                if (!entityName.isEmpty())
                    table = entityName;
            }
            
            return table;
        }

        public static String getEntityPrimaryKeyName(Class<?> clazz) {
            String idField = "";
            
            for(Field f : clazz.getDeclaredFields()) {
                if(f.isAnnotationPresent(jakarta.persistence.Id.class)) {
                    idField = f.getName();
                    break;
                }
            }
            
            if(idField.isEmpty())
                throw new PrimaryKeyNotFoundException(clazz);
            
            return idField;
        }

        public static <E, T> T getEntityPrimaryKeyValue(E e) throws IllegalAccessException {
            Class<E> clazz = (Class<E>) e.getClass();

            for(Field f : clazz.getDeclaredFields()) {
                if(f.isAnnotationPresent(jakarta.persistence.Id.class)) {
                    f.setAccessible(true);
                    return (T) f.get(e);
                }
            }

            throw new PrimaryKeyNotFoundException(clazz);
        }
    }
    
    public static Connection createConnection(
        String driver,
        String url,
        String user,
        String password
    ) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(
            url,
            user,
            password
        );
    }

    private static boolean isResultSetHaveColumn(ResultSet resultSet, String column) {
        try {
            return resultSet.findColumn(column) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private static Object value(String fieldName, Class<?> clazz, ResultSet resultSet) throws SQLException {
        if(!isResultSetHaveColumn(resultSet, fieldName))
            return null;

        if (clazz.equals(Integer.class)) {
            return resultSet.getInt(fieldName);
        } else if(clazz.equals(Double.class)) {
            return resultSet.getDouble(fieldName);
        } else if(clazz.equals(Long.class)) {
            return resultSet.getLong(fieldName);
        } else if(clazz.equals(Boolean.class)) {
            return resultSet.getBoolean(fieldName);
        } else if(clazz.equals(Date.class)) {
            return resultSet.getDate(fieldName);
        } else if(clazz.equals(LocalDate.class)) {
            return resultSet.getDate(fieldName).toLocalDate();
        } else if(clazz.equals(LocalDateTime.class)) {
            return LocalDateTime.from(resultSet.getDate(fieldName).toInstant());
        } else if(clazz.equals(LocalTime.class)) {
            return LocalTime.from(resultSet.getDate(fieldName).toInstant());
        } else if(clazz.equals(Float.class)) {
            return resultSet.getFloat(fieldName);
        } else if(clazz.equals(String.class)) {
            return resultSet.getString(fieldName);
        } else if (clazz.equals(Byte.class)) {
            return resultSet.getByte(fieldName);
        } else if (clazz.equals(byte[].class)) {
            return resultSet.getBytes(fieldName);
        }

        return resultSet.getObject(fieldName);
    }

    private static byte[] getBlob(InputStream is) {
        if(is == null) return null;

        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes, 0, bytes.length);
            return bytes;
        } catch(Exception e) {
            return null;
        }
    }

    private static Object blob(String fieldName, Class<?> clazz, ResultSet resultSet) throws SQLException {
        if(!isResultSetHaveColumn(resultSet, fieldName))
            return null;

        byte[] arr = getBlob(resultSet.getBinaryStream(fieldName));

        if(arr == null)
            return null;

        if(clazz.equals(String.class))
            return new String(arr);

        return arr;
    }

    private static <E> E createFromResultSet(ResultSet resultSet, Class<E> clazz)
            throws IllegalAccessException, SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        if(ClassHelper.isPrimitive(clazz))
            return (E) value(metaData.getColumnName(1), clazz, resultSet);

        if(clazz.equals(Object[].class)) {
            Object[] res = new Object[resultSet.getMetaData().getColumnCount()];

            for(int i = 0 ; i < res.length ; i++)
                res[i] = value(metaData.getColumnName(i + 1), Object.class, resultSet);

            return (E) res;
        }

        E e = ClassHelper.createObject(clazz);

        for(Field f : clazz.getDeclaredFields()) {
            if(f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Id.class)) {
                String fieldName = f.getName();
                if(f.isAnnotationPresent(Column.class)) {
                    String alias = f.getAnnotation(Column.class).name();
                    if(!alias.isEmpty())
                        fieldName = alias;
                }

                f.setAccessible(true);
                f.set(e, value(fieldName, f.getType(), resultSet));
            } else if(f.isAnnotationPresent(Lob.class)) {
                f.setAccessible(true);
                f.set(e, blob(f.getName(), f.getType(), resultSet));
            }
        }

        return e;
    }

    public static <E> E fetchOne(ResultSet resultSet, Class<E> clazz)
            throws IllegalAccessException, SQLException {
        if(resultSet.next()) {
            return createFromResultSet(resultSet, clazz);
        }
        return null;
    }

    public static <E> List<E> fetch(ResultSet resultSet, Class<E> clazz)
            throws IllegalAccessException, SQLException {
        List<E> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(createFromResultSet(resultSet, clazz));
        }

        return list;
    }

    public static <E> int insert(Connection connection, E e)
            throws IllegalAccessException, SQLException {
        Class<?> clazz = e.getClass();

        String table = ClassParsingHelper.getTableName(clazz);

        List<String> cols = new ArrayList<>();
        List values = new ArrayList();
        List<Class<?>> classes = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                f.setAccessible(true);
                String fieldName = f.getName();
                Object val = f.get(e);

                if(val != null) {
                    cols.add(fieldName);
                    values.add(val);
                    classes.add(f.getType());
                }
            } else if (f.isAnnotationPresent(Column.class)) {
                String fieldName = f.getName();
                if (f.isAnnotationPresent(Column.class)) {
                    String alias = f.getAnnotation(Column.class).name();
                    if (!alias.isEmpty())
                        fieldName = alias;
                }

                f.setAccessible(true);
                Object val = f.get(e);
                cols.add(fieldName);
                values.add(val);
                classes.add(f.getType());
            } else if (f.isAnnotationPresent(Lob.class)) {
                String fieldName = f.getName();
                f.setAccessible(true);
                Object val = f.get(e);

                cols.add(fieldName);
                values.add(val);
                classes.add(f.getType());
            }
        }

        String qry = "INSERT INTO %s(%s) VALUES(%s);".formatted(
            table,
            String.join(",", cols),
            IntStream.range(0, cols.size()).mapToObj(i -> "?").collect(Collectors.joining(","))
        );

        PreparedStatement preparedStatement = connection.prepareStatement(qry);
        for(int i=0;i<cols.size();i++) {
            preparedStatement.setObject(
                i + 1,
                values.get(i)
            );
        }

        return preparedStatement.executeUpdate();
    }

    public static <E, Id> int updateById(Connection connection, E e, Id id)
            throws IllegalAccessException, SQLException {
        Class<?> clazz = e.getClass();
        String table = ClassParsingHelper.getTableName(clazz);
        String idField = ClassParsingHelper.getEntityPrimaryKeyName(clazz);

        List<String> cols = new ArrayList<>();
        List values = new ArrayList();
        List<Class<?>> classes = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(jakarta.persistence.Id.class)) {
                /* ignore the update of the id column value */
            } else if (f.isAnnotationPresent(Column.class)) {
                String fieldName = f.getName();
                if (f.isAnnotationPresent(Column.class)) {
                    String alias = f.getAnnotation(Column.class).name();
                    if (!alias.isEmpty())
                        fieldName = alias;
                }

                f.setAccessible(true);
                Object val = f.get(e);
                cols.add(fieldName);
                values.add(val);
                classes.add(f.getType());
            } else if (f.isAnnotationPresent(Lob.class)) {
                String fieldName = f.getName();
                f.setAccessible(true);
                Object val = f.get(e);

                cols.add(fieldName);
                values.add(val);
                classes.add(f.getType());
            }
        }

        String qry = "UPDATE %s SET %s WHERE %s;".formatted(
            table,
            cols
                .stream()
                .map(col -> col + " = ?")
                .collect(Collectors.joining(", ")),
            idField + " = " + id
        );

        PreparedStatement preparedStatement = connection.prepareStatement(qry);
        for(int i=0;i<cols.size();i++) {
            preparedStatement.setObject(
                i + 1,
                values.get(i)
            );
        }

        return preparedStatement.executeUpdate();
    }

    public static <E, Id> int save(Connection connection, EntityModel<E> entity, E e)
            throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Id id = ClassParsingHelper.getEntityPrimaryKeyValue(e);

        if(findById(connection, entity, id) == null)
            return updateById(connection, e, id);
        else
            return insert(connection, e);
    }

    public static <E, Id> int update(Connection connection, E e)
            throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Id id = ClassParsingHelper.getEntityPrimaryKeyValue(e);
        return updateById(connection, e, id);
    }

    public static <E> int update(
        Connection connection,
        EntityModel<E> entity,
        SetColumn<?>[] cols, //the target columns to set
        QueryBuilder<E> where //where clause
    ) throws SQLException {
        String table = entity.getTable();

        String qry = "UPDATE %s SET %s %s;".formatted(
            table,
            Arrays
                .stream(cols)
                .map(sc -> sc.column().column() + " = ?")
                .collect(Collectors.joining(", ")),
            LogicalStream
                .of(where)
                .ifTrue(Objects::nonNull)
                .thenReturn(w -> " where " + where.query().sql())
                .otherwise(w -> "")
                .get()
        );

        PreparedStatement preparedStatement = connection.prepareStatement(qry);
        for(int i = 0; i< cols.length; i++) {
            preparedStatement.setObject(
                i + 1,
                cols[i].value()
            );
        }
        return preparedStatement.executeUpdate();
    }

    public static <E> List<E> getAll(Statement statement, EntityModel<E> entity)
            throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return fetch(statement.executeQuery("select * from " + entity.getTable()), entity.getClazz());
    }

    public static <E, Id> E findById(Connection connection, EntityModel<E> entity, Id id)
            throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        String idField = ClassParsingHelper.getEntityPrimaryKeyName(entity.getClazz());

        String qry = "select * from " + entity.getTable() + " where " + idField + " = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(qry);
        preparedStatement.setObject(1, id);

        return fetchOne(preparedStatement.executeQuery(), entity.getClazz());
    }

    public static <E> int deleteAll(Statement statement, EntityModel<E> entity)
            throws SQLException {
        return statement.executeUpdate("delete from " + entity.getTable());
    }

    public static <E, Id> int deleteById(Connection connection, EntityModel<E> entity, Id id)
            throws SQLException {
        String idField = ClassParsingHelper.getEntityPrimaryKeyName(entity.getClazz());

        PreparedStatement preparedStatement = connection.prepareStatement("delete from " + entity.getTable() + " where " + idField + " = ?");
        preparedStatement.setObject(1, id);
        return preparedStatement.executeUpdate();
    }

    public static <E> int delete(Connection connection, EntityModel<E> entity, QueryBuilder<E> where)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "delete from " + entity.getTable() + LogicalStream
                    .of(where)
                    .ifTrue(Objects::nonNull)
                    .thenReturn(w -> " where " + where.query().sql())
                    .otherwise(w -> "")
                    .get()
        );
        return preparedStatement.executeUpdate();
    }

    public static <E, R> List<R> select(
        Statement statement,
        EntityModel<E> entity, //target table
        Join<?, ?> join, //joining
        ColumnInfo<?>[] cols, //the target columns to select
        QueryBuilder<E> where, //where clause,
        ColumnInfo<?>[] groupCols,
        QueryBuilder<E> having,
        OrderColumn<?>[] orderByCols,
        long limit,
        long skip,
        Class<R> resultClazz
    ) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return fetch(
            statement.executeQuery(
                buildSelectQuery(
                    entity, join, cols, where, groupCols, having, orderByCols, limit, skip
                )
            ),
            resultClazz
        );
    }

    public static <E, R> List<R> select(
        Statement statement,
        EntityModel<E> entity,
        ColumnInfo<?>[] cols,
        QueryBuilder<E> where,
        Class<R> resultClazz
    ) throws SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return select(
            statement,
            entity,
            null,
            cols,
            where,
            new ColumnInfo[0],
            null,
            new OrderColumn[0],
            -1,
            -1,
            resultClazz
        );
    }

    public static <E> List<E> select(
        Statement statement,
        EntityModel<E> entity,
        QueryBuilder<E> where
    ) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return select(
            statement,
            entity,
            null,
            new ColumnInfo[0],
            where,
            new ColumnInfo[0],
            null,
            new OrderColumn[0],
            -1,
            -1,
            entity.getClazz()
        );
    }

    public static <E> String buildSelectQuery(
        EntityModel<E> entity, //target table
        Join<?, ?> join, //joining
        ColumnInfo<?>[] cols, //the target columns to select
        QueryBuilder<E> where, //where clause,
        ColumnInfo<?>[] groupCols,
        QueryBuilder<E> having,
        OrderColumn<?>[] orderByCols,
        long limit,
        long skip
    ) {
        String table = entity.sql();

        return
            "SELECT "

            +

            /* START - SELECT COLUMNS */
            LogicalStream
                .of(cols)
                .ifTrue(c -> c != null && c.length > 0)
                .thenReturn(arr -> Arrays
                    .stream(arr)
                    .map(ColumnInfo::toString)
                    .collect(Collectors.joining(", "))
                )
                .otherwise(s -> "*")
                .get()
            /* END - SELECT COLUMNS */

            +

            " FROM " + (join != null ? join.sql() : table)

            +

            /* START - WHERE CLAUSE */
            LogicalStream
                .of(where)
                .ifTrue(w -> w != null && where.query() != null)
                .thenReturn(w -> " WHERE " + where.query().sql())
                .otherwise(w -> "")
                .get()
            /* END - WHERE CLAUSE */

            +

            /* START - GROUP BY */
            LogicalStream
                .of(groupCols)
                .ifTrue(c -> c != null && c.length > 0)
                .thenReturn(arr -> " GROUP BY " + Arrays
                    .stream(arr)
                    .map(ColumnInfo::groupNaming)
                    .collect(Collectors.joining(", "))
                )
                .otherwise(s -> "")
                .get()
            /* END - GROUP BY */

            +

            /* START - HAVING CLAUSE */
            LogicalStream
                .of(having)
                .ifTrue(w -> w != null && having.query() != null)
                .thenReturn(w -> " HAVING " + having.query().sql())
                .otherwise(w -> "")
                .get()
            /* END - HAVING CLAUSE */

            +

            /* START - ORDER BY */
            LogicalStream
                .of(orderByCols)
                .ifTrue(c -> c != null && c.length > 0)
                .thenReturn(arr -> " ORDER BY " + Arrays
                    .stream(arr)
                    .map(OrderColumn::sql)
                    .collect(Collectors.joining(", "))
                )
                .otherwise(s -> "")
                .get()
            /* END - ORDER BY */

            +

            /* START - LIMIT */
            LogicalStream
                .of(limit)
                .ifTrue(l -> l > 0)
                .thenReturn(l -> " LIMIT " + l)
                .otherwise(l -> "")
                .get()
            /* END - LIMIT */

            +

            /* START - SKIP */
            LogicalStream
                .of(skip)
                .ifTrue(l -> l > 0)
                .thenReturn(l -> " OFFSET " + l)
                .otherwise(l -> "")
                .get();
            /* END - SKIP */
    }
}
