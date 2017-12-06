package BattleShip;

import ProvidedClasses.Constants;
import ProvidedClasses.ShipLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author aaron
 */
public class JavaBoss implements BattleShipPlayer{
    private String[][] board = new String[10][10];
    
    private int[][] cardinalDirections = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    
    private HashMap<String, Integer> shipLength = new HashMap<>();
    private HashMap<String, Boolean> shipLeft = new HashMap<>();
    
    private static final boolean GENERATE_OUTPUT = false;
    
    private final Comparator<HashSet<Coord>> hashSetSizeComparator = (HashSet<Coord> t, HashSet<Coord> t1) -> t1.size() - t.size();
    
    private class Coord{
        int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + this.x;
            hash = 11 * hash + this.y;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            final Coord other = (Coord) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", x, y);
        }
        
        
    }
    
    public JavaBoss(){
        shipLength.put(Constants.CARRIER,    5);
        shipLength.put(Constants.BATTLESHIP, 4);
        shipLength.put(Constants.SUBMARINE,  3);
        shipLength.put(Constants.DESTROYER,  3);
        shipLength.put(Constants.PATROL,     2);
        
        shipLeft.put(Constants.CARRIER,    true);
        shipLeft.put(Constants.BATTLESHIP, true);
        shipLeft.put(Constants.SUBMARINE,  true);
        shipLeft.put(Constants.DESTROYER,  true);
        shipLeft.put(Constants.PATROL,     true);
    }
    
    @Override
    public int[] getShotLocation() {
        //look for a hit to cluster around 
        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){
                //if we have hit here
                if(isHit(x, y)){
                    boolean hasHitRight = isHit(x+1, y);
                    boolean hasHitBelow = isHit(x, y+1);
                    
                    //new code:
                    if(hasHitRight){
                        //to the right
                        int d = 1;
                        while(isHit(x+d, y)){
                            d++;
                        }
                        //at this point, we either fell out of bounds or we found a cell that is not a hit
                        if(isInBounds(x+d, y) && !isShot(x+d, y)){
                            return new int[]{x+d, y};
                        }
                        
                        //to the left
                        if(isInBounds(x-1, y) && !isShot(x-1, y)){
                            return new int[]{x-1, y};
                        }
                    }
                    
                    if(hasHitBelow){
                        //to the bottom
                        int d = 1;
                        while(isHit(x, y+d)){
                            d++;
                        }
                        //at this point, we either fell out of bounds or we found a cell that is not a hit
                        if(isInBounds(x, y+d) && !isShot(x, y+d)){
                            return new int[]{x, y+d};
                        }
                        
                        //to the top
                        if(isInBounds(x, y-1) && !isShot(x, y-1)){
                            return new int[]{x, y-1};
                        }
                    }
                    
                    //if we get here, then we dont have a hit to the right or below.
                    //see if we can shoot around this hit
                    for(int[] dir : cardinalDirections){
                        if(isInBounds(x+dir[0], y+dir[1]) && !isShot(x+dir[0], y+dir[1])){
                            return new int[]{x+dir[0], y+dir[1]};
                        }
                    }
                    
                    //old code:
//                    //check for neighboring hits to follow in the direction
//                    for(int[] dir : cardinalDirections){
//                        //keep track of the distance in this direction
//                        int d = 1;
//                        
//                        //keep going while we are in bounds and we have a hit here
//                        while(isHit(x+dir[0]*d, y+dir[1]*d)){
//                            hasHitRight = x+dir[0]*d > x;
//                            hasHitBelow = y+dir[1]*d > y;
//                            d++;
//                        }
//                        
//                        //if we broke out, check if we are in bounds and we haven't shot here
//                        //if so, then shoot here
//                        if(isInBounds(x+dir[0]*d, y+dir[1]*d) && !isShot(x+dir[0]*d, y+dir[1]*d)){
//                            return new int[]{x+dir[0]*d, y+dir[1]*d};
//                        }
//                        
//                        //if not, see if we have a shot to the right or below.
//                        //if we do, shoot left or above if we haven't already
//                        if(hasHitRight && isInBounds(x-1, y) && !isShot(x-1, y)){
//                            return new int[]{x-1, y};
//                        }
//                        if(hasHitBelow && isInBounds(x, y-1) && !isShot(x, y-1)){
//                            return new int[]{x, y-1};
//                        }
//                        
//                    }
                }
            }
        }
        
        //if we did not find a hit, then we need to pick a new location to shoot.
        //keep track of the longest stretches of no shots for both vertical and horizontal
        ArrayList<HashSet<Coord>> horizontalStretches = new ArrayList<>();
        ArrayList<HashSet<Coord>> verticalStretches = new ArrayList<>();
        
        HashSet<Coord> currentHorizontal = new HashSet<>();
        HashSet<Coord> currentVertical = new HashSet<>();
        
        //loop through horizontally and add to the stretches if we run into the wall or find a shot
        for(int y=0;y<10;y++){
            for(int x=0;x<=10;x++){
                if(isShot(x, y) || x == 10){
                    if(currentHorizontal.size() > 0){
                        horizontalStretches.add(currentHorizontal);
                        currentHorizontal = new HashSet<>();
                    }
                }
                else if(x<10){
                    currentHorizontal.add(new Coord(x, y));
                }
            }
        }
        
        //loop through vertically and add to the stretches if we run into the wall or find a shot
        for(int x=0;x<10;x++){
            for(int y=0;y<=10;y++){
                if(isShot(x, y) || y == 10){
                    if(currentVertical.size() > 0){
                        verticalStretches.add(currentVertical);
                        currentVertical = new HashSet<>();
                    }
                }
                else if(y<10){
                    currentVertical.add(new Coord(x, y));
                }
            }
        }
        
        //sort our stretches by size of the stretch
        Collections.sort(horizontalStretches, hashSetSizeComparator);
        Collections.sort(verticalStretches, hashSetSizeComparator);
        
        //print out our stretches
        if(GENERATE_OUTPUT){
            System.out.println("Horizontal Stretches: " + horizontalStretches.size());
            System.out.println(horizontalStretches.toString());
        }
        if(GENERATE_OUTPUT){
            System.out.println("Vertical Stretches: " + verticalStretches.size());
            System.out.println(verticalStretches.toString());
        }

        //keep track of the min dist that we need. Basically, if we have the
        //carrier left, there's no point to shoot in spaces less than 5 cells
        int minDist = 1;
        
        if(shipLeft.get(Constants.PATROL)){
            minDist = shipLength.get(Constants.PATROL);
        }
        if(shipLeft.get(Constants.DESTROYER)){
            minDist = shipLength.get(Constants.DESTROYER);
        }
        if(shipLeft.get(Constants.SUBMARINE)){
            minDist = shipLength.get(Constants.SUBMARINE);
        }
        if(shipLeft.get(Constants.BATTLESHIP)){
            minDist = shipLength.get(Constants.BATTLESHIP);
        }
        if(shipLeft.get(Constants.CARRIER)){
            minDist = shipLength.get(Constants.CARRIER);
        }
        
        //we want to find an intersection using the largest possible streaks
        ArrayList<HashSet<Coord>> unions = new ArrayList<>();
        for(int i=0;i<horizontalStretches.size();i++){
            for(int j=0;j<verticalStretches.size();j++){
                //largest ship cannot fit here anyways
                if(horizontalStretches.get(i).size() < minDist && verticalStretches.get(j).size() < minDist){
                    continue;
                }
                
                HashSet<Coord> union = new HashSet<>(horizontalStretches.get(i));
                union.addAll(verticalStretches.get(j));
                
                //only add this if there was an intersection
                if(union.size() < horizontalStretches.get(i).size() + verticalStretches.get(j).size()){
                    unions.add(union);
                }
            }
        }
        
        //prevent preferring shooting around the edges
        Collections.shuffle(unions);
        
        //and then sort again
        Collections.sort(unions, hashSetSizeComparator);
        
        if(GENERATE_OUTPUT){
            System.out.println("Unions: " + unions.size());
            System.out.println(unions.toString());
        }
        
        //go through all of our unions of intersections
        for(int i=0;i<unions.size();i++){
            //see if x or y repeats itself.
            int repeatX = -1;
            int repeatY = -1;
            int[] xCounter = new int[10];
            int[] yCounter = new int[10];
            
            for(Coord c : unions.get(i)){
                if(++xCounter[c.x] > 1){
                    repeatX = c.x;
                }
                if(++yCounter[c.y] > 1){
                    repeatY = c.y;
                }
                
                //if we have a repeating x and y, then that is our intersection
                if(repeatX >= 0 && repeatY >= 0){
                    return new int[]{repeatX, repeatY};
                }
            }
        }
        
        //if we got here, that means that we didn't have repeating x's and y's
        //so we know our streaks are 1 cell wide.
        //go back through them and shoot at the centroid
        for(int i=0;i<unions.size();i++){
            //see if x or y repeats itself.
            int sumX = 0;
            int sumY = 0;
            
            for(Coord c : unions.get(i)){
                sumX += c.x;
                sumY += c.y;
            }
            
            sumX /= unions.get(i).size();
            sumY /= unions.get(i).size();
            
            return new int[]{sumX, sumY};
        }
        
        //if we got here, I have no idea what went wrong.. soooooooo yeah
        //just find an unshot area
        for(int x=0;x<10;x++){
            for (int y = 0; y < 10; y++) {
                if(!isShot(x, y)){
                    return new int[]{x, y};
                }
            }
        }
        
        //fail safe? I guess..
        return new int[]{-1, -1};
    }

    @Override
    public ShipLayout getShipLayout() {
        ShipLayout sl = new ShipLayout();
        
        sl.placeShip(Constants.CARRIER, 1, 1, Constants.VERTICAL);
        sl.placeShip(Constants.BATTLESHIP, 5, 2, Constants.HORIZONTAL);
        sl.placeShip(Constants.SUBMARINE, 5, 6, Constants.VERTICAL);
        sl.placeShip(Constants.DESTROYER, 6, 5, Constants.HORIZONTAL);
        sl.placeShip(Constants.PATROL, 1, 8, Constants.HORIZONTAL);
        
        return sl;
    }

    @Override
    public void shotNotification(boolean yourShot, int x, int y, String result, String shipSunk) {
        if(GENERATE_OUTPUT){
            System.out.println("\n-----------\n");
            System.out.printf("Shot notification: %s, %d, %d, %s, %s%n%n", yourShot, x, y, result, shipSunk);
        }
        
        if(yourShot){
            board[x][y] = result;
            
            if(shipSunk != null){
                //we no longer have this ship
                shipLeft.put(shipSunk, false);
                int lengthOfSunkShip = shipLength.get(shipSunk);
                
                //we need to detect which direction this ship is placed
                direction:
                for(int[] dir : cardinalDirections){
                    //for this dir, make sure we have enough hits in a row
                    for(int i=1;i<lengthOfSunkShip;i++){
                        if(!isInBounds(x+dir[0]*i, y+dir[1]*i) || 
                                !isHit(x+dir[0]*i, y+dir[1]*i)){
                            continue direction;
                        }
                    }
                    
                    //if we got here, we have found it. If any straight line
                    //was not in bounds or a hit, we would have continued
                    //the outer for loop.
                    for(int i=0;i<lengthOfSunkShip;i++){
                        board[x+dir[0]*i][y+dir[1]*i] = shipSunk;
                    }
                    
                    //just to be sure we don't find another one, break
                    break;
                }
            }
        }
        
        if(GENERATE_OUTPUT){
            System.out.println(printBoard());
        }
    }

    @Override
    public void gameOver(boolean won) {
    }
    
    /**
     * Test whether or not we have shot here i.e. either hit or miss
     * @param x the x coord
     * @param y the y coord
     * @return true if we have shot here, false otherwise
     */
    private boolean isShot(int x, int y){
        return isInBounds(x, y) && board[x][y] != null;
    }
    
    /**
     * Test whether or not we have hit a ship here
     * @param x the x coord
     * @param y the y coord
     * @return true if we have hit here, false otherwise
     */
    private boolean isHit(int x, int y){
        return isInBounds(x, y) && board[x][y] != null && board[x][y].equals(Constants.HIT);
    }

    /**
     * Test whether or not the coord is in bounds
     * @param x the x coord
     * @param y the y coord
     * @return true if in bounds, false otherwise
     */
    private boolean isInBounds(int x, int y){
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    
    private String printBoard(){
        StringBuilder sb = new StringBuilder();
        //print the top row
        sb.append(String.format("%5s", ""));
        for(int i=0;i<10;i++){
            sb.append(String.format("%5d", i));
        }
        sb.append(String.format("%n"));
        
        //print the rows from the board
        for(int y=0;y<10;y++){
            sb.append(String.format("%5s", y));
            for(int x=0;x<10;x++){
                sb.append(String.format("%5s", board[x][y]==null?"":board[x][y]));
            }
            sb.append(String.format("%n"));
        }
        return sb.toString();
    }
}
