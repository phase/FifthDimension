package five.main.hub.util;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
/**
 * 
 * 
 * NMS/OBC: None
 * <p>
 * Xern-Util Reliances: None
 * <p>
 * Notes: 
 * <p>
 * A wrapper for doors that works better than the current bukkit implementation
 * @author Jogy34
 */
@SuppressWarnings("deprecation")
public class Door
{
    /**
     * An exception that is thrown when the Location that is sent in for the door
     * does not contain a door.
     */
    public static class NonExistantDoorException extends Exception
    {
        private static final long serialVersionUID = 1L;
        
        protected Location loc;
        public NonExistantDoorException(Location loc)
        {
            this.loc = loc;
        }
        
        @Override
        public void printStackTrace()
        {
            System.err.println("Block at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " in World " +
                    loc.getWorld().getName() + " is not a valid door but was attempted to be made into a door");
            for(StackTraceElement st : this.getStackTrace())
            {
                System.err.println("    Caused by " + st.getClassName() + " in method " + st.getMethodName() + 
                        " at Line " + st.getLineNumber());
            }
        }
    }
    
    /**
     * The different states that a door can be in. They take in the data value of
     * the bottom half of a door when it is closed and if the state represents
     * when the door is closed or not. The directional portions correspond to the
     * side of the block the door is on when it is closed.
     */
    public static enum DoorState
    {
        CLOSED_WEST( (byte) 0, true),
        CLOSED_NORTH((byte) 1, true),
        CLOSED_EAST( (byte) 2, true),
        CLOSED_SOUTH((byte) 3, true),
        
        OPEN_WEST( (byte) 4, false),
        OPEN_NORTH((byte) 5, false),
        OPEN_EAST( (byte) 6, false),
        OPEN_SOUTH((byte) 7, false);
        
        public final byte bottomData;
        public final boolean isClosed;
        private DoorState(byte bottomData, boolean isClosed)
        {
            this.bottomData = bottomData;
            this.isClosed = isClosed;
        }
        
        /**
         * Retrieves the DoorState that matches with the sent in data value
         * 
         * @param data The current data value of the bottom half of the door
         * @return The DoorState that corresponds to the data value or CLOSED_WEST if one isn't found
         */
        public static DoorState getState(byte data)
        {
            for(DoorState s : values())
            {
                if(s.bottomData == data) return s;
            }
            
            return CLOSED_WEST;
        }
    }
    
    public static Map<DoorState, DoorState> correspondingStates = new HashMap<DoorState, DoorState>();
    
    protected Location loc;
    
    /**
     * Creates a new Door object from a Location
     * 
     * @param loc The location of the door. Can be either the top or bottom half. It is automatically mapped to the bottom.
     * @throws NonExistantDoorException If the Location sent in doesn't correspond to a wooden or iron door.
     */
    public Door(Location loc) throws NonExistantDoorException
    {
        if(loc.getBlock().getType() == Material.WOODEN_DOOR || loc.getBlock().getType() == Material.IRON_DOOR_BLOCK)
        {
            org.bukkit.material.Door bukkitDoor = (org.bukkit.material.Door) loc.getBlock().getState().getData();
            if(bukkitDoor.isTopHalf()) this.loc = loc.getBlock().getRelative(BlockFace.DOWN).getLocation();
            else this.loc = loc;
        }
        else
        {
            throw new NonExistantDoorException(loc);
        }
    }
    
    /**
     * @return If the door that this object corresponds to is closed or not
     * @throws NonExistantDoorException If the location no longer corresponds to a wooden or iron door
     */
    public boolean isClosed() throws NonExistantDoorException
    {
        if(!this.isStillDoor()) throw new NonExistantDoorException(loc);
        return DoorState.getState(loc.getBlock().getData()).isClosed;
    }
    
    /**
     * Toggles the state of the door from either closed to open or open to closed.
     * Plays the toggle sound
     * 
     * @throws NonExistantDoorException If the location for this door no longer corresponds to a wooden or iron door
     */
    public void toggle() throws NonExistantDoorException
    {
        this.toggle(true);
    }
    
    /**
     * Toggles the state of the door from either closed to open or open to closed.
     * 
     * @param playSound Wether or not the door toggle sound should be played
     * @throws NonExistantDoorException If the location for this door no longer corresponds to a wooden or iron door
     */
    public void toggle(boolean playSound) throws NonExistantDoorException
    {
        if(!this.isStillDoor()) throw new NonExistantDoorException(loc);
        
        DoorState state = DoorState.getState(loc.getBlock().getData());
        
        loc.getBlock().setData(correspondingStates.get(state).bottomData);
        
        if(playSound) loc.getWorld().playEffect(loc, Effect.DOOR_TOGGLE, 0);
    }
    
    /**
     * @return The location of the bottom half of this door
     */
    public Location getBottom()
    {
        return loc;
    }
    
    /**
     * @return The location of the top half of this door
     */
    public Location getTop()
    {
        return loc.getBlock().getRelative(BlockFace.UP).getLocation();
    }
    
    /**
     * @return The current DoorState that this door is in
     * @throws NonExistantDoorException If the location for this door no longer corresponds to a wooden or iron door
     */
    public DoorState getDoorState() throws NonExistantDoorException
    {
        if(!this.isStillDoor()) throw new NonExistantDoorException(loc);
        
        return DoorState.getState(loc.getBlock().getData());
    }
    
    /**
     * Changes the location that this corresponds to so the objects can be recycled
     * 
     * @param loc The new location of the door
     * @throws NonExistantDoorException If the location that is sent in doesn't correspond to a wooden or iron door
     */
    public void setNewLocation(Location loc) throws NonExistantDoorException
    {
        if(loc.getBlock().getType() == Material.WOODEN_DOOR || loc.getBlock().getType() == Material.IRON_DOOR_BLOCK)
        {
            org.bukkit.material.Door bukkitDoor = (org.bukkit.material.Door) loc.getBlock().getState().getData();
            if(bukkitDoor.isTopHalf()) this.loc = loc.getBlock().getRelative(BlockFace.DOWN).getLocation();
            else this.loc = loc;
        }
        else
        {
            throw new NonExistantDoorException(loc);
        }
    }
    
    /**
     * @return if the location that this corresponds to is still a wooden or iron door
     */
    public boolean isStillDoor()
    {
        return (loc.getBlock().getType() == Material.WOODEN_DOOR || loc.getBlock().getType() == Material.IRON_DOOR_BLOCK);
    }
    
    static
    {
        correspondingStates.put(DoorState.CLOSED_EAST, DoorState.OPEN_EAST);
        correspondingStates.put(DoorState.OPEN_EAST, DoorState.CLOSED_EAST);
        
        correspondingStates.put(DoorState.CLOSED_NORTH, DoorState.OPEN_NORTH);
        correspondingStates.put(DoorState.OPEN_NORTH, DoorState.CLOSED_NORTH);
        
        correspondingStates.put(DoorState.CLOSED_SOUTH, DoorState.OPEN_SOUTH);
        correspondingStates.put(DoorState.OPEN_SOUTH, DoorState.CLOSED_SOUTH);
        
        correspondingStates.put(DoorState.OPEN_WEST, DoorState.CLOSED_WEST);
        correspondingStates.put(DoorState.CLOSED_WEST, DoorState.OPEN_WEST);
    }
}
