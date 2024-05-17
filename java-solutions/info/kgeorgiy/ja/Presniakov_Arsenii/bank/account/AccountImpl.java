package info.kgeorgiy.ja.Presniakov_Arsenii.bank.account;

import java.rmi.RemoteException;

public class AccountImpl implements Account {

    private final String fullId;
    private int balance;

    public AccountImpl(String fullId) {
        this.fullId = fullId;
        this.balance = 0;
    }

    public AccountImpl(Account account) throws RemoteException {
        this.fullId = account.getId();
        this.balance = account.getAmount();
    }

    @Override
    public String getId() {
        return this.fullId;
    }

    @Override
    public synchronized int getAmount()  {
        return this.balance;
    }

    @Override
    public synchronized void setAmount(int amount)  {
        this.balance = amount;
    }

    @Override
    public boolean equals(Object o)  {
        return o instanceof Account
                && ((AccountImpl) o).getAmount() == this.getAmount()
                && ((AccountImpl) o).getId().equals(this.getId());
    }
}
