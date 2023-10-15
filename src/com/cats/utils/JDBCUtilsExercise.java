package com.cats.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName: JDBCUtils
 * @Description:
 *      v1.0版本工具类
 *          内部包含一个连接池对象,并对外提供获取连接和回收连接的方法!
 *      小建议: 工具类的方法,推荐写成静态,外部调用也会更加方便
 *      实现:
 *          属性 连接对象 [实例化一次]
 *              单例模式
 *              static {
 *                  全局调用一次
 *              }
 *      方法:
 *          对外提供连接的方法
 *          回收外部传入连接方法
 *
 * @author: HungryCats
 * @date: 2023/10/14 14:47
 */

public class JDBCUtilsExercise {

    private static final DataSource dataSource; // 连接池对象

    static {
        // 初始化连接池对象
        Properties properties = new Properties();
        InputStream ips = JDBCUtilsExercise.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            properties.load(ips);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对外提供连接的方法
     * @return 返回连接
     */
    public static Connection getConnection() throws SQLException {
        // 不能保证同一个线程,两次getConnection()得到的不是同一个
        // 如果不能保证是同一个连接对象,就无法保证事务的管理
        return dataSource.getConnection();
    }

    public static void freeConnection(Connection connection) throws SQLException {
        connection.close(); // 回收
    }
}
