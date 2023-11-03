package com.cats.dbUtils;

import com.cats.dbUtils.dao.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author: HungryCats
 * @Description: DBUtils工具类的QueryRunner的使用
 * @Date: 2023-11-03 8:34
 */
public class QueryRunnerTest {

    /*
        BeanHandler<>:是ResultHandler接口的实现类,用于封装表中的一条记录
     */

    @Test
    public void testQuery1() {
        QueryRunner runner = new QueryRunner();
        Connection con = JDBCUtils.getConnection();
        String sql = "select id ,account,password,nickname from tb_user where id = ?";
        BeanHandler<User> handler = new BeanHandler<>(User.class);
        User user = null;
        try {
            user = runner.query(con, sql, handler, 1);
            System.out.println(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.freeConnection();
        }
    }

    /*
        BeanListHandler<>:是ResultHandler接口的实现类,用于封装表中的多条条记录
     */
    @Test
    public void testQuery2() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,account,password,nickname from tb_user where id < ?";
        BeanListHandler<User> handler = new BeanListHandler<>(User.class);
        try {
            List<User> list = runner.query(conn, sql, handler, 3);
            list.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.freeConnection();
        }
    }

    /*
        MapHandler<>:是ResultHandler接口的实现类,对应表中的一条数据.
        将字段及相应字段的值作为map中的key和value
     */
    @Test
    public void testQuery3() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,account,password,nickname from tb_user where id = ?";
        MapHandler handler = new MapHandler();
        try {
            Map<String, Object> map = runner.query(conn, sql, handler, 3);
            System.out.println(map);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.freeConnection();
        }
    }


    /*
        MapListHandler<>:是ResultHandler接口的实现类,对应表中的多条数据.
        将字段及相应字段的值作为map中的key和value. 将这些map添加到List中
     */
    @Test
    public void testQuery4() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,account,password,nickname from tb_user where id < ?";
        MapListHandler handler = new MapListHandler();
        try {
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 4);
            mapList.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.freeConnection();
        }
    }

    /*
        ScalarHandler:用于查询特殊值
     */
    @Test
    public void testQuery5() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "select count(*) from tb_user";
        ScalarHandler<Object> handler = new ScalarHandler<>();
        try {
            Object count = runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.freeConnection();
        }
    }

}
