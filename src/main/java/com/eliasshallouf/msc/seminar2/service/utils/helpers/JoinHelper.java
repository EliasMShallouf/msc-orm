package com.eliasshallouf.msc.seminar2.service.utils.helpers;

import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.join.Join;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;

public class JoinHelper {
    public static <E> String join(E root) { // TODO replace join with join types
        if(root instanceof Join j)
            return join(j.getLeft()) + " join " + join(j.getRight()) + " on " + j.on().sql() + "\n";

        if(root instanceof EntityModel e)
            return e.sql();

        return "";
    }
}
