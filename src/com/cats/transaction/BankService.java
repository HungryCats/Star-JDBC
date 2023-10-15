package com.cats.transaction;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @ClassName: BankService
 * @Description: TODO 业务层 调用dao层方法
 * @author: HungryCats
 * @date: 2023/10/14 9:44
 */
public class BankService {

    /**
     * 事务的添加是在业务方法中!
     * 利用try catch代码块,开启事务,提交事务,和事务回滚
     * 将connection传入dao层即可! dao只负责使用,不要close();
     * @param addAccount 存款账号
     * @param subAccount 收款账号
     * @param money 金额
     * @throws Exception 业务异常 | 事务回滚
     */

    public void transfer(String addAccount, String subAccount, int money) throws Exception {

        BankDao bankDao = new BankDao();

        // 一个事务的最基本要求,必须是同一个连接对象 connection
        // 一个转账方法,属于一个事务 (加钱,减钱)

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://localhost:3306/xs_jdbc?user=root&password=123456");


        try {
            // 开启事务 | 关闭事务提交
            connection.setAutoCommit(false);

            // 执行数据库操作
            bankDao.sub(subAccount, money, connection);
            System.out.println("--------");
            bankDao.add(addAccount, money, connection);

            // 事务提交
            connection.commit();
        } catch (Exception e) {
            // 事务回滚
            connection.rollback();
            System.out.println("转账失败");
            throw new RuntimeException(e);
        }finally {
            connection.close();
        }

    }
}
