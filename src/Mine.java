import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class Mine extends Button {
    enum Status{
        none, flag, safe
    }
    private int x, y;
    private Status status = Status.none;
    private boolean isMine = false;

    public Mine(int x, int y) {
        setX(x);
        setY(y);
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setFocused(true);
                setStyle("-fx-background-color: #00f0ff");
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setFocused(false);
                setStyle("-fx-background-color: #00ffff");
            }
        });
        setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        setAlignment(Pos.CENTER);
        setFocusTraversable(false);
        setStyle("-fx-background-color: #00ffff");
        setFont(new Font(24));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine() {
        isMine = true;
    }
}
