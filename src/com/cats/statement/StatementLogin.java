package com.cats.statement;

import java.sql.*;
import java.util.Scanner;

public class StatementLogin {

    /*
        模拟用户登录
        1. 明确jdbc的使用流程 和 详细讲解内部设计api方法
        2. 发现问题,引出preparedStatement

        输入账号和密码
        进行数据库的查询
        反馈登录成功还是失败

        1.键盘输入事件,收集账号和密码信息
        2. 注册驱动
        3. 获取连接
        4. 创建statement
        5. 发送查询sql语句,并返回结果集
        6.结果判断,显示登录成功还是失败
        7.关闭资源
    */

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // 1. 获取用户信息
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入账号:");
        String account = sc.nextLine();
        System.out.println("请输入密码:");
        String password = sc.nextLine();

        // 2.注册驱动

        /*
            方案一:
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())
                注意: 8+ com.mysql.cj.jdbc.Driver
                     5+ com.mysql.jdbc.Driver
                问题: 注册两次驱动
                        1.DriverManager.registerDriver() 方法本身会注册一次
                        2.Driver.static{ DriverManager.registerDriver() } 静态代码块也会注册一次
                解决: 只想注册一次驱动
                      只触发静态代码块即可! Driver
                触发静态代码块:
                    类加载机制: 类加载的时刻,会触发静态代码块!
                               加载 [class文件 -> jvm虚拟机的class对象]
                               连接 [验证(检查文件类型) -> 准备(静态变量默认值) -> 解析(触发静态代码块)]
                               初始化(静态属性赋真实值)
                触发类加载:
                    1. new关键字
                    2.调用静态方法
                    3.调用静态属性
                    4.接口 jdk1.8 default默认实现
                    5.反射
                    6.子类触发父类
                    7.程序的入口 Mian
        */

        /*
             方案一: DriverManager.registerDriver(new Driver());
             方案二: new Driver();
                注册驱动固定写法! mysql - mysql Driver || 切换了数据库 oracle driver | 还需要改代码
             方案三: 反射字符串的Driver全限字符,可以引导外部的配置文件 -> xx.properties -> oracle -> 配置文件修改
             Class.forName("com.mysql.cj.jdbc.Driver");
        */

        Class.forName("com.mysql.cj.jdbc.Driver");

        // 3.获取数据库连接

        /*
            getConnection( , , ,)是一个重载方法
            允许开发者,用不同的形式传入数据库连接的核心参数

            核心属性:
                1.数据库软件的主机ip地址: localhost | 127.0.0.1
                2.数据库软件所在的主机的端口号: 3306
                3.连接的具体库: xs_jdbc
                4.连接的账号: root
                5.连接的密码: 123456
                6.可选的信息

            三个参数:
                String url          数据库软件所在的信息,连接的具体库,以及其他可选信息
                                    语法: jdbc:数据库管理软件名称[mysql,oracle]://IP地址|主机名:port端口号/数据库名?
                                          key=value&key=value
                                    具体: jdbc:mysql://127.0.0.1:3306/xs_jdbc
                                         jdbc:mysql://localhost:3306/xs_jdbc
                                    本机的省略写法: 如果数据库软件安装到本机,可以进行省略
                                    jdbc:mysql:///xs_jdbc
                                    强调: 必须是本机,并且端口号是3306
                String user     数据库账号 root
                String password 数据库密码 123456

            二个参数:
                String url          与上述一样
                Properties info     存储账号密码

            三个参数:
                String url      数据库ip,端口号,具体的数据库 可选信息(账号密码)
                                jdbc:数据库名称://ip:port/数据库名?key=value&key=value

                                jdbc:mysql://localhost:3306/xs_jdbc?user=root&password=123456
                                携带固定的参数名    user password 传递账号和密码信息 [乌龟的屁股,规定]

            url的路径可选信息:
                url?user=账号&password=密码

                8.0.27版本驱动,下面都是一些可选属性
                8.0.25以后自动识别时区! serverTimezone=Asia/Shanghai 不用添加
                8版本以后,默认使用的就是utf-8  useUnicode=true&characterEncoding=utf-8&useSSL=true 可省
                serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true
        */

        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1/xs_jdbc", "root", "123456");

        /*
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "123456");
            DriverManager.getConnection("jdbc:mysql:///cats_jdb", info);

            DriverManager.getConnection("jdbc:mysql:///cats_jdb?user=root&password=123456");
        */

        // 4.创建发送sql语句的statement对象
        // statement可以发送sql语句到数据库,并且获取到返回结果
        Statement statement = connection.createStatement();

        // 5.发送sql语句(1.编写sql语句 2.发送sql语句)
        // sql语句需要字符串拼接,比较麻烦
        // 只能拼接字符串类型,其他的数据库类型无法处理
        // 可能发生注入攻击
        // ' or '1' = '1
        String sql = "SELECT * FROM tb_user WHERE account = '" + account + "' and password = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(sql);

       /* while(resultSet.next()){
            // 指向当前行了
            int id = resultSet.getInt(1);
            String ac = resultSet.getString(2);
            String pwd = resultSet.getString(3);
            String nick = resultSet.getString(4);
        }
        */

        if(resultSet.next()) {
            System.out.println("登录成功");
        }else {
            System.out.println("登录失败");
        }

        // 6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
