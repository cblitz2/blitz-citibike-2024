package blitz.citibike;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CitiBikeService {

    @GET("/gbfs/en/station_information.json")
    Single<StationsResponse> getStationInformation();

    @GET("/gbfs/en/station_status.json")
    Single<StatusResponse> getStationStatus();
}
