package com.cats.ultimately.service;

import com.cats.ultimately.dao.CustomerDao;
import com.cats.ultimately.domain.Customer;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: CustomerService
 * @Description: Service
 * @author: HungryCats
 * @date: 2023/10/14 20:03
 */

public class CustomerService {

    private final CustomerDao customerDao = new CustomerDao();

    public List<Customer> getList() throws Exception {

        return customerDao.findAll();
    }

    public void addCustomer(Customer customer) throws SQLException {
        customerDao.addCustomer(customer);
    }

    public Customer getCustomer(int id) throws Exception {
        return customerDao.findById(id);
    }

    public boolean modifyCustomer(int id, Customer customer) throws SQLException {
        int rows = customerDao.updateById(customer);
        return rows != 0;
    }

    public boolean removeCustomer(int id) throws SQLException {
        int rows = customerDao.removeById(id);
        return rows != 0;
    }
}
