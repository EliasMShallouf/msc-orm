package com.eliasshallouf.msc.seminar2.service.utils.functions;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;

public interface Function<T> {
    ColumnInfo<T> result();
}
