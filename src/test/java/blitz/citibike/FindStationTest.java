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


        StationsResponse stationsResponse = service.getStationInformation().blockingGet();
        StatusResponse statusResponse = service.getStationStatus().blockingGet();

        // when
        StationsResponse.Station stationFound = station.findClosestAvailable(
                stationsResponse, statusResponse, latitude, longitude);
        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "11 Ave & W 59 St");
    }

    @Test
    void findClosestReturn() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        FindStation station = new FindStation(service);
        double latitude = 40.772200;
        double longitude = -73.990900;

        StationsResponse stationsResponse = service.getStationInformation().blockingGet();
        StatusResponse statusResponse = service.getStationStatus().blockingGet();

        // when
        StationsResponse.Station stationFound = station.findClosestReturn(
                stationsResponse, statusResponse, latitude, longitude);

        // then
        assertNotNull(station);
        assertEquals(stationFound.name, "11 Ave & W 59 St");
    }
}

