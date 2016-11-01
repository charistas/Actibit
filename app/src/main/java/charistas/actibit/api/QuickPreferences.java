package charistas.actibit.api;

/**
 * This class is responsible for keeping various info about the Fitbit API and the way the app
 * communicates with it.
 */
public class QuickPreferences {
    // Preferences
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String HAVE_AUTHORIZATION = "accessTokenExists";
    public static final String USER_ID = "clientID";
    public static final String FULL_AUTHORIZATION = "fullAuthorizationToken";
    public static final String TOKEN_TYPE = "tokenType";

    // Header Parameters
    static final String AUTHORIZATION = "Authorization";

    // POST Parameters
    static final String ACTIVITY_ID = "activityId";
    public static final String DATE = "date";
    public static final String START_TIME = "startTime";
    public static final String DURATION = "durationMillis";
    public static final String DISTANCE = "distance";

    // Resource URLs
    static final String AUTH_URL = "https://www.fitbit.com/oauth2/authorize?"
            +"response_type=token"
            +"&client_id=foo" // TODO: ***Attention*** You need to replace 'foo' with a real client id taken from https://dev.fitbit.com/apps
            +"&redirect_uri=actibit%3A%2F%2Flogin"  // Corresponds to redirect_uri = actibit://login
            +"&scope=activity"
            +"&expires_in=31536000" // Equals to 1 year
            +"&prompt=login";
    public static final String BASE_URL = "https://api.fitbit.com/";
    static final String ACTIVITIES_ENDPOINT = "1/user/-/activities.json";

    // Response Codes
    public static final int CODE_OK = 200;
    public static final int CODE_CREATED = 201;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_BAD_GATEWAY = 502;
}
