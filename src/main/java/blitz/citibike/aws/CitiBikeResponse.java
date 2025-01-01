package blitz.citibike.aws;

import blitz.citibike.*;

public class CitiBikeResponse {

    public Point from;
    public Point to;
    public StationsResponse.Station start;
    public StationsResponse.Station end;

    public CitiBikeResponse(Point from, Point to, StationsResponse.Station start, StationsResponse.Station end) {
        this.from = from;
        this.to = to;
        this.start = start;
        this.end = end;
    }
}


