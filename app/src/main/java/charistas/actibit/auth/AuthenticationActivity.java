package charistas.actibit.auth;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import charistas.actibit.PickActivity;
import charistas.actibit.R;

@SuppressLint("SetJavaScriptEnabled")
public class AuthenticationActivity extends Activity {
    public final static String CONSUMER_KEY = "47481d0855aa4f02d5e238d5b2d33581";
    public  final static String CONSUMER_SECRET = "1ef1d9d56153d9aa6f7d1d94fa5bb786";
    static OAuthService service;
    static Token requestToken;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_authentication);
        final WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(/*this*/), "HtmlViewer");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        service = new ServiceBuilder().provider(FitbitApi.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();

        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                requestToken = service.getRequestToken();
                final String authURL = service.getAuthorizationUrl(requestToken);

                // Webview navigation should run on main thread
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        webview.loadUrl(authURL);
                    }
                });
            }
        }).start();
    }

    class MyJavaScriptInterface
    {
        boolean firstRun=true;

        public MyJavaScriptInterface() {
        }

        @android.webkit.JavascriptInterface
        public void showHTML(final String html) {
            if(firstRun){
                firstRun=false;
                return;
            }

            String divStr = "gap20\">";
            int first = html.indexOf(divStr);
            int second = html.indexOf("</div>",first);

            if(first!=-1){
                final String pin = html.substring(first+divStr.length(),second);
                final Verifier verifier = new Verifier(pin);
                new Thread(new Runnable() {
                    public void run() {
                        final Token accessToken = service.getAccessToken(requestToken, verifier);
                        SharedPreferences.Editor editor = getSharedPreferences("charistas.actibit", MODE_PRIVATE).edit();
                        editor.putString("ACCESS_TOKEN", accessToken.getToken());
                        editor.putString("ACCESS_SECRET", accessToken.getSecret());
                        editor.putString("ACCESS_RAW_RESPONSE", accessToken.getRawResponse());
                        editor.commit();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show();
                                PickActivity.setSignedInStatus(true);
                            }
                        });
                    }
                }).start();
                finish();
            }
        }
    }
}