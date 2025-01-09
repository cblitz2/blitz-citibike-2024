package blitz.citibike.aws;

import blitz.citibike.*;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.amazonaws.services.lambda.runtime.Context;

public class CitiBikeRequestHandler implements RequestHandler
        <APIGatewayProxyRequestEvent, CitiBikeResponse> {

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);

        CitiBikeServiceFactory serviceFactory = new CitiBikeServiceFactory();
        CitiBikeService service = serviceFactory.getService();

        StationsResponse stationsResponse = service.getStationInformation().blockingGet();
        StatusResponse statusResponse = service.getStationStatus().blockingGet();

        FindStation findStation = new FindStation(service);

        StationsResponse.Station startStation = findStation.findClosestAvailable(
                stationsResponse, statusResponse, request.from.lat, request.from.lon);
        StationsResponse.Station endStation = findStation.findClosestReturn(
                stationsResponse, statusResponse, request.to.lat, request.to.lon);

        return new CitiBikeResponse(request.from, request.to, startStation, endStation);
    }
}
