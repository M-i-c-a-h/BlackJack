
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Implements game logic
 */
public class BlackjackGame extends Application {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }


    ArrayList<Card> playerHand = new ArrayList<>();
    ArrayList<Card> bankerHand = new ArrayList<>();

    BlackjackGame gameLogic = new BlackjackGame();
    BlackjackDealer theDealer = new BlackjackDealer();


    double currentBet = 0.0;
    double totalWinnings = 0.0;

    /**
     * Determine if the user won or lost their bet and return the amount won
     * or lost based on the value in currentBet
     * @return amount won or lost
     */
    public double evaluateWinnings(){

        return 0.0;
    }
    private Button b1, b2;
    private TextField centreTextField, rightTextField;
    private VBox v1;

    private HBox h1;

    private BorderPane root;
    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.setTitle("Micah Olugbamila Homework 3");	// title stage

        // create boarderPane
        root = new BorderPane();

        // create button object & corresponding event handler
        b1 = new Button("button 1");
        b2 = new Button("button 2");
        // group button 1 & 2 in a Vbox
        v1 = new VBox(10, b1,b2);
        v1.setAlignment(Pos.CENTER_LEFT);
        BorderPane.setMargin(v1, new Insets(20));
        // set node position
        root.setLeft(v1);

        // create textField objects
        centreTextField = new TextField();
        centreTextField.setPromptText("enter text here then press button 1");
        centreTextField.setPrefWidth(250);
        centreTextField.setAlignment(Pos.CENTER);
        BorderPane.setMargin(centreTextField, new Insets(20));


        rightTextField = new TextField("final string goes here");
        rightTextField.setPrefWidth(250);
        rightTextField.setEditable(false);		// set as non-editable
        rightTextField.setAlignment(Pos.CENTER);
        BorderPane.setMargin(rightTextField, new Insets(20));

        h1 = new HBox(20, centreTextField, rightTextField);
        h1.setAlignment(Pos.CENTER);
        root.setCenter(h1);
        root.setPadding(new Insets(0,20,0,20));

        // anonymous event handler for b1
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rightTextField.setText(centreTextField.getText() + " : from the center text field!");
                b1.setDisable(true);  // disable b1
                b1.setText("pressed");
            }
        });

        // lambda event handles b2
        b2.setOnAction(e-> {
            centreTextField.clear();
            rightTextField.clear();
            b1.setDisable(false);
            b1.setText("button one");
        } );

        Scene frontPage = new Scene(root, 700, 700);   // create front scene

        primaryStage.setScene(frontPage);		// set stage to scene frontPage

        primaryStage.show();
    }
};
