package blitz.citibike.map;

import org.jxmapviewer.*;
import org.jxmapviewer.input.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class MapComponent extends JComponent {
    private final JXMapViewer mapViewer;
    private GeoPosition startPosition;
    private GeoPosition endPosition;
    private RoutePainter routePainter;
    private WaypointPainter<Waypoint> locationWaypointPainter;
    private WaypointPainter<Waypoint> stationWaypointPainter;


    public MapComponent(MapFrame frame) {
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
                Point2D.Double point = new Point2D.Double(x, y);
                GeoPosition position = mapViewer.convertPointToGeoPosition(point);

                if (startPosition == null) {
                    startPosition = position;
                    frame.updateStartLabel(startPosition);
                } else if (endPosition == null) {
                    endPosition = position;
                    frame.updateEndLabel(endPosition);
                    drawLocationWaypoints(Set.of(startPosition, endPosition));
                    mapViewer.zoomToBestFit(Set.of(startPosition, endPosition), 1.0);
                } else {
                    startPosition = position;
                    endPosition = null;
                    frame.updateStartLabel(startPosition);
                    frame.updateEndLabel(null);
                }
            }
        });

    }

    public GeoPosition getStartPosition() {
        return startPosition;
    }

    public GeoPosition getEndPosition() {
        return endPosition;
    }

    public void clearSelections() {
        startPosition = null;
        endPosition = null;
        repaint();
    }

    public void drawRoute(List<GeoPosition> route) {
        this.routePainter = new RoutePainter(route);
        mapViewer.setOverlayPainter(new CompoundPainter<>(List.of(routePainter)));
        mapViewer.setAddressLocation(route.get(0));
        mapViewer.repaint();
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

    public void drawStationWaypoints(Set<GeoPosition> stationPositions) {
        if (stationWaypointPainter == null) {
            stationWaypointPainter = new WaypointPainter<>();
        }
        Set<Waypoint> waypoints = new HashSet<>();
        for (GeoPosition position : stationPositions) {
            waypoints.add(new DefaultWaypoint(position));
        }
        stationWaypointPainter.setWaypoints(waypoints);
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

}

