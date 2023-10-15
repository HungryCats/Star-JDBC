package com.cats.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName: DruidDemo
 * @Description: TODO druid使用
 * @author: HungryCats
 * @date: 2023/10/14 10:57
 */

public class DruidDemo {

    /**
     * 直接使用代码设置连接池连接参数方式
     * 1.创建一个druid连接池对象
     * 2.设置连接池参数 [必须 | 非必须]
     * 3.获取连接 [通用方法,所有连接池都一样]
     * 4.回收连接 [通用方法, 所有连接池都一样]
     */

    public void hardTest() throws SQLException {
        // 连接池对象
        DruidDataSource dataSource = new DruidDataSource();

        // 连接池参数设置
        // 必须 连接数据库驱动类的权限定字符[注册驱动] url | user | password
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // 帮助我们进行驱动注册和获取连接
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/xs_jdbc");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        // 非必须 初始化连接池数量,最大的连接数量...
        dataSource.setInitialSize(5); // 初始化连接数量
        dataSource.setMaxActive(10); // 最大的数量

        // 获取连接
        Connection connection = dataSource.getConnection();

        // 数据库的CRUD操作

        // 回收连接
        connection.close(); // 连接池提供的回收,close()就是回收连接
    }
    
    public void testSoft() throws Exception {

        // 1.读取外部配置文件 Properties
        Properties properties = new Properties();

        // src下的文件,可以使用类加载器提供的方法
        InputStream ips = DruidDemo.class.getClassLoader().getResourceAsStream("druid.properties");
        properties.load(ips);

        // 2.使用连接池的工具类的工厂模式,创建连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();

        // 数据库的CRUD操作

        connection.close();
    }
}

/*
    JDBC的数据库池使用 javax.sql.DataSource接口进行规范,所有的第三方连接池都实现此接口

    总结:
        1.这种模式开发，存在的问题:
            （1）普通的JDBC数据库连接使用 DriverManager 来获取，每次向数据库建立连接的时候都要将 Connection加载到内存中，再验证用户名和密码(得花费0.05s～1s的时间)。需要数据库连接的时候，就向数据库要求一个，执行完成后再断开连接。这样的方式将会消耗大量的资源和时间。数据库的连接资源并没有得到很好的重复利用。若同时有几百人甚至几千人在线，频繁的进行数据库连接操作将占用很多的系统资源，严重的甚至会造成服务器的崩溃。
            （2）对于每一次数据库连接，使用完后都得断开。否则，如果程序出现异常而未能关闭，将会导致数据库系统中的内存泄漏，最终将导致重启数据库。（回忆：何为Java的内存泄漏？）
            （3）这种开发不能控制被创建的连接对象数，系统资源会被毫无顾及的分配出去，如连接过多，也可能导致内存泄漏，服务器崩溃
        2.数据库连接池的基本思想：
            就是为数据库连接建立一个“缓冲池”。预先在缓冲池中放入一定数量的连接，当需要建立数据库连接时，只需从“缓冲池”中取出一个，使用完毕之后再放回去。
            数据库连接池负责分配、管理和释放数据库连接，它允许应用程序重复使用一个现有的数据库连接，而不是重新建立一个。

        3.数据库连接池技术的优点：
            （1）资源重用
                由于数据库连接得以重用，避免了频繁创建，释放连接引起的大量性能开销。
                在减少系统消耗的基础上，另一方面也增加了系统运行环境的平稳性。

            （2）更快的系统反应速度
                数据库连接池在初始化过程中，往往已经创建了若干数据库连接置于连接池中备用。此时连接的初始化工作均已完成。
                对于业务请求处理而言，直接利用现有可用连接，避免了数据库连接初始化和释放过程的时间开销，从而减少了系统的响应时间

            （3）新的资源分配手段
                对于多应用共享同一数据库的系统而言，可在应用层通过数据库连接池的配置，
                实现某一应用最大可用数据库连接数的限制，避免某一应用独占所有的数据库资源

            （4）统一的连接管理，避免数据库连接泄漏
                在较为完善的数据库连接池实现中，可根据预先的占用超时设定，强制回收被占用连接，
                从而避免了常规数据库连接操作中可能出现的资源泄露
*/