package cargame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class CarGame extends JFrame implements Runnable {

    Graphics dbG;
    Image dbImage;

    Random ran = new Random();

    @Override
    public void paint(Graphics g) {
        dbImage = createImage(700, 600);
        dbG = dbImage.getGraphics();
        paintComponents(dbG);
        g.drawImage(dbImage, 0, 0, this);
    }

    public CarGame() {
        setSize(700, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(3);
        addKeyListener(new Mover());
        block1 = new Rectangle(blockX1, blockY1, 100, 50);
        block2 = new Rectangle(blockX2, blockY2, 100, 50);
        block3 = new Rectangle(blockX3, blockY3, 100, 50);
        car = new Rectangle(carX, 500, 50, 50);
        coin = new Rectangle(coinX, coinY, 50, 50);
    }

    int carX = 400;
    int blockX1 = 0, blockY1 = 400;
    int blockX2 = 200, blockY2 = 200;
    int blockX3 = 400, blockY3 = 0;
    int coinX = ran.nextInt(550), coinY = 300;
    Rectangle block1, block2, block3, car, coin;

    int score = 0;

    @Override
    public void paintComponents(Graphics g) {
        g.fillRect(carX, 500, 50, 50);
        g.fillRect(blockX1, blockY1, 200, 50);
        g.fillRect(blockX2, blockY2, 200, 50);
        g.fillRect(blockX3, blockY3, 200, 50);
        g.fillOval(coinX, coinY, 50, 50);
        g.drawString("Score: " + score, 550, 580);
        repaint();
    }

    public void setDirection(int d) {
        movement = d;
    }

    int movement = 0;

    public void move() {
        carX += movement;
        car = new Rectangle(carX, 500, 50, 50);
        if (carX <= 0) {
            carX = 0;
        }
        if (carX >= 650) {
            carX = 650;
        }
    }

    int dead = 0;

    public void checkHit() {
        if (car.intersects(block1) || car.intersects(block2) || car.intersects(block3)) {
            dead = 1;
        }
    }

    public void moveBlock() {
        if (blockY1 >= 600) {
            blockY1 = 0;
            blockX1 = ran.nextInt(500);
        }
        if (blockY2 >= 600) {
            blockY2 = 0;
            blockX2 = ran.nextInt(500);
        }
        if (blockY3 >= 600) {
            blockY3 = 0;
            blockX3 = ran.nextInt(500);
        }
        if (coinY >= 600) {
            coinY = 0;
            coinX = ran.nextInt(550);
        }
        block1 = new Rectangle(blockX1, blockY1, 200, 50);
        block2 = new Rectangle(blockX2, blockY2, 200, 50);
        block3 = new Rectangle(blockX3, blockY3, 200, 50);
        coin = new Rectangle(coinX, coinY, 50, 50);
        blockY1++;
        blockY2++;
        blockY3++;
        coinY++;
    }

    public void checkScore() {
        if (car.intersects(coin)) {
            score++;
            coinY = 600;
        }
    }

    public void checkIntersect() {
        if (coin.intersects(block1) || coin.intersects(block2) || coin.intersects(block3)) {
            coinY++;
        }
    }

    public static void main(String[] args) {
        CarGame cg = new CarGame();
        Thread th = new Thread(cg);
        th.start();
    }

    @Override
    public void run() {
        try {
            JOptionPane.showMessageDialog(null, "Start Game", "", JOptionPane.WARNING_MESSAGE);
            while (true) {
                move();
                moveBlock();
                checkHit();
                checkScore();
                checkIntersect();
                Thread.sleep(3);
                if (dead == 1) {
                    int choice = JOptionPane.showConfirmDialog(null, "Start Over", "", JOptionPane.YES_NO_OPTION);
                    if (choice == 1) {
                        System.exit(1);
                    } else {
                        score = 0;
                        dead = 0;
                        carX = 400;
                        blockX1 = 0;
                        blockY1 = 400;
                        blockX2 = 200;
                        blockY2 = 200;
                        blockX3 = 400;
                        blockY3 = 0;
                        coinX = ran.nextInt(550);
                        coinY = 300;
                        movement = 0;
                    }
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    public class Mover extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_LEFT) {
                setDirection(-1);
            }
            if (keyCode == e.VK_RIGHT) {
                setDirection(1);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_LEFT) {
                setDirection(0);
            }
            if (keyCode == e.VK_RIGHT) {
                setDirection(0);
            }
        }
    }
}
