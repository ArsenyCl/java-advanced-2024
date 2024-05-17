package info.kgeorgiy.ja.Presniakov_Arsenii.bank.person;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public interface Person extends Remote {
    /**
     * Returns name of the person
     *
     * @return {@link String} associated with the name of the person
     */
    String getName() throws RemoteException;

    /**
     * Returns surname of the person
     *
     * @return {@link String} associated with the surname of the person
     */
    String getSurname() throws RemoteException;

    /**
     * Returns passportId of the person
     *
     * @return {@link String} associated with the passportId of the person
     */
    String getPassportID() throws RemoteException;

    /**
     * Returns the map of accounts associated with the person
     *
     * @return {@link ConcurrentMap} associated with the person accounts
     */
    ConcurrentMap<String, Account> getAccounts() throws RemoteException;

    /**
     * Creates a new account associated with the person specified by subId
     * if there is no account with such subId, otherwise returns existing account
     *
     * @param subId {@link String} subId of the account
     * @return new {@link Account} associated with the person
     */
    Account createAccount(String subId) throws RemoteException;

    /**
     * Returns account associated with the person specified by subId.
     * If there is no account with such subId returns null
     *
     * @param subId {@link String} subId of the account
     * @return {@link Account} associated with the person specified by subId
     */
    Account getAccount(String subId) throws RemoteException;
}
