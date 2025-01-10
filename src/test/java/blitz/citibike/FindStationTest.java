package blitz.citibike;

import blitz.citibike.aws.StationsCache;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FindStationTest {

    @Test
    void findClosestAvailable() {
        // given
        StationsCache cache = new StationsCache();
        FindStation station = new FindStation(cache);
        double latitude = 40.772200;
        double longitude = -73.990900;

        Stations stationsResponse = cache.getStations();
        Stations statusResponse = cache.getStations();

        // when
        Station stationFound = station.findClosestAvailable(
                stationsResponse, statusResponse, latitude, longitude);
        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "11 Ave & W 59 St");
    }

    @Test
    void findClosestReturn() {
        // given
        StationsCache cache = new StationsCache();
        FindStation station = new FindStation(cache);
        double latitude = 40.772200;
        double longitude = -73.990900;

        Stations stationsResponse = cache.getStations();
        Stations statusResponse = cache.getStations();

        // when
        Station stationFound = station.findClosestReturn(
                stationsResponse, statusResponse, latitude, longitude);

        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "11 Ave & W 59 St");
    }
}

