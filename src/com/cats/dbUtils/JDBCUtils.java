package com.cats.dbUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Author: HungryCats
 * @Description: JDBC封装
 * @Date: 2023-11-03 8:59
 */
public class JDBCUtils {

    static String driverClass;
    static String url;
    static String username;
    static String password;

    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    static {
        // 初始化连接池对象
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream("src/resources/druid.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverClass = properties.getProperty("driverClassName");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    /**
     * 对外提供连接的方法
     * @return 返回连接
     */
    public static Connection getConnection() {

        // 线程本地变量中是否存在
        Connection connection = threadLocal.get();

        // 第一次没有
        if (connection == null) {
            // 线程本地变量没有,连接池获取
            try {
                Class.forName(driverClass);
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            threadLocal.set(connection);
        }
        return connection;
    }

    public static void freeConnection() {
        Connection connection = threadLocal.get();
        if (connection != null) {
            threadLocal.remove(); // 清空线程本地变量
            try {
                connection.setAutoCommit(true); // 事务状态回归 false
                connection.close(); // 回收到连接池即可
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
