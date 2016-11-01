package charistas.actibit.api;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;

/**
 * This class is responsible for configuring the way Retrofit library will work when trying to
 * communicate with Fitbit's servers. More specifically it specifies the header and fields that will
 * send for each of the supported Fitbit activities.
 */
public interface EndpointInterface {
    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logTennis (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logCycling (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis,
            @Field(QuickPreferences.DISTANCE) String distance
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logCleaning (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logCooking (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logBasketball (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logBilliard (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logSoccer (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logYoga (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logVolleyball (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );

    @FormUrlEncoded
    @POST(QuickPreferences.ACTIVITIES_ENDPOINT)
    Call<Void> logSwimming (
            @Header(QuickPreferences.AUTHORIZATION)String accessToken,
            @Field(QuickPreferences.ACTIVITY_ID) String activityId,
            @Field(QuickPreferences.DATE) String date,
            @Field(QuickPreferences.START_TIME) String startTime,
            @Field(QuickPreferences.DURATION) String durationMillis
    );
}
