package info.kgeorgiy.ja.Presniakov_Arsenii.bank;

import info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank.Bank;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank.RemoteBank;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;

public class BankServer {
    public static void main(String[] args) {
        if (args == null || args.length != 1 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments are incorrect!");
            return;
        }

        final int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument(port) must be integer: " + e.getMessage());
            return;
        }

        final Bank bank = new RemoteBank(port);
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            UnicastRemoteObject.exportObject(bank, 1234);
            registry.rebind("//localhost/bank", bank);
            System.out.println("Work started");
        } catch (final RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
