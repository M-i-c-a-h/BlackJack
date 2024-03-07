
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
 * GUI of BlackjackGame
 */
public class BlackjackGame extends Application {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    private ArrayList<Card> playerHand;
    private ArrayList<Card> bankerHand;
    private BlackjackDealer theDealer;

    private double currentBet = 0.0;
    private double totalWinnings = 0.0;

    private HashMap<String,Scene> scenes = new HashMap<>();
    Scene homepage, rules, banking, begin;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BlackJack");

        scenes = new HashMap<>();
        homepage = buildHomepage(primaryStage);
        rules = buildRules(primaryStage);

        scenes.put("homepage", homepage);
        scenes.put("rules", rules);

        primaryStage.setScene(scenes.get("homepage"));


        primaryStage.show();
        // Set focus off TextField after the scene is shown
        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());
        // Other methods and variables can be added here
    }

    private Scene buildBankingPage (Stage primaryStage){
        // create bank background
        Background bankBackground = createBackGroundImage("images/blkj.webp");

        // create layout
        StackPane root = new StackPane();
        root.setBackground(bankBackground);


        return null;
    }
    private Scene buildHomepage(Stage primaryStage){
        // create homepage background
        Background homebackground = createBackGroundImage("images/rules.jpg");

        // create a layout stack pane
        StackPane root = new StackPane();

        // set background
        root.setBackground(homebackground);

        TextField homeT1 = new TextField("WELCOME TO BLACKJACK\n by Micah");
        homeT1.setPrefSize(400, 30);
        homeT1.setAlignment(Pos.CENTER);
        homeT1.setEditable(false);


        Button homeB1 = createButton1("Rules");
        homeB1.setOnAction(e-> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished( e2->{
                primaryStage.setScene(scenes.get("rules"));
            });
            pause.play();
        });

        Button homeB2 = createButton1("Place Bet");
        Button homeB3 = createButton1("Begin");
        homeB3.setDisable(true);    // disable begin until bet is placed


        HBox homeHBox1 = new HBox(homeT1);
        homeHBox1.setAlignment(Pos.CENTER);

        HBox homeHBox2 = new HBox(20, homeB1, homeB2, homeB3);
        homeHBox2.setAlignment(Pos.CENTER);

        VBox homeV1 = new VBox(20, homeHBox1, homeHBox2);
        homeV1.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane(homeV1);
        pane.setCenter(homeV1);
        pane.setPadding(new Insets(50, 20, 20,50));

        root.getChildren().add(pane);
        homeV1.requestFocus();
        return new Scene(root, 800, 600);
    }
    private Scene buildRules(Stage primaryStage){

        // Create a background with the background image
        Background backgroundWithImage = createBackGroundImage("images/rules.jpg");

        // Create a layout pane
        StackPane root = new StackPane();

        // Set the background to the layout pane
        root.setBackground(backgroundWithImage);

        // create a return button and add to root
        Button returnButton = createButton1("Return");
        returnButton.setPrefSize(100,50);
        returnButton.setOnAction(e -> primaryStage.setScene(scenes.get("homepage")));

        HBox h1 = new HBox();
        h1.setAlignment(Pos.CENTER);
        h1.setPadding(new Insets(100));

        // Create a StackPane for the text area to maintain background
        StackPane textPane = new StackPane();

        TextArea text = getRulesTextArea();

        textPane.getChildren().add(text);
        StackPane.setAlignment(textPane,Pos.CENTER);

        h1.getChildren().addAll(textPane,returnButton);
        h1.setSpacing(10);
        root.getChildren().addAll(h1);

        return new Scene(root, 800, 600);
    }

    private static TextArea getRulesTextArea() {
        TextArea text = new TextArea("Welcome to Blackjack by Micah\n" +
                "\n" +
                "\n" +
                "Rules:\n" +
                "\n" +
                "Once you place a bet in the betting area on the homepage, click the begin button to start\n" +
                "\n" +
                " The objective is to have a hand with a total value higher than the dealer’s without going over 21. \n\n" +
                "Kings, Queens, Jacks, and Tens are worth a value of 10. An Ace has a value of 1 or 11. " +
                "The remaining cards are counted at face value.\n" +
                "\n" +
                " You are dealt two cards each whilst the dealer is dealt one face up. \n" +
                "If your first 2 cards add up to 21 (an Ace and a card valued 10), that’s Blackjack! \n" +
                "If they have any other total, decide whether you wish to ‘draw’ or ‘stay’. \n" +
                "You can continue to draw cards until you are happy with your hand.\n" +
                "\n" +
                "Once all cards are drawn, whoever has a total closer to 21 than the dealer wins. \n\n" +
                "If player’s hand and dealer’s hand have an equal value, it’s a tie.\n" +
                "\n" +
                "All winning bets are paid 1:1 but when you get Blackjack you get paid 150%" +
                "\n\n\n" +
                "Click on return button at right to go back to homepage");

        text.setEditable(false);
        text.setWrapText(true);
        text.setPrefSize(400,400);
        return text;
    }

    Button createButton1(String buttonName){
        Button newB = new Button(buttonName);
        newB.setAlignment(Pos.CENTER);
        return newB;
    }

    Background createBackGroundImage(String src){

        Image backgroundImage = new Image(src);

        // create background with image
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100,100,true,true,true,true));

        return new Background(background);
    }

};
