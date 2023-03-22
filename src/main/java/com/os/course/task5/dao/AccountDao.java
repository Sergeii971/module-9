package com.os.course.task5.dao;

import com.os.course.task5.entity.Account;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class AccountDao {
    public void createAccount(Account account) {
        account.setId(UUID.randomUUID().toString());
        try(FileWriter fileWriter = new FileWriter("/data/" + account.getId())) {
            fileWriter.write(account.getId());
            fileWriter.write(account.getLogin());
            fileWriter.write(account.getMoney().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAccount(String id) {
        try {
            List<String> data = Files.readAllLines(Paths.get("/data/" + id));
            Account account = new Account();
            account.setId(data.get(0));
            account.setLogin(data.get(1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
