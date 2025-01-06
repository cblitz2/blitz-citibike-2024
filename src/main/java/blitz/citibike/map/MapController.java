package blitz.citibike.map;

import blitz.citibike.aws.*;
import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.viewer.GeoPosition;
import java.util.*;

public class MapController {

    private final LambdaService service;
    private Point from;
    private Point to;

    public MapController(double fromLat, double fromLon, double toLat, double toLon) {
        this.service = new LambdaServiceFactory().getService();
        this.from = new Point(fromLat, fromLon);
        this.to = new Point(toLat, toLon);
    }

    public Single<List<GeoPosition>> getRoute() {
        CitiBikeRequest request = new CitiBikeRequest();
        request.from = this.from;
        request.to = this.to;

        return service.findRoute(request)
                .map(responseBody -> List.of(
                        new GeoPosition(responseBody.from.lat, responseBody.from.lon),
                        new GeoPosition(responseBody.start.lat, responseBody.start.lon),
                        new GeoPosition(responseBody.end.lat, responseBody.end.lon),
                        new GeoPosition(responseBody.to.lat, responseBody.to.lon)
                ));
    }

    public void setFrom(double fromLat, double fromLon) {
        this.from = new Point(fromLat, fromLon);
    }

    public void setTo(double toLat, double toLon) {
        this.to = new Point(toLat, toLon);
    }

}


