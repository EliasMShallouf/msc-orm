package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.CustomerDemographics;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class CustomerDemographicsTable extends EntityModel<CustomerDemographics> {
	public static class Columns extends TableColumns<CustomerDemographics> {
		public final NumericColumn<Long> customerTypeId = new NumericColumn<Long>(this, "customerTypeId");
		public final TextColumn customerDesc = new TextColumn(this, "customerDesc");

		public Columns(EntityModel<CustomerDemographics> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public CustomerDemographicsTable() {
        super(CustomerDemographics.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> customerTypeId() {
		return columns.customerTypeId;
	}

	public TextColumn customerDesc() {
		return columns.customerDesc;
	}
}
