package ru.otus.merets.services;

import org.springframework.stereotype.Service;
import ru.otus.merets.core.model.User;
import ru.otus.merets.core.service.DBServiceUser;

@Service
public class DBInitServiceImpl implements DBInitService {
    private final DBServiceUser dbServiceUser;

    public DBInitServiceImpl(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public void initStorageWithData(){
        User artem = new User.Builder("Artem")
                .withAge(30)
                .withPhone("+70001112233")
                .withPhone("+70001112244")
                .withAddress("Varshavskoe highway")
                .withPassword("123456")
                .build();
        User elvira = new User.Builder("Elvira")
                .withAge(30)
                .withAddress("Varshavskoe highway")
                .withPassword("123456")
                .build();
        User dmitry = new User.Builder("Dmitry")
                .withAge(4)
                .withAddress("Varshavskoe highway")
                .withPassword("123456")
                .build();
        User maxim = new User.Builder("Maxim")
                .withAge(1)
                .withAddress("Varshavskoe highway")
                .withPassword("123456")
                .build();
        dbServiceUser.saveUser( artem );
        dbServiceUser.saveUser( elvira );
        dbServiceUser.saveUser( dmitry );
        dbServiceUser.saveUser( maxim );
    }
}
