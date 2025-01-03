package blitz.citibike.map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LambdaServiceFactory {
    public LambdaService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://l3frhnewxqq3drmcrnl4umtmai0dntrg.lambda-url.us-east-1.on.aws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(LambdaService.class);
    }
}
