import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Consumer;

public class ControllerOfScene {
    @FXML
    private Label time, restOfMine;
    @FXML
    private GridPane grid;

    private Stage self;
    private String defaultLevel = "Normal (16 * 16)";
    private Mine mines[][] = new Mine[9][9];

    private Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            time.setText(Integer.toString(Integer.parseInt(time.getText()) + 1));
        }
    }));

    private HashSet<MouseButton> mouseCombine = new HashSet<>();
    private EventHandler<MouseEvent> press = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Mine mine = (Mine) event.getSource();
            mouseCombine.add(event.getButton());
            if (mine.isFocused() && mouseCombine.contains(MouseButton.PRIMARY) && mouseCombine.contains(MouseButton.SECONDARY)){
                int x = mine.getX(), y = mine.getY();
//                    周圍變色
                for (int i = x-1; i <= x+1; i++){
                    for (int j = y-1; j <= y+1; j++){
//                            避免超出地圖 和 省略有狀態的
                        if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length && mines[i][j].getStatus() == Mine.Status.none){
                            mines[i][j].setStyle("-fx-background-color: #00f0ff");
                        }
                    }
                }
            }
        }
    };
    private EventHandler<MouseEvent> release = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            timer.play();
            Mine mine = (Mine) event.getSource();
//            兩個按鍵
            if (mouseCombine.contains(MouseButton.PRIMARY) && mouseCombine.contains(MouseButton.SECONDARY)) {
                int x = mine.getX(), y = mine.getY();
                boolean hasMine = false;

                if (mine.getStatus() == Mine.Status.safe) {
                    for (int i = x - 1; i <= x + 1; i++) {
                        for (int j = y - 1; j <= y + 1; j++) {
//                            避免超出地圖 和 省略safe狀態
                            if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length) {
                                if (mines[i][j].getStatus() == Mine.Status.none) {
                                    mines[i][j].setStyle("-fx-background-color: #00ffff");

                                    if (mines[i][j].isMine()) {
                                        hasMine = true;
                                    }
                                } else if (mines[i][j].getStatus() == Mine.Status.flag && !mines[i][j].isMine()) {
//                                Dead
                                    timer.stop();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
//                                顯示地雷
                                            for (int i = 0; i < mines.length; i++) {
                                                for (int j = 0; j < mines[i].length; j++) {
                                                    if (mines[i][j].isMine() && mines[i][j].getStatus() != Mine.Status.flag) {
                                                        mines[i][j].setText("✶");
                                                        mines[i][j].setFont(new Font(22.518992));
                                                        mines[i][j].setTextFill(Paint.valueOf("#f00000"));
                                                    }
                                                    if (!mines[i][j].isMine() && mines[i][j].getStatus() == Mine.Status.flag) {
                                                        mines[i][j].setText("X");
                                                        mines[i][j].setTextFill(Paint.valueOf("#f00000"));
                                                    }
                                                }
                                            }
                                        }
                                    });

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("失敗");
                                    alert.setHeaderText("");
                                    alert.setContentText("You lose");
                                    alert.showAndWait();

                                    newGame();
                                }
                            }
                        }
                    }
//                如果周圍都沒炸彈
                    if (!hasMine) {
                        for (int i = x - 1; i <= x + 1; i++) {
                            for (int j = y - 1; j <= y + 1; j++) {
//                            避免超出地圖 和 省略有狀態的
                                if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length && mines[i][j].getStatus() == Mine.Status.none) {
                                    mineCheck(mines[i][j]);
                                }
                            }
                        }
                        winCheck();
                    }
                }else if (mine.getStatus() == Mine.Status.none || mine.getStatus() == Mine.Status.flag){
                    for (int i = x-1; i <= x+1; i++){
                        for (int j = y-1; j <= y+1; j++){
//                            避免超出地圖 和 省略有狀態的
                            if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length && mines[i][j].getStatus() == Mine.Status.none){
                                mines[i][j].setStyle("-fx-background-color: #00ffff");
                            }
                        }
                    }
                }
//                    清除組合鍵
                mouseCombine.remove(MouseButton.PRIMARY);
                mouseCombine.remove(MouseButton.SECONDARY);
                return;
            }

//            單個按鍵
            if (mine.isFocused()) {
                if (mouseCombine.contains(MouseButton.PRIMARY) && mine.getStatus() == Mine.Status.none){
                    if (mine.isMine()){
//                        Dead
                        timer.stop();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
//                                顯示地雷
                                for (int i = 0; i < mines.length; i++){
                                    for (int j = 0; j < mines[i].length; j++){
                                        if (mines[i][j].isMine() && mines[i][j].getStatus() != Mine.Status.flag){
                                            mines[i][j].setText("✶");
                                            mines[i][j].setFont(new Font(22.518992));
                                            mines[i][j].setTextFill(Paint.valueOf("#f00000"));
                                        }
                                        if (!mines[i][j].isMine() && mines[i][j].getStatus() == Mine.Status.flag){
                                            mines[i][j].setText("X");
                                            mines[i][j].setTextFill(Paint.valueOf("#f00000"));
                                        }
                                    }
                                }
                            }
                        });

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("失敗");
                        alert.setHeaderText("");
                        alert.setContentText("You lose");
                        alert.showAndWait();

                        newGame();
                    }else {
                        mineCheck(mine);
                        winCheck();
                    }
                }else if (mouseCombine.contains(MouseButton.SECONDARY)){
                    if (mine.getStatus() == Mine.Status.none) {
                        mine.setText("⚑");
                        mine.setTextFill(Paint.valueOf("#ff0000"));
                        restOfMine.setText(Integer.toString(Integer.parseInt(restOfMine.getText()) - 1));
                        mine.setStatus(Mine.Status.flag);
                        winCheck();
                    }else if (mine.getStatus() == Mine.Status.flag){
                        mine.setTextFill(Paint.valueOf("black"));
                        mine.setText("");
                        restOfMine.setText(Integer.toString(Integer.parseInt(restOfMine.getText()) + 1));
                        mine.setStatus(Mine.Status.none);
                    }
                }
            }
            mouseCombine.remove(event.getButton());
        }
    };

    public void initialize(){
        timer.setCycleCount(Timeline.INDEFINITE);
        newGame();
    }

    private void mineCheck(Mine mine){
        int numberOfMines = 0;  //周圍炸彈數量
        int x = mine.getX(), y = mine.getY();   //要檢查的座標

//        檢查周圍格子
        for (int i = x-1; i <= x+1; i++){
            for (int j = y-1; j <= y+1; j++){
//                避免超出地圖
                if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length){
                    if (mines[i][j].isMine()){
                        numberOfMines++;
                    }
                }
            }
        }

//        如果自己本身是零 周圍也要檢查一次
        if (numberOfMines == 0){
            mine.setDisable(true);
            mine.setStatus(Mine.Status.safe);
            for (int i = x-1; i <= x+1; i++){
                for (int j = y-1; j <= y+1; j++){
//                避免超出地圖 和 省略有狀態的
                    if (i >= 0 && i < mines.length && j >= 0 && j < mines[i].length && mines[i][j].getStatus() == Mine.Status.none){
                        mineCheck(mines[i][j]);
                    }
                }
            }
        }else {
            mine.setText(Integer.toString(numberOfMines));
            mine.setStatus(Mine.Status.safe);
        }
    }

    private void winCheck(){
        boolean finish = true;
        boolean correct = true;
        skip:
        for (int i = 0; i < mines.length; i++) {
            for (int j = 0; j < mines[i].length; j++) {
                switch (mines[i][j].getStatus()) {
                    case none:                      //進入這裡 表示還沒按完
                        finish = false;
                        break skip;
                    case safe:
                        if (mines[i][j].isMine()){  //進入這裡 表示錯誤
                            correct = false;
                        }
                        break;
                    case flag:
                        if (!mines[i][j].isMine()){  //進入這裡 表示錯誤
                            correct = false;
                        }
                        break;
                }
            }
        }

        if (finish){
            if (correct){
                timer.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("獲勝");
                alert.setHeaderText("");
                alert.setContentText("You win");
                alert.showAndWait();

                try {
                    File boardFile = new File("TopBoard");
                    Record record = new Record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), Integer.parseInt(time.getText()));
                    if (boardFile.exists()) {
                        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(boardFile));
                        TopBoard board = (TopBoard) inputStream.readObject();
                        inputStream.close();
                        switch (defaultLevel) {
                            case "Easy (9 * 9)":
                                board.addToEasy(record);
                                break;
                            case "Normal (16 * 16)":
                                board.addToNormal(record);
                                break;
                            case "Hard (16 * 30)":
                                board.addToHard(record);
                                break;
                        }

                        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(boardFile));
                        outputStream.writeObject(board);
                        outputStream.flush();
                        outputStream.close();
                    }else {
                        TopBoard board = new TopBoard();
                        switch (defaultLevel) {
                            case "Easy (9 * 9)":
                                board.addToEasy(record);
                                break;
                            case "Normal (16 * 16)":
                                board.addToNormal(record);
                                break;
                            case "Hard (16 * 30)":
                                board.addToHard(record);
                                break;
                        }
                        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(boardFile));
                        outputStream.writeObject(board);
                        outputStream.close();
                    }
                }catch (IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }

                newGame();
            }
        }
    }

    @FXML
    private void newGame() {
//        清空地圖
        for (int i = 0; i < mines.length; i++) {
            for (int j = 0; j < mines[i].length; j++) {
                grid.getChildren().remove(mines[i][j]);
            }
        }
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        restOfMine.setText("0");
        time.setText("0");
        timer.stop();
        mouseCombine.removeAll(new HashSet<>(Arrays.asList(MouseButton.PRIMARY, MouseButton.SECONDARY)));
//        清空地圖
//        難度選擇
        ChoiceDialog<String> level = new ChoiceDialog<>(defaultLevel,"Easy (9 * 9)", "Normal (16 * 16)", "Hard (16 * 30)");
        level.setTitle("Choose Level");
        level.setHeaderText("");
        level.setContentText("Level:");
        level.showAndWait().ifPresent(new Consumer<String>() {
            @Override
            public void accept(String s) {
                defaultLevel = s;
                switch (s) {
                    case "Easy (9 * 9)":
                        restOfMine.setText("10");
                        mines = new Mine[9][9];
                        if (self != null){
                            self.setWidth(516);
                            self.setMinWidth(516);
                            self.setHeight(615);
                            self.setMinHeight(615);
                            self.centerOnScreen();
                        }
                        break;
                    case "Normal (16 * 16)":
                        restOfMine.setText("50");
                        mines = new Mine[16][16];
                        if (self != null){
                            self.setWidth(873);
                            self.setMinWidth(873);
                            self.setHeight(972);
                            self.setMinHeight(972);
                            self.centerOnScreen();
                        }
                        break;
                    case "Hard (16 * 30)":
                        restOfMine.setText("99");
                        mines = new Mine[30][16];
                        if (self != null){
                            self.setWidth(1587);
                            self.setMinWidth(1587);
                            self.setHeight(972);
                            self.setMinHeight(972);
                            self.centerOnScreen();
                        }
                        break;
                }
            }
        });
        if (level.getResult() == null){
            System.exit(0);
        }
//        難度選擇
//        建構地圖
        for (int i = 0; i < mines.length; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints(50, 50, 50, Priority.NEVER, HPos.CENTER, true);
            grid.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < mines[0].length; i++) {
            RowConstraints rowConstraints = new RowConstraints(50, 50, 50, Priority.NEVER, VPos.CENTER, true);
            grid.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < mines.length; i++){
            for (int j = 0; j < mines[i].length; j++){
                mines[i][j] = new Mine(i, j);
                Mine mine = mines[i][j];
                mine.setOnMousePressed(press);
                mine.setOnMouseReleased(release);
                GridPane.setFillWidth(mine, true);
                GridPane.setFillHeight(mine, true);
                grid.add(mine, i , j);
            }
        }
//        建構地圖
//        產生炸彈
        Random random = new Random(System.currentTimeMillis());
        int i = 0;
        while (i < Integer.parseInt(restOfMine.getText())) {
            int x = random.nextInt(mines.length);
            int y = random.nextInt(mines[0].length);
            if (mines[x][y].isMine()){
                continue;
            }
            mines[x][y].setMine();
            i++;
        }
//        產生炸彈
    }

    @FXML
    private void openBoard(ActionEvent event) throws IOException {
        timer.pause();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
        Parent root = loader.load();

        Stage board = new Stage();
        board.setResizable(false);
        board.initOwner(self);
        board.initModality(Modality.APPLICATION_MODAL);
        board.setScene(new Scene(root));
        board.setTitle("Top Board");
        board.showAndWait();

        if (timer.getStatus() == Animation.Status.PAUSED){
            timer.play();
        }
    }

    @FXML
    private void about(ActionEvent event) {
        timer.pause();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("關於");
        alert.setHeaderText("版本 - 3.0");
        alert.setContentText("What's new?\n" +
                "   1. 滑鼠組合鍵\n" +
                "   2. 版面優化\n");
        alert.showAndWait();
        if (timer.getStatus() == Animation.Status.PAUSED){
            timer.play();
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    public Stage getSelf() {
        return self;
    }

    public void setSelf(Stage self) {
        this.self = self;
    }

    public Timeline getTimer() {
        return timer;
    }

    public void setTimer(Timeline timer) {
        this.timer = timer;
    }
}
