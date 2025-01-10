package blitz.citibike;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiBikeServiceTest {

    @Test
    void getStationInformation() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        // when
        Stations response = service.getStationInformation().blockingGet();

        // then
        assertNotNull(response.data.stations[0].station_id);
        assertNotNull(response.data.stations[0].name);
        assertNotEquals(0, response.data.stations[0].lon);
        assertNotEquals(0, response.data.stations[0].lat);
    }

    @Test
    void getStationStatus() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        // when
        Stations response = service.getStationStatus().blockingGet();

        // then
        assertNotNull(response.data.stations[2].station_id);
        assertNotEquals(0, response.data.stations[2].num_docks_available);

    }
}