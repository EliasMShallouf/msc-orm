package com.eliasshallouf.msc.seminar2.service.utils.naming;

public interface Naming {
    @FunctionalInterface
    interface NamingFunction {
        String name(String s);
    }

    NamingFunction wrapStrategy(); //for wrap tables and columns names with quotations if needed

    NamingFunction namingStrategy(); //for change the tables and columns names to another case like lower case

    NamingFunction sqlKeywordsNamingStrategy(); //for sql reserved keywords

    default String doChange(String s) {
        if(s == null)
            return "";

        if(s.equals("*"))
            return s;

        return wrapStrategy().name(namingStrategy().name(s));
    }

    static Naming defaults() {
        return new Naming() {
            @Override
            public NamingFunction wrapStrategy() {
                return (s) -> String.format("`%s`", s);
            }

            @Override
            public NamingFunction namingStrategy() {
                return (s) -> s;
            }

            @Override
            public NamingFunction sqlKeywordsNamingStrategy() {
                return (s) -> s;
            }
        };
    }
}
