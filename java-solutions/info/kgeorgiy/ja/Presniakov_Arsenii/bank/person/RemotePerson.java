package info.kgeorgiy.ja.Presniakov_Arsenii.bank.person;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.AccountImpl;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class RemotePerson extends AbstractPerson {
    private final int port;

    public RemotePerson(String passportId, String name, String surname, int port) {
        super(passportId, name, surname, new ConcurrentHashMap<>());
        this.port = port;
    }



    @Override
    public Account createAccount(String subId) throws RemoteException {
        Account account = new AccountImpl(toFullId(subId));
        if (super.accounts.put(subId, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(subId);
        }
    }
}
