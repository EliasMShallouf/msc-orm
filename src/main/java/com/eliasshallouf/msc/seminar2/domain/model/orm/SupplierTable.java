package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Supplier;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class SupplierTable extends EntityModel<Supplier> {
	public static class Columns extends TableColumns<Supplier> {
		public final NumericColumn<Long> supplierId = new NumericColumn<Long>(this, "supplierId");
		public final TextColumn companyName = new TextColumn(this, "companyName");
		public final TextColumn contactName = new TextColumn(this, "contactName");
		public final TextColumn contactTitle = new TextColumn(this, "contactTitle");
		public final TextColumn address = new TextColumn(this, "address");
		public final TextColumn city = new TextColumn(this, "city");
		public final TextColumn region = new TextColumn(this, "region");
		public final TextColumn postalCode = new TextColumn(this, "postalCode");
		public final TextColumn country = new TextColumn(this, "country");
		public final TextColumn phone = new TextColumn(this, "phone");
		public final TextColumn email = new TextColumn(this, "email");
		public final TextColumn fax = new TextColumn(this, "fax");
		public final TextColumn homePage = new TextColumn(this, "HomePage");

		public Columns(EntityModel<Supplier> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public SupplierTable() {
        super(Supplier.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> supplierId() {
		return columns.supplierId;
	}

	public TextColumn companyName() {
		return columns.companyName;
	}

	public TextColumn contactName() {
		return columns.contactName;
	}

	public TextColumn contactTitle() {
		return columns.contactTitle;
	}

	public TextColumn address() {
		return columns.address;
	}

	public TextColumn city() {
		return columns.city;
	}

	public TextColumn region() {
		return columns.region;
	}

	public TextColumn postalCode() {
		return columns.postalCode;
	}

	public TextColumn country() {
		return columns.country;
	}

	public TextColumn phone() {
		return columns.phone;
	}

	public TextColumn email() {
		return columns.email;
	}

	public TextColumn fax() {
		return columns.fax;
	}

	public TextColumn homePage() {
		return columns.homePage;
	}
}
