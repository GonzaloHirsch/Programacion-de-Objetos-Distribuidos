package ar.edu.itba.pod.concurrency.example.liveness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Deadlock example with bank account.d
 */
public class BankAccountSynchDeadLock {
    double balance;
    int id;

    BankAccountSynchDeadLock(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    void withdraw(double amount) {
        // Wait to simulate io like database access ...
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance -= amount;
    }

    void deposit(double amount) {
        // Wait to simulate io like database access ...
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance += amount;
    }

    static void transfer(BankAccountSynchDeadLock from, BankAccountSynchDeadLock to, double amount) {
        synchronized (from) {
            from.withdraw(amount);
            synchronized (to) {
                to.deposit(amount);
            }
        }
    }

    public static void main(String[] args) {
        final BankAccountSynchDeadLock fooAccount = new BankAccountSynchDeadLock(1, 100d);
        final BankAccountSynchDeadLock barAccount = new BankAccountSynchDeadLock(2, 100d);
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> BankAccountSynchDeadLock.transfer(fooAccount, barAccount, 10d));
        executor.submit(() -> BankAccountSynchDeadLock.transfer(barAccount, fooAccount, 10d));
    }
}