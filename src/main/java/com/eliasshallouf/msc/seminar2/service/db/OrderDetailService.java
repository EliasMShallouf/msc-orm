package com.eliasshallouf.msc.seminar2.service.db;

import com.eliasshallouf.msc.seminar2.domain.model.OrderDetail;
import com.eliasshallouf.msc.seminar2.domain.model.orm.OrderDetailTable;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.paging.Paging;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    public ConnectionManager connectionManager;
    private OrderDetailTable table = new OrderDetailTable();
    private final EntityManager<OrderDetail> manager;

    public OrderDetailService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.manager = table.manager(connectionManager);
    }

    public List<OrderDetail> getAll() {
        return manager.getAll();
    }

    public Paging<OrderDetail> paging() {
        return manager.pages(1000, false);
    }

    public long size() {
        return manager.count();
    }
}
