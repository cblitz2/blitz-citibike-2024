package blitz.citibike;

import java.util.List;

public class StatusResponse {
    public Data data;

    public class Data {
        public List<StationStatus> stations;
    }

    public class StationStatus {
        //CHECKSTYLE:OFF
        public String station_id;
        public int num_bikes_available;
        public int num_docks_available;
        public int num_ebikes_available;
        //CHECKSTYLE:ON
    }
}
