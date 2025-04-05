package com.eliasshallouf.msc.seminar2.service.utils.query;

public interface Query {
    String sql();

    default BiQuery and(Query q) {
        return new BiQuery(this, q, BiQuery.BiOperator.AND);
    }

    default BiQuery or(Query q) {
        return new BiQuery(this, q, BiQuery.BiOperator.OR);
    }

    default Query not() {
        return () -> String.format("not (%s)", sql());
    }
}
