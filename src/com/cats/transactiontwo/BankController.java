package com.cats.transactiontwo;

public class BankController {
    public static void main(String[] args) throws Exception {
        BankService bankService = new BankService();
        bankService.transfer("张三","admin",500);
    }
}
