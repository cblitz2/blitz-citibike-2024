package blitz.citibike.map;

import blitz.citibike.aws.*;
import org.jxmapviewer.viewer.GeoPosition;
import retrofit2.*;
import java.io.IOException;
import java.util.*;

public class MapController {

    private final LambdaService service;

    public MapController() {
        this.service = new LambdaServiceFactory().getService();
    }

    public List<GeoPosition> getRoute(double fromLat, double fromLon, double toLat, double toLon) {
        CitiBikeRequest request = new CitiBikeRequest();
        request.from = new Point(fromLat, fromLon);
        request.to = new Point(toLat, toLon);

        Call<CitiBikeResponse> call = service.findRoute(request);

        try {
            Response<CitiBikeResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                CitiBikeResponse responseBody = response.body();

                return List.of(
                        new GeoPosition(responseBody.from.lat, responseBody.from.lon),
                        new GeoPosition(responseBody.start.lat, responseBody.start.lon),
                        new GeoPosition(responseBody.end.lat, responseBody.end.lon),
                        new GeoPosition(responseBody.to.lat, responseBody.to.lon)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}


