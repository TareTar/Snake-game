import javax.swing.JPanel;
import javax.swing.JLabel;

public class Tile
{
    public boolean isEmpty;
    public boolean isSnake;
    public boolean isFruit;
    
    //Nutritional value of the tile (if it is a fruit)
    public int nutrition;
    
    //Location of tile
    public int row;
    public int col;
    
    //Panel and label of the tile
    public JPanel panel;
    public JLabel label;
    
    //Tile constructor
    //Defaults to an empty tile
    public Tile(int row, int col)
    {   
        this.row = row;
        this.col = col;
        
        //Creating a panel and label
        panel = new JPanel();
        panel.setLayout(null);
        panel.setVisible(true);
        
        label = new JLabel();
        panel.add(label);
        
        set("empty");
    }
    
    
    //Sets a tile to empty, snake, or fruit
    public void set(String type)
    {
        switch (type)
        {
            case "empty": 
                isEmpty = true;
                isSnake = false;
                isFruit = false;
            break;
            case "snake":
                isEmpty = false;
                isSnake = true;
                isFruit = false;
            break;
        }
        
        Screen.tiles.add(this);
    }
    
    //Overloading the set method
    public void set(int nutrition)
    {
        isEmpty = false;
        isSnake = false;
        isFruit = true;
        this.nutrition = nutrition;
        
        Screen.tiles.add(this);
    }
}