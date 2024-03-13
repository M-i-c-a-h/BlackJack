
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.animation.PauseTransition;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * GUI of BlackjackGame
 */
public class BlackjackGame extends Application {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    ArrayList<Card> playerHand;
    ArrayList<Card> bankerHand;
    BlackjackDealer theDealer;
    BlackjackGameLogic gameLogic;
    private double currentBet = 0.0;
    private double totalWinnings = 0.0;
    private TextField totalWinsAmount, currBetAmount;
    private  Button Start, UpdateBet, Stay, Hit;
    private HBox displayBox;
    private HBox dealerBox = new HBox();
    private HBox deckBox = new HBox();
    private VBox buttonBox = new VBox();
    private HBox playerBox = new HBox();
    private Button HOME, advance;

    private HashMap<String,Scene> scenes = new HashMap<>();
    Scene homepage, rules, banking, game;
    private boolean gameMode = false;
    private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BlackJack");

        HOME = createHomeButton(primaryStage);
        addScenes(primaryStage);
        primaryStage.setScene(scenes.get("homepage"));
        primaryStage.show();
        // Set focus off TextField after the scene is shown
        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());

    }
    private void initialiseGame(){

        theDealer = new BlackjackDealer();
        theDealer.generateDeck();
        theDealer.shuffleDeck();

        playerHand = new ArrayList<>();
        playerHand = theDealer.dealHand();

        bankerHand = new ArrayList<>();
        bankerHand = theDealer.dealHand();

        gameLogic = new BlackjackGameLogic();
        dealerBox.getChildren().clear();
        playerBox.getChildren().clear();
    }
    public void playGame(Stage primaryStage){

        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());

        Hit.setOnAction(ev1->{
           attemptToHit(playerHand, primaryStage);
        });

        Stay.setOnAction(ev2->{
            if(!checkStatus(playerHand)){
                makeBankersMove();
            }
            end(primaryStage);
        });

    }
    /**
     * method will determine if the user won or lost their bet and
     * return the amount won or lost based on the value in currentBet
     * @return profit
     */
    public double evaluateWinnings(){
        String result = gameLogic.whoWon(playerHand, bankerHand);

        switch(result){
            case "push":    // game is tied
                return currentBet;
            case "dealer":  // dealer wins
                return  (-1 * currentBet);
            case "player":  // player wins by blackjack
                if(blackjackWin(playerHand)){
                    return (1.50 * currentBet) + currentBet;
                }
        }
        return currentBet * 2;  // player has a normal win
    }
    private boolean blackjackWin(ArrayList<Card>hand){
        int sum = 0; boolean hasAnAce = false; boolean hasFaceCard = false;
        for(Card card : hand){
            if(card.suit.contains("ace")){ hasAnAce = true; }
            if(card.suit.contains("queen") || card.suit.contains("king") || card.suit.contains("jack")){ hasFaceCard = true; }
            sum += card.value;
        }
        // if player has an ace card and a face card -> player gets a blackjack win
        return (hasAnAce && hasFaceCard) && (sum + 10 == 21);
    }
    boolean checkStatus(ArrayList<Card> opponent){
        return bust(opponent);
    }
    private void makeBankersMove(){
        while(gameLogic.evaluateBankerDraw(bankerHand)){
            bankerHand.add(theDealer.drawOne());
            updateDealerBox(dealerBox,false);
        }
    }
    private void attemptToHit(ArrayList<Card> aPlayer, Stage primaryStage){
        if(bust(aPlayer)) { end(primaryStage); return;}
        hit(aPlayer);
        updatePlayerBox(playerBox);
    }

    boolean bust(ArrayList<Card> busted){return (gameLogic.handTotal(busted) > 21);}
    void hit(ArrayList<Card> playerToHit){playerToHit.add(theDealer.drawOne());}
    void end(Stage primaryStage){

        updateBoxes(true);
        gameMode = false;
        String result = gameLogic.whoWon(playerHand,bankerHand);
        System.out.println("banker had " + gameLogic.handTotal(bankerHand));
        System.out.println("player had " +gameLogic.handTotal(playerHand));
        System.out.println(result + "won!");


        // alert user on game result
        Alert alert = createAlert("Round result",result);
        alert.showAndWait();

        double val = evaluateWinnings();
        if(val >= 0){
            totalWinnings += val;
            updateTextAmountField("totalWinsAmount");
        }

        // allow another round, if user has sufficient funds
        if(totalWinnings == 0){
            advance.setDisable(true);

            Alert alert1 = createAlert(null,"null");
            alert1.showAndWait();

            primaryStage.setScene(scenes.get("homepage"));
        }
        else{
            Start.setDisable(false);
        }
        currBetAmount.setDisable(false);
        UpdateBet.setDisable(false);
        Hit.setDisable(true);
        Stay.setDisable(true);
        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());
    }
    private Alert createAlert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(getAlertResult(text));

        ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonType);

        return alert;
    }
    private String getAlertResult(String result){
        switch(result){
            case "push":
                return "Game tied! Refund processed";
            case "dealer":
                return "You lost your bet, dealer won\nBetter luck next time";
            case "player":
                return "Congratulations! You won the game.\nYour earnings has been added to your account";

        }
        return "You have been kicked out of the game due to insufficient funds." + "\n" +
                "Please go to bank to continue playing.";
    }
    private void addScenes(Stage primaryStage){
        scenes = new HashMap<>();
        homepage = buildHomepage(primaryStage);
        rules = buildRules(primaryStage);
        banking = buildBankingPage(primaryStage);

        scenes.put("homepage", homepage);
        scenes.put("rules", rules);
        scenes.put("banking",banking);
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
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished( e2->{
                primaryStage.setScene(scenes.get("rules"));
            });
            pause.play();
        });

        Button homeB2 = createButton1("Go to bank");
        homeB2.setOnAction(e3 -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished( e2->{
                primaryStage.setScene(scenes.get("banking"));
            });
            pause.play();
        });


        Button homeB3 = createButton1("Quick play");
        homeB3.setOnAction(special->{
            totalWinnings = 500.0;
            currentBet = 300;
            initialiseGame();
            game = buildGameScene(primaryStage); //Todo: is this needed?
            scenes.put("game",game);
            primaryStage.setScene(scenes.get("game"));
        });

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
        return new Scene(root, screenWidth, screenHeight);
    }
    private Scene buildRules(Stage primaryStage){

        // Create a background with the background image
        Background backgroundWithImage = createBackGroundImage("images/rules2.jpg");

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

        return new Scene(root, screenWidth, screenHeight);
    }

    private static TextArea getRulesTextArea() {
        TextArea text = new TextArea("Welcome to Blackjack by Micah\n" +
                "\n" +
                "\n" +
                "To start:\n\n" +
                "Use Quick play to begin game with $500 deposited in your account" +
                "with a starting bet of $300. You can change starting bet before hitting START." +
                "\n\n" +
                "Additional you can deposit money and place starting bet\n" +
                "in our bank and enjoy your winnings." +
                "\n\n\n" +
                "Rules:\n" +
                "\n" +
                " The objective is to have a hand with a total value higher than the dealer’s without going over 21. \n\n" +
                "Kings, Queens, Jacks, and Tens are worth a value of 10. An Ace has a value of 1 or 11. " +
                "The remaining cards are counted at face value.\n" +
                "\n" +
                " You are dealt two cards each whilst the dealer is dealt one face up. \n\n" +
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
    private Scene buildBankingPage (Stage primaryStage){
        // create bank background
        Background bankBackground = createBackGroundImage("images/board.jpg");

        // create layout
        VBox root = new VBox();
        root.setBackground(bankBackground);

        // Create a StackPane for the text area to maintain background
        StackPane textPane = new StackPane();

        // create text area for bank page
        TextArea bankText = createBankText();

        textPane.getChildren().add(bankText);
        StackPane.setAlignment(textPane,Pos.CENTER);

        // create buttons
        advance = createButton1("Advance");
        advance.setPrefSize(100, 70);
        advance.setDisable(true);
        advance.setOnAction(e->{
            initialiseGame();
            game = buildGameScene(primaryStage);
            scenes.put("game",game);
            primaryStage.setScene(scenes.get("game"));
        });


        HBox top = new HBox();
        top.setSpacing(80);
        top.getChildren().addAll(textPane,advance);
        top.setAlignment(Pos.TOP_CENTER);
        top.setPadding(new Insets(0,0,0,0));

        // create VBox
        HBox homeBox = new HBox();

        Button home = createHomeButton(primaryStage);
        homeBox.getChildren().add(home);
        homeBox.setAlignment(Pos.TOP_RIGHT);

        VBox bar = new VBox(100,homeBox,top);
        bar.setAlignment(Pos.CENTER);
        // create bank account
        TextField bankAcc = createTextField("Enter amount to deposit");
        bankAcc.setOnAction(actionEvent -> {
            double val = Double.parseDouble(bankAcc.getText());
            if(val <= 0){
                bankAcc.clear();
                bankAcc.setPromptText("Invalid amount");
            }
            else{
                totalWinnings = val;
                if(totalWinnings > 0 && currentBet > 0 && totalWinnings >= currentBet){ advance.setDisable(false);}
            }

        });
        // create initial bet
        TextField initialBet = createTextField("Enter starting bet");
        initialBet.setOnAction(actionEvent -> {
            double val = Double.parseDouble(initialBet.getText());
            if(val < 0){
                initialBet.clear();
                initialBet.setPromptText("Invalid amount");
            }
            else{
                currentBet = val;
                if(totalWinnings > 0 && currentBet > 0 ){ advance.setDisable(false);}
            }

        });


        HBox middle = new HBox(50,bankAcc, initialBet);
        middle.setAlignment(Pos.CENTER);
        middle.setPadding(new Insets(0,200,0,0));


        root.getChildren().addAll(bar, middle);
        root.setSpacing(80);

        return new Scene(root, screenWidth, screenHeight);
    }
    private Scene buildGameScene(Stage primaryStage){
        // create background
        Background gameBackground = createBackGroundImage("images/backd2.jpg");

        // create VBox
        VBox gameRoot = new VBox();

        // set background
        gameRoot.setBackground(gameBackground);

        // create displayBox
        displayBox = createDisplayBox(primaryStage);

        deckBox = createDeckBox();
        buttonBox = createButtonBox();
        HBox bottom = new HBox(500,buttonBox,playerBox);
        bottom.setPadding(new Insets(80,50,50,50));

        gameRoot.setSpacing(20);
        gameRoot.getChildren().addAll(displayBox,dealerBox,deckBox,bottom);

        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());
        return new Scene(gameRoot, screenWidth, screenHeight);
    }
    private VBox createButtonBox(){
        Stay = createButton2("STAY");
        Stay.setTextFill(Paint.valueOf("red"));

        Hit = createButton2("HIT");
        Hit.setTextFill(Paint.valueOf("green"));

        return new VBox(50,Hit, Stay);
    }

    private HBox createDisplayBox(Stage primaryStage){
        // instance textArea
        TextField totalWins = createTextField2("Total winnings:");
        totalWins.setPrefWidth(100);
        totalWinsAmount = createTextField2("$");
        totalWinsAmount.setPrefWidth(200);
        updateTextAmountField("totalWinsAmount");

        TextField currBet = createTextField2("Current bet:");
        currBet.setPrefWidth(100);
        currBetAmount = createTextField2("$");
        currBetAmount.setPrefWidth(200);
        updateTextAmountField("currBetAmount");

        // instance buttons
        UpdateBet = createButton1("Update bet");
        UpdateBet.setPrefSize(80,10);

        UpdateBet.setOnAction(ev->{
            currBetAmount.setEditable(true);
            currBetAmount.clear();
        });
        // listen for change in curr bet
        currBetAmount.setOnAction(ev2 -> {
            currentBet = Double.parseDouble(currBetAmount.getText());
            updateTextAmountField("currBetAmount");

        });

        Start = createButton1("Start");
        Start.setPrefSize(80,10);
        Start.setOnAction(ev3->{
            // start game if user has sufficient funds
            if(totalWinnings >= currentBet){
                UpdateBet.setDisable(true);
                totalWinnings -= currentBet;
                updateTextAmountField("totalWinsAmount");
                currBetAmount.setDisable(true);
                gameMode = true;
                initialiseGame();
                updateBoxes(false);
                Stay.setDisable(false);
                Hit.setDisable(false);
                playGame(primaryStage);
            }
        });

        // create HBox homeBox
        HBox homeBox = new HBox();
        homeBox.getChildren().add(HOME);
        homeBox.setAlignment(Pos.TOP_RIGHT);

        // group into HBox
        VBox vBox1 = new VBox(totalWins,currBet);
        VBox vBox2 = new VBox(totalWinsAmount,currBetAmount);
        VBox vBox3 = new VBox(Start,UpdateBet);

        HBox grp1 = new HBox(vBox1,vBox2,vBox3);
        return new HBox(1000,grp1,homeBox);
    }
    private HBox createPlayerBox(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        // create image for dealers card;
        createCardImage(playerHand, hBox);
        return hBox;
    }
    private  HBox createDeckBox(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(20,0,0,20));
        String fileName = "images/cardsF/deckback.png";
        for(int i=0;i<2;i++){
            Image image = new Image(fileName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);     imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            hBox.getChildren().add(imageView);
        }

        return hBox;
    }
    private Button createHomeButton(Stage primaryStage){
        Button home =  createButton2("Home");
        home.setDisable(false);
        home.setOnAction(ev->primaryStage.setScene(scenes.get("homepage")));
        return home;
    }
    private HBox createDealerBox(boolean reveal){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(50,60,0,20));
        // create image for dealers card;
        if(reveal){
            createCardImage(bankerHand, hBox);
        }
        else{
            createCardImageDealer(bankerHand, hBox);
        }
        return hBox;
    }
    private void createCardImageDealer(ArrayList<Card> hand, HBox hBox){
        int i = 0;
        for(Card card : hand){
            String fileName = String.format("images/cardsF/%s.png", card.suit);
            if(i == 0){fileName =  "images/cardsF/deckback.png"; i++;}

            Image image = new Image(fileName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);     imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            hBox.getChildren().add(imageView);
        }
    }
    private void createCardImage(ArrayList<Card> hand, HBox hBox){
        for(Card card : hand){
            String fileName = String.format("images/cardsF/%s.png", card.suit);
            Image image = new Image(fileName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);     imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            hBox.getChildren().add(imageView);
        }
    }
    private void updateDealerBox(HBox whichBox, boolean reveal) {
        whichBox.getChildren().clear(); // Clear existing content
        whichBox.getChildren().add(createDealerBox(reveal)); // Add updated content
    }
    private void updatePlayerBox(HBox whichBox) {
        whichBox.getChildren().clear(); // Clear existing content
        whichBox.getChildren().add(createPlayerBox()); // Add updated content
    }
    private void updateBoxes(boolean reveal){
        updateDealerBox(dealerBox, reveal); // Update dealer box
        updatePlayerBox(playerBox); // update player box
    }

    private static TextField createTextField2(String desc){

        TextField textField = new TextField(desc);
        textField.setEditable(false);
        textField.setAlignment(Pos.CENTER);
        textField.setPrefSize(50,10);
        return textField;
    }
    private static TextField createTextField(String prompt){
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setAlignment(Pos.CENTER);
        textField.setPrefSize(150,20);
        return textField;
    }
    private static TextArea createBankText(){
        TextArea bankText = new TextArea("To begin game add money via the bank area below."+
                "\n" +
                " Then place a starting your desired starting bet, which is drawn from you bank account" +
                "\n" +
                "You can update your bet amount at the start of each round while there is sufficient funds." +
                "\n" +
                "If bank becomes empty you will be kicked out of game and will be required \nto restart game to continue playing" +
                "\n\n" +
                "Click the advance button to begin\n"+
                "Enjoy!!!!!");
        bankText.setEditable(false);
        bankText.setWrapText(false);
        bankText.setPrefSize(500, 150);
        return bankText;
    }
    private void updateTextAmountField(String whichTextField){
        if(whichTextField.equals("totalWinsAmount")){
            totalWinsAmount.setText(String.format("$%.2f",totalWinnings));
        }
        else{
            currBetAmount.setText(String.format("$%.2f",currentBet));
        }
    }

    Button createButton1(String buttonName){
        Button newB = new Button(buttonName);
        newB.setAlignment(Pos.CENTER);
        return newB;
    }
    Button createButton2(String buttonName){
        Button newB = new Button(buttonName);
        newB.setAlignment(Pos.CENTER);
        newB.setDisable(true); // until game start
        newB.setPrefSize(100, 50);
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
