package info.kgeorgiy.ja.Presniakov_Arsenii.bank.account;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Account extends Remote {

    /**
     * Returns ID of the account
     *
     * @return {@link String} ID of the account
     */
    String getId() throws RemoteException;


    /**
     * Returns amount of money in the account
     *
     * @return amount of money in the account
     */

    int getAmount() throws RemoteException;

    /**
     * Set amount of money in the account
     */
    void setAmount(int amount) throws RemoteException;
}
