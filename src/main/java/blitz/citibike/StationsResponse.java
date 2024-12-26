package blitz.citibike;

import java.util.List;

public class StationsResponse {
    public Data data;

    public class Data {
        public List<Station> stations;
    }

    public class Station {
        //CHECKSTYLE:OFF
        public String station_id;
        //CHECKSTYLE:ON
        public String name;
        public double lat;
        public double lon;
    }
}
