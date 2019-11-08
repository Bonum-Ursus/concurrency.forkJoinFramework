import java.util.concurrent.*;

public class ForkJoinFramework {
    static int pNumber = Runtime.getRuntime().availableProcessors();
    static long numberOfOperations = 10_000_000_000L;
    public static void main(String[] args) {
        long j = 0;
        System.out.println("Number of processors: " + pNumber);

        System.out.println("One thread implementation start");
        long start = System.currentTimeMillis();
        for (long i = 0; i < numberOfOperations; i++) {
            j += i;
        }
        long finish = System.currentTimeMillis();
        System.out.println("One thread implementation finish. Result: " +
                (finish - start) + "ms");
        System.out.println("***********************************************");

        System.out.println("Multi thread implementation start");
        long start2 = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(pNumber+1);
        pool.invoke(new MyFork(0, numberOfOperations));
        long finish2 = System.currentTimeMillis();
        System.out.println("Multi thread implementation finish. Result: " +
                (finish2 - start2) + "ms");
    }
    static class MyFork extends RecursiveTask<Long>{
        long from, to;

        public MyFork(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if(to - from <= numberOfOperations/(pNumber)){
                long k = 0;
                for (long i = from; i < to; i++) {
                    k += i;
                }
                return k;
            }else {
                long middle = (to + from)/2;
                MyFork firstHalf = new MyFork(from, middle);
                firstHalf.fork();
                MyFork secondHalf = new MyFork(middle + 1, to);
                long secondValue = secondHalf.compute();
                return firstHalf.join() + secondValue;
            }
        }
    }
}

