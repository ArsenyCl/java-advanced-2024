package info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.LocalPerson;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.Person;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.RemotePerson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {

    private final int port;
    private final ConcurrentMap<String, RemotePerson> persons = new ConcurrentHashMap<>();

    public RemoteBank(int port) {
        this.port = port;
    }

    @Override
    public Account getAccount(String id) throws RemoteException {
        String[] passportIdAndSubId = id.split(":");
        Person person = getPerson(passportIdAndSubId[0], true);
        if (person == null) {
            System.out.println("No such person with passport ID: " + passportIdAndSubId[0]);
            return null;
        }
        return person.getAccount(passportIdAndSubId[1]);
    }

    @Override
    public Person getPerson(String passportId, boolean isRemote) throws RemoteException {
        RemotePerson person = persons.get(passportId);
        if (Objects.isNull(person)) {
            return null;
        }

        return isRemote ? person : new LocalPerson(person);
    }

    @Override
    public Person createPerson(String passportId, String name, String surname) throws RemoteException {
        RemotePerson person = new RemotePerson(passportId, name, surname, port);
        if (Objects.isNull(persons.putIfAbsent(passportId, person))) {
            UnicastRemoteObject.exportObject(person, port);
            return person;
        } else {
            Person getPerson = getPerson(passportId, true);
            if (person.equals(getPerson)) {
                return person;
            } else {
                return null;
            }
        }
    }
}
