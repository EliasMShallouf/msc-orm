package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Customer;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class CustomerTable extends EntityModel<Customer> {
	public static class Columns extends TableColumns<Customer> {
		public final NumericColumn<Long> custId = new NumericColumn<Long>(this, "custId");
		public final TextColumn companyName = new TextColumn(this, "companyName");
		public final TextColumn contactName = new TextColumn(this, "contactName");
		public final TextColumn contactTitle = new TextColumn(this, "contactTitle");
		public final TextColumn address = new TextColumn(this, "address");
		public final TextColumn city = new TextColumn(this, "city");
		public final TextColumn region = new TextColumn(this, "region");
		public final TextColumn postalCode = new TextColumn(this, "postalCode");
		public final TextColumn country = new TextColumn(this, "country");
		public final TextColumn phone = new TextColumn(this, "phone");
		public final TextColumn mobile = new TextColumn(this, "mobile");
		public final TextColumn email = new TextColumn(this, "email");
		public final TextColumn fax = new TextColumn(this, "fax");

		public Columns(EntityModel<Customer> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public CustomerTable() {
        super(Customer.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> custId() {
		return columns.custId;
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

	public TextColumn mobile() {
		return columns.mobile;
	}

	public TextColumn email() {
		return columns.email;
	}

	public TextColumn fax() {
		return columns.fax;
	}
}
