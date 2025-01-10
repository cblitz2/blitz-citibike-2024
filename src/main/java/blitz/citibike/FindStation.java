package blitz.citibike;

import blitz.citibike.aws.StationsCache;

public class FindStation {

    private StationsCache cache;

    public FindStation(StationsCache cache) {
        this.cache = cache;
    }

    public Station findClosestAvailable(Stations stationsResponse,
                                         Stations statusResponse,
                                         double latitude,
                                         double longitude) {
        return findClosestStation(stationsResponse, statusResponse, latitude, longitude, true);
    }

    public Station findClosestReturn(Stations stationsResponse,
                                     Stations statusResponse,
                                      double latitude,
                                      double longitude) {
        return findClosestStation(stationsResponse, statusResponse, latitude, longitude, false);
    }

    public Station findClosestStation(Stations stationsResponse,
                                      Stations statusResponse,
                                      double latitude, double longitude,
                                      boolean findBikes) {
        Station closestStation = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Station station : stationsResponse.data.stations) {
            for (Station status : statusResponse.data.stations) {
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