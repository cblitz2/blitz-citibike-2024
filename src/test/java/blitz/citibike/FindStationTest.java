package blitz.citibike;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FindStationTest {

    @Test
    void findStatusById() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        FindStation station = new FindStation(service);
        String stationId = "a4d041fd-c5fa-4f52-800c-25d7a661a875";

        // when
        StatusResponse.StationStatus status = station.findStatusById(stationId);

        // then
        assertNotNull(status);
        assertEquals(stationId, status.station_id);
    }

    @Test
    void findClosestAvailable() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        FindStation station = new FindStation(service);
        double latitude = 40.772200;
        double longitude = -73.990900;

        // when
        StationsResponse.Station stationFound = station.findClosestAvailable(latitude, longitude);

        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "West End Ave & W 60 St");
    }

    @Test
    void findClosestReturn() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        FindStation station = new FindStation(service);
        double latitude = 40.772200;
        double longitude = -73.990900;

        // when
        StationsResponse.Station stationFound = station.findClosestReturn(latitude, longitude);

        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "11 Ave & W 59 St");
    }
}

