package info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.Person;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    /**
     * Returns account by account ID
     *
     * @param id ID of account
     * @return {@link Account} associated with specified ID
     */
    Account getAccount(String id) throws RemoteException;

    /**
     * Returns person by passport ID
     *
     * @param passportId passport ID of the person
     * @return {@link Person} associated with specified passport ID
     */
    Person getPerson(String passportId, boolean isRemote) throws RemoteException;

    /**
     * Creates and returns person with specified passport ID, name and surname
     *
     * @param passportId passport ID of the person
     * @param surname    surname of the person
     * @param name       name of the person
     * @return created {@link Person}
     */
    Person createPerson(String passportId, String name, String surname) throws RemoteException;
}
