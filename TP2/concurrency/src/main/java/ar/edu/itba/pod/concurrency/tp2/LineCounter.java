package ar.edu.itba.pod.concurrency.tp2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.*;

public class LineCounter {
    private String path;
    private long lines = 0;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final StringBuffer sb = new StringBuffer();

    public LineCounter(String path) {
        this.path = path;
    }

    protected synchronized void addLines(int n) {
        this.lines += n;
    }

    public void countAllLines() {
        File directory = new File(this.path);
        Optional<File[]> contents = Optional.ofNullable(directory.listFiles());
        contents.ifPresent(files -> {
            for (File f : files) {
                try {
                    this.sb.append(this.pool.submit(new CompletableFileCallable(f)).get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Error getting calable result");
                }
            }
            this.pool.shutdown();
            try {
                this.pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error interrupting pool");
            }
            System.out.println(this.sb.toString());
        });
    }

    @Deprecated
    private class FileRunnable implements Runnable {
        private File file;

        public FileRunnable(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (!this.file.isFile()) {
                LineCounter.this.addLines(0);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                    int lines = 0;
                    while (reader.readLine() != null) lines++;
                    LineCounter.this.addLines(lines);
                } catch (IOException e) {
                    throw new RuntimeException("Error opening file");
                }
            }
        }
    }

    private class CompletableFileRunnable implements Runnable {
        private File file;

        public CompletableFileRunnable(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (!this.file.isFile()) {
                System.out.println(this.file.getAbsolutePath() + " IS A DIRECTORY");
            } else {
                CompletableFuture<Long> lineCountSupplier = CompletableFuture.supplyAsync(() -> {
                    try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                        long lines = 0;
                        while (reader.readLine() != null) lines++;
                        return lines;
                    } catch (IOException e) {
                        throw new RuntimeException("Error opening file");
                    }
                });
                CompletableFuture<Long> sizeSupplier = CompletableFuture.supplyAsync(() -> {
                    try {
                        return Files.size(Paths.get(this.file.getAbsolutePath()));
                    } catch (IOException e) {
                        throw new RuntimeException("Error opening file");
                    }
                });
                CompletableFuture<String> combined = lineCountSupplier.thenCombineAsync(sizeSupplier, (l, s) -> String.format("\nFILE: %s\nSIZE: %d\nLINES: %d\n", this.file.getAbsolutePath(), s, l));
                try {
                    System.out.println(combined.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Error completing task");
                }
            }
        }
    }

    private class ExtendedFileRunnable implements Runnable {
        private File file;

        public ExtendedFileRunnable(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (!this.file.isFile()) {
                LineCounter.this.addLines(0);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                    int lines = 0;
                    while (reader.readLine() != null) lines++;
                    System.out.println("FILE: " + this.file.getAbsolutePath());
                    System.out.println("SIZE: " + Files.size(Paths.get(this.file.getAbsolutePath())));
                    System.out.println("LINES: " + lines);
                } catch (IOException e) {
                    throw new RuntimeException("Error opening file");
                }
            }
        }
    }

    private class CompletableFileCallable implements Callable<String> {
        private File file;

        public CompletableFileCallable(File file) {
            this.file = file;
        }

        @Override
        public String call() {
            if (!this.file.isFile()) {
                return this.file.getAbsolutePath() + " IS A DIRECTORY";
            } else {
                CompletableFuture<Long> lineCountSupplier = CompletableFuture.supplyAsync(() -> {
                    try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                        long lines = 0;
                        while (reader.readLine() != null) lines++;
                        return lines;
                    } catch (IOException e) {
                        throw new RuntimeException("Error opening file");
                    }
                });
                CompletableFuture<Long> sizeSupplier = CompletableFuture.supplyAsync(() -> {
                    try {
                        return Files.size(Paths.get(this.file.getAbsolutePath()));
                    } catch (IOException e) {
                        throw new RuntimeException("Error opening file");
                    }
                });
                CompletableFuture<String> combined = lineCountSupplier.thenCombineAsync(sizeSupplier, (l, s) -> String.format("\nFILE: %s\nSIZE: %d\nLINES: %d\n", this.file.getAbsolutePath(), s, l));
                try {
                    return combined.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Error completing task");
                }
            }
        }
    }
}
