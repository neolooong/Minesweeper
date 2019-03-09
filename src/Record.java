import java.io.Serializable;

public class Record implements Serializable{
    private String date;
    private int time;

    public Record(String date, int time) {
        setDate(date);
        setTime(time);
    }

    public String getDate() {
        return date;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    private void setTime(int time) {
        this.time = time;
    }
}
