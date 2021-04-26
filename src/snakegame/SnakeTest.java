package snakegame;

import static org.junit.Assert.*;
import static snakegame.SnakeEngine.*;
import static snakegame.SnakeEngine.SNAKEY_SPEED;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class SnakeTest {

    @Test
    void isMet_collide() {
        Image rockImage = new ImageIcon(ClassLoader.getSystemResource("pics/rock.png")).getImage();
        Rock rock1 = new Rock(100, 100, 20, 20, rockImage);
        Rock rock2 = new Rock(95, 95, 20, 20, rockImage);

        assertTrue(rock1.isMet(rock2));
    }
    @Test
    void isMet_notCollide() {
        Image rockImage = new ImageIcon(ClassLoader.getSystemResource("pics/rock.png")).getImage();
        Rock rock3 = new Rock(100, 110, 20, 20, rockImage);
        Rock rock4 = new Rock(85, 85, 20, 20, rockImage);

        assertFalse(rock3.isMet(rock4));
    }
}