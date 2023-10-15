package com.cats.preparedstatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * @ClassName: PreStaLogin
 * @Description: 防止注入攻击,演示preparedStatement使用流程
 * @author: HungryCats
 * @date: 2023/10/13 15:38
 */
public class PreStaLogin {
    public static void main(String[] args) throws Exception {
        // 1. 获取用户信息
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入账号:");
        String account = sc.nextLine();
        System.out.println("请输入密码:");
        String password = sc.nextLine();

        // preparedStatement
        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///xs_jdbc?user=root&password=123456");

        /*
            statement
                1.创建statement
                2.拼接sql语句
                3.发送sql语句,并且获取返回结果

            preparedStatement
                1.编写sql语句结果 不包含动态值部分的语句,动态部分使用占位符 ? 替代      注意!! ? 只能替代动态值
                2.创建preparedStatement,并且传入动态值
                3.动态值 占位符 赋值 ? 单独赋值即可
                4.发送sql语句即可,并返回结果
         */

        // 3.编写sql语句结果
        String sql = "SELECT * FROM tb_user where account = ? AND password = ?;";

        // 4.创建预编译preparedStatement并且设置sql语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5.单独的占位符进行赋值
        // 参数一: index 占位符的位置, 参数二: object 占位符的值
        preparedStatement.setObject(1,account);
        preparedStatement.setObject(2,password);

        // 6.发送sql语句,并且获取返回结果
        // statement.executeUpdate | executeQuery(String sql)
        // preparedStatement.executeUpdate | executeQuery()     !!! 因为已经知道语句和语句动态值
        ResultSet resultSet = preparedStatement.executeQuery();

        // 7.结果集解析
        if (resultSet.next()){
            System.out.println("登录成功");
        }else {
            System.out.println("登录失败");
        }

        // 8. 关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
