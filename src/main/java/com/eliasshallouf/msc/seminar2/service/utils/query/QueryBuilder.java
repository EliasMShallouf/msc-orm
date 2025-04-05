package com.eliasshallouf.msc.seminar2.service.utils.query;

import com.eliasshallouf.msc.seminar2.service.utils.query.Query;

@FunctionalInterface
public interface QueryBuilder<E> {
    Query query();
}