package info.kgeorgiy.ja.Presniakov_Arsenii.bank.person;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.AccountImpl;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson extends AbstractPerson  {


    public LocalPerson(Person person) throws RemoteException {

        super(person.getPassportID(),
                person.getName(),
                person.getSurname(),
                copyConcurrentMap(person.getAccounts())
        );
    }

    private static ConcurrentMap<String, Account> copyConcurrentMap(ConcurrentMap<String, Account> map) throws RemoteException {
        ConcurrentMap<String, Account> res = new ConcurrentHashMap<>();
        for (var each : map.entrySet()) {
            res.put(each.getKey(), new AccountImpl(each.getValue()));
        }
        return res;
    }


    @Override
    public Account createAccount(String subId) {
        return super.accounts.computeIfAbsent(subId, e -> new AccountImpl(toFullId(subId)));
    }
}
