package com.cats.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BaseDao
 * @Description: TODO:  封装dao数据库重复代码
 * 封装两个方法 一个简化非DQL
 * 一个简化DQL
 * @author: HungryCats
 * @date: 2023/10/14 16:05
 */
public abstract class BaseDao {

    /**
     * 封装简化非DQL语句
     *
     * @param sql    带占位符的SQL语句
     * @param params 占位符的值   注意,传入占位符的值,必须等于SQL语句 ? 的位置
     * @return 执行影响的行数
     * @throws SQLException 异常
     */

    public int executeUpdate(String sql, Object... params) throws SQLException {

        // 获取连接
        Connection connection = JDBCUtils.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        // 可变的参数可以当作数组使用
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        int rows = preparedStatement.executeUpdate();

        preparedStatement.close();

        // 判断是否开启事务
        // connection.setAutoCommit(false); 开启了事务不要关闭连接,业务层处理!
        if (connection.getAutoCommit()) {
            // 没有开启事务,正常回收连接
            JDBCUtils.freeConnection();
        }
        return rows;
    }

    /*
        非DQL语句封装方法 -> 返回值 固定为int
        DQL -> List<Map> -> 一行 -> map -> List<Map>
        DQL语句封装方法 -> 返回值是什么类型?? -> List<T>
            并不是List<Map> map key和value自定义,不用先设定好!
                           但 map 没有数据校验机制
                              map 也不支持反射操作
            一般情况: 数据库数据 -> java的实体类
            表 -> 一行 -> java类的一个对象 -> 多行 -> List<java实体类> list;
        <T> 声明一个泛型,不确定类型
            1.确定类型 User.class T = User
            2.要使用反射技术属性赋值
        public <T> List<T> executeQuery(Class<T> tClass, String sql, Object... params)
    */

    /**
     * 将查询结果封装到一个实体类集合
     * @param tClass    要接值的实体类集合的模板对象
     * @param sql   查询语句,要求列名或者别名等于实体类的属性名
     * @param params    占位符的值 要和?位置对象传递
     * @return  查询的实体类集合
     * @param <T>   声明的结果的类型!
     * @throws Exception 异常
     */

    public <T> List<T> executeQuery(Class<T> tClass, String sql, Object... params) throws Exception {

        // 获取连接
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (params != null && params.length != 0) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            T t = tClass.newInstance(); // 调用类的无参构造实例化对象!
            for (int i = 1; i <= columnCount; i++) {
                // 对象的属性值
                Object value = resultSet.getObject(i);
                // 获取指定列下角标的列的名称! ResultSetMetaData
                String propertyName = metaData.getColumnLabel(i);
                // 反射,给对象的属性赋值
                Field field = tClass.getDeclaredField(propertyName);
                field.setAccessible(true); // 属性值可以设置,打破private的修饰限制
                /*
                    参数一: 要赋值的对象 如果属性是静态,第一个参数可以为null
                    参数二: 具体的属性值
                */
                field.set(t, value);
            }
            list.add(t);
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();

        if (connection.getAutoCommit()) {
            // 没有事务 可以关闭
            JDBCUtils.freeConnection();
        }

        return list;
    }
}
