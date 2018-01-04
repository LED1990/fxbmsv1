package sample.constans;

/**
 * Created by artur on 2017-12-29.
 */
public enum BaudRate {
    BAUD1(9200),
    BAUD2(19200),
    BAUD3(38400),
    BAUD4(57600),
    BAUD5(1115200);

    private int baud;

    BaudRate(int baud) {
        this.baud = baud;
    }
    public int getBaud(){
        return baud;
    }
}


