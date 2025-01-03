package blitz.citibike.aws;

import blitz.citibike.*;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.amazonaws.services.lambda.runtime.Context;

public class CitiBikeRequestHandler implements RequestHandler
        <APIGatewayProxyRequestEvent, CitiBikeResponse> {
    private final FindStation findStation;

    public CitiBikeRequestHandler() {
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        this.findStation = new FindStation(service);
    }

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);

        StationsResponse.Station startStation = findStation.findClosestAvailable(request.from.lat, request.from.lon);
        StationsResponse.Station endStation = findStation.findClosestStation(request.to.lat, request.to.lon, false);

        return new CitiBikeResponse(request.from, request.to, startStation, endStation);
    }
}
