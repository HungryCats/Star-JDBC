package com.cats.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
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
 *  TODO:
 *      利用线程本地变量,存储连接信息! 确保一个线程的多个方法可以获取同一个connection!
 *      优势: 事务操作的时候 service 和 dao 属于同一个线程,不再传递参数了!
 *      大家都可以调用 getConnection自动获取的是相同的连接池
 *
 * @author: HungryCats
 * @date: 2023/10/14 14:47
 */

public class JDBCUtils {

    private static final DataSource dataSource; // 连接池对象

    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    static {
        // 初始化连接池对象
        Properties properties = new Properties();
        InputStream ips = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            properties.load(ips);
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

        // 线程本地变量中是否存在
        Connection connection = threadLocal.get();

        // 第一次没有
        if (connection == null) {
            // 线程本地变量没有,连接池获取
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

    public static void freeConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection != null){
            threadLocal.remove(); // 清空线程本地变量
            connection.setAutoCommit(true); // 事务状态回归 false
            connection.close(); // 回收到连接池即可
        }
    }
}
