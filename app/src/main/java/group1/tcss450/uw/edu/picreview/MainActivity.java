package group1.tcss450.uw.edu.picreview;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Credentials to SQL database. Use SQLyog or MYSQL workbench to login.
 * Username: demyan15
 * Password: UnunItHo
 */

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
                                            RegisterFragment.OnFragmentInteractionListener, UserAccessFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
        {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new UserAccessFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onFragmentInteraction(String toStart) {
        Log.d("Action", "Gets HERE: " + toStart);
        if (toStart.equals(getString(R.string.toStartLoginFragment)))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).addToBackStack(null).commit();
        }
        else if (toStart.equals(getString(R.string.toStartRegisterFragment)))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RegisterFragment()).addToBackStack(null).commit();
        }
    }






    // Transfer these over later to proper fragment
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class saveNewReviewService extends AsyncTask<String, Void, String> {
        private final String SERVICE = "http://cssgate.insttech.washington.edu/" +
                "~demyan15/" + "makeReview.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("Username", "UTF-8")
                        + "=" + URLEncoder.encode(strings[0], "UTF-8")
                        + "&" + URLEncoder.encode("tag", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("reviewText", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getContext(), "Result: " + result, Toast.LENGTH_LONG)
           //         .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class getUserReviewsService extends AsyncTask<String, Void, String> {
        private final String SERVICE = "http://cssgate.insttech.washington.edu/" +
                "~demyan15/" + "getUserReviews.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 1) {
                throw new IllegalArgumentException("One String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("Username", "UTF-8")
                        + "=" + URLEncoder.encode(strings[0], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getContext(), "Result: " + result, Toast.LENGTH_LONG)
           //         .show();
        }
    }

    // TODO:  Implement the button listener interfaces.
}
