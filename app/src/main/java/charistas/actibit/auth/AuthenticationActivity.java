package charistas.actibit.auth;
import org.scribe.builder.ServiceBuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import charistas.actibit.R;

@SuppressLint("SetJavaScriptEnabled")
public class AuthenticationActivity extends Activity {
    public final static String CONSUMER_KEY = "47481d0855aa4f02d5e238d5b2d33581";
    public  final static String CONSUMER_SECRET = "1ef1d9d56153d9aa6f7d1d94fa5bb786";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        LoginActivity.service = new ServiceBuilder().provider(FitbitApi.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();

        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                LoginActivity.requestToken = LoginActivity.service.getRequestToken();
                final String authURL = LoginActivity.service.getAuthorizationUrl(LoginActivity.requestToken);

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
                Intent intent = new Intent();
                intent.putExtra("PIN",pin);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}