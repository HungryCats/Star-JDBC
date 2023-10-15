package com.cats.preparedstatement;

import org.junit.Test;

import java.sql.*;

/**
 * @ClassName: PreStaOther
 * @Description: TODO 主键回显和主键值获取 && 批量插入性能提升
 * @author: HungryCats
 * @date: 2023/10/14 8:25
 */
public class TestPreStaOther {

    /**
     * 主键回显和主键值获取
     *
     * @throws Exception 抛异常
     */

    @Test
    public void fetchPrimaryKey() throws Exception {

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc", "root", "123456");
        // 3.编写sql
        String sql = "insert into tb_user (account,password,nickname) values (?,?,?)";
        // 4.创建statement时,告知,携带数据库自增长的主键值(sql,Statement.RETURN_GENERATED_KEYS)
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        // 5.占位符赋值
        preparedStatement.setObject(1, "other");
        preparedStatement.setObject(2, "other123");
        preparedStatement.setObject(3, "普通用户");
        // 6.发送sql语句,获取结果集
        int rows = preparedStatement.executeUpdate();
        // 7.结果集解析
        if (rows > 0) {
            System.out.println("插入成功");
            // 获取当前的数据库的主键值,一行一列 调用 resultSet.next()
            // 获取完后,调用rs.getObject获取其ID值
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                System.out.println("获取主键后的数据库主键值是:" + resultSet.getObject(1));
            }
        } else {
            System.out.println("插入失败");
        }
        // 8.关闭资源
        preparedStatement.close();
        connection.close();
    }

    /**
     * 使用普通方式插入10000条数据
     *
     * @throws Exception 抛异常
     */

    @Test
    public void testInsert() throws Exception {

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc", "root", "123456");
        // 3.编写sql
        String sql = "insert into tb_user (account,password,nickname) values (?,?,?)";
        // 4.创建statement时,告知,携带数据库自增长的主键值(sql,Statement.RETURN_GENERATED_KEYS)
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        long start = System.currentTimeMillis();

        // 5.占位符赋值
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setObject(1, "otherInsert" + 1);
            preparedStatement.setObject(2, "other123" + 1);
            preparedStatement.setObject(3, "普通用户" + 1);
            // 6.发送sql语句,获取结果集
            preparedStatement.executeUpdate();
        }
        long end = System.currentTimeMillis();

        // 7.结果集解析
        System.out.println("执行一万次消耗的时间为: " + (end - start));
        // 8.关闭资源
        preparedStatement.close();
        connection.close();
    }


    /**
     * 使用批量插入的方式插入 10000条数据
     * 细节:
     *      1.url?rewriteBatchedStatements=true
     *      2.insert 语句必须使用values
     *      3.语句后面不能添加分号
     *      4.不是执行每条语句,时批量添加 addBatch()
     *          遍历完毕 最后 executeBatch();
     * @throws Exception 抛异常
     */

    @Test
    public void testBatchInsert() throws Exception {

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xs_jdbc?rewriteBatchedStatements=true", "root", "123456");
        // 3.编写sql
        String sql = "insert into tb_user (account,password,nickname) values (?,?,?)";
        // 4.创建statement时,告知,携带数据库自增长的主键值(sql,Statement.RETURN_GENERATED_KEYS)
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        long start = System.currentTimeMillis();

        // 5.占位符赋值
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setObject(1, "otherBatchInsert" + 1);
            preparedStatement.setObject(2, "otherBatch123" + 1);
            preparedStatement.setObject(3, "普通用户" + 1);
            // 不执行,追加到values后面
            preparedStatement.addBatch();
        }

        // 执行批量操作
        preparedStatement.executeBatch();

        long end = System.currentTimeMillis();

        // 7.结果集解析
        System.out.println("执行一万次消耗的时间为: " + (end - start));
        // 8.关闭资源
        preparedStatement.close();
        connection.close();
    }

}
