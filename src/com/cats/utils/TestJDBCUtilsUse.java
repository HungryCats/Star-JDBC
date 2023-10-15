package com.cats.utils;

import org.junit.Test;

import java.sql.*;


/**
 * @ClassName: PreStaCRUD
 * @Description: 使用preparedStatement进行增删改查操作
 * @author: HungryCats
 * @date: 2023/10/13 16:17
 */

public class TestJDBCUtilsUse extends BaseDao{

    @Test
    public void testInsert() throws SQLException {

        // 3.编写sql语句,动态值用?代替
        String sql = "insert into tb_user (account,password,nickname) values (?,?,?);";

        int rows = executeUpdate(sql, "simp", "simp123", "普通用户");

        if (rows > 0) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    @Test
    public void testUpdate() throws SQLException {

        // 3.编写sql语句,动态值使用?替代
        String sql = "update tb_user set password = ? where id = ?";
        int rows = executeUpdate(sql, "123", 3);
        // 7.输出结果
        if (rows > 0) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    @Test
    public void testDelete() throws SQLException {

        // 3.编写sql语句,动态值使用?替代
        String sql = "delete from tb_user where id = ?";
        int rows = executeUpdate(sql, 6);
        // 7.输出结果
        if (rows > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }

}
