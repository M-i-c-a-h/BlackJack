
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.animation.PauseTransition;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.HashMap;

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

    private ArrayList<Card> playerHand;
    private ArrayList<Card> bankerHand;
    private BlackjackDealer theDealer;
    private BlackjackGameLogic gameLogic;
    private double currentBet = 0.0;
    private double startingAmount = 0.0;
    private double totalWinnings = 0.0;
    private TextField totalWinsAmount, currBetAmount;
    private  Button Start, UpdateBet, Stay, Hit;
    private HBox displayBox;
    private HBox dealerBox = new HBox();
    private HBox deckBox = new HBox();
    private VBox buttonBox = new VBox();
    private HBox playerBox = new HBox();

    private HashMap<String,Scene> scenes = new HashMap<>();
    Scene homepage, rules, banking, game;
    private boolean gameMode = false;

    /**
     * method will determine if the user won or lost their bet and
     * return the amount won or lost based on the value in currentBet
     * @return profit
     */
    public double evaluateWinnings(){
        if(startingAmount == totalWinnings){
            return -currentBet;
        }

        double valToReturn = totalWinnings - startingAmount;
        startingAmount = totalWinnings; // update startingAmount
        return valToReturn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BlackJack");
        addScenes(primaryStage);
        primaryStage.setScene(scenes.get("homepage"));


        primaryStage.show();
        // Set focus off TextField after the scene is shown
        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());

    }
    public void playGame(){

        startingAmount = totalWinnings;

        theDealer = new BlackjackDealer();
        theDealer.generateDeck();
        theDealer.shuffleDeck();

        playerHand = new ArrayList<>();
        playerHand = theDealer.dealHand();

        bankerHand = new ArrayList<>();
        bankerHand = theDealer.dealHand();

        gameLogic = new BlackjackGameLogic();


    }

    boolean bust(ArrayList<Card> busted){
        return (gameLogic.handTotal(busted) > 21);
    }
    void hit(ArrayList<Card> playerToHit){
        playerToHit.add(theDealer.drawOne());
    }
    void stay(){

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

        // Todo: this is my cheat button for debugging pls remove on completion!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Button homeB3 = createButton1("Begin");
        //homeB3.setDisable(true);    // disable begin until bet is placed
        homeB3.setOnAction(special->{
            totalWinnings = 500.0;
            currentBet = 300;
            game = buildGameScene(primaryStage); //Todo: is this needed?
            scenes.put("game",game);
            primaryStage.setScene(scenes.get("game"));
        });

        // Todo: this is my cheat button for debugging pls remove on completion!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

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
        return new Scene(root, 800, 700);
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

        return new Scene(root, 800, 700);
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
        Button advance = createButton1("Advance");
        advance.setPrefSize(100, 70);
        advance.setDisable(true);
        advance.setOnAction(e->{
            game = buildGameScene(primaryStage); //Todo: is this needed?
            scenes.put("game",game);
            primaryStage.setScene(scenes.get("game"));
        });

        // create VBox
        HBox top = new HBox();
        top.setSpacing(80);
        top.getChildren().addAll(textPane,advance);
        top.setAlignment(Pos.TOP_CENTER);
        top.setPadding(new Insets(50,0,50,0));

        // create bank account
        TextField bankAcc = createTextField("Enter amount to deposit");
        bankAcc.setOnAction(actionEvent -> {
            double val = Double.parseDouble(bankAcc.getText()); //Todo try catch here
            if(val <= 0){
                bankAcc.clear();
                bankAcc.setPromptText("Invalid amount");
            }
            else{
                totalWinnings = val;
                advance.setDisable(false);
            }


        });
        // create initial bet
        TextField initialBet = createTextField("Enter starting bet");
        initialBet.setOnAction(actionEvent -> {
            double val = Double.parseDouble(initialBet.getText()); //Todo try catch here
            if(val < 0){
                initialBet.clear();
                initialBet.setPromptText("Invalid amount");
            }
            else{
                currentBet = val;
            }

        });

        HBox middle = new HBox(50,bankAcc, initialBet);
        middle.setAlignment(Pos.CENTER);


        root.getChildren().addAll(top, middle);

        return new Scene(root, 800, 700);
    }
    private Scene buildGameScene(Stage primaryStage){
        // create background
        Background gameBackground = createBackGroundImage("images/board.jpg");

        // create VBox
        VBox gameRoot = new VBox();

        // set background
        gameRoot.setBackground(gameBackground);

        // create displayBox
        displayBox = createDisplayBox(primaryStage);
        deckBox = createDeckBox();
        buttonBox = createButtonBox();
        HBox bottom = new HBox(200,buttonBox,playerBox);
        bottom.setPadding(new Insets(50,50,50,50));

        gameRoot.setSpacing(20);
        gameRoot.getChildren().addAll(displayBox,dealerBox,deckBox,bottom);

        Platform.runLater(() -> primaryStage.getScene().getRoot().requestFocus());
        return new Scene(gameRoot, 800, 700);
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
        totalWinsAmount.setText("$" + totalWinnings);

        TextField currBet = createTextField2("Current bet:");
        currBet.setPrefWidth(100);
        currBetAmount = createTextField2("$");
        currBetAmount.setPrefWidth(200);
        currBetAmount.setText("$" + currentBet);

        // instance buttons
        UpdateBet = createButton1("Update bet");
        UpdateBet.setPrefSize(50,10);
        UpdateBet.setOnAction(ev->{
            currBetAmount.setEditable(true);
            currBetAmount.clear();
        });
        // listen for change in curr bet
        currBetAmount.setOnAction(ev2 -> {
            currentBet = Double.parseDouble(currBetAmount.getText());
            currBetAmount.setText("$ " + currentBet);

        });

        Start = createButton1("Start");
        Start.setPrefSize(50,10);
        Start.setOnAction(ev3->{
            // start game if user has sufficient funds
            if(totalWinnings >= currentBet){
                UpdateBet.setDisable(true);
                totalWinnings -= currentBet;
                totalWinsAmount.setText("$" + totalWinnings);
                currBetAmount.setDisable(true);
                gameMode = true;
                playGame();
                updateBoxes(primaryStage);
                Stay.setDisable(false);
                Hit.setDisable(false);
            }

            // Todo: clear deck
        });
        // permit change to current bet if user has some amount of money
        if(currentBet > totalWinnings){
            UpdateBet.setDisable(false);
            Start.setDisable(true);
        }

        // group into HBox
        VBox vBox1 = new VBox(totalWins,currBet);
        VBox vBox2 = new VBox(totalWinsAmount,currBetAmount);
        VBox vBox3 = new VBox(Start,UpdateBet);

        return new HBox(vBox1,vBox2,vBox3);
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
    private HBox createDealerBox(){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0,20,0,20));
        // create image for dealers card;
        createCardImage(bankerHand, hBox);
        return hBox;
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
    private void updateDealerBox(HBox whichBox) {
        whichBox.getChildren().clear(); // Clear existing content
        whichBox.getChildren().add(createDealerBox()); // Add updated content
    }
    private void updatePlayerBox(HBox whichBox) {
        whichBox.getChildren().clear(); // Clear existing content
        whichBox.getChildren().add(createPlayerBox()); // Add updated content
    }
    private void updateBoxes(Stage primaryStage){
        updateDealerBox(dealerBox); // Update dealer box
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
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    void test(){
        playGame();
        System.out.println("banker had " + gameLogic.handTotal(bankerHand));
        System.out.println("player had " +gameLogic.handTotal(playerHand));
        System.out.println("---------------------------");

        int count = 0;
        while(gameMode){
            if(!bust(playerHand)){
                hit(playerHand);
                if(bust(playerHand)){
                    end();
                    break;
                }
            }
            else{
                end();
                break;

            }

            while(gameLogic.evaluateBankerDraw(bankerHand)){
                hit(bankerHand);
            }
            count++;

            if(count == 2){
                gameMode = false;
                System.out.println("ending loop");
                String result = gameLogic.whoWon(playerHand,bankerHand);
                System.out.println(result +" won!");
            }
            System.out.println("banker had " + gameLogic.handTotal(bankerHand));
            System.out.println("player had " +gameLogic.handTotal(playerHand));

        }
    }
    void end(){
        gameMode = false;
        System.out.println("banker had " + gameLogic.handTotal(bankerHand));
        System.out.println("player had " +gameLogic.handTotal(playerHand));
        System.out.println("dealer won!");
    }
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
    // Todo: this is a test!!!!!!!!!!!!!!!!!!!!!!!!!!**********************************************
};
