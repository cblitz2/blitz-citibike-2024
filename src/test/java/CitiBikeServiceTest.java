import blitz.citibike.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiBikeServiceTest {

    @Test
    void getStationInformation() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        // when
        StationsResponse response = service.getStationInformation().blockingGet();

        // then
        StationsResponse.Station station = response.data.stations.get(0);
        assertNotNull(station.station_id);
        assertNotNull(station.name);
        assertNotNull(station.lon);
        assertNotNull(station.lat);
    }

    @Test
    void getStationStatus() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        // when
        StatusResponse response = service.getStationStatus().blockingGet();

        // then
        StatusResponse.StationStatus status = response.data.stations.get(0);
        assertNotNull(response.data.stations.get(2).station_id);
        assertNotNull(status.num_bikes_available);
        assertNotNull(status.num_ebikes_available);
        assertNotNull(status.num_docks_available);
    }
}