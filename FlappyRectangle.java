package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.DoubleToIntFunction;

import static javafx.application.Platform.exit;

/**
 * Main game class
 * @version 1.1
 * @author KiriEg
 *
 */
public class FlappyRectangle extends Application {
    public static Pane appRoot = new Pane();
    public static Pane gameRoot = new Pane();
    //public Scene scene = new Scene(createMenu());
    static Integer STEP = 250;
    static Integer WALL_NUMBER = 10;
    boolean ENDGAME = false;
    AnimationTimer timer;
    static boolean SAVED = false;
    static boolean GOT_SAVED = false;

    public static ArrayList<Double> taps = new ArrayList<>();
    //public static ArrayList<SavedMatch> SavedMatches = new ArrayList<>();
    public static SavedMatch sm;// = new SavedMatch();

    public static ArrayList<Wall> walls = new ArrayList<>();
    Bird bird = new Bird();
    public static int score = 0;
    public Label scoreLabel = new Label("Score: " + score);

    //public boolean stopped = false;
    //public Rectangle pause;

    /**
     * Creates background, walls, etc.
     * and adds it to roots
     * @return appRoot - the main pane of program
     */
    public Parent createContent() {
        ENDGAME = false;
        bird = new Bird();
        score = 0;
        gameRoot = new Pane();
        appRoot = new Pane();
        Image bg = new Image(getClass().getResourceAsStream("bg.jpg"));
        ImageView img_bg = new ImageView(bg);
        img_bg.setFitHeight(600);
        img_bg.setFitWidth(600);
        appRoot.getChildren().add(img_bg);

        //gameRoot.setPrefSize((WALL_NUMBER + 1)*STEP + 600, 600);//////////////////////////////////


        //SAVED = true;/////////////////////////////////////////


        gameRoot.setPrefSize(600,600);

        if(!SAVED) {
            taps.clear();
            walls = new ArrayList<>();
            int i = 0;
            for (i = 0; i < WALL_NUMBER; i++) {
                int enter = (int) (Math.random() * 100 + 50);
                int height = new Random().nextInt(600 - enter);
                Wall wall1 = new Wall(height);
                wall1.setTranslateX(i * STEP + 600);//350
                wall1.setTranslateY(0);
                walls.add(wall1);

                Wall wall2 = new Wall(600 - enter - height);
                wall2.setTranslateX(i * STEP + 600);//350
                wall2.setTranslateY(height + enter);
                walls.add(wall2);

                gameRoot.getChildren().addAll(wall1, wall2);
            }
            Wall end = new Wall(600);
            end.setTranslateX((i+1) * STEP + 700);
            walls.add(end);
            end.setVisible(true);//false
            gameRoot.getChildren().add(end);
        }
        else {
            for(Wall w : walls) gameRoot.getChildren().add(w);
            score = 0;
         }



        gameRoot.getChildren().add(bird);
        appRoot.getChildren().addAll(gameRoot, scoreLabel);


        return appRoot;
    }

    /**
     * Updates position of Bird(x,y), offset of gameRoot and the score
     */
    public boolean update() {
        if(bird.getTranslateX() == 100) bird.jump();
        if(bird.velocity.getY() < 5){
            bird.velocity = bird.velocity.add(0,1);
        }

        bird.moveX((int)bird.velocity.getX());
        bird.moveY((int)bird.velocity.getY());
        scoreLabel.setText("Score: " + score);

        bird.translateXProperty().addListener((obs, old, newValue)->{
            int offset = newValue.intValue();
            if(offset > 200) gameRoot.setLayoutX(-(offset - 200));
            int step = STEP, wall_number = WALL_NUMBER;



            if(SAVED) {step = sm.step; wall_number = sm.wall_number;}
            if(offset > (step+20)*wall_number + 300) ENDGAME = true;
        });


        return true;
    }

    @Override
    /**
     * The main function of program. Point of entrance.
     */
    public void start(Stage primaryStage) throws Exception {
        gameRoot.setPrefSize(600, 600);

        Image bg = new Image(getClass().getResourceAsStream("bg.jpg"));
        ImageView img_bg = new ImageView(bg);
        img_bg.setFitHeight(600);
        img_bg.setFitWidth(600);
        appRoot.getChildren().add(img_bg);

        ///////create menu
        MenuItem newGame = new MenuItem("NEW GAME");
        MenuItem difficulty = new MenuItem("DIFFICULTY");
        MenuItem myMatches = new MenuItem("PLAY SAVED MATCH");
        MenuItem Exit = new MenuItem("EXIT");
        MenuItem ex = new MenuItem("EXIT");
        MenuItem ext = new MenuItem("EXIT");

        SubMenu mainMenu = new SubMenu(
                newGame, difficulty, myMatches, Exit
        );

        MenuItem easyMode = new MenuItem("EASY");
        MenuItem midMode = new MenuItem("MIDDLE");
        MenuItem hardMode = new MenuItem("HARD");
        MenuItem back = new MenuItem("BACK");

        SubMenu difficultySM = new SubMenu(
                easyMode, midMode, hardMode, back
        );


        MenuItem contin = new MenuItem("CONTINUE");
        MenuItem toMM = new MenuItem("MAIN MENU");
        MenuItem toMainMenu = new MenuItem("MAIN MENU");

        SubMenu ingameMenu = new SubMenu(
                contin, toMM, ex
        );

        MenuItem saveMatch = new MenuItem("SAVE YOUR MATCH");

        SubMenu endgameMenu = new SubMenu(
                saveMatch, toMainMenu, ext
        );

        Menu menu = new Menu(mainMenu);
        menu.setVisible(true);

        appRoot.getChildren().add(menu);
        Scene scene = new Scene(appRoot);

        //bird.jump();///////////////////////////////////////////////////////////////useless
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                if(ENDGAME == true) {
                    timer.stop();
                    //System.out.println("END");
                    menu.setSubMenu(endgameMenu);
                    FadeTransition ft = new FadeTransition(Duration.seconds(1), menu);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.play();
                    menu.setVisible(true);
                }
            }
        };
        //timer.start();



        newGame.setOnMouseClicked(event -> {
            GOT_SAVED = false;
            menu.setSubMenu(ingameMenu);
            menu.setVisible(false);
            Scene scene2 = new Scene(createContent());
            appRoot.getChildren().add(menu);
            gameRoot.setOnMouseClicked(evt->{
                if(!SAVED){
                    bird.jump(); taps.add(bird.getTranslateX());//+20
                }
            });
            primaryStage.setScene(scene2);
            timer.start();
            scene2.setOnKeyPressed(evt -> {
                if(!ENDGAME) {
                    if (evt.getCode() == KeyCode.ESCAPE) {
                        FadeTransition ft = new FadeTransition(Duration.seconds(1), menu);
                        if (!menu.isVisible()) {
                            timer.stop();
                            ft.setFromValue(0);
                            ft.setToValue(1);
                            ft.play();
                            menu.setVisible(true);
                        } else {
                            ft.setFromValue(1);
                            ft.setToValue(0);
                            ft.setOnFinished(evnt -> menu.setVisible(false));
                            ft.play();
                            timer.start();
                        }
                    }
                }
            });
            timer.start();
            //primaryStage.setScene(scene);

        });
        difficulty.setOnMouseClicked(event -> menu.setSubMenu(difficultySM));
        Exit.setOnMouseClicked(event -> System.exit(0));
        ex.setOnMouseClicked(event -> System.exit(0));
        ext.setOnMouseClicked(event -> System.exit(0));
        contin.setOnMouseClicked(event -> {
            menu.setVisible(false);
            timer.start();
        });
        toMM.setOnMouseClicked(event -> {
            SAVED = false;////////////////////////////uncomment
            menu.setSubMenu(mainMenu);
        });
        toMainMenu.setOnMouseClicked(event -> {
            SAVED = false;////////////////////////////uncomment
            menu.setSubMenu(mainMenu);
        });
        easyMode.setOnMouseClicked(event -> {
            STEP = 450;
            menu.setSubMenu(mainMenu);
        });
        midMode.setOnMouseClicked(event -> {
            STEP = 350;
            menu.setSubMenu(mainMenu);
        });
        hardMode.setOnMouseClicked(event -> {
            STEP = 250;
            menu.setSubMenu(mainMenu);
        });
        back.setOnMouseClicked(event -> {
            menu.setSubMenu(mainMenu);
        });

        saveMatch.setOnMouseClicked(event -> {
            //menu.setSubMenu(Matches2);
            sm = new SavedMatch(WALL_NUMBER, STEP, taps, walls);
            menu.setSubMenu(mainMenu);
            GOT_SAVED = true;
        });


        myMatches.setOnMouseClicked(event -> {
            if(GOT_SAVED) {
                menu.setSubMenu(ingameMenu);
                menu.setVisible(false);
                SAVED = true;
                Scene scene2 = new Scene(createContent());
                appRoot.getChildren().add(menu);
                primaryStage.setScene(scene2);
                timer.start();
                scene2.setOnKeyPressed(evt -> {
                    if(!ENDGAME) {
                        if (evt.getCode() == KeyCode.ESCAPE) {
                            FadeTransition ft = new FadeTransition(Duration.seconds(1), menu);
                            if (!menu.isVisible()) {
                                timer.stop();
                                ft.setFromValue(0);
                                ft.setToValue(1);
                                ft.play();
                                menu.setVisible(true);
                            } else {
                                ft.setFromValue(1);
                                ft.setToValue(0);
                                ft.setOnFinished(evnt -> menu.setVisible(false));
                                ft.play();
                                timer.start();
                            }
                        }
                    }
                });
            }
        });



        primaryStage.setScene(scene);
        primaryStage.setTitle("FlappyRectangle");
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}