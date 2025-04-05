package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.SalesOrder;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.DateColumn;
import java.lang.Double;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class SalesOrderTable extends EntityModel<SalesOrder> {
	public static class Columns extends TableColumns<SalesOrder> {
		public final NumericColumn<Long> orderId = new NumericColumn<Long>(this, "orderId");
		public final NumericColumn<Long> custId = new NumericColumn<Long>(this, "custId");
		public final NumericColumn<Long> employeeId = new NumericColumn<Long>(this, "employeeId");
		public final DateColumn orderDate = new DateColumn(this, "orderDate");
		public final DateColumn requiredDate = new DateColumn(this, "requiredDate");
		public final DateColumn shippedDate = new DateColumn(this, "shippedDate");
		public final NumericColumn<Long> shipperId = new NumericColumn<Long>(this, "shipperid");
		public final NumericColumn<Double> freight = new NumericColumn<Double>(this, "freight");
		public final TextColumn shipName = new TextColumn(this, "shipName");
		public final TextColumn shipAddress = new TextColumn(this, "shipAddress");
		public final TextColumn shipCity = new TextColumn(this, "shipCity");
		public final TextColumn shipRegion = new TextColumn(this, "shipRegion");
		public final TextColumn shipPostalCode = new TextColumn(this, "shipPostalCode");
		public final TextColumn shipCountry = new TextColumn(this, "shipCountry");

		public Columns(EntityModel<SalesOrder> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public SalesOrderTable() {
        super(SalesOrder.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> orderId() {
		return columns.orderId;
	}

	public NumericColumn<Long> custId() {
		return columns.custId;
	}

	public NumericColumn<Long> employeeId() {
		return columns.employeeId;
	}

	public DateColumn orderDate() {
		return columns.orderDate;
	}

	public DateColumn requiredDate() {
		return columns.requiredDate;
	}

	public DateColumn shippedDate() {
		return columns.shippedDate;
	}

	public NumericColumn<Long> shipperId() {
		return columns.shipperId;
	}

	public NumericColumn<Double> freight() {
		return columns.freight;
	}

	public TextColumn shipName() {
		return columns.shipName;
	}

	public TextColumn shipAddress() {
		return columns.shipAddress;
	}

	public TextColumn shipCity() {
		return columns.shipCity;
	}

	public TextColumn shipRegion() {
		return columns.shipRegion;
	}

	public TextColumn shipPostalCode() {
		return columns.shipPostalCode;
	}

	public TextColumn shipCountry() {
		return columns.shipCountry;
	}
}
