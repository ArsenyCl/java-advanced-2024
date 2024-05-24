package info.kgeorgiy.ja.Presniakov_Arsenii.bank;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.account.Account;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank.Bank;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.bank.RemoteBank;
import info.kgeorgiy.ja.Presniakov_Arsenii.bank.person.Person;
import org.junit.jupiter.api.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    private static final String BANK_URL = "//localhost/bank";

    private static final Random rng = new Random(228225);
    private static Registry registry;
    private Bank bank;


    @BeforeAll
    public static void createRegistry() throws RemoteException {
        registry = LocateRegistry.createRegistry(1099);
    }


    @BeforeEach
    public void startServer() throws RemoteException {
        bank = new RemoteBank(1234);
        UnicastRemoteObject.exportObject(bank, 1234);
        registry.rebind("//localhost/bank", bank);
        System.out.println("Work started");
    }

    @AfterEach
    public void stopServer() throws RemoteException, NotBoundException {
        UnicastRemoteObject.unexportObject(bank, true);
        registry.unbind("//localhost/bank");
        System.out.println("Work stopped");
    }



    @Test
    public void test1_CreateSinglePerson() throws RemoteException {
        createRandomPerson();
    }

    @Test
    public void test2_CreateSingleAccount() throws RemoteException {
        createRandomAccount(createRandomPerson());
    }


    @Test
    public void test3_CheckPersonEqualities() throws RemoteException {
        Person person = createRandomPerson();
        Person getPerson = bank.getPerson(person.getPassportID(), true);
        Person badPerson = bank.createPerson(person.getPassportID(), "plohoeimya228", "plohayafamiliya228");
        Person goodPerson = bank.createPerson(person.getPassportID(), person.getName(), person.getSurname());
        assertEquals(person, getPerson);
        assertEquals(person, goodPerson);
        assertNull(badPerson);
    }

    @Test
    public void test4_GetAccount() throws RemoteException {
        Person person = createRandomPerson();
        Account account = createRandomAccount(person);
        Account getAccount = bank.getAccount(account.getId());
        assertEquals(account, getAccount);
        assertTrue(getAccount.getId().contains(person.getPassportID()));
    }


    @Test
    public void test5_ChangeAmount() throws RemoteException {
        Person person = createRandomPerson();
        Account account = createRandomAccount(person);
        Account getAccount = bank.getAccount(account.getId());
        getAccount.setAmount(1000);
        assertEquals(1000, account.getAmount());
    }

    @Test
    public void test6_CreateManyAccounts() throws RemoteException {
        Person person = createRandomPerson();
        for (int i = 0; i < 10; i++) {
            person.createAccount(String.valueOf(i)).setAmount(10 * i);
        }

        assertEquals(10, person.getAccounts().size());

        for (int i = 0; i < 10; i++) {
            assertEquals(10 * i, person.getAccount(String.valueOf(i)).getAmount());
            assertEquals(10 * i, bank.getAccount(person.getPassportID() + ":" + i).getAmount());
        }
    }

    @Test
    public void test7_LocalPerson() throws RemoteException {
        Person person = createRandomPerson();
        Account account = person.createAccount("12345");
        account.setAmount(100);
        Person localPerson = bank.getPerson(person.getPassportID(), false);
        Account localAccount = localPerson.getAccount("12345");
        assertEquals(100, localAccount.getAmount());
        account.setAmount(200);
        assertEquals(100, localAccount.getAmount());
        localAccount.setAmount(300);
        assertEquals(300, localAccount.getAmount());
        assertEquals(200, account.getAmount());
        createRandomAccount(localPerson);
        assertNotEquals(localPerson.getAccounts().size(), person.getAccounts().size());
    }

    @Test
    public void test8_ManyPersons() throws RemoteException {
        int num = 1000;
        int threads = 10;
        int accounts = 5;
        try(ExecutorService workers = Executors.newFixedThreadPool(threads)) {
            CountDownLatch timer = new CountDownLatch(num);
            IntStream.range(1, num + 1).forEach(i ->
                workers.submit(() -> {
                    try {
                        Person person = bank.createPerson(String.valueOf(i), randomEnglishString(5), randomEnglishString(10));

                        for (int j = 1; j <= accounts; j++) {
                            person.createAccount(String.valueOf(j)).setAmount(i * j);
                            assertEquals(bank.getAccount(person.getPassportID() + ":" + j).getAmount(), i * j);
                        }
                        assertEquals(person, bank.getPerson(person.getPassportID(), true));

                    } catch (RemoteException e) {
                        System.err.println("Remote exception: " + e.getMessage());
                    } finally {
                        timer.countDown();
                    }
                })
            );
            timer.await();
        } catch(InterruptedException e) {
            System.err.println("Interrupted exception in many persons: " + e);
        }
    }

    private Account createRandomAccount(Person person) throws RemoteException {
        return person.createAccount(String.valueOf(rng.nextLong(1, Long.MAX_VALUE)));
    }

    private Person createRandomPerson() throws RemoteException {
        return bank.createPerson(String.valueOf(rng.nextLong(1, Long.MAX_VALUE)), randomEnglishString(5), randomEnglishString(10));
    }

    private static String randomEnglishString(int size) {
        return rng.ints(size, 'a', 'z' ).boxed().map(Character::toString).collect(Collectors.joining(""));
    }
}
