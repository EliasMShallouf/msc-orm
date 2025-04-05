package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.OrderDetail;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import java.lang.Double;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import java.lang.Integer;

public class OrderDetailTable extends EntityModel<OrderDetail> {
	public static class Columns extends TableColumns<OrderDetail> {
		public final NumericColumn<Long> orderDetailId = new NumericColumn<Long>(this, "orderDetailId");
		public final NumericColumn<Long> orderId = new NumericColumn<Long>(this, "orderId");
		public final NumericColumn<Long> productId = new NumericColumn<Long>(this, "productId");
		public final NumericColumn<Double> unitPrice = new NumericColumn<Double>(this, "unitPrice");
		public final NumericColumn<Integer> quantity = new NumericColumn<Integer>(this, "quantity");
		public final NumericColumn<Double> discount = new NumericColumn<Double>(this, "discount");

		public Columns(EntityModel<OrderDetail> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public OrderDetailTable() {
        super(OrderDetail.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> orderDetailId() {
		return columns.orderDetailId;
	}

	public NumericColumn<Long> orderId() {
		return columns.orderId;
	}

	public NumericColumn<Long> productId() {
		return columns.productId;
	}

	public NumericColumn<Double> unitPrice() {
		return columns.unitPrice;
	}

	public NumericColumn<Integer> quantity() {
		return columns.quantity;
	}

	public NumericColumn<Double> discount() {
		return columns.discount;
	}
}
