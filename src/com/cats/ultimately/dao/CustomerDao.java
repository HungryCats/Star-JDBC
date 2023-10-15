package com.cats.ultimately.dao;

import com.cats.ultimately.domain.Customer;
import com.cats.utils.BaseDao;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName: CustomerDao
 * @Description: Dao层
 * @author: HungryCats
 * @date: 2023/10/14 20:02
 */
public class CustomerDao extends BaseDao {

    /**
     * 查询客户信息
     *
     * @return 客户信息的集合
     */
    public List<Customer> findAll() throws Exception {
        String sql = "select * from tb_customer";
        return executeQuery(Customer.class, sql);
    }

    /**
     * 添加客户信息
     *
     * @param customer customer
     */
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "insert into tb_customer (name,gender,age,address) values (?,?,?,?);";
        executeUpdate(sql, customer.getName(), customer.getGender(), customer.getAge(), customer.getAddress());
    }

    /**
     * 修改客户信息
     *
     * @param customer 编号
     * @return 影响行数
     */
    public int updateById(Customer customer) throws SQLException {
        String sql = "update tb_customer set name = ?, gender = ?, age = ?, address = ?;";
        return executeUpdate(sql, customer.getName(), customer.getGender(), customer.getAge(), customer.getAddress());
    }

    /**
     * 根据id查询客户信息
     *
     * @param id 编号
     * @return id
     */
    public Customer findById(int id) throws Exception {
        String sql = "select * from tb_customer where id = ?;";
        List<Customer> customerList = executeQuery(Customer.class, sql, id);
        if (customerList != null && !customerList.isEmpty()){
            return customerList.get(0);
        }
        return null;
    }

    /**
     * 删除客户信息
     *
     * @param id 删除的id
     * @return 影响的函数
     */
    public int removeById(int id) throws SQLException {
        String sql = "delete from tb_customer where id = ?;";
        return executeUpdate(sql, id);
    }
}
