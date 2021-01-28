package ar.edu.itba.pod.concurrency.example.liveness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Deadlhock with {@link Lock}
 */
public class BankAccountLockDeadLock {
    double balance;
    final int id;
    final Lock lock = new ReentrantLock();

    BankAccountLockDeadLock(int id, double balance) {
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

    static void transfer(BankAccountLockDeadLock from, BankAccountLockDeadLock to, double amount) {
        from.lock.lock();
        from.withdraw(amount);
        to.lock.lock();
        to.deposit(amount);
        to.lock.unlock();
        from.lock.unlock();
    }

    public static void main(String[] args) {
        final BankAccountSynchDeadLock fooAccount = new BankAccountSynchDeadLock(1, 100d);
        final BankAccountSynchDeadLock barAccount = new BankAccountSynchDeadLock(2, 100d);
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> BankAccountSynchDeadLock.transfer(fooAccount, barAccount, 10d));
        executor.submit(() -> BankAccountSynchDeadLock.transfer(barAccount, fooAccount, 10d));

    }
}
