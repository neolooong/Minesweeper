import javafx.animation.Animation;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));
        Parent root = loader.load();

        ControllerOfScene controller = loader.getController();
        controller.setSelf(primaryStage);

        primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (primaryStage.isIconified()){
                    controller.getTimer().pause();
                }else {
                    if (controller.getTimer().getStatus() == Animation.Status.PAUSED){
                        controller.getTimer().play();
                    }
                }
            }
        });

        primaryStage.setTitle("踩地雷");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
