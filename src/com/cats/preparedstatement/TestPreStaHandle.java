package com.cats.preparedstatement;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PreStaCRUD
 * @Description: 使用preparedStatement进行增删改查操作
 * @author: HungryCats
 * @date: 2023/10/13 16:17
 */

public class TestPreStaHandle {

    @Test
    public void testSelect() throws Exception {

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc", "root", "123456");
        // 3.编写sql语句,动态值使用?替代
        String sql = "select * from tb_user";
        // 4.创建preparedStatement,并传入sql语句结构
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 5.省略占位符赋值
        // 6.发送sql语句
        ResultSet resultSet = preparedStatement.executeQuery();
        // 7.输出结果
        List<Map> list = new ArrayList<>();

        // 获取列的信息对象
        // metaData 装的是结果集列的信息对象! (可以根据列的名称根据下标,可以获取列的数量)
        ResultSetMetaData metaData = resultSet.getMetaData();

        // 有了他以后,我们可以水平遍历列!
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map map = new HashMap();
            // 一行数据对应一个map

            /*
                map.put("id", resultSet.getInt("id"));
                map.put("account", resultSet.getString("account"));
                map.put("password", resultSet.getString("password"));
                map.put("nickname", resultSet.getString("nickname"));
            */

            for (int i = 1; i <= columnCount; i++) {
                // 获取指定列下角标的值! resultSet
                Object value = resultSet.getObject(i);

                // 获取指定列下角标的列的名称! ResultSetMetaData
                // getColumnLabel: 会获取别名,如果没有别名才是列名
                // 不要使用 getColumnName: 只会获取列的名称
                String columnLabel = metaData.getColumnLabel(i);
                map.put(columnLabel, value);
            }
            // 将map中的数据添加到list中
            list.add(map);
        }
        System.out.println("list = " + list);
        // 8.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc", "root", "123456");
        // 3.编写sql语句,动态值用?代替
        String sql = "insert into tb_user (account,password,nickname) values (?,?,?);";
        // 4.创建preparedStatement并传入sql语句结构
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 5.占位符赋值
        preparedStatement.setObject(1, "张三");
        preparedStatement.setObject(2, "wangwu");
        preparedStatement.setObject(3, "普通用户");
        // 6.发送sql语句
        int rows = preparedStatement.executeUpdate();
        // 7.输出结果
        if (rows > 0) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
        // 8.关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException {

        /*
            修改id=3用户的account = 李四
         */

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///xs_jdbc", "root", "123456");
        // 3.编写sql语句,动态值使用?替代
        String sql = "update tb_user set account = ? where id = ?";
        // 4.创建preparedStatement,并传入sql语句结构
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 5.占位符赋值
        preparedStatement.setObject(1, "李四");
        preparedStatement.setObject(2, 3);
        // 6.发送sql语句
        int rows = preparedStatement.executeUpdate();
        // 7.输出结果
        if (rows > 0) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
        // 8.关闭资源
        preparedStatement.close();
        connection.close();

    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {

        /*
            删除 id = 4的用户
         */

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc", "root", "123456");
        // 3.编写sql语句,动态值使用?替代
        String sql = "delete from tb_user where id = ?";
        // 4.创建preparedStatement,并传入sql语句结构
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 5.占位符赋值
        preparedStatement.setObject(1, 4);
        // 6.发送sql语句
        int rows = preparedStatement.executeUpdate();
        // 7.输出结果
        if (rows > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        // 8.关闭资源
        preparedStatement.close();
        connection.close();

    }

}
