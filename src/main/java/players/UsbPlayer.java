package players;

import de.eso.rxplayer.Audio;
import de.eso.rxplayer.Browser;
import de.eso.rxplayer.EntertainmentService;
import de.eso.rxplayer.Player;
import io.reactivex.schedulers.Schedulers;

public class UsbPlayer {

    Player usbService;

    Browser browserService;

    Audio audioService;

    UsbPlayer(){
        EntertainmentService entertainmentService = new EntertainmentService(Schedulers.computation());
        usbService = entertainmentService.getUsb();
        browserService = entertainmentService.getBrowser();
        audioService = entertainmentService.getAudio();
    }



    public static void main(String[] args) {

    }
}
