import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.io.*;

public class Game extends JPanel implements KeyListener
{
    private Timer animationTimer;
    private boolean carsCollided = false; // true when the cars collide with each other
    private boolean playersSetup = false; // true once both players selected different cars
    private int laps = 3; // number of laps in order to win the game. Set to 3 by default
    // Central area, grass by default, user can change via option dialogue
    private Color track = Color.green;

    JFrame messageBox = new JFrame("Game Over"); // used for message and confirm dialogues
    AudioInputStream audio;
    File carCrashSound = new File("src/crash_metal2.wav"); // file locations for wav files
    File winnerSound = new File("src/Congratulations.wav");

     Car car1 = new Car(370,505,"RacingCarPurple" ); // Car objects for player 1 and 2
     Car car2 = new Car(370,555, "RacingCarBlue");

    public Game()  // Constructor
   {
        addKeyListener(this); // Adding keyListener, to make it possible to control the carts
        setFocusable(true); // will enable the JPanel to be focussed
        setFocusTraversalKeysEnabled(false); // keys like Tab not needed here to direct focus
        selectRaceTrack(); // Presents user with option dialogue choices of track (central area)
        setupLaps(); // Presents user with option dialogue of choices for number of laps

   } // end of Game constructor

       public void keyPressed(KeyEvent e) // This will deal with key presses for player 1 and 2
       {
           final int  TOTAL_IMAGES = 16;
           switch(e.getKeyCode())
           {
               // PLAYER 1 CONTROLS

               case KeyEvent.VK_W:   // Player 1 pressed the Up key. Car makes anti-clockwise turn

                   if(car1.getCurrentCar() == 0)
                   {
                       car1.setCurrentCar(15);
                   }
                   else {
                       car1.setCurrentCar((car1.getCurrentCar() - 1) % TOTAL_IMAGES);
                   }
                   break;

               case KeyEvent.VK_E: // Player one pressed the Down key. Car makes clockwise turn

                   car1.setCurrentCar((car1.getCurrentCar() + 1) % TOTAL_IMAGES);
                   break;

               case KeyEvent.VK_A: // Player one pressed Accelerate

                   car1.increaseSpeed();
                   break;

               case KeyEvent.VK_S: // Player one pressed de-accelerate

                   car1.decreaseSpeed();
                   break;


                   // PLAYER TWO CONTROLS

               case KeyEvent.VK_UP:  // Player two pressed Up key. Car makes anti-clockwise turn

                   if(car2.getCurrentCar() == 0)
                   {
                       car2.setCurrentCar(15);
                   }
                   else
                   {
                       car2.setCurrentCar((car2.getCurrentCar() - 1) % TOTAL_IMAGES);
                   }
                   break;

               case KeyEvent.VK_DOWN:  // Player two pressed Down key. Car makes clockwise turn

                   car2.setCurrentCar((car2.getCurrentCar() + 1) % TOTAL_IMAGES);

                   break;

               case KeyEvent.VK_LEFT: // Player two pressed Accelerate
                     car2.increaseSpeed();
                   break;

               case KeyEvent.VK_RIGHT: // Player two pressed De-accelerate
                   car2.decreaseSpeed();
                   break;

           } // end of switch
       }  // End of keyPressed

    // keyTyped & KeyReleased need to be included, even if unused
    public void keyTyped(KeyEvent e) { }
       public void keyReleased(KeyEvent e) { }

// painting the racetrack, trees, arrows and cars
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

         new Racetrack(g, track); // painting the racetrack

        // The trees to the right of the racetrack
         new Tree(g, 820, 550);
         new Tree(g, 820, 450);
         new Tree(g, 820, 350);
         new Tree(g, 820, 250);
         new Tree(g, 820,150);
         new Tree(g, 820, 50);
        // The trees to the left of the racetrack
         new Tree(g, 20, 550);
         new Tree(g, 20, 450);
         new Tree(g, 20, 350);
         new Tree(g, 20, 250);
         new Tree(g, 20,150);
         new Tree(g, 20, 50);

        // The trees at the top
         new Tree(g, 790, 50);
         new Tree(g, 760,50);
         new Tree(g, 730,50);
         new Tree(g, 700, 50);
         new Tree(g, 670, 50);
         new Tree(g, 640,50);
         new Tree(g, 610,50);
         new Tree(g, 580, 50);
         new Tree(g, 550,50);
         new Tree(g, 520,50);
         new Tree(g, 490, 50);
         new Tree(g, 460, 50);
         new Tree(g, 430,50);
         new Tree(g, 400,50);
         new Tree(g, 370, 50);
         new Tree(g, 340,50);
         new Tree(g, 310,50);
         new Tree(g, 280, 50);
         new Tree(g, 250, 50);
         new Tree(g, 220,50);
         new Tree(g, 190,50);
         new Tree(g, 160,50);
         new Tree(g, 130,50);
         new Tree(g, 100,50);
         new Tree(g, 70,50);
         new Arrow(g,500,525);
         new Arrow(g, 500, 575);
         new Arrow(g, 600, 525);
         new Arrow(g, 600, 575);
       // Painting the cars
        car1.Cars[car1.getCurrentCar()].paintIcon(this, g, car1.getXPosition(), car1.getYPosition());
        car2.Cars[car2.getCurrentCar()].paintIcon(this, g, car2.getXPosition(), car2.getYPosition());

    } // End of paintComponent

    // Will start the action timer once both players are set up
    public void startAnimation()
    {
        if(animationTimer == null)
        {
            final int ANIMATION_DELAY = 100; // in millisecs,new action event fired each interval
            // create timer that fires action events
            animationTimer = new Timer(ANIMATION_DELAY, new TimerHandler());
            selectCars();

            animationTimer.start(); // Effectively starts game, as action events processed.
        } // end of if statement

        else  // then there already is an animation timer. Restart animation
        {
            if( ! animationTimer.isRunning())
            {
                animationTimer.restart();
            }
        } // end of else

    } // end of startAnimation method

    // will check that the two cars collide into each other and end the game if so
    public void checkCarsCollide()
    {
        // will be set to true if the two cars intersect
        carsCollided = car1.getXPosition() <= car2.getXPosition() + 25 && car1.getXPosition() + 25 >= car2.getXPosition() &&
                car1.getYPosition() <= car2.getYPosition() + 25 && car1.getYPosition() + 25 >= car2.getYPosition();

        if(carsCollided) // if cars have collided
        {
            playSound(carCrashSound);
            JOptionPane.showMessageDialog(messageBox, "The cars have collided! The game is over");
            playAgainOption();
        }

    }

// checks if both cars  collided with inner edge (& thus cannot move) & ends game if satisfied
  public void checkBothInnerCollided()
  {
      if(car1.getHasCrashedInner() && car2.getHasCrashedInner())
      {
        JOptionPane.showMessageDialog(messageBox, "Both cars collided with inner edge and cannot move, so the game is over");
        playAgainOption();
      }

  }
// Will play either the winner sound or the car crash sound
    public void playSound(File f)
    {
        {
            try
            {
                audio = AudioSystem.getAudioInputStream(f); // audio is wav's entry point
                Clip clip = AudioSystem.getClip(); // will be used to play audio
                clip.open(audio);
                clip.start();
            }
            catch (UnsupportedAudioFileException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (LineUnavailableException e)
            {
                e.printStackTrace();
            }
        }
    }

// Will be called when a game is over, offering the user the choice to play again
    public void playAgainOption()
    {
        int playAgainChoice = JOptionPane.showConfirmDialog(messageBox, "Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);

        if(playAgainChoice == 0) // User selected yes
        {
            resetGame();
            selectCars();
        }
        else // User selected no
        {
            JOptionPane.showMessageDialog(messageBox, "Good Bye!");
            System.exit(0); // Quitting the application
        }

    }

    // This checks that either player has completed the required number of laps
   public void checkWinner()
   {
       car1.checkCompletedLaps();
       car2.checkCompletedLaps();

       if (car1.getLaps() == laps)
       {
           playSound(winnerSound);
           JOptionPane.showMessageDialog(messageBox, "Player 1 is the winner, Congratulations!");
           playAgainOption();
       }
       if (car2.getLaps() == laps)
       {
           playSound(winnerSound);
           JOptionPane.showMessageDialog(messageBox, "Player 2 is the winner, Congratulations!");
           playAgainOption();
       }
   }

   // Called at the start of game, allowing the user to select number of laps
    public void setupLaps()
    {
        String[] lapChoices = new String[] {"One", "Two", "Three", "Five"};
        int option = JOptionPane.showOptionDialog(null, "Please choose number of laps", "Lap Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, lapChoices, lapChoices[0]);

        switch(option)
        {
            case 0:
                laps = 1;
                break;

            case 1:
                laps = 2;

                break;

            case 2:
                laps = 3;
                break;

            case 3:
                laps = 5;
                break;
        }
    }
    // Called at the start of game, allowing the user to select racetrack
    public void selectRaceTrack()
    {
        String[] racetrackChoices = new String[] {"Park", "Riverside", "Volcano"};

        int option = JOptionPane.showOptionDialog(null, "Hello and welcome to the Racing game! " +
                        "Please choose a Racetrack", "Race Track Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, racetrackChoices, racetrackChoices[0]);

        switch (option)
        {
            case 0:
                track = Color.green;
                break;

            case 1:
                track = Color.cyan;
                break;

            case 2:
                track = Color.red;
                break;
        }
    }
    // Player 1 & 2 select cars, also ensures same car not chosen, re-prompt player 2 if necessary
    public void selectCars()
    {
        car1.setupCar(); // player 1 selects choice of car
        while (!playersSetup)
        {
            car2.setupCar(); // player 2 selects choice of car
            if(car1.getCarColor().equals(car2.getCarColor())) // player 1 & 2 selected same car
            {
                JOptionPane.showMessageDialog(messageBox, "You have chosen the same car as player 1 " +
                        " please choose a car that is not already selected ");
            }
            else  // Both players have selected different cars
            {
                playersSetup = true;
            }
        }

    }

// Called after a race completed, it resets all flags, positions and re-prompts user for selections
   public void resetGame()
   {
       car1.setXPosition(370);
       car1.setYPosition(505);
       car1.setCurrentCar(0);
       car2.setXPosition(370);
       car2.setYPosition(555);
       car2.setCurrentCar(0);
       car1.setSpeed(0);
       car2.setSpeed(0);
       carsCollided = false;
       playersSetup = false;
       car1.setHasCrashedInner(false);
       car1.setCrashedOuter(false);
       car2.setCrashedOuter(false);
       car2.setHasCrashedInner(false);
       car1.setCheckPoint1(false);
       car2.setCheckPoint1(false);
       car1.setCheckPoint2(false);
       car2.setCheckPoint2(false);
       car1.setCheckPoint3(false);
       car2.setCheckPoint3(false);
       car1.setCheckPoint4(false);
       car2.setCheckPoint4(false);
       car1.setLaps(0);
       car2.setLaps(0);
       selectRaceTrack();
       setupLaps();

   }

// This will check for game events as well as repainting the cars in their new positions
    private class TimerHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent actionEvent)
        {
            car1.moveCar();
            car2.moveCar();
            car1.checkCollideInnerEdge();
            car2.checkCollideInnerEdge();
            car1.checkCollideOuterEdge();
            car2.checkCollideOuterEdge();
            checkCarsCollide();
            checkBothInnerCollided();
            car1.checkPassedCheckPoints();
            car2.checkPassedCheckPoints();
            checkWinner();
            repaint();
        } // End of actionPerformed
    }  // End of TimerHandler class

} // End of Game class
