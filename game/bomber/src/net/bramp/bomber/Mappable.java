package net.bramp.bomber;

/**
 * Something that sits on the map
 * 
 * @author bramp
 *
 */
public interface Mappable {
	public int getMapX();
	public int getMapY();
	
	public void setMapPosition(int map_x, int map_y);
}
