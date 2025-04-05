package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Shipper;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class ShipperTable extends EntityModel<Shipper> {
	public static class Columns extends TableColumns<Shipper> {
		public final NumericColumn<Long> shipperId = new NumericColumn<Long>(this, "shipperId");
		public final TextColumn companyName = new TextColumn(this, "companyName");
		public final TextColumn phone = new TextColumn(this, "phone");

		public Columns(EntityModel<Shipper> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public ShipperTable() {
        super(Shipper.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> shipperId() {
		return columns.shipperId;
	}

	public TextColumn companyName() {
		return columns.companyName;
	}

	public TextColumn phone() {
		return columns.phone;
	}
}
