package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Product;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import java.lang.Double;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;
import java.lang.Integer;

public class ProductTable extends EntityModel<Product> {
	public static class Columns extends TableColumns<Product> {
		public final NumericColumn<Long> productId = new NumericColumn<Long>(this, "productId");
		public final NumericColumn<Long> supplierId = new NumericColumn<Long>(this, "supplierId");
		public final NumericColumn<Long> categoryId = new NumericColumn<Long>(this, "categoryId");
		public final TextColumn productName = new TextColumn(this, "productName");
		public final TextColumn quantityPerUnit = new TextColumn(this, "quantityPerUnit");
		public final NumericColumn<Double> unitPrice = new NumericColumn<Double>(this, "unitPrice");
		public final NumericColumn<Integer> unitsInStock = new NumericColumn<Integer>(this, "unitsInStock");
		public final NumericColumn<Integer> unitsOnOrder = new NumericColumn<Integer>(this, "unitsOnOrder");
		public final NumericColumn<Integer> reorderLevel = new NumericColumn<Integer>(this, "reorderLevel");
		public final NumericColumn<Integer> discontinued = new NumericColumn<Integer>(this, "discontinued");

		public Columns(EntityModel<Product> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public ProductTable() {
        super(Product.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> productId() {
		return columns.productId;
	}

	public NumericColumn<Long> supplierId() {
		return columns.supplierId;
	}

	public NumericColumn<Long> categoryId() {
		return columns.categoryId;
	}

	public TextColumn productName() {
		return columns.productName;
	}

	public TextColumn quantityPerUnit() {
		return columns.quantityPerUnit;
	}

	public NumericColumn<Double> unitPrice() {
		return columns.unitPrice;
	}

	public NumericColumn<Integer> unitsInStock() {
		return columns.unitsInStock;
	}

	public NumericColumn<Integer> unitsOnOrder() {
		return columns.unitsOnOrder;
	}

	public NumericColumn<Integer> reorderLevel() {
		return columns.reorderLevel;
	}

	public NumericColumn<Integer> discontinued() {
		return columns.discontinued;
	}
}
