package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SnakeEngine extends JPanel {

    private HighScoreManager highScores = new HighScoreManager();
    private String playerName;

    private JLabel timeLabel;

    private final int FPS = 15;
    public static final int SNAKEY_SPEED = 10;
    public static final int BOARDHEIGHT = 400;
    public static final int BOARDWIDTH = 600;
    public static final int ROCK_COUNT = 15;

    private Image backgroundImage;
    private Image snakeyImage;
    private Image snakeyTailImage;
    private Image fruitsImage;
    private Image rockImage;

    private ArrayList<Snakey> snakey;
    private SnakeyMethods snakeyMethods;
    private Fruits fruits;
    private ArrayList<Rock> rock;
    private boolean gameIsPaused;
    private boolean isEndOfGame;
    private int fruitEatenCounter;
    private long gamePlayTime;
    private long currentTime;

    private Random rand = new Random();
    private Timer frameTimer;
    private String lastpressedkey;

    public SnakeEngine() throws SQLException {
        super();

        backgroundImage = new ImageIcon(ClassLoader.getSystemResource("pics/background.jpg")).getImage();

        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        this.getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lastpressedkey!="DOWN"){
                    lastpressedkey = "UP";
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        this.getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lastpressedkey!="UP"){
                    lastpressedkey = "DOWN";
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        this.getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lastpressedkey!="RIGHT"){
                    lastpressedkey = "LEFT";
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        this.getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lastpressedkey!="LEFT"){
                    lastpressedkey = "RIGHT";
                }
            }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameIsPaused = !gameIsPaused;
            }
        });

        fruitsImage = new ImageIcon(ClassLoader.getSystemResource("pics/fruits.png")).getImage();
        rockImage = new ImageIcon(ClassLoader.getSystemResource("pics/rock.png")).getImage();
        snakeyImage = new ImageIcon(ClassLoader.getSystemResource("pics/snakey.png")).getImage();
        snakeyTailImage = new ImageIcon(ClassLoader.getSystemResource("pics/snakey_tail.png")).getImage();

        this.startnewgame();

        timeLabel = new JLabel(" ");
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);

        frameTimer = new Timer(1000/FPS, new FrameListener());
        frameTimer.start();
    }

    public void startnewgame(){

        playerName = null;
        gameIsPaused = false;
        isEndOfGame = false;
        fruitEatenCounter = 0;
        currentTime = System.currentTimeMillis();
        generateSnakey();
        generateRocks(ROCK_COUNT);
        generateFruit();
    }

    private void generateSnakey(){
        snakeyMethods = new SnakeyMethods();
        snakey = new ArrayList<>();

        int initDirection = rand.nextInt(4);

        switch (initDirection){
            case 0:
                lastpressedkey = "LEFT";
                snakeyMethods.getSnakeyPoints().add(new Point(snakeyMethods.getSnakeyPoints().get(0).getX()+SNAKEY_SPEED, snakeyMethods.getSnakeyPoints().get(0).getY()));
                break;
            case 1:
                lastpressedkey = "RIGHT";
                snakeyMethods.getSnakeyPoints().add(new Point(snakeyMethods.getSnakeyPoints().get(0).getX()-SNAKEY_SPEED, snakeyMethods.getSnakeyPoints().get(0).getY()));
                break;
            case 2:
                lastpressedkey = "UP";
                snakeyMethods.getSnakeyPoints().add(new Point(snakeyMethods.getSnakeyPoints().get(0).getX(), snakeyMethods.getSnakeyPoints().get(0).getY()+SNAKEY_SPEED));
                break;
            case 3:
                lastpressedkey = "DOWN";
                snakeyMethods.getSnakeyPoints().add(new Point(snakeyMethods.getSnakeyPoints().get(0).getX(), snakeyMethods.getSnakeyPoints().get(0).getY()-SNAKEY_SPEED));
                break;
        }

        for (int i = 0; i< snakeyMethods.getSnakeyPoints().size()-1; i++){
            snakey.add(new Snakey(snakeyMethods.getSnakeyPoints().get(i).getX(), snakeyMethods.getSnakeyPoints().get(i).getY(), 10, 10, snakeyImage));
        }
        snakey.add(new Snakey(snakeyMethods.getSnakeyPoints().get(snakeyMethods.getSnakeyPoints().size()-1).getX(), snakeyMethods.getSnakeyPoints().get(snakeyMethods.getSnakeyPoints().size()-1).getY(), 10, 10, snakeyTailImage));
    }

    private void generateRocks(int countOfRocks){
        rock = new ArrayList<>();
        Rock temp = new Rock(rand.nextInt(BOARDWIDTH-20), rand.nextInt(BOARDHEIGHT-20), 20, 20, rockImage);
        for (int i =0; i<countOfRocks; i++){
            boolean isCollidesWithRocksOrSnakey = true;
            while(isCollidesWithRocksOrSnakey){
                isCollidesWithRocksOrSnakey = false;
                temp = new Rock(rand.nextInt(BOARDWIDTH-20), rand.nextInt(BOARDHEIGHT-20), 20, 20, rockImage);
                for (int j = 0; j<rock.size(); j++){
                    if(temp.isMet(rock.get(j))){
                        isCollidesWithRocksOrSnakey = true;
                    }
                }
                for(int k=0; k<snakey.size(); k++){
                    if(temp.isMet(snakey.get(k))){
                        isCollidesWithRocksOrSnakey = true;
                    }
                }
            }
            rock.add(temp);
        }
    }
    private void generateFruit(){
        Fruits temp = new Fruits(rand.nextInt(BOARDWIDTH-20), rand.nextInt(BOARDHEIGHT-20), 20, 20, fruitsImage);
        boolean isCollidesWithOthers = true;
        while(isCollidesWithOthers){
            isCollidesWithOthers = false;
            temp = new Fruits(rand.nextInt(BOARDWIDTH-20), rand.nextInt(BOARDHEIGHT-20), 20, 20, fruitsImage);
            for (int j = 0; j<rock.size(); j++){
                if(temp.isMet(rock.get(j))){
                    isCollidesWithOthers = true;
                }
            }
            for(int k=0; k<snakey.size(); k++){
                if(temp.isMet(snakey.get(k))){
                    isCollidesWithOthers = true;
                }
            }
        }
        fruits = temp;
    }

    private boolean isCollidedWithRock(){
        boolean result=false;
        for(int i=0; i<rock.size(); i++){
            if(snakey.get(0).isMet(rock.get(i))){
                result=true;
            }
        }
        return result;
    }

    private boolean isRunOutOfTrack(){
        boolean result=false;
        if(snakey.get(0).getX()>=BOARDWIDTH || snakey.get(0).getY()>=BOARDHEIGHT || snakey.get(0).getX()<0 || snakey.get(0).getY()<0){
            result = true;
        }
        return result;
    }

    private boolean isCollidedWithItself(){
        boolean result=false;
        for(int i=2; i<snakey.size(); i++){
            if(snakey.get(0).isMet(snakey.get(i))){
                result=true;
            }
        }
        return result;
    }

    private void updateSnakeyAfterMove(){
        if(!snakey.get(0).isMet(fruits)){
            snakey.remove(snakey.size()-1);

            Snakey tail = snakey.get(snakey.size()-1);
            snakey.remove(snakey.size()-1);

            snakey.add(new Snakey(tail.getX(), tail.getY(), 10, 10, snakeyTailImage));
        }
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public HighScoreManager getHighScores(){
        return highScores;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, BOARDWIDTH, BOARDHEIGHT, null);

        for(int i=0; i<snakey.size(); i++){
            snakey.get(i).draw(g);
        }

        fruits.draw(g);
        for(int i = 0; i<rock.size(); i++){
            rock.get(i).draw(g);
        }
    }

    class FrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            if(!gameIsPaused){
                isEndOfGame = isCollidedWithRock() || isRunOutOfTrack() || isCollidedWithItself();

                snakeyMethods.move(lastpressedkey);

                snakey.add(0, new Snakey(snakeyMethods.getSnakeyPoints().get(snakeyMethods.getSnakeyPoints().size()-1).getX(), snakeyMethods.getSnakeyPoints().get(snakeyMethods.getSnakeyPoints().size()-1).getY(), 10, 10, snakeyImage));

                updateSnakeyAfterMove();

                if(snakey.get(0).isMet(fruits)){
                    fruitEatenCounter++;
                    generateFruit();
                }
            }

            if(isEndOfGame){
                playerName = JOptionPane.showInputDialog("Congratulations!\n" + "Your score is " + fruitEatenCounter + "\n"+ "Give your name:");

                if (playerName != null){
                    try {
                        highScores.putHighScore(playerName, fruitEatenCounter);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                startnewgame();
            }

            gamePlayTime = System.currentTimeMillis();

            int elapsedtime =(int) ((gamePlayTime-currentTime)/1000);

            timeLabel.setText(String.valueOf(elapsedtime + " sec "));

            repaint();
        }
    }
}
