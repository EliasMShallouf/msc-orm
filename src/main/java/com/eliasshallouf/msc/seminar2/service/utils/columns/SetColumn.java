package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.Mapper;

public interface SetColumn <T> {
    ColumnInfo<T> column();
    T value();

    default String sql() {
        return column().column() + " = " + (value() == null ? "null" : Mapper.mapToSqlValue(value()));
    }
}
