package blitz.citibike.map;

import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class MapFrame extends JFrame {
    private final MapComponent mapComponent;
    private final JTextField startField;
    private final JTextField endField;
    private final JLabel startLabel;
    private final JLabel endLabel;
    private final JButton submitButton;
    private final JButton clearButton;
    private final JButton zoomInButton;
    private final JButton zoomOutButton;

    public MapFrame(MapController controller) {
        this.mapComponent = new MapComponent(this);

        setTitle("CitiBike");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        startLabel = new JLabel("Start (lat, lon): ");
        startField = new JTextField(25);
        endLabel = new JLabel("End (lat, lon): ");
        endField = new JTextField(25);

        submitButton = getSubmitButton(controller);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            updateStartLabel(null);
            updateEndLabel(null);
            mapComponent.clearSelections();
        });

        zoomInButton = new JButton("Zoom In");
        zoomOutButton = new JButton("Zoom Out");
        zoomInButton.addActionListener(e -> mapComponent.zoomIn());
        zoomOutButton.addActionListener(e -> mapComponent.zoomOut());

        JPanel inputPanel = new JPanel();
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

            controller.setFrom(start.getLatitude(), start.getLongitude());
            controller.setTo(end.getLatitude(), end.getLongitude());

            List<GeoPosition> route = controller.getRoute().blockingGet();

            mapComponent.drawRoute(route);

            Set<GeoPosition> locationWaypoints = Set.of(
                    route.get(0),
                    route.get(route.size() - 1)
            );
            mapComponent.drawLocationWaypoints(locationWaypoints);
            Set<GeoPosition> stationWaypoints = Set.copyOf(route);
            mapComponent.drawStationWaypoints(stationWaypoints);
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


