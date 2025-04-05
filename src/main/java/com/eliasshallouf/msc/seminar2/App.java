package com.eliasshallouf.msc.seminar2;

import com.eliasshallouf.msc.seminar2.domain.model.Category;
import com.eliasshallouf.msc.seminar2.domain.model.OrderDetail;
import com.eliasshallouf.msc.seminar2.domain.model.Territory;
import com.eliasshallouf.msc.seminar2.domain.model.orm.CategoryTable;
import com.eliasshallouf.msc.seminar2.service.db.*;
import com.eliasshallouf.msc.seminar2.service.utils.ConnectionManager;
import com.eliasshallouf.msc.seminar2.service.utils.columns.ColumnInfo;
import com.eliasshallouf.msc.seminar2.service.utils.columns.NumericColumn;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.TaskRunner;
import com.eliasshallouf.msc.seminar2.service.utils.paging.Page;
import com.eliasshallouf.msc.seminar2.service.utils.paging.Paging;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityManager;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootApplication(exclude = {
	DataSourceAutoConfiguration.class
})
public class App {
	@Bean
	public ConnectionManager createConnectionManager(
		@Value("${spring.datasource.driver-class-name}") String driver,
		@Value("${spring.datasource.url}") String url,
		@Value("${spring.datasource.username}") String user,
		@Value("${spring.datasource.password}") String password
	) {
		try {
			System.out.println("Creating connection with this properties :");
			System.out.println(" - " + driver);
			System.out.println(" - " + url);
			System.out.println(" - " + user);
			System.out.println(" - " + password);
			return new ConnectionManager(driver, url, user, password);
		} catch (Exception e) {
			System.err.println("Error creating connection with the previous properties");
			return null;
		}
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(App.class, args);

		ConnectionManager manager = applicationContext.getBean(ConnectionManager.class);

		testEntityWithoutDeclareClass(manager);

		/*CategoryTable categoryTable = new CategoryTable();

		EntityManager<Category> categoryEntityManager = categoryTable.manager(manager);

		categoryEntityManager.getAll().forEach(c -> System.out.println(c.getCategoryId()));*/

		//test(applicationContext);

		//EmployeeService employeeService = applicationContext.getBean(EmployeeService.class);

		//System.out.println(employeeService.updateEmployeeTitle("Mr", "King"));

		//System.out.println(employeeService.findByFirstAndLastName("Sara Davis"));

		//employeeService.test().forEach(t -> System.out.println(t.with1));

		//CustomerService customerService = applicationContext.getBean(CustomerService.class);

		//customerService.getCustomersWithTotals().forEach(cwt -> System.out.println(cwt.getName() + " : " + cwt.getTotal()));

		//SalesOrderService salesOrderService = applicationContext.getBean(SalesOrderService.class);

		//salesOrderService.getAllWithTotalsBetween(5000, 12000).forEach(salesOrder -> System.out.println(salesOrder.getOrderId()));

		/*OrderDetailService orderDetailService = applicationContext.getBean(OrderDetailService.class);

		System.out.println("Order detail rows = " + orderDetailService.size());

		Paging<OrderDetail> pages = orderDetailService.paging();

		while (pages.hasNext()) {
			long t = System.currentTimeMillis();

			Page<OrderDetail> page = pages.next();

			System.out.println("Loaded page " + page.getIndex() + " in " + (System.currentTimeMillis() - t) + "ms with data size = " + page.getSize());
		}*/
	}

	static void testEntityWithoutDeclareClass(ConnectionManager manager) {
		EntityModel<Void> model = EntityModel.defineEntity("address");

		ColumnInfo<String> ip = ColumnInfo.defineColumn(model, "ip");
		ColumnInfo<String> country = ColumnInfo.defineColumn(model, "country");
		ColumnInfo<String> city = ColumnInfo.defineColumn(model, "city");
		NumericColumn<Double> lon = ColumnInfo.defineColumn(model, "lon").asNumber();
		NumericColumn<Double> lat = ColumnInfo.defineColumn(model, "lat").asNumber();

		EntityManager<Void> modelManager = model.manager(manager);

		Object[] res = modelManager
			.query()
			//.select(ip, country, city, lon, lat)
			.select(model)
			.where(country.equal(ColumnInfo.valueOf("Syria")))
			.find(Object[].class);

		//Object[] res = manager.findOne("select * from address where country = 'Syria'", Object[].class);

		for(Object o : res)
			System.out.println(o + " : " + o.getClass().getSimpleName());
	}

	static void test(ApplicationContext applicationContext) {
		TaskRunner runner = TaskRunner.defaultRunner();

		EmployeeService employeeService = applicationContext.getBean(EmployeeService.class);
		TerritoryService territoryService = applicationContext.getBean(TerritoryService.class);
		OrderDetailService orderDetailService = applicationContext.getBean(OrderDetailService.class);
		CustomerService customerService = applicationContext.getBean(CustomerService.class);
		ProductService productService = applicationContext.getBean(ProductService.class);
		SalesOrderService salesOrderService = applicationContext.getBean(SalesOrderService.class);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q1", employeeService::getAllEmployees), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q2", orderDetailService::getAll), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q3", () -> employeeService.findEmployeeById(7L)), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q4", () -> employeeService.findByFirstAndLastName("Sven Buck")), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q5", () -> customerService.deleteById(280L)), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q6", () -> {
			List<Territory> territoryList = territoryService.getAllTerritories();
			territoryService.deleteAll();
			territoryService.save(territoryList.toArray(new Territory[0]));
		}), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q7", customerService::getCustomersWithTotals), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q8", customerService::getCustomersLivePlaces), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q9", productService::getProductsWithTotals), 100);

		runner.executeOnceThenRepeatedly(new TaskRunner.Task("Q10", () -> salesOrderService.getAllWithTotalsBetween(5000, 12000)), 100);
	}
}
