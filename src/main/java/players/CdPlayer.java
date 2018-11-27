package players;

import de.eso.rxplayer.EntertainmentService;
import de.eso.rxplayer.Player;
import io.reactivex.schedulers.Schedulers;

public class CdPlayer {
    public static void main(String[] args) {
        EntertainmentService entertainmentService = new EntertainmentService(Schedulers.computation());
        Player player = entertainmentService.getCd();

        player.list().subscribe(System.out::println);
    }
}
