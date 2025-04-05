package com.eliasshallouf.msc.seminar2.service.utils.columns;

import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.Mapper;

import java.util.Objects;

public class Value<E> extends ColumnInfo<E> {
    private E value;

    public Value(E e) {
        super("", "");
        this.value = e;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    @Override
    public String column() {
        return LogicalStream
            .of(this.value)
            .ifTrue(Objects::nonNull)
            .thenReturn(Mapper::mapToSqlValue)
            .otherwise(v -> "null")
            .get();
    }
}
