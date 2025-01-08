package blitz.citibike.aws;

public class CitiBikeRequest {
    public Point from;
    public Point to;

    public CitiBikeRequest(Point from, Point to) {
        this.from = from;
        this.to = to;
    }
}