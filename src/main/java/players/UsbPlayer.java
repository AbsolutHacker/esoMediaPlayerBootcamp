package players;

import de.eso.rxplayer.*;

public class UsbPlayer {

    Player usbService;

    Browser browserService;

    Audio audioService;

    UsbPlayer(){
        EntertainmentService entertainmentService = myEntertainmentService.getEntertainmentService();
        usbService = entertainmentService.getUsb();
        browserService = entertainmentService.getBrowser();
        audioService = entertainmentService.getAudio();
    }



    public static void main(String[] args) {

    }
}
