package com.eliasshallouf.msc.seminar2.service.utils.dialects;

import com.eliasshallouf.msc.seminar2.service.utils.naming.Naming;

public interface Dialect {
    String tableCountRowsQuery(String table);

    Naming namingStrategy();

    static Dialect defaultDialect() {
        return new Dialect() {
            @Override
            public String tableCountRowsQuery(String table) {
                return null;
            }

            @Override
            public Naming namingStrategy() {
                return null;
            }
        };
    }
}
