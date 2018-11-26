import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

public class test {


    public static void main(String[] args) {
        Function<? super Scheduler, ? extends Scheduler> rxplay = RxJavaPlugins.getIoSchedulerHandler();
    }
}
