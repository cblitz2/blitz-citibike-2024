package blitz.citibike.map;

import blitz.citibike.Station;
import blitz.citibike.Stations;
import blitz.citibike.StationsResponse;
import blitz.citibike.aws.CitiBikeResponse;
import org.jxmapviewer.*;
import org.jxmapviewer.input.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MapComponent extends JComponent {
    private final JXMapViewer mapViewer;
    private GeoPosition startPosition;
    private GeoPosition endPosition;
    private RoutePainter routePainter;
    private Set<Waypoint> waypoints;
    private WaypointPainter<Waypoint> locationWaypointPainter;
    private WaypointPainter<Waypoint> stationWaypointPainter;


    public MapComponent(MapController controller) {
        mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory factory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(factory);
        factory.setThreadPoolSize(8);

        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(40.7128, -74.0060));
        setLayout(new BorderLayout());
        add(new JScrollPane(mapViewer), BorderLayout.CENTER);

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                controller.handleMapClick(x, y);
            }
        });
    }

    public void clearSelections() {
        startPosition = null;
        endPosition = null;
        repaint();
    }

    public void drawRoute(CitiBikeResponse response) {
        List<GeoPosition> track = new ArrayList<>();
        Station startStation = response.start;
        Station endStation = response.end;
        track.add(new GeoPosition(response.from.lat, response.from.lon));
        track.add(new GeoPosition(startStation.lat, startStation.lon));
        track.add(new GeoPosition(endStation.lat, endStation.lon));
        track.add(new GeoPosition(response.to.lat, response.to.lon));
        this.routePainter = new RoutePainter(track);
        mapViewer.setOverlayPainter(new CompoundPainter<>(List.of(routePainter)));
        mapViewer.repaint();
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypoints = Set.of(
                new DefaultWaypoint(startStation.lat, startStation.lon),
                new DefaultWaypoint(endStation.lat, endStation.lon)
        );
        waypointPainter.setWaypoints(waypoints);
        drawStationWaypoints(waypoints);
        updatePainters();
    }

    public void drawLocationWaypoints(Set<GeoPosition> positions) {
        if (locationWaypointPainter == null) {
            locationWaypointPainter = new WaypointPainter<>();
        }
        Set<Waypoint> waypoints = new HashSet<>();
        for (GeoPosition position : positions) {
            waypoints.add(new DefaultWaypoint(position));
        }
        locationWaypointPainter.setWaypoints(waypoints);
        updatePainters();
    }

    public void drawStationWaypoints(Set<Waypoint> stationPositions) {
        if (stationWaypointPainter == null) {
            stationWaypointPainter = new WaypointPainter<>();
        }
        stationWaypointPainter.setWaypoints(stationPositions);
        updatePainters();
    }


    private void updatePainters() {
        List<Painter<JXMapViewer>> painters = new ArrayList<>();

        if (locationWaypointPainter != null) {
            painters.add(locationWaypointPainter);
        }
        if (stationWaypointPainter != null) {
            painters.add(stationWaypointPainter);
        }
        if (routePainter != null) {
            painters.add(routePainter);
        }
        if (!painters.isEmpty()) {
            CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
            mapViewer.setOverlayPainter(compoundPainter);
        } else {
            mapViewer.setOverlayPainter(null);
        }

        mapViewer.repaint();
    }

    public void zoomIn() {
        int currentZoom = mapViewer.getZoom();
        mapViewer.setZoom(currentZoom - 1);
    }

    public void zoomOut() {
        int currentZoom = mapViewer.getZoom();
        mapViewer.setZoom(currentZoom + 1);
    }

    public JXMapViewer getMapViewer() {
        return mapViewer;
    }

}