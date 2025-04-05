package com.eliasshallouf.msc.seminar2.service.api;

import com.eliasshallouf.msc.seminar2.domain.model.Employee;
import com.eliasshallouf.msc.seminar2.service.db.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeEndpointService {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() throws Exception {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public Employee findEmployeeById(@PathVariable Long employeeId) throws Exception {
        return employeeService.findEmployeeById(employeeId);
    }
}
