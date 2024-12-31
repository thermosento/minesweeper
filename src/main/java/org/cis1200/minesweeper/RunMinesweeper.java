package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunMinesweeper implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation((int) width / 2 - 180, (int) height / 2 - 150);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
//        INSTRUCTION WINDOW
        // Game board
        final Minesweeper board = new Minesweeper(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.initGame()); //implement restart
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.saveToFile());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.loadSavedFile());
        control_panel.add(load);

        final JPanel help_panel = new JPanel();
        frame.add(help_panel, BorderLayout.WEST);

        final JButton help = new JButton("Help");
        help.addActionListener(e -> helpWindow());
        help_panel.add(help);

        // Put the frame on the screen
        frame.pack();
        frame.setSize(360, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.initGame();
    }

    private void helpWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        JFrame f = new JFrame();
        JDialog d = new JDialog(f, "Instructions", true);
        d.setLayout(new FlowLayout());
        d.setLocation((int) width / 2 - 125, (int) height / 2 - 112);

        d.add(new JLabel("welcome to minesweeper :-)"));
        d.add(new JLabel("=====----------------====="));
        d.add(new JLabel("to win, you must uncover all of the"));
        d.add(new JLabel("empty tiles without uncovering"));
        d.add(new JLabel("any mines."));
        d.add(new JLabel("=====----------------====="));
        d.add(new JLabel("controls:"));
        d.add(new JLabel("left click to uncover a tile"));
        d.add(new JLabel("right click to flag a tile"));



        d.pack();
        d.setSize(250, 225);
        d.setVisible(true);
    }
}