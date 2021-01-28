package ar.edu.itba.pod.concurrency.example.liveness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 */
public class BankAccountLiveLock {
    double balance;
    int id;
    Lock lock = new ReentrantLock();

    BankAccountLiveLock(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    boolean withdraw(double amount) {
        if (this.lock.tryLock()) {
            // Wait to simulate io like database access ...
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
            }
            balance -= amount;
            return true;
        }
        return false;
    }

    boolean deposit(double amount) {
        if (this.lock.tryLock()) {
            // Wait to simulate io like database access ...
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
            }
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean tryTransfer(BankAccountLiveLock destinationAccount, double amount) {
        if (this.withdraw(amount)) {
            if (destinationAccount.deposit(amount)) {
                return true;
            } else {
                // destination account busy, refund source account.
                this.deposit(amount);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        final BankAccountLiveLock fooAccount = new BankAccountLiveLock(1, 500d);
        final BankAccountLiveLock barAccount = new BankAccountLiveLock(2, 500d);

        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> new Transaction(fooAccount, barAccount, 10d));
        executor.submit(() -> new Transaction(barAccount, fooAccount, 10d));

    }

}

class Transaction implements Runnable {
    private BankAccountLiveLock sourceAccount, destinationAccount;
    private double amount;

    Transaction(BankAccountLiveLock sourceAccount, BankAccountLiveLock destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public void run() {
        while (!sourceAccount.tryTransfer(destinationAccount, amount))
            continue;
        System.out.printf("%s completed ", Thread.currentThread().getName());
    }

}
