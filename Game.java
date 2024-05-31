import java.util.Scanner;
import java.util.ArrayList;

public class Game
{
    //Whether runner thread is running or not
    public boolean isRunning;
    
    //Dimensions of the grid
    public int width;
    public int height;
    
    //Makes up all the tiles inside of the game
    public Tile[][] grid;
    
    //The time between each tick in the game
    public int timeInterval;
    
    //The probability of grape generation (between 0 and 1)
    private float grapeProbability;
    
    //The location of the sanke head
    private int row;
    private int col;
    
    //The amount of length deficit or surplus in snake
    private int nutrition;
    
    //The tiles that make up the snake
    private ArrayList<Tile> snake;
    
    //A queue of direction inputs
    public ArrayList<String> directions;
    
    //The current direction
    public String direction;
    
    private GameEvents eventHandler;
    private Screen screen;
    
    
    //Constructor
    public Game()
    {
        setGameSettings();
        
        createSnake();
        
        eventHandler = new GameEvents(this);
        screen = new Screen(this, eventHandler);
        
        Screen.updateScreen(screen);
        
        generateConsumable();
        
        Screen.updateScreen(screen);
        
        screen.frame.setVisible(true);
        
        isRunning = false;
    }
        
    
    //Sets the width, height, timeInterval, and grapeProbability variables
    public void setGameSettings()
    {
        Scanner input = new Scanner(System.in);

        try 
        {            
            System.out.print("Input grid width: ");
            width = input.nextInt();
            
            System.out.print("Input grid height: ");
            height = input.nextInt();
            
            System.out.print("Input update interval (miliseconds): ");
            timeInterval = input.nextInt();
            
            System.out.print("Input grape probability: ");
            grapeProbability = input.nextFloat();
            
            if (0 > grapeProbability || grapeProbability > 1)
            {
                throw new ArithmeticException("probability must be between 0 and 1");
            }
            
            //Creates new tile objects for each square in the grid
            grid = new Tile[height][width];
            
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    grid[i][j] = new Tile(i, j);
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex + "\n");
            setGameSettings();
        }
    }
    
    //Creates a snake and sets its directions
    public void createSnake()
    {
        directions = new ArrayList<String>();
        direction = "undefined";
        
        row = height / 2;
        col = width / 2;
        
        nutrition = 0;
        
        snake = new ArrayList<Tile>();
        snake.add(grid[row][col]);
        grid[row][col].set("snake");
    }
    
    //Looks for an empty tile
    //If three or more spots are available, an apple and a grape are generated
    //Otherwise only an apple is generated
    //Update - golden apples have a small chance of generating
    public void generateConsumable()
    {
        ArrayList<Tile> availableSpots = new ArrayList<Tile>();
        
        for (Tile[] i : grid)
        {
            for (Tile tile : i)
            {
                if (tile.isEmpty)
                {
                    availableSpots.add(tile);
                }
            }
        }
        
        int size = availableSpots.size();
        
        if (size == 0)
        {
            return;
        }
        
        int index = (int)(size * Math.random());
        
        //Golden Apples have 10% chance of generating
        availableSpots.remove(index).set(Math.random() > 0.1 ? 1 : 5);
        
        //If more than two spots are available, a grape can generate 
        //based on probability inputted by the user
        if (size > 2 && Math.random() <= grapeProbability)
        {
            index = (int)((size - 1) * Math.random());
            availableSpots.remove(index).set(Math.random() > 0.1 ? -1 : -5);
        }
    }
    
    
    //Standard run procedure
    //Returns whether the snake is still alive or not
    public boolean runProcedure()
    {
        //The direction is either the direction from the previous tick 
        if (directions.size() > 0)
        {   
            direction = directions.remove(0);
        }
        
        //Snake moves one tile
        switch (direction)
        {
            case "right": col++;
            break;
            case "left": col--;
            break;
            case "up": row--;
            break;
            case "down": row++;
        }
        
        //If snake hits a wall
        if (row < 0 || height <= row || width <= col || col < 0)
        {
            return false;
        }
        
        Tile tile = grid[row][col];
        
        //If snake hits part of itself
        if (tile.isSnake)
        {
            return false;
        }
        
        //If snake eats a fruit
        if (tile.isFruit)
        {
            nutrition += tile.nutrition;
            snake.add(grid[row][col]);
            grid[row][col].set("snake");
            
            //If snake eats an apple or golden apple
            if (tile.nutrition > 0)
            {
                generateConsumable();
            }
        }
        else
        {
            snake.add(grid[row][col]);
            grid[row][col].set("snake");
        }
        
        //If snake has surplus nutrition
        //It will gain length
        if (nutrition > 0)
        {
            nutrition--;
        }
        else
        {
            //If snake has nutrition deficit
            //It will lose length
            if (nutrition < 0)
            {
                snake.remove(0).set("empty");
                nutrition++;
            }
            
            snake.remove(0).set("empty");
            
            //If snake no longer has length
            //Game is over
            if (snake.size() == 0)
            {
                Screen.updateScreen(screen);
                return false;
            }
        }
        
        Screen.updateScreen(screen);
        
        return true;
    }
    
    
    //Resets the game
    public void reset()
    {   
        //Prompts the user whether they want to reset the game settings
        Scanner input = new Scanner(System.in);
        
        while (true)
        {
            System.out.println("Would you like to change game settings? (yes/no)");
            String answer = input.nextLine();
                
            if (answer.toLowerCase().equals("yes"))
            {
                setGameSettings();        
                screen.frame.dispose();
                screen = new Screen(this, eventHandler);
                break;
            }
            
            if (answer.toLowerCase().equals("no"))
            {
                for (int i = 0; i < height; i++)
                {
                    for (int j = 0; j < width; j++)
                    {
                        grid[i][j].set("empty");
                    }
                }
                break;
            }
            
            System.out.println("Try again");
        }
 
        createSnake();
        
        Screen.updateScreen(screen);
        
        generateConsumable();
        
        Screen.updateScreen(screen);
        
        screen.frame.setVisible(true);
        
        isRunning = false;
    }
}