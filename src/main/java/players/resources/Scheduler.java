package players.resources;


import io.reactivex.schedulers.Schedulers;

public final class Scheduler {
    public static final io.reactivex.Scheduler scheduler = Schedulers.computation();
}
