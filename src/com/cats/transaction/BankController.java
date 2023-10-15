package com.cats.transaction;

/**
 * @ClassName: BankController
 * @Description: TODO 调用service
 * @author: HungryCats
 * @date: 2023/10/14 10:02
 */
public class BankController {
    public static void main(String[] args) throws Exception {
        BankService bankService = new BankService();
        bankService.transfer("张三","admin",500);
    }
}

/*
    事务概念:
        数据库中的事务是指对数据库执行一批操作，
        在同一个事务当中，这些操作最终要么全部执行成功，要么全部失败，不会存在部分成功的情况。
            举个例子：
                比如A用户给B用户转账100操作，过程如下：
                从A账户扣100
                给B账户加100
                如果在事务的支持下，上面最终只有2种结果：
                操作成功：A账户减少100；B账户增加100
                操作失败：A、B两个账户都没有发生变化
                如果没有事务的支持，可能出现错：A账户减少了100，此时系统挂了，导致B账户没有加上100，而A账户凭空少了100。
    事务特性:
        1.原子性(Atomicity):
                事务的整个过程如原子操作一样，最终要么全部成功，或者全部失败，
                这个原子性是从最终结果来看的，从最终结果来看这个过程是不可分割的。
        2.一致性(Consistency):
                一个事务必须使数据库从一个一致性状态变换到另一个一致性状态。
        3.隔离性(Isolation):
                一个事务的执行不能被其他事务干扰。(隔离级别)
        4.持久性(Durability):
                一个事务一旦提交，他对数据库中数据的改变就应该是永久性的。
                当事务提交之后，数据会持久化到硬盘，修改是永久性的。
    事务类型: 自动提交 手动提交

    jdbc中的事务：
        try {
            // 开启事务 | 关闭事务提交
            connection.setAutoCommit(false);
            // 执行数据库操作
            // 事务提交
            connection.commit();
        } catch (Exception e) {
            // 事务回滚
            connection.rollback();
            throws e;
        }finally {
            connection.close();
        }
*/
