import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Car
{
 private int XPosition;
 private int YPosition;
 private int speed = 0;
 ImageIcon[] Cars; // ImageIcon array will store the car images
 private int currentCar = 0; // the number of the Racing car png file
 private boolean hasCrashedOuter = false; // Set to false again if player faces away from outer
 private boolean hasCrashedInner = false; // Once true, the player can never move again
    File collideEdgeSound = new File("src/carCrash.wav");
    AudioInputStream audio;
    int playSoundCounter = 0; // incremented to stop a sound playing repeatedly for same event
    private boolean checkPoint1 = false; // true when a car passes through certain co-ordinates
    private boolean checkPoint2 = false;
    private boolean checkPoint3 = false;
    private boolean checkPoint4 = false;
    private String carColor; // This will match the base name of the PNG file
    private int laps = 0;

 public Car(int Xpos, int Ypos, String color)  // constructor
 {
     carColor = color;
     XPosition = Xpos;
     YPosition = Ypos;
    // Setting up the Cars array and loading all 16 png images of the desired color
     final int TOTAL_IMAGES = 16;
     Cars = new ImageIcon[TOTAL_IMAGES];

     for (int i = 0; i < Cars.length; i++)
     {
         Cars[i] = new ImageIcon(getClass().getResource("res/" + carColor + i + ".png"));
     }

 }  // end of constructor

public void increaseSpeed() // Used in Game class to increase the speed after a key press
{
    if(speed < 100 && !hasCrashedOuter  && !hasCrashedInner )
    {
        speed += 1;
    }
}
public void decreaseSpeed() // Used in Game class to decrease the speed after a key press
{
    if(speed > 0)
    {
        speed -= 1;
    }
}

public void setLaps(int l) // Used in Game, to set laps back to 0 once race has completed
{
    laps = l;
}


public int getLaps() // Used in Game, check if player's completed laps == set number of laps
    {                // If true that player will be declared the winner
        return laps;
    }

// Used in Game to paint car at current position & check if Car 1 & Car 2 crashed into eachother
public int getXPosition()
{
    return XPosition;
}
public int getYPosition()
{
    return YPosition;
}
// Gets the car's image number. Used in paint and for up and down key presses
public int getCurrentCar()
{
    return currentCar;
}

// Used to reset outer crashed back to false after race been completed
 public void setCrashedOuter(boolean b)
 {
     hasCrashedOuter = b;
 }

// Used in resetGame method in Game to set to false after a race has been completed
public void setHasCrashedInner(boolean b)
{
    hasCrashedInner = b;
}
// Used in CheckInnerCollision method in Game to check if both players have collided with inner
public boolean getHasCrashedInner()
{
    return hasCrashedInner;
}
// Used in Game class for up/down events and to set back to 0 in resetGame method
public void setCurrentCar(int car)
{
 currentCar = car;
}
// Used in Game class resetGame method, to set the Car back to starting position
public void setXPosition(int x)
{
    XPosition = x;
}
public void setYPosition(int y)
{
    YPosition = y;
}
// Used to set car speed back to 0 in the resetGame method in Game class
public void setSpeed(int s)
{
    speed = s;
}

// Will be used in Game to set back to false in resetGame method
public void setCheckPoint1(boolean b)
{
    checkPoint1 = b;
}
public void setCheckPoint2(boolean b)
{
    checkPoint2 = b;
}

public void setCheckPoint3(boolean b)
{
    checkPoint3 = b;
}
public void setCheckPoint4(boolean b)
{
    checkPoint4 = b;
}
// Used in actionPerformed, moving the Car to it's new position
    public void moveCar()
    {
        switch(currentCar)
        {
            case 0:             // Car facing straight right
                XPosition = XPosition + 2 * speed;
                break;

            case 1:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition +     speed;
                break;

            case 2:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 3:
                XPosition = XPosition +     speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 4:            // Car facing straight down
                YPosition = YPosition + 2 * speed;
                break;

            case 5:
                XPosition = XPosition -     speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 6:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition + 2 * speed;
                break;

            case 7:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition +     speed;
                break;

            case 8:           // Car facing straight left
                XPosition = XPosition - 2 * speed;
                break;

            case 9:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition -     speed;
                break;

            case 10:
                XPosition = XPosition - 2 * speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 11:
                XPosition = XPosition -     speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 12:         // Car facing straight up
                YPosition = YPosition - 2 * speed;
                break;

            case 13:
                XPosition = XPosition +     speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 14:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition - 2 * speed;
                break;

            case 15:
                XPosition = XPosition + 2 * speed;
                YPosition = YPosition -     speed;
                break;
        }
    }

    // Used in actionPerfomed in Game to check if car crashes with inner edge
    public void checkCollideInnerEdge()
    {
        if( (XPosition >= 140 && XPosition <= 685) && (YPosition <= 495 && YPosition >= 165) )
        {
            hasCrashedInner = true;
            playCollideEdgeSound();
            speed = 0;
        }
    }
    // Used in actionPerfomed in Game to check if car crashes with outer edge
    public void checkCollideOuterEdge()
    {
        if( YPosition >= 565) // collide with bottom outer area
        {
            hasCrashedOuter = true;
            playCollideEdgeSound();
            speed = 0;
            if(currentCar > 8) // car now faces an upward position, to get back on track.
            {
                hasCrashedOuter = false;
                playSoundCounter = 0;
            }
        }
        if((XPosition >= 50 && XPosition <= 800) && YPosition <= 100 ) // collide top outer area
        {
            hasCrashedOuter = true;
            playCollideEdgeSound();
            speed = 0;

            if(currentCar > 0 && currentCar < 8) // car faces downwards, to get back on track
            {
                hasCrashedOuter = false;
                playSoundCounter = 0;
            }

        }

        if(XPosition >= 750) // collide with right outer area
        {
            hasCrashedOuter = true;
            playCollideEdgeSound();
            speed = 0;

            if(currentCar > 4 && currentCar < 12) // the car faces left, to get back on track
            {
                hasCrashedOuter = false;
                playSoundCounter = 0;
            }
        }

        if(XPosition <= 50) // collide with left outer area
        {
            hasCrashedOuter = true;
            playCollideEdgeSound();
            speed = 0;

            if(currentCar < 4 || currentCar > 12) // the car faces right, to get back on track
            {
                hasCrashedOuter = false;
                playSoundCounter = 0;
            }
        }

    } // End of CollideOuterEdge method

    // Used in actionPerformed in Game to check if the checkpoints have been passed
    public void checkPassedCheckPoints()
    {
        if((XPosition >= 685 && XPosition <= 750) && (YPosition <= 490 && YPosition >= 470) && currentCar > 8  )
        {
            checkPoint1 = true;
        }

        if((XPosition >= 685 && XPosition <= 750) && (YPosition <= 210 && YPosition >= 190) && currentCar > 8)
        {
            checkPoint2 = true;
        }

        if((XPosition >= 50 && XPosition <= 112) && (YPosition <= 175 && YPosition >= 155) && currentCar < 8)
        {
            checkPoint3 = true;
        }

        if((XPosition >= 50 && XPosition <= 112) && (YPosition <= 460 && YPosition >= 440) && currentCar < 8)
        {
            checkPoint4 = true;
        }

    }
// checks car crossed finish line & pass all check points. If satisfied 'laps' incremented
    public void checkCompletedLaps()
    {
       if((XPosition >= 384 && XPosition <= 424) && (YPosition >= 480 && YPosition <= 585) &&
          checkPoint1  && checkPoint2  && checkPoint3  && checkPoint4 )
       {
           laps++;
           checkPoint1 = false; // resetting all check points once a lap has been completed
           checkPoint2 = false;
           checkPoint3 = false;
           checkPoint4 = false;
       }
    }

    public void playCollideEdgeSound() {
        {
            try {
                audio = AudioSystem.getAudioInputStream(collideEdgeSound); // wav's entry point
                Clip clip = AudioSystem.getClip(); // clip will enable audio to be played
                clip.open(audio);

                if(playSoundCounter == 0)
                {
                    clip.start();
                    playSoundCounter++;
                }

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

// Called in startAnimation & playAgain methods in Game class, prompting user to enter a choice
    public void setupCar()
    {

        String[] carChoices = new String[] {"Blue", "Purple", "Red", "Green", "Yellow"};
        int option = JOptionPane.showOptionDialog(null, "Please choose car", "Car Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, carChoices, carChoices[0]);

        switch(option)
        {
            case 0:
               carColor = "RacingCarBlue";
               loadCar();
            break;

            case 1:
                carColor = "RacingCarPurple";
                loadCar();
                break;

            case 2:
                carColor = "RacingCarRed";
                loadCar();
                break;

            case 3:
                carColor = "RacingCarGreen";
                loadCar();
                break;

            case 4:
                carColor = "RacingCarYellow";
                loadCar();
                break;

        }

    }
// This will load all the images into the Cars image array
   public void loadCar()
   {
       for (int i = 0; i < Cars.length; i++)
       {
           Cars[i] = new ImageIcon(getClass().getResource("res/" + carColor + i + ".png"));
       }
   }
   // Used in playAgain() and startAnimation(), ensure player 1 & player 2 do not select same car
   public String getCarColor()
   {
       return  carColor;
   }


} // End of Car class
