package com.eliasshallouf.msc.seminar2.domain.model.orm;

import com.eliasshallouf.msc.seminar2.domain.model.Category;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import java.lang.Long;
import com.eliasshallouf.msc.seminar2.service.utils.columns.TextColumn;
import com.eliasshallouf.msc.seminar2.service.utils.columns.BlobColumn;

public class CategoryTable extends EntityModel<Category> {
	public static class Columns extends TableColumns<Category> {
		public final NumericColumn<Long> categoryId = new NumericColumn<Long>(this, "categoryId");
		public final TextColumn categoryName = new TextColumn(this, "categoryName");
		public final TextColumn description = new TextColumn(this, "description");
		public final BlobColumn picture = new BlobColumn(this, "picture");

		public Columns(EntityModel<Category> model) {
			super(model);
		}
	}

    private final Columns columns = new Columns(this);

    public CategoryTable() {
        super(Category.class);
    }

    @Override
    public Columns columns() {
        return columns;
    }

	public NumericColumn<Long> categoryId() {
		return columns.categoryId;
	}

	public TextColumn categoryName() {
		return columns.categoryName;
	}

	public TextColumn description() {
		return columns.description;
	}

	public BlobColumn picture() {
		return columns.picture;
	}
}
