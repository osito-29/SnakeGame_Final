package snakegame;

import java.util.ArrayList;

import static snakegame.SnakeEngine.*;

public class SnakeyMethods {

        private ArrayList<Point> snakeyPoints = new ArrayList<Point>();

        public ArrayList<Point> getSnakeyPoints(){
            return this.snakeyPoints;
        }

        public void setSnakeyPoints(ArrayList<Point> arrayList){
            this.snakeyPoints = arrayList;
        }

        public SnakeyMethods(){
            this.snakeyPoints.add(new Point((BOARDWIDTH+SNAKEY_SPEED)/2, (BOARDHEIGHT+SNAKEY_SPEED)/2));
        }

        public void move(String direction){
            if(direction=="UP"){
                this.snakeyPoints.add(0, new Point(snakeyPoints.get(0).getX(), snakeyPoints.get(0).getY()-SNAKEY_SPEED));
                this.snakeyPoints.remove(snakeyPoints.size()-1);
            }
            if(direction=="DOWN"){

                this.snakeyPoints.add(0, new Point(snakeyPoints.get(0).getX(), snakeyPoints.get(0).getY()+SNAKEY_SPEED));
                this.snakeyPoints.remove(snakeyPoints.size()-1);
            }
            if(direction=="LEFT"){
                this.snakeyPoints.add(0, new Point(snakeyPoints.get(0).getX()-SNAKEY_SPEED, snakeyPoints.get(0).getY()));
                this.snakeyPoints.remove(snakeyPoints.size()-1);
            }
            if(direction=="RIGHT"){
                this.snakeyPoints.add(0, new Point(snakeyPoints.get(0).getX()+SNAKEY_SPEED, snakeyPoints.get(0).getY()));
                this.snakeyPoints.remove(snakeyPoints.size()-1);
            }
        }
}
