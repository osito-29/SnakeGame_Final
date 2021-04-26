package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SnakeGUI {

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenuItem newGameMenu;
    private JMenuItem highScoreMenu;
    private JMenuItem exitGameMenu;

    private JPanel boardPanel;

    private SnakeEngine snakeEngine;

    public SnakeGUI () throws SQLException {

        this.frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        this.newGameMenu = new JMenuItem("New game");
        menuBar.add(newGameMenu);
        newGameMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeEngine.startnewgame();
            }
        });

        this.highScoreMenu = new JMenuItem("High Score");
        menuBar.add(highScoreMenu);
        highScoreMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topScore = new String();
                try {
                    topScore = getLeaderBoardText(10);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                JOptionPane.showMessageDialog(boardPanel, topScore,"Leaderboard", JOptionPane.PLAIN_MESSAGE);
            }
        });

        this.exitGameMenu = new JMenuItem("Exit");
        menuBar.add(exitGameMenu);
        exitGameMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.snakeEngine = new SnakeEngine();
        snakeEngine.setPreferredSize(new Dimension(snakeEngine.BOARDWIDTH, snakeEngine.BOARDHEIGHT));
        frame.getContentPane().add(snakeEngine);

        frame.getContentPane().add(snakeEngine.getTimeLabel(), BorderLayout.SOUTH);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public String getLeaderBoardText(int numberOfScores) throws SQLException {

        int dataBaseSize = snakeEngine.getHighScores().getSortedHighScores().size();
        int maximum;
        String result = "";

        if(dataBaseSize<numberOfScores){
            maximum = dataBaseSize;
        }
        else{
            maximum = numberOfScores;
        }

        for (int i =0; i<maximum; i++){
            result += (i+1) +"." + " " + snakeEngine.getHighScores().getSortedHighScores().get(i).toString();
        }

        return result;
    }

}
