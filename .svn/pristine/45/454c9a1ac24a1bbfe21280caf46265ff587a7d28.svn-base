package BattleShip;

import ProvidedClasses.ShipLayout;

/**
 *
 * @author aabuchho
 */
public interface BattleShipPlayer {

    /**
     * This method is called by the protocol when it is the Player's turn to make a shot
     * 
     * @return an array of two integers that specify the desired {x, y} coordinate of the shot
     */
    int[] getShotLocation();
    
    /**
     * This method is called by the protocol when it is time to place your ships
     * 
     * @return A reference to a ShipLayout object containing the placed ships
     */
    ShipLayout getShipLayout();
    
    /**
     * This method is called by the protocol whenever a shot occurs in the game
     * 
     * @param yourShot If you made this shot, this value will be true.  False if it was your opponent's shot
     * @param x The x coordinate of the shot
     * @param y The y coordinate of the shot
     * @param result The result of the shot.  Will be one of:
     *      <ul><li>Constants.HIT</li>
     *          <li>Constants.MISS</li></ul>
     * @param shipSunk If this shot caused a ship to sink, this will be one of:
     *      <ul><li>Constants.CARRIER</li>
     *          <li>Constants.BATTLESHIP</li>
     *          <li>Constants.SUBMARINE</li>
     *          <li>Constants.DESTROYER</li>
     *          <li>Constants.PATROL</li></ul>
     */
    void shotNotification(boolean yourShot, int x, int y, String result, String shipSunk);
    
    /**
     * This method is called by the protocol when the game has ended
     * @param won true if your AI has won, otherwise false
     */
    void gameOver(boolean won);
    
    /**
     * This method is called by the protocol when your AI causes any errors in the server
     * @param message The error message that occurred on the server
     */
    default void errorMessage(String message){
        System.err.printf("ERROR: %s%n", message);
    }
}
