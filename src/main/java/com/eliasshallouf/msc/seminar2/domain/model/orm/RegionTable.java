package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Region;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;

public class RegionTable extends EntityModel<Region> {
	public static class Columns extends TableColumns<Region> {
		public final NumericColumn<Long> regionId = new NumericColumn<Long>(this, "regionId");
		public final TextColumn regionDescription = new TextColumn(this, "regiondescription");

		public Columns(EntityModel<Region> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public RegionTable() {
        super(Region.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> regionId() {
		return columns.regionId;
	}

	public TextColumn regionDescription() {
		return columns.regionDescription;
	}
}
