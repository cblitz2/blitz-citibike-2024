package blitz.citibike.map;

import org.jxmapviewer.viewer.GeoPosition;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class MapFrame extends JFrame {
    private final MapComponent mapComponent;
    private final JTextField startField;
    private final JTextField endField;

    public MapFrame(MapController controller) {
        this.mapComponent = new MapComponent(this);

        setTitle("CitiBike");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        JLabel startLabel = new JLabel("Start (lat, lon): ");
        startField = new JTextField(25);
        JLabel endLabel = new JLabel("End (lat, lon): ");
        endField = new JTextField(25);

        JButton submitButton = getSubmitButton(controller);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            updateStartLabel(null);
            updateEndLabel(null);
            mapComponent.clearSelections();
        });

        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");
        zoomInButton.addActionListener(e -> mapComponent.zoomIn());
        zoomOutButton.addActionListener(e -> mapComponent.zoomOut());

        inputPanel.add(startLabel);
        inputPanel.add(startField);
        inputPanel.add(endLabel);
        inputPanel.add(endField);
        inputPanel.add(submitButton);
        inputPanel.add(clearButton);
        inputPanel.add(zoomInButton);
        inputPanel.add(zoomOutButton);

        add(inputPanel, BorderLayout.NORTH);
        add(mapComponent, BorderLayout.CENTER);
    }

    private JButton getSubmitButton(MapController controller) {
        JButton submitButton = new JButton("Find Route");
        submitButton.addActionListener(e -> {
            GeoPosition start = mapComponent.getStartPosition();
            GeoPosition end = mapComponent.getEndPosition();

            List<GeoPosition> route = controller.getRoute(
                    start.getLatitude(), start.getLongitude(),
                    end.getLatitude(), end.getLongitude()
            );

            if (route.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unable to find a route.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                mapComponent.drawRoute(route);

                Set<GeoPosition> LocationWaypoints = Set.of(
                        route.get(0),
                        route.get(route.size() - 1)
                );
                mapComponent.drawLocationWaypoints(LocationWaypoints);
                Set<GeoPosition> stationWaypoints = Set.copyOf(route);
                mapComponent.drawStationWaypoints(stationWaypoints);
            }
        });
        return submitButton;
    }

    public void updateStartLabel(GeoPosition startPosition) {
        startField.setText(startPosition != null ? startPosition.toString() : "");
        startField.repaint();
    }

    public void updateEndLabel(GeoPosition endPosition) {
        endField.setText(endPosition != null ? endPosition.toString() : "");
        endField.repaint();
    }

}


