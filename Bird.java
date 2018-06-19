package sample;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class Bird
 * Consists rectangle and point2D, which defines speed
 */
public class Bird extends Pane {
    public Point2D velocity;
    private Rectangle rect;

    //Wall intersect_wall;
    boolean intersectionX = false;
    //boolean intersectionY = false;

    /**
     * constructor of Bird class
     */
    public Bird(){
        rect = new Rectangle(20,20, Color.RED);
        velocity = new Point2D(0,0);
        setTranslateX(100);
        setTranslateY(400);//300
        getChildren().addAll(rect);
    }

    /**
     * Changes Y position of Bird
     * @param value
     */
    public void moveY(int value){
        boolean moveDown = value>0? true:false;
        for(int i = 0; i < Math.abs(value); i++){
            for(Wall w : FlappyRectangle.walls){
                if(this.getBoundsInParent().intersects(w.getBoundsInParent())){
                    //intersectionY = true;
                    //intersect_wall = w;
                    if(moveDown){
                        setTranslateY(getTranslateY() - 1);
                        //FlappyRectangle.score--;
                        return;
                    } else{
                        setTranslateY((getTranslateY() + 1));
                        //FlappyRectangle.score--;
                        return;
                    }
                }
            }

            if(getTranslateY() < 0){
                setTranslateY(0);
                //FlappyRectangle.score--;
            }
            if(getTranslateY() > 580){
                setTranslateY(580);
                //FlappyRectangle.score--;
            }
            setTranslateY(getTranslateY() + (moveDown ? 1 : -1));
        }
    }


    /**
     * Changes X position of Bird
     * @param value - offset on X
     */
    public void moveX(int value){
        for(int i = 0; i < value; i++){
            if(FlappyRectangle.SAVED){
                for(Double tap : SavedMatch.taps){//sm.taps
                    if (getTranslateX() == tap/* || bird.getTranslateX() == 105 || bird.getTranslateX() == 110 || bird.getTranslateX() == 279*/) jump();
                }
            }
            setTranslateX(getTranslateX() + 1);
            for(Wall w : FlappyRectangle.walls) {
                if (this.getBoundsInParent().intersects(w.getBoundsInParent())) {
                    intersectionX = true;
                    //intersect_wall = w;
                    if (getTranslateX() + 20 == w.getTranslateX()) {
                        setTranslateX(getTranslateX() - 1);
                        //FlappyRectangle.score--;
                        //Wall temp_wall = w;
                        return;
                    }
                }
                if (getTranslateX() == w.getTranslateX() + 20) {
                    if(intersectionX /*|| intersectionY*/) { intersectionX = false; /*intersectionY = false;*/ return;}
                    FlappyRectangle.score++;
                    return;
                }
            }
        }
    }

    /**
     * Changes position of the bird(makes jump)
     */
    public void jump(){
        velocity = new Point2D(3,-15);
        //System.out.println("x: " + getTranslateX() + ", y: " + getTranslateY());
        //velocity.add(3,-15);
    }
}