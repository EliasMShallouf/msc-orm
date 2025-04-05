package com.eliasshallouf.msc.seminar2.service.db;

import com.eliasshallouf.msc.seminar2.domain.model.Employee;
import com.eliasshallouf.msc.seminar2.domain.model.EmployeeTerritory;
import com.eliasshallouf.msc.seminar2.domain.model.Territory;
import com.eliasshallouf.msc.seminar2.domain.model.orm.EmployeeTable;
import com.eliasshallouf.msc.seminar2.domain.model.orm.EmployeeTerritoryTable;
import com.eliasshallouf.msc.seminar2.domain.model.orm.TerritoryTable;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.functions.Concat;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo.valueOf;

@Service
public class EmployeeService {
    @Autowired
    public ConnectionManager connectionManager;
    private final EmployeeTable table = new EmployeeTable();
    public EntityManager<Employee> entityManager;

    public EmployeeService(@Autowired ConnectionManager manager) {
        this.connectionManager = manager;
        entityManager = table.manager(connectionManager);
    }

    public List<Employee> getAllEmployees() {
        return
            entityManager
                .getAll()
                .stream()
                .peek(eo -> {
                    /*
                        select t.*
                        from EmployeeTerritory et
                        join territory t on t.territoryId = et.territoryId
                        where et.employeeId = $e.getEmployeeId()
                     */

                    EmployeeTerritoryTable et = new EmployeeTerritoryTable().aliased("et");
                    TerritoryTable t = new TerritoryTable().aliased("t");

                    eo.setTerritories(new HashSet<>(
                        connectionManager
                            .createEntityManager(et)
                            .<EmployeeTerritory>query()
                            .select(t)
                            .join(t, t.territoryId().equal(et.territoryId()))
                            .where(et.employeeId().equal(valueOf(eo.getEmployeeId())))
                            .list(Territory.class)
                    ));
                })
                .toList();
    }

    public Employee findEmployeeById(Long id) {
        return entityManager.findById(id);
    }

    public Employee findByFirstAndLastName(String fullName) {
        /*
            select * from employee where concat(firstname, ' ', lastname) = '$fullName'
        */

        return entityManager
            .<Employee>query()
            .where(
                Concat.of(
                    table.firstName(),
                    valueOf(" "),
                    table.lastName()
                ).equal(valueOf(fullName))
            ).find();
    }

    public int updateEmployeeTitle(String title, String newTitle) {
        return entityManager.update(
            table.title().equal(valueOf(title)),
            table.title().setTo(newTitle)
        );
    }
}
