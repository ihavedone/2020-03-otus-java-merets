/*
 * Copyright (c) 2020 Artem Merets
 */

package ru.otus.merets.department;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.merets.atm.ATM;
import ru.otus.merets.atm.ATMImpl;
import ru.otus.merets.atm.CassetteImpl;
import ru.otus.merets.banknote.Banknotes;
import ru.otus.merets.banknote.BundleOfMoneyImpl;
import ru.otus.merets.department.command.ReportBalance;
import ru.otus.merets.department.command.Rollback;

@DisplayName("Department must ")
public class DepartmentTest {
    private Department department;

    @BeforeEach
    public void init() {
        ATM atm1 = new ATMImpl.Builder()
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_5000).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_2000).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_1000).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_500).withSize(100).build())
                .build();

        ATM atm2 = new ATMImpl.Builder()
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_500).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_200).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_1000).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_5000).withSize(100).build())
                .build();

        ATM atm3 = new ATMImpl.Builder()
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_100).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_100).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_100).withSize(100).build())
                .withCassette(new CassetteImpl.Builder(Banknotes.BANKNOTE_500).withSize(100).build())
                .build();

        department = new Department();
        department.addListener(atm1);
        department.addListener(atm2);
        department.addListener(atm3);
    }

    @Test
    @DisplayName(" collect summary balance")
    public void onBalanceTest() {
        final Integer[] sum = {0};
        department.event(new ReportBalance(), (p) -> sum[0] += (Integer) p);

        Assertions.assertEquals(
                department.get(0).getBalance()
                        + department.get(1).getBalance()
                        + department.get(2).getBalance(),
                sum[0]
        );
    }

    @Test
    @DisplayName(" rollback all ATMs to the start point")
    public void onRollbackTest() {
        final Integer[] initialSum = {0};
        department.event(new ReportBalance(), (p) -> initialSum[0] += (Integer) p);

        department.get(0).giveMoney(5500);
        department.get(1).giveMoney(1500);
        department.get(2).giveMoney(2500);

        department.get(0).takeMoney(new BundleOfMoneyImpl.Builder(null)
                .withBanknotes(Banknotes.BANKNOTE_500, 3)
                .build());

        final Integer[] intermediateSum = {0};
        department.event(new ReportBalance(), (p) -> intermediateSum[0] += (Integer) p);

        Assertions.assertEquals(true, initialSum[0] - intermediateSum[0] > 0);

        department.event(new Rollback(), (p) -> System.out.println(p));

        final Integer[] finalSum = {0};
        department.event(new ReportBalance(), (p) -> finalSum[0] += (Integer) p);

        Assertions.assertEquals(initialSum[0], finalSum[0]);
    }


}
