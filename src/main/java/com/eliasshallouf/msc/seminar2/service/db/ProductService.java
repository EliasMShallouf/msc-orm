package com.eliasshallouf.msc.seminar2.service.db;

import com.eliasshallouf.msc.seminar2.domain.model.Product;
import com.eliasshallouf.msc.seminar2.domain.model.helpers.ProductWithTotal;
import com.eliasshallouf.msc.seminar2.domain.model.orm.OrderDetailTable;
import com.eliasshallouf.msc.seminar2.domain.model.orm.ProductTable;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation.Sum;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo.valueOf;

@Service
public class ProductService {
    @Autowired
    public ConnectionManager connectionManager;
    private final EntityManager<Product> entityManager;

    public ProductService(@Autowired ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.entityManager = new ProductTable().manager(connectionManager);
    }

    public List<ProductWithTotal> getProductsWithTotals() {
        /*
            SELECT p.productName as name, sum(od.unitPrice * od.quantity * (1 - od.discount)) as total
            FROM product p
            join orderdetail od on od.productId = p.productId
            GROUP by p.productId
            ORDER by total DESC;
        */

        ProductTable p = new ProductTable().aliased("p");
        OrderDetailTable od = new OrderDetailTable().aliased("od");

        var total = Sum.of(
            od.unitPrice().multiple(
                od.quantity().castTo(Double.class).multiple(
                    valueOf(1).<Double>asNumber().subtract(od.discount())
                )
            )
        ).as("total");

        return
            entityManager
                .<Product>query()
                .table(p)
                .select(
                    p.productName().as("name"),
                    total
                )
                .join(od, od.productId().equal(p.productId()))
                .groupBy(p.productId())
                .orderBy(total.descendingOrder())
                .list(ProductWithTotal.class);
    }
}
