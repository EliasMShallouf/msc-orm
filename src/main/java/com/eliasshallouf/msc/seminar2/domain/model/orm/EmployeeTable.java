package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Employee;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.DateColumn;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;
import java.lang.Integer;
import java.time.LocalDate;
import java.util.Date;

import com.eliasshallouf.msc.seminar2.service.utils.columns.BlobColumn;

public class EmployeeTable extends EntityModel<Employee> {
	public static class Columns extends TableColumns<Employee> {
		public final NumericColumn<Long> employeeId = new NumericColumn<Long>(this, "employeeId");
		public final TextColumn lastName = new TextColumn(this, "lastname");
		public final TextColumn firstName = new TextColumn(this, "firstname");
		public final TextColumn title = new TextColumn(this, "title");
		public final TextColumn titleOfCourtesy = new TextColumn(this, "titleOfCourtesy");
		public final DateColumn<LocalDate> birthDate = new DateColumn<>(this, "birthDate");
		public final DateColumn<LocalDate> hireDate = new DateColumn<>(this, "hireDate");
		public final TextColumn address = new TextColumn(this, "address");
		public final TextColumn city = new TextColumn(this, "city");
		public final TextColumn region = new TextColumn(this, "region");
		public final TextColumn postalCode = new TextColumn(this, "postalCode");
		public final TextColumn country = new TextColumn(this, "country");
		public final TextColumn phone = new TextColumn(this, "phone");
		public final TextColumn extension = new TextColumn(this, "extension");
		public final TextColumn mobile = new TextColumn(this, "mobile");
		public final TextColumn email = new TextColumn(this, "email");
		public final BlobColumn photo = new BlobColumn(this, "photo");
		public final BlobColumn notes = new BlobColumn(this, "notes");
		public final NumericColumn<Integer> mgrId = new NumericColumn<Integer>(this, "mgrId");
		public final TextColumn photoPath = new TextColumn(this, "photoPath");

		public Columns(EntityModel<Employee> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public EmployeeTable() {
        super(Employee.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> employeeId() {
		return columns.employeeId;
	}

	public TextColumn lastName() {
		return columns.lastName;
	}

	public TextColumn firstName() {
		return columns.firstName;
	}

	public TextColumn title() {
		return columns.title;
	}

	public TextColumn titleOfCourtesy() {
		return columns.titleOfCourtesy;
	}

	public DateColumn birthDate() {
		return columns.birthDate;
	}

	public DateColumn hireDate() {
		return columns.hireDate;
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

	public TextColumn extension() {
		return columns.extension;
	}

	public TextColumn mobile() {
		return columns.mobile;
	}

	public TextColumn email() {
		return columns.email;
	}

	public BlobColumn photo() {
		return columns.photo;
	}

	public BlobColumn notes() {
		return columns.notes;
	}

	public NumericColumn<Integer> mgrId() {
		return columns.mgrId;
	}

	public TextColumn photoPath() {
		return columns.photoPath;
	}
}
