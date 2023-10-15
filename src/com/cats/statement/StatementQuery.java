package com.cats.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

public class StatementQuery {

    /**
     * DriverManager
     * Connection
     * Statement
     * PreparedStatement
     * ResultSet
     *
     * @param args
     */

    public static void main(String[] args) throws SQLException {

        // 1.注册驱动

        /*
          DriverManager 注册驱动
          依赖: 驱动版本 8+ com.mysql.cj.jdbc.Driver
                       5+ com.mysql.jdbc.Driver
        */

        DriverManager.registerDriver(new Driver());

        // 2.获取连接

        /*
            参数一: url
                   jdbc: 数据库厂家://IP地址:port/数据库名
            参数二: username 数据库的账号 root
            参数三: password 数据库的密码 123456
        */

        // java.sql 接口 = 实现类
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/xs_jdbc", "root", "123456");

        // 3.创建statement
        Statement statement = connection.createStatement();
        // 4.发送sql语句,并获取返回结果
        String sql = "select * from tb_user";
        ResultSet resultSet = statement.executeQuery(sql);
        // 5.进行结果集解析
        // 看看有没有下一行数据

        /*
             ResultSet 游标最初位于第一行之前;
             对该方法 next 的第一次调用使第一行成为当前行;
             第二次调用使第二行成为当前行，依此类推。
             当对方法的next调用返回false时，光标位于最后一行之后。
         */

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
       }
        // 6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
