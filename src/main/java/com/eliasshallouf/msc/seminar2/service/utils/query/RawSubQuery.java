package com.eliasshallouf.msc.seminar2.service.utils.query;

import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;

public class RawSubQuery extends EntityModel<Void> {
    private String qry;

    public RawSubQuery(String qry) {
        super(null);
        this.qry = qry;
    }

    public RawSubQuery setQuery(String qry) {
        this.qry = qry;
        return this;
    }

    @Override
    public TableColumns<Void> columns() {
        return new TableColumns<>(this) { };
    }

    @Override
    public String getTableName() {
        return "(" + this.qry + ")";
    }
}
