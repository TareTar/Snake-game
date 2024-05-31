import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Screen
{
    public Dimension screenSize;
    public int tileSize;
    public JFrame frame;
    
    //A queue of tiles that need to be updated
    public static ArrayList<Tile> tiles = new ArrayList<Tile>();
    
    //Cache of colors to be used
    private static final Color blue = new Color(0, 127, 255);
    private static final Color green1 = new Color(0, 200, 0);
    private static final Color green2 = new Color(0, 210, 0);
    
    //Cache of images to be used
    private ImageIcon apple;
    private ImageIcon bomb;
    private ImageIcon goldenApple;
    private ImageIcon grape;
    
    
    //Screen constructor
    public Screen(Game game, GameEvents eventHandler)
    {
        //Obtaining the codeHS screen size;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int width = game.width;
        int height = game.height;
        
        //Normalizing tile size according to the screen size
        tileSize = (int) Math.min(screenSize.getWidth() / width, screenSize.getHeight() / height);
        
        //Creating the frame
        frame = new JFrame();
        frame.setSize(width * tileSize, height * tileSize);
        frame.setLayout(new GridLayout(height, width));
        frame.setResizable(false);
        frame.addKeyListener(eventHandler);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Creating the images
        apple = createImage("images/apple.png");
        bomb = createImage("images/bomb.png");
        goldenApple = createImage("images/goldenApple.png");
        grape = createImage("images/grape.png");

        //Adding panels to the frame
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {   
                frame.add(game.grid[i][j].panel);
            }
        }
    }
    
    public ImageIcon createImage(String image)
    {
        //Voodoo magic - changing image resolution
        ImageIcon imageIcon = new ImageIcon(image);
        Image temp = imageIcon.getImage();
        Image newing = temp.getScaledInstance(tileSize, tileSize, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newing);
    }
    
    //Updating all tiles inside of the tiles queue
    public static void updateScreen(Screen screen)
    {    
        while (tiles.size() > 0)
        {   
            Tile tile = tiles.remove(0);
            
            if (tile.isEmpty)
            {
                //Checkerboard pattern for the background grass
                tile.panel.setBackground(tile.row % 2 == tile.col % 2 ? green1 : green2);
                tile.label.setIcon(null);
                continue;
            }
            
            //Colors the snake tiles blue
            if (tile.isSnake)
            {
                tile.panel.setBackground(blue);
                tile.label.setIcon(null);
                continue;
            }
            
            tile.label.setSize(screen.tileSize, screen.tileSize);
            
            //Assigns images to fruits depending on their nutrition value
            int nutrition = tile.nutrition;
            
            if (nutrition > 1)
            {
                tile.label.setIcon(screen.goldenApple);
                continue;
            }
            
            if (nutrition > 0)
            {
                tile.label.setIcon(screen.apple);
                continue;
            }
            
            if (nutrition < -1)
            {
                tile.label.setIcon(screen.bomb);
                continue;
            }
              
            tile.label.setIcon(screen.grape);
        }
    }
}