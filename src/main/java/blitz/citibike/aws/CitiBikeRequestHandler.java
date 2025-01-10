package blitz.citibike.aws;

import blitz.citibike.*;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.amazonaws.services.lambda.runtime.Context;

public class CitiBikeRequestHandler implements RequestHandler
        <APIGatewayProxyRequestEvent, CitiBikeResponse> {
    private final StationsCache cache = new StationsCache();

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);
        FindStation findStation = new FindStation(cache);

        CitiBikeService service = (CitiBikeService) cache.getStations();
        Stations info = cache.getStations();
        Stations status = service.getStationStatus().blockingGet();

        Station startStation = findStation.findClosestAvailable(
                info, status, request.from.lat, request.from.lon);
        Station endStation = findStation.findClosestReturn(
                info, status, request.to.lat, request.to.lon);

        return new CitiBikeResponse(request.from, request.to, startStation, endStation);
    }
}
