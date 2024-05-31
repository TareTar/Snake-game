import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//KeyListener and KeyEvent used for keyboard inputs
public class GameEvents implements KeyListener
{
    private Game game;
    private Runner runner;
    
    //Constructor
    public GameEvents(Game game)
    {
        this.game = game;
    }
    
    
    //Key Listener
    //https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e)
    {   
        int size = game.directions.size();
        
        //Input queue cannot exceed five
        if (size > 5)
        {
            game.directions.remove(0);
            size--;
        }
        
        String direction;
        
        if (size > 0)
        {
            direction = game.directions.get(size - 1);
        }
        else
        {
            direction = game.direction;
        }
        
        //Adding to the queue of direction inputs using WASD or arrow keys
        int key = e.getKeyCode();
        
        if (!direction.equals("left") && (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT))
        {
            game.directions.add("right");
        }
        else if (!direction.equals("right") && (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT))
        {
            game.directions.add("left");
        }
        
        else if (!direction.equals("down") && (key == KeyEvent.VK_W || key == KeyEvent.VK_UP))
        {
            game.directions.add("up");
        }
        
        else if (!direction.equals("up") && (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN))
        {
            game.directions.add("down");
        }
        else if (key != KeyEvent.VK_SPACE)
        {
            return;
        }
        
        if (!game.isRunning && key != KeyEvent.VK_SPACE)
        {
            System.out.println("Game running");
            game.isRunning = true;
            
            //Starts the runner thread
            runner = new Runner(game);
            runner.start();
        }
        
        //Pause feature using the space key
        else if (game.isRunning && key == KeyEvent.VK_SPACE)
        {
            runner.pause();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e){}
    
    @Override
    public void keyTyped(KeyEvent e){}
}