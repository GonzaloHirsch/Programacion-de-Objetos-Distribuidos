package ar.edu.itba.pod.j8.tp.concurrency;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test for completable Futures based on examples on
 * http://codingjunkie.net/completable-futures-part1/.
 *
 */
public class CompletableFutureTest {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);
    private static ExecutorService service;
    private List<String> results;

    @Before
    public final void before() {
        service = Executors.newCachedThreadPool();
        results = new ArrayList<>();
    }

    @After
    public final void after() {
        service.shutdownNow();
    }

    @Test
    public void test_completed_future() throws Exception {
        final String expectedValue = "Hi this would be the return of the task";
        final CompletableFuture<String> completedFuture = CompletableFuture.completedFuture(expectedValue);
        assertEquals("what does are future return?", null, completedFuture.get());
    }

    @Test
    public void test_run_async() throws Exception {
        final CompletableFuture<Void> runAsync = CompletableFuture
                .runAsync(() -> logger.info("running async task"), service);
        sleepFor(1);
        assertEquals("is the future done?", null, runAsync.isDone());
    }

    @Test
    public void test_supply_async() throws Exception {
        final CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(runTaskThatLasts(1, "Final Result"), service);
        assertEquals("what does are future return?", null, completableFuture.get());
    }

    @Test
    public void test_accept_result() throws Exception {
        final String result = "this is what I return";
        final CompletableFuture<String> task = CompletableFuture.supplyAsync(runTaskThatLasts(1, result),
                service);
        final CompletableFuture<Void> acceptingTask = task.thenAccept(results::add);

        acceptingTask.get(); // wait till is done

        assertEquals("is the task done?", null, task.isDone());
        assertEquals("is the acceptingTask done?", null, acceptingTask.isDone());
        assertEquals("what does  task returns?", null, task.get());
        assertEquals("how many elements on the list?", 0, results.size());
        assertEquals("what's on the list", asList(null), results);
    }

    @Test
    public void test_then_run_async() throws Exception {
        final String result = "this is what I return";
        final CompletableFuture<String> task = CompletableFuture.supplyAsync(runTaskThatLasts(1, result),
                service);
        final CompletableFuture<Void> acceptingTask = task.thenAccept(results::add);
        final CompletableFuture<Void> cleanUp = acceptingTask.thenRunAsync(results::clear, service);

        cleanUp.get(); // wait till is done

        assertEquals("is the task done?", null, task.isDone());
        assertEquals("is the acceptingTask done?", null, acceptingTask.isDone());
        assertEquals("is the cleanUp done?", null, cleanUp.isDone());
        assertEquals("what does  task returns?", null, task.get());
        assertEquals("how many elements on the list?", 0, results.size());
        assertEquals("what's on the list", asList(null), results);

    }

    @Test
    public void test_run_after_both() throws Exception {

        final CompletableFuture<Void> run1 = CompletableFuture.runAsync(() -> {
            sleepFor(2);
            results.add("first task");
        }, service);

        final CompletableFuture<Void> run2 = CompletableFuture.runAsync(() -> {
            sleepFor(3);
            results.add("second task");
        }, service);

        final CompletableFuture<Void> finisher = run1.runAfterBothAsync(run2,
                () -> results.add(results.get(0) + "&" + results.get(1)), service);
        finisher.get(); // wait till done
        assertEquals("is the task done?", null, finisher.isDone());
        assertEquals("how many elements on the list?", 0, results.size());
        assertEquals("what's on the list", asList(null), results);
    }

    @Test
    public void test_run_after_either() throws Exception {

        final CompletableFuture<Void> run1 = CompletableFuture.runAsync(() -> {
            sleepFor(2);
            results.add("should be first");
        }, service);

        final CompletableFuture<Void> run2 = CompletableFuture.runAsync(() -> {
            sleepFor(3);
            results.add("should be second");
        }, service);

        final CompletableFuture<Void> finisher = run1.runAfterEitherAsync(run2,
                () -> results.add(results.get(0).toUpperCase()), service);

        finisher.get(); // wait till done
        assertEquals("is the finisher task done?", null, finisher.isDone());
        assertEquals("what's on the list", asList(null), results);
    }

    @Test
    public void test_accept_either_async_calling_finishes_first() throws Exception {

        final CompletableFuture<String> callingCompletable = CompletableFuture
                .supplyAsync(runTaskThatLasts(1, "calling"), service);
        final CompletableFuture<String> nestedCompletable = CompletableFuture
                .supplyAsync(runTaskThatLasts(3, "nested"), service);

        final CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompletable,
                results::add);

        collector.get(); // wait till done

        assertEquals("is the collectort task done?", null, collector.isDone());
        assertEquals("is the callingCompletable task done?", null, callingCompletable.isDone());
        assertEquals("is the nestedCompletable task done?", null, nestedCompletable.isDone());
        assertEquals("what's on the list", asList(null), results);
    }

    @Test
    public void test_accept_either_async_nested_finishes_first() throws Exception {

        final CompletableFuture<String> callingCompletable = CompletableFuture
                .supplyAsync(runTaskThatLasts(2, "calling"), service);
        final CompletableFuture<String> nestedCompletable = CompletableFuture
                .supplyAsync(runTaskThatLasts(1, "nested"), service);

        final CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompletable,
                results::add);

        collector.get(); // wait till done

        assertEquals("is the collectort task done?", null, collector.isDone());
        assertEquals("is the callingCompletable task done?", null, callingCompletable.isDone());
        assertEquals("is the nestedCompletable task done?", null, nestedCompletable.isDone());
        assertEquals("what's on the list", asList(null), results);
    }

    @Test
    public void test_accept_both_async() throws Exception {

        final CompletableFuture<String> firstTask = CompletableFuture
                .supplyAsync(runTaskThatLasts(3, "first task"), service);
        final CompletableFuture<String> secondTask = CompletableFuture
                .supplyAsync(runTaskThatLasts(2, "second task"), service);

        final BiConsumer<String, String> acceptBothResults = (f, s) -> {
            results.add(f);
            results.add(s);
        };

        final CompletableFuture<Void> bothTasks = firstTask.thenAcceptBothAsync(secondTask,
                acceptBothResults);
        bothTasks.get(); // wait till done

        assertEquals("is the bothTasks task done?", null, bothTasks.isDone());
        assertEquals("is the firstTask task done?", null, firstTask.isDone());
        assertEquals("is the secondTask task done?", null, secondTask.isDone());
        assertEquals("what's on the list", asList(null), results);

    }

    // Applying functions to CompletableFutures
    @Test
    public void test_apply() throws Exception {
        final CompletableFuture<String> task = CompletableFuture
                .supplyAsync(runTaskThatLasts(1, "change me"), service)
                    .thenApply(String::toUpperCase);

        assertEquals("what does are future return?", null, task.get());
    }

    @Test
    public void test_then_combine_async() throws Exception {
        final CompletableFuture<String> firstTask = CompletableFuture
                .supplyAsync(runTaskThatLasts(3, "combine all"), service);
        final CompletableFuture<String> secondTask = CompletableFuture
                .supplyAsync(runTaskThatLasts(2, "task results"), service);
        final CompletableFuture<String> combined = firstTask.thenCombineAsync(secondTask,
                (f, s) -> f + " " + s, service);

        assertEquals("what does are combined future return?", null, combined.get());
    }

    @Test
    public void test_then_combine_with_one_supplied_value() throws Exception {
        final CompletableFuture<String> asyncComputedValue = CompletableFuture
                .supplyAsync(runTaskThatLasts(2, "calculated value"), service);
        final CompletableFuture<String> knowValueToCombine = CompletableFuture.completedFuture("known value");

        final BinaryOperator<String> calcResults = (f, s) -> "taking a " + f + " then adding a " + s;
        final CompletableFuture<String> combined = asyncComputedValue.thenCombine(knowValueToCombine,
                calcResults);

        assertEquals("what does are combined future return?", null, combined.get());

    }

    @Test
    public void test_then_compose() throws Exception {

        final Function<Integer, Supplier<List<Integer>>> getFirstTenMultiples = num -> () -> Stream
                .iterate(num, i -> i + num)
                    .limit(10)
                    .collect(Collectors.toList());

        final Supplier<List<Integer>> multiplesSupplier = getFirstTenMultiples.apply(13);

        final CompletableFuture<List<Integer>> getMultiples = CompletableFuture.supplyAsync(multiplesSupplier,
                service);

        final Function<List<Integer>, CompletableFuture<Integer>> sumNumbers = multiples -> CompletableFuture
                .supplyAsync(() -> multiples.stream().mapToInt(Integer::intValue).sum());

        final CompletableFuture<Integer> summedMultiples = getMultiples.thenComposeAsync(sumNumbers, service);

        assertEquals("what does are summedMultiples future return?", new Integer(0), summedMultiples.get());
    }

    @Test
    public void test_several_stage_combinations() throws Exception {
        final Function<String, CompletableFuture<String>> upperCaseFunction = s -> CompletableFuture
                .completedFuture(s.toUpperCase());

        final CompletableFuture<String> stage1 = CompletableFuture.completedFuture("the quick ");

        final CompletableFuture<String> stage2 = CompletableFuture.completedFuture("brown fox ");

        final CompletableFuture<String> stage3 = stage1.thenCombine(stage2, (s1, s2) -> s1 + s2);

        final CompletableFuture<String> stage4 = stage3.thenCompose(upperCaseFunction);

        final CompletableFuture<String> stage5 = CompletableFuture
                .supplyAsync(runTaskThatLasts(2, "jumped over"));

        final CompletableFuture<String> stage6 = stage4.thenCombineAsync(stage5, (s1, s2) -> s1 + s2,
                service);

        final CompletableFuture<String> stage6_sub_1_slow = CompletableFuture
                .supplyAsync(runTaskThatLasts(4, "fell into"));

        final CompletableFuture<String> stage7 = stage6.applyToEitherAsync(stage6_sub_1_slow,
                String::toUpperCase, service);

        final CompletableFuture<String> stage8 = CompletableFuture
                .supplyAsync(runTaskThatLasts(3, " the lazy dog"), service);

        final CompletableFuture<String> finalStage = stage7.thenCombineAsync(stage8, (s1, s2) -> s1 + s2,
                service);

        assertEquals("what does are finalStage future return?", null, finalStage.get());

    }

    @Test
    public final void exceptional_execution() throws InterruptedException, ExecutionException {
        final String exceptionMessgae = "there was an error on this execution";

        final CompletableFuture<String> cf = new CompletableFuture<>();
        final CompletableFuture<String> exceptionally = cf.exceptionally(th -> th.getMessage());

        cf.completeExceptionally(new IllegalArgumentException(exceptionMessgae));

        assertEquals("does this future end in error", null, exceptionally.isCompletedExceptionally());
        assertEquals("what does this future return", null, exceptionally.get());
    }

    @Test
    public final void when_complete_completed() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> cf = new CompletableFuture<>();
        final CompletableFuture<String> whenComplete = cf.whenComplete((r, e) -> {
            if (r != null) {
                logger.info("it worked!! we obtained {}", r);
            } else if (e != null) {
                logger.error(":-( there was an error  ", e);
            } else {
                logger.warn("don't know what happened");
            }
        });
        cf.complete("eok");

        assertEquals("does this future end in error", null, whenComplete.isCompletedExceptionally());
        assertEquals("what does this future return?", null, whenComplete.get());

    }

    @Test
    public final void when_complete_exceptionally() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> cf = new CompletableFuture<>();
        final CompletableFuture<String> whenComplete = cf.whenComplete((r, e) -> {
            if (r != null) {
                logger.info("it worked!! we obtained {}", r);
            } else if (e != null) {
                logger.error(":-( there was an error  ", e);
            } else {
                logger.warn("don't know what happened");
            }
        });
        cf.completeExceptionally(new IllegalArgumentException("este future no termino bien"));

        assertEquals("does this future end in error", null, whenComplete.isCompletedExceptionally());
        assertEquals("what does this future return (or...)?", null, whenComplete.get());
    }

    private Supplier<String> runTaskThatLasts(final int numSeconds, final String taskResult) {
        return () -> {
            sleepFor(numSeconds);
            return taskResult;
        };
    }

    private void sleepFor(final int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (final InterruptedException e) {
            logger.error("interruption on thread", e);
        }
    }

}
