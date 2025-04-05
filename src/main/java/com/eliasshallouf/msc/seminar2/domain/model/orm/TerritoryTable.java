package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Territory;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;
import java.lang.Long;

public class TerritoryTable extends EntityModel<Territory> {
	public static class Columns extends TableColumns<Territory> {
		public final TextColumn territoryId = new TextColumn(this, "territoryId");
		public final TextColumn territoryDescription = new TextColumn(this, "territorydescription");
		public final NumericColumn<Long> regionId = new NumericColumn<Long>(this, "regionId");

		public Columns(EntityModel<Territory> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public TerritoryTable() {
        super(Territory.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public TextColumn territoryId() {
		return columns.territoryId;
	}

	public TextColumn territoryDescription() {
		return columns.territoryDescription;
	}

	public NumericColumn<Long> regionId() {
		return columns.regionId;
	}
}
