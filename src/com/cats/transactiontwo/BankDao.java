package com.cats.transactiontwo;

import com.cats.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BankDao {

    /**
     * 加钱的数据库操作方法
     *
     * @param account 存款账号
     * @param money   存款金额
     */

    public void add(String account, int money) throws Exception {

        Connection connection = JDBCUtils.getConnection();

        // 3.编写sql语句
        String sql = "update tb_bank set tb_bank.money = money + ? where account = ?;";
        // 4.创建statement
        PreparedStatement statement = connection.prepareStatement(sql);
        // 5.占位符赋值
        statement.setObject(1, money);
        statement.setObject(2, account);
        // 6.发送sql语句
        statement.executeUpdate();
        // 7.关闭资源
        statement.close();

        System.out.println("存款成功!");
    }

    /**
     * 减钱的数据库操作方法
     *
     * @param account 支出账号
     * @param money   支出金额
     */

    public void sub(String account, int money) throws Exception {

        Connection connection = JDBCUtils.getConnection();

        // 3.编写sql语句
        String sql = "update tb_bank set tb_bank.money = money - ? where account = ?;";
        // 4.创建statement
        PreparedStatement statement = connection.prepareStatement(sql);
        // 5.占位符赋值
        statement.setObject(1, money);
        statement.setObject(2, account);
        // 6.发送sql语句
        statement.executeUpdate();
        // 7.关闭资源
        statement.close();

        System.out.println("支出成功!");

    }
}
