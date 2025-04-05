package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.EmployeeTerritory;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class EmployeeTerritoryTable extends EntityModel<EmployeeTerritory> {
	public static class Columns extends TableColumns<EmployeeTerritory> {
		public final NumericColumn<Long> employeeId = new NumericColumn<Long>(this, "employeeId");
		public final TextColumn territoryId = new TextColumn(this, "territoryId");

		public Columns(EntityModel<EmployeeTerritory> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public EmployeeTerritoryTable() {
        super(EmployeeTerritory.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> employeeId() {
		return columns.employeeId;
	}

	public TextColumn territoryId() {
		return columns.territoryId;
	}
}
