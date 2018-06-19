package sample;

import java.util.ArrayList;

public class SavedMatch {
    public ArrayList<Wall> walls;// = new ArrayList<>();
    public static ArrayList<Double> taps;// = new ArrayList<>();
    public Integer step;
    public Integer wall_number;
    //public String name;// = new String();

    public SavedMatch(Integer wall_number, Integer step, ArrayList<Double> taps, ArrayList<Wall> walls) {
        //this.name = name;
        this.taps = (ArrayList<Double>) taps.clone();
        this.walls = (ArrayList<Wall>) walls.clone();
        this.wall_number = wall_number;
        this.step = step;
    }


}
