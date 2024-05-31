public class Runner extends Thread
{
    private Game game;
    private boolean paused;
    
    //Constuctor
    public Runner(Game game)
    {
        this.game = game;
        paused = false;
    }
    
    
    //Runs the run procedure at a set time interval
    @Override
    public void run()
    {   
        long startTime = System.currentTimeMillis();
        
        //If the game is not paused and the snake dies
        if (!paused && !game.runProcedure())
        {
            System.out.println("Game Over\n");
            game.reset();
            return;
        }
        
        long endTime = System.currentTimeMillis();
    
        //Debug tool - amount of miliseconds to process a "tick"
        //System.out.println("MSPT: " + (endTime - startTime));
        
        long waitTime = game.timeInterval - endTime + startTime;
        
        //If execution time does not exceed the time interval
        if (waitTime > 0)
        {
            tick(waitTime);
        }
        
        run();
    }
    
    
    //Pauses the thread for a specified period of time
    public static void tick(long waitTime)
    {
        try
        {
            Thread.sleep(waitTime);
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
        
    
    //Pauses the runner
    public void pause()
    {
        paused = !paused;
    }
}