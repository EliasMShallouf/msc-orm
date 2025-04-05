package com.eliasshallouf.msc.seminar2.service.db;

import com.eliasshallouf.msc.seminar2.domain.model.Territory;
import com.eliasshallouf.msc.seminar2.domain.model.orm.TerritoryTable;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TerritoryService {
    @Autowired
    public ConnectionManager connectionManager;
    private final TerritoryTable table = new TerritoryTable();
    private final EntityManager<Territory> entityManager;

    public TerritoryService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.entityManager = table.manager(connectionManager);
    }

    public List<Territory> getAllTerritories() {
        return entityManager.getAll();
    }

    public void save(Territory ...territories) {
        entityManager.save(territories);
    }

    public void deleteAll() {
        entityManager.deleteAll();
    }
}
