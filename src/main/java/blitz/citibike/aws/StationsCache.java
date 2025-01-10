package blitz.citibike.aws;

import blitz.citibike.*;
import com.google.gson.Gson;
import io.reactivex.rxjava3.annotations.NonNull;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.time.*;

public class StationsCache {
    private final String bucketName = "blitz.citibike";
    private final String key = "info.json";
    private final S3Client s3Client;
    private final CitiBikeService citiBikeService;
    private final Region region = Region.US_EAST_1;
    private Stations stations;
    private Station station = new Station();
    private final Gson gson = new Gson();
    private Instant lastModified;

    public StationsCache() {
        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.citiBikeService = new CitiBikeServiceFactory().getService();
    }

    public Stations getStations() {
        try {
            if (stations != null && lastModified != null
                    && Duration.between(lastModified, Instant.now()).toHours() < 1) {
                return stations;
            }
            if ((stations == null && lastModifiedS3())
                    || (lastModified == null || Duration.between(lastModified, Instant.now()).toHours() >= 1)) {
                stations = citiBikeService.getStationInformation().blockingGet();
                lastModified = Instant.now();
                writeToS3();
            } else if (stations == null) {
                stations = readFromS3();
                lastModified = getLastModified();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stations;
    }

    public void writeToS3() {
        try {
            Gson gson = new Gson();
            String content = gson.toJson(stations);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stations readFromS3() {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            InputStream in = s3Client.getObject(getObjectRequest);
            stations = gson.fromJson(new InputStreamReader(in), Stations.class);
        } catch (Exception e) {
            return null;
        }
        return stations;
    }


    private boolean lastModifiedS3() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
            return Duration.between(lastModified, Instant.now()).toHours() >= 1;
        } catch (Exception e) {
            return false;
        }
    }

    private Instant getLastModified() {
        return lastModified;
    }

}