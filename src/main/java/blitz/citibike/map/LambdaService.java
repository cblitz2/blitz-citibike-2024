package blitz.citibike.map;

import blitz.citibike.aws.CitiBikeRequest;
import blitz.citibike.aws.CitiBikeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LambdaService {
    @POST("/")
    Call<CitiBikeResponse> findRoute(@Body CitiBikeRequest request);
}
