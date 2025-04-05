package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.naming.Naming;

public interface OrderColumn<T> {
    enum OrderBy { ASC, DESC };

    ColumnInfo<T> column();
    OrderBy type();

    default String sql() {
        return (column().alias.isEmpty() ? column().column() : Naming.defaults().doChange(column().alias)) + " " + type().name();
    }
}
