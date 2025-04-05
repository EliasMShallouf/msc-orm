package com.eliasshallouf.msc.seminar2.service.utils.join;

import com.eliasshallouf.msc.seminar2.service.utils.query.Query;

@FunctionalInterface
public interface JoinQuery<E1, E2> {
    Query join(E1 left, E2 right);
}
