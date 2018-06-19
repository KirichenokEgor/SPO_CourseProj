package sample;


import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * class Wall
 * consists rectangle and it's height
 */
public class Wall extends Pane{
    Rectangle rect;
    private int height;

    /**
     * constructor of Wall class
     * @param height
     */
    public Wall(int height){
        this.height = height;
        rect = new Rectangle(20, height);

        getChildren().add(rect);
    }

}