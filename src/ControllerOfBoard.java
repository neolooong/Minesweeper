import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;

public class ControllerOfBoard {
    @FXML
    private GridPane easy, normal, hard;

    public void initialize(){
        try {
            File boardFile = new File("TopBoard");
            TopBoard board;
            if (!boardFile.exists()){
                board = new TopBoard();
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(boardFile));
                outputStream.writeObject(board);
                outputStream.close();
            }else {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(boardFile));
                board = (TopBoard) inputStream.readObject();
                inputStream.close();
            }

//            Easy
            ArrayList<Record> e = board.getEasy();
            for (int i = 1; i <= 10; i++){
                if (i <= e.size()){
                    Label time = new Label();
                    time.setText(Integer.toString(e.get(i-1).getTime()));
                    time.setAlignment(Pos.CENTER);
                    time.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(time, true);
                    GridPane.setFillHeight(time, true);
                    easy.add(time, 1, i);
                    Label date = new Label();
                    date.setText(e.get(i-1).getDate());
                    date.setAlignment(Pos.CENTER);
                    date.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(date, true);
                    GridPane.setFillHeight(date, true);
                    easy.add(date, 2, i);
                }else {
                    Label none = new Label();
                    none.setText("---");
                    none.setAlignment(Pos.CENTER);
                    none.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none, true);
                    GridPane.setFillHeight(none, true);
                    easy.add(none, 1, i);
                    Label none1 = new Label();
                    none1.setText("---");
                    none1.setAlignment(Pos.CENTER);
                    none1.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none1, true);
                    GridPane.setFillHeight(none1, true);
                    easy.add(none1, 2, i);
                }
            }
//            Normal
            ArrayList<Record> n = board.getNormal();
            for (int i = 1; i <= 10; i++){
                if (i <= n.size()){
                    Label time = new Label();
                    time.setText(Integer.toString(n.get(i-1).getTime()));
                    time.setAlignment(Pos.CENTER);
                    time.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(time, true);
                    GridPane.setFillHeight(time, true);
                    normal.add(time, 1, i);
                    Label date = new Label();
                    date.setText(n.get(i-1).getDate());
                    date.setAlignment(Pos.CENTER);
                    date.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(date, true);
                    GridPane.setFillHeight(date, true);
                    normal.add(date, 2, i);
                }else {
                    Label none = new Label();
                    none.setText("---");
                    none.setAlignment(Pos.CENTER);
                    none.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none, true);
                    GridPane.setFillHeight(none, true);
                    normal.add(none, 1, i);
                    Label none1 = new Label();
                    none1.setText("---");
                    none1.setAlignment(Pos.CENTER);
                    none1.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none1, true);
                    GridPane.setFillHeight(none1, true);
                    normal.add(none1, 2, i);
                }
            }
//            Hard
            ArrayList<Record> h = board.getHard();
            for (int i = 1; i <= 10; i++){
                if (i <= h.size()){
                    Label time = new Label();
                    time.setText(Integer.toString(h.get(i-1).getTime()));
                    time.setAlignment(Pos.CENTER);
                    time.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(time, true);
                    GridPane.setFillHeight(time, true);
                    hard.add(time, 1, i);
                    Label date = new Label();
                    date.setText(h.get(i-1).getDate());
                    date.setAlignment(Pos.CENTER);
                    date.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(date, true);
                    GridPane.setFillHeight(date, true);
                    hard.add(date, 2, i);
                }else {
                    Label none = new Label();
                    none.setText("---");
                    none.setAlignment(Pos.CENTER);
                    none.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none, true);
                    GridPane.setFillHeight(none, true);
                    hard.add(none, 1, i);
                    Label none1 = new Label();
                    none1.setText("---");
                    none1.setAlignment(Pos.CENTER);
                    none1.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    GridPane.setFillWidth(none1, true);
                    GridPane.setFillHeight(none1, true);
                    hard.add(none1, 2, i);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
