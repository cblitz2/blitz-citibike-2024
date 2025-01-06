package blitz.citibike.map;

import blitz.citibike.aws.CitiBikeRequest;
import blitz.citibike.aws.CitiBikeResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LambdaService {
    @POST("/")
    Single<CitiBikeResponse> findRoute(@Body CitiBikeRequest request);
}
