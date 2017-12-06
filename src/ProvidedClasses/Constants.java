package ProvidedClasses;

/**
 *
 * @author aabuchho
 */
public class Constants {
    /**The HIT result from a shot in the game*/
    public static final String HIT          = "HIT";
    /**The MISS result from a shot in the game*/
    public static final String MISS         = "MISS";
    
    /**The carrier's ship id*/
    public static final String CARRIER      = "C";
    /**The battleships's ship id*/
    public static final String BATTLESHIP   = "B";
    /**The submarine's ship id*/
    public static final String SUBMARINE    = "S";
    /**The destroyer's ship id*/
    public static final String DESTROYER    = "D";
    /**The patrol boat's ship id*/
    public static final String PATROL       = "P";
    
    /**The horizontal orientation*/
    public static final String HORIZONTAL   = "h";
    /**The vertical orientation*/
    public static final String VERTICAL     = "v";
    
    /**
     * Helper method to ensure the reference to the constant is used over a string with equal characters.
     * @param result The result to convert
     * @return The reference to the constant value
     */
    public static String resultToConstant(String result){
        if(result.equalsIgnoreCase(HIT)) return HIT;
        if(result.equalsIgnoreCase(MISS)) return MISS;
        return null;
    }
    
    /**
     * Helper method to ensure the reference to the constant is used over a string with equal characters.
     * @param ship The ship to convert
     * @return The reference to the constant value
     */
    public static String shipToConstant(String ship){
        if(ship.equalsIgnoreCase(CARRIER)) return CARRIER;
        if(ship.equalsIgnoreCase(BATTLESHIP)) return BATTLESHIP;
        if(ship.equalsIgnoreCase(SUBMARINE)) return SUBMARINE;
        if(ship.equalsIgnoreCase(DESTROYER)) return DESTROYER;
        if(ship.equalsIgnoreCase(PATROL)) return PATROL;
        return null;
    }
    
    /**
     * Helper method to ensure the reference to the constant is used over a string with equal characters.
     * @param orientation The orientation to convert
     * @return The reference to the constant value
     */
    public static String orientationToConstant(String orientation){
        if(orientation.equalsIgnoreCase(HORIZONTAL)) return HORIZONTAL;
        if(orientation.equalsIgnoreCase(VERTICAL)) return VERTICAL;
        return null;
    }
    
    /**
     * Prevent instances of the Constant class
     */
    private Constants(){}
}
