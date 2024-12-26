package blitz.citibike;

public class FindStation {

    private final CitiBikeService service;

    public FindStation(CitiBikeService service) {
        this.service = service;
    }

    public StatusResponse.StationStatus findStatusById(String stationId) {
        StatusResponse.StationStatus foundStation = null;
        StatusResponse response = service.getStationStatus().blockingGet();

        for (StatusResponse.StationStatus station : response.data.stations) {
            if (station.station_id.equals(stationId)) {
                foundStation = station;
                break;
            }
        }
        return foundStation;
    }

    public StationsResponse.Station findClosestAvailable(double latitude, double longitude) {
        return findClosestStation(latitude, longitude, true);
    }

    public StationsResponse.Station findClosestReturn(double latitude, double longitude) {
        return findClosestStation(latitude, longitude, false);
    }

    public StationsResponse.Station findClosestStation(double latitude, double longitude, boolean findBikes) {
        StationsResponse stationsResponse = service.getStationInformation().blockingGet();
        StatusResponse statusResponse = service.getStationStatus().blockingGet();

        StationsResponse.Station closestStation = null;
        double shortestDistance = Double.MAX_VALUE;

        for (StationsResponse.Station station : stationsResponse.data.stations) {
            for (StatusResponse.StationStatus status : statusResponse.data.stations) {
                if (status.station_id.equals(station.station_id)
                        && (findBikes ? status.num_bikes_available > 0 : status.num_docks_available > 0)) {

                    double currentDistance = distance(latitude, longitude, station.lat, station.lon);
                    if (currentDistance < shortestDistance) {
                        shortestDistance = currentDistance;
                        closestStation = station;
                    }
                    break;
                }
            }
        }
        return closestStation;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double xDist = lat2 - lat1;
        double yDist = lon2 - lon1;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
}