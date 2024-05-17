package info.kgeorgiy.ja.Presniakov_Arsenii.bank.person;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractPerson implements Person, Serializable {

    private final String passportId;
    private final String name;
    private final String surname;
    protected final ConcurrentMap<String, Account> accounts;


    AbstractPerson(String passportId, String name, String surname, ConcurrentMap<String, Account> accounts) {
        this.passportId = passportId;
        this.name = name;
        this.surname = surname;
        this.accounts = accounts;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSurname()  {
        return this.surname;
    }

    @Override
    public String getPassportID() {
        return this.passportId;
    }

    @Override
    public ConcurrentMap<String, Account> getAccounts() {
        return accounts;
    }

    protected String toFullId(String subId) {
        return passportId + ":" + subId;
    }

    @Override
    public Account getAccount(String subId)  {
        return accounts.get(subId);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AbstractPerson
                && ((AbstractPerson) o).passportId.equals(this.passportId)
                && ((AbstractPerson) o).name.equals(this.name)
                && ((AbstractPerson) o).surname.equals(this.surname)
                && ((AbstractPerson) o).accounts.equals(this.accounts);
    }
}
