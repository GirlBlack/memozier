package memozier;

import java.util.concurrent.*;

/**
 * Title: PACKAGE_NAME<br>
 * Description: 为计算结果建立高效 可伸缩的缓存
 * Copyright: Copyright (c) 2016<br>
 *
 * @author lili 2016/5/31
 */
public class Memoizer<T, R>{

    private ConcurrentHashMap <T, FutureTask<R>>cache = new ConcurrentHashMap();
    private final Computable<T,R> c;

    public Memoizer(Computable<T,R> c) {
        this.c = c;
    }

    public R compute(T args) {
        FutureTask<R> futureTask;
        futureTask = cache.get(args);

        if(futureTask == null ){
            FutureTask<R> newFutureTask = new FutureTask<R>(new Callable<R>() {
                @Override
                public R call() throws Exception {
                    return c.convert(args);
                }
            });
            futureTask = cache.putIfAbsent(args, newFutureTask);
            if(futureTask == null){
                futureTask = newFutureTask;
                newFutureTask.run();// call方法在这里才开始调用
            }
        }else{
            System.out.println("字符串"+args +"已在缓存中");
        }

        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            cache.remove(args);
        } catch (ExecutionException e) {
            cache.remove(args);
        }finally {
            futureTask.cancel(false);
        }
        return null;
    }

    public static String getRandomStr(){
        StringBuilder builder = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            builder.append((char) (ThreadLocalRandom.current().nextInt(44, 47)));
        }
        return builder.toString();
    }

    public  static void main(String [] args){
        StrCompute s = new StrCompute();
        Memoizer m = new Memoizer(s);

        Thread t1 =  new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i<6){
                    System.out.println("线程1开始执行");
                    String randomStr = Memoizer.getRandomStr();
                    m.compute(randomStr);
                    i++;
                }
            }
        });

        Thread t2 =  new Thread(new Runnable() {
            @Override
            public void run() {
                int j=0;
                while(j<6){
                    System.out.println("线程2开始执行!");
                    String randomStr = Memoizer.getRandomStr();
                    m.compute(randomStr);
                    j++;
                }
            }
        });

        t1.start();
        t2.start();
    }
}
