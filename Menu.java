package sample;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

class MenuItem extends StackPane{
    public MenuItem(String name){
        Rectangle bg = new Rectangle(200,20, Color.WHITE);
        bg.setOpacity(0.5);

        Text text = new Text(name);
        text.setFill(Color.GRAY);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        setAlignment(Pos.CENTER);
        getChildren().addAll(bg, text);
        FillTransition st = new FillTransition(Duration.seconds(0.5), bg);
        setOnMouseEntered(event->{
            st.setFromValue(Color.DARKGRAY);
            st.setToValue(Color.DARKGOLDENROD);
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);
            st.play();
        });

        setOnMouseExited(event->{
            st.stop();
            bg.setFill(Color.WHITE);
        });
    }
}


public class Menu extends Pane {
    static SubMenu subMenu;

    public Menu(SubMenu subMenu){
        Menu.subMenu = subMenu;
        setVisible(false);
        Rectangle bg = new Rectangle(600,600, Color.LIGHTBLUE);
        bg.setOpacity(0.4);
        getChildren().addAll(bg, subMenu);
    }
    public void setSubMenu(SubMenu subMenu){
        getChildren().remove(Menu.subMenu);
        Menu.subMenu = subMenu;
        getChildren().add(subMenu);
    }

}

class SubMenu extends VBox{
    public SubMenu(MenuItem...items){
        setSpacing(15);
        for(MenuItem item : items){
            setTranslateX(200);
            setTranslateY(200);
            getChildren().add(item);
        }
    }
}

