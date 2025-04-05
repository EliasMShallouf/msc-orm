package com.eliasshallouf.msc.seminar2.service.db;

import com.eliasshallouf.msc.seminar2.domain.model.SalesOrder;
import com.eliasshallouf.msc.seminar2.domain.model.orm.OrderDetailTable;
import com.eliasshallouf.msc.seminar2.domain.model.orm.SalesOrderTable;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.functions.aggergation.Sum;
import com.eliasshallouf.msc.seminar2.service.utils.query.SubQuery;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo.valueOf;

@Service
public class SalesOrderService {
    @Autowired
    public ConnectionManager connectionManager;
    private EntityManager<SalesOrder> entityManager;

    public SalesOrderService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.entityManager = new SalesOrderTable().manager(connectionManager);
    }

    public List<SalesOrder> getAllWithTotalsBetween(double low, double high) {
        /*
            SELECT a.* from (
                select so.*, sum(od.unitPrice * od.quantity * (1 - od.discount)) total
                FROM salesorder so
                join orderdetail od on od.orderId = so.orderId
                GROUP by so.orderId
                ORDER by total desc
            ) a where a.total BETWEEN %f and %f
         */

        SalesOrderTable so = new SalesOrderTable().aliased("so");
        OrderDetailTable od = new OrderDetailTable().aliased("od");

        Sum<Double> total = Sum.of(
            od.unitPrice().multiple(
                od.quantity().castTo(Double.class).multiple(
                    valueOf(1).<Double>asNumber().subtract(od.discount())
                )
            )
        ).as("total");

        var subQuery =
            new SubQuery<SalesOrder>()
                .table(so)
                .select(
                    so.allColumns(),
                    total
                )
                .join(od, od.orderId().equal(so.orderId()))
                .groupBy(so.orderId())
                .orderBy(total.descendingOrder())
                .aliased("a");

        return
            entityManager
                .query()
                .table(subQuery)
                .select(subQuery.allColumns())
                .where(total.between(valueOf(low), valueOf(high)))
                .list();
    }
}
