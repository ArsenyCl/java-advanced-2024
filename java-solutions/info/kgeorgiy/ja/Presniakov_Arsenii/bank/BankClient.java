package info.kgeorgiy.ja.Presniakov_Arsenii.bank;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank.Bank;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.Person;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Objects;

public class BankClient {
    private static boolean checkNameEquality(String name, String surname, Person person) throws RemoteException {
        if (!person.getName().equals(name)) {
            System.out.println("Person name doesn't match!");
            return false;
        }

        if (!person.getSurname().equals(surname)) {
            System.out.println("Person surname doesn't match!");
            return false;
        }

        return true;
    }

    public static void main(String[] args)  {
        if (args == null || args.length != 5 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments are incorrect!");
            return;
        }

        try {
            final Bank bank;
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                bank = (Bank) registry.lookup("//localhost/bank");
            } catch (final NotBoundException e) {
                System.out.println("Bank is not bound");
                return;
            }

            final String name = args[0];
            final String surname = args[1];
            final String passportId = args[2];
            final String accountId = args[3];
            final int newAmount;
            try {
                newAmount = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format " + e.getMessage());
                return;
            }


            Person person = bank.getPerson(passportId, true);
            if (person == null) {
                System.out.println("Creating person " + passportId);
                person = bank.createPerson(passportId, name, surname);
                System.out.println("Person created");
            } else {
                System.out.println("Person already exists");
                if (!checkNameEquality(name, surname, person)) {
                    return;
                }
            }

            Account account = person.getAccount(accountId);
            if (account == null) {
                System.out.println("Creating account " + accountId);
                account = person.createAccount(accountId);
                System.out.println("Account created");
            } else {
                System.out.println("Account already exists");
            }

            System.out.println("Amount of money in the account: " + account.getAmount());
            System.out.println("Setting new amount of money...");
            account.setAmount(newAmount);
            System.out.println("New amount of money in the account: " + account.getAmount());
        } catch (RemoteException e) {
            System.out.println("Remote exception occurred: " + e.getMessage());
        }
    }
}
