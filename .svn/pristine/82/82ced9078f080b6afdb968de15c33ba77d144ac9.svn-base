package ProvidedClasses;

/**
 *
 * @author aabuchho
 */
public class ShipLayout {
    
    private String[] layout = new String[5];
    
    /**
     * Constructs an object to specify your ship's locations
     */
    public ShipLayout(){
        
    }
    
    /**
     * Method used to place a ship within this ShipLayout 
     * 
     * @param ship The ship id to place. Must be one of:
     *      <ul><li>Constants.CARRIER</li>
     *          <li>Constants.BATTLESHIP</li>
     *          <li>Constants.SUBMARINE</li>
     *          <li>Constants.DESTROYER</li>
     *          <li>Constants.PATROL</li></ul>
     * @param x The x coordinate of the most top-left tile of the ship
     * @param y The y coordinate of the most top-left tile of the ship
     * @param orientation The orientation to place the ship in. Must be one of: 
     *      <ul><li>Constants.HORIZONTAL</li>
     *          <li>Constants.VERTICAL</li></ul>
     */
    public void placeShip(String ship, int x, int y, String orientation){
        int index;
        ship = ship.toUpperCase();
        orientation = orientation.toLowerCase();
        switch(ship){
            case Constants.CARRIER:
                index = 0;
                break;
            case Constants.BATTLESHIP:
                index = 1;
                break;
            case Constants.SUBMARINE:
                index = 2;
                break;
            case Constants.DESTROYER:
                index = 3;
                break;
            case Constants.PATROL:
                index = 4;
                break;
            default:
                return;
        }
        switch(orientation){
            case Constants.HORIZONTAL:
            case Constants.VERTICAL:
                break;
            default:
                return;
        }
        layout[index] = String.format("\"%s\": [%d, %d, \"%s\"]", ship, x, y, orientation);
    }
            
    /**
     * Returns a string representation of this layout in a JSON structure 
     * 
     * @return the JSON string
     */
    @Override
    public String toString(){
        String output = "{";
        boolean first = true;
        for(int i=0;i<layout.length;i++){
            if(layout[i] != null){
                if(!first){
                    output += ", ";
                }
                output += layout[i];
                first = false;
            }
        }
        output += "}";
        return output;
    }        
}
