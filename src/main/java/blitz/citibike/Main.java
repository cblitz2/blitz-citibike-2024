
package blitz.citibike;

import blitz.citibike.map.MapController;
import blitz.citibike.map.MapFrame;

public class Main {
    public static void main(String[] args) {
        MapController controller = new MapController(0,0,0,0);
        new MapFrame(controller).setVisible(true);
    }
}