package ProvidedClasses;

import BattleShip.BattleShipPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The class used for communication with the BattleShipServer
 * 
 * @author aabuchho
 */
public class Protocol {

    private BattleShipPlayer player;
    
    private int[] lastShot;
    
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    private boolean running;
    
    /**
     * Constructs an instance of a protocol for communication with the BattleShipServer
     * 
     * @param player A reference to a BattleShipPlayer object
     * @param port The port of the BattleShipServer
     */
    public Protocol(BattleShipPlayer player, int port){
        this.player = player;
        this.port = port;
        
        this.host = "localhost";
        this.running = false;
    }
    
    /**
     * Constructs an instance of a protocol for communication with the BattleShipServer
     * 
     * @param player A reference to a BattleShipPlayer object
     * @param host The host name or IP address of the BattleShipServer
     * @param port The port of the BattleShipServer
     */
    public Protocol(BattleShipPlayer player, String host, int port){
        this.player = player;
        this.host = host;
        this.port = port;
        
        this.running = false;
    }
    
    /**
     * Method used to start listening to the server and processing the messages
     */
    public void start(){
        if(!connect()) return;
        
        try{
            String message;
            while(running && (message = in.readLine()) != null){
                processMessage(message);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        disconnect();
    }
    
    /**
     * Helper method used for establishing the connection with the BattleShipServer
     * 
     * @return true if connection established, false otherwise.
     */
    private boolean connect(){
        try{
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            this.running = true;
            return true;
        }
        catch(IOException e){
            e.printStackTrace();
            disconnect();
            return false;
        }
    }

    /**
     * Helper method to close the connection to the BattleShipServer
     */
    private void disconnect(){
        running = false;
        try{if(socket!=null) socket.close();}
        catch(IOException e){}
    }
            
    /**
     * Helper method to process a given message from the BattleShipServer
     * 
     * @param message the message to process
     */
    private void processMessage(String message) {
        switch(message.toUpperCase().substring(0, 3)){
            case "HEL":
                System.out.println(player.getClass().getSimpleName());
                disconnect();
                break;
            case "NAM":
                sendName(player.getClass().getSimpleName());
                break;
            case "SHI":
                sendShipLayout(player.getShipLayout());
                break;
            case "SHO":
                lastShot = player.getShotLocation();
                sendShotLocation(lastShot);
                break;
            case "HIT":
                player.shotNotification(true, lastShot[0], lastShot[1], Constants.HIT, null);
                break;
            case "MIS":
                player.shotNotification(true, lastShot[0], lastShot[1], Constants.MISS, null);
                break;
            case "SUN":{
                String shipSunk = message.toUpperCase().substring(5, 6);
                shipSunk = Constants.shipToConstant(shipSunk);
                player.shotNotification(true, lastShot[0], lastShot[1], Constants.HIT, shipSunk);
                break;
            }
            case "OPP":{
                int shotX = Integer.parseInt(message.toUpperCase().substring(14, 15));
                int shotY = Integer.parseInt(message.toUpperCase().substring(16, 17));
                String result = message.toUpperCase().substring(18);
                String shipSunk = null;
                if(result.startsWith("SUNK")){
                    result = Constants.HIT;
                    shipSunk = message.toUpperCase().substring(23, 24);
                    shipSunk = Constants.shipToConstant(shipSunk);
                }
                else{
                    result = Constants.resultToConstant(result);
                }
                player.shotNotification(false, shotX, shotY, result, shipSunk);
                break;
            }
            case "WIN":
                player.gameOver(true);
                disconnect();
                break;
            case "LOS":
                player.gameOver(false);
                disconnect();
                break;
            case "ERR":{
                String error = message.substring(6);
                player.errorMessage(error);
                break;
            }
        }
    }

    /**
     * Helper method to send the Player's name to the BattleShipServer
     * 
     * @param name The name to send to the BattleShipServer
     */
    private void sendName(String name) {
        if(!running) return;
        out.printf("%s\n", name);
    }

    /**
     * Helper method to send the Player's desired shot location to the BattleShipServer in the correct format
     * 
     * @param coords The coordinates to send to the BattleShipServer
     */
    private void sendShotLocation(int[] coords) {
        if(!running) return;
        out.printf("[%d, %d]\n", coords[0], coords[1]);
    }

    /**
     * Helper method to send the Player's ShipLayout to the BattleShipServer in the correct format
     * 
     * @param layout The layout to send to the BattleShipServer
     */
    private void sendShipLayout(ShipLayout layout) {
        if(!running) return;
        out.printf("%s\n", layout.toString());
    }
}
