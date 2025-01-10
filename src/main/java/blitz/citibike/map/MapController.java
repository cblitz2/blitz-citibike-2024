package blitz.citibike.map;

import blitz.citibike.aws.*;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;
import java.util.*;

public class MapController {

    private final LambdaService service;
    private Point from;
    private Point to;
    private GeoPosition startPosition;
    private GeoPosition endPosition;
    private final MapComponent component;
    private final MapFrame frame;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MapController(MapFrame frame) {
        this.service = new LambdaServiceFactory().getService();
        this.component = new MapComponent(this);
        this.frame = frame;
    }

    public void getRoute() {
        CitiBikeRequest request = new CitiBikeRequest(from, to);
        disposables.add(service.findRoute(request)
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        response -> {
                            if (response.start != null && response.end != null) {
                                component.drawRoute(response);
                            }
                        },
                        Throwable::printStackTrace
                ));
    }

    public void setFrom(double fromLat, double fromLon) {
        this.from = new Point(fromLat, fromLon);
    }

    public void setTo(double toLat, double toLon) {
        this.to = new Point(toLat, toLon);
    }

    public void handleMapClick(int x, int y) {
        Point2D.Double point = new Point2D.Double(x, y);
        GeoPosition position = component.getMapViewer().convertPointToGeoPosition(point);

        if (startPosition == null) {
            startPosition = position;
            setFrom(x, y);
            frame.updateStartLabel(startPosition);
        } else if (endPosition == null) {
            endPosition = position;
            setTo(x, y);
            frame.updateEndLabel(endPosition);
            component.drawLocationWaypoints(Set.of(startPosition, endPosition));
            component.getMapViewer().zoomToBestFit(Set.of(startPosition, endPosition), 1.0);
        } else {
            startPosition = position;
            setFrom(x, y);
            endPosition = null;
            frame.updateStartLabel(startPosition);
            frame.updateEndLabel(null);
        }
    }

    public MapComponent getComponent() {
        return component;
    }

    public GeoPosition getStartPosition() {
        return startPosition;
    }

    public GeoPosition getEndPosition() {
        return endPosition;
    }

}


