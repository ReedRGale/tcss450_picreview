package group1.tcss450.uw.edu.picreview.main;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.login_register_service.UserAccessFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.LoginFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.RegisterFragment;
import group1.tcss450.uw.edu.picreview.review_service.CaptionFragment;
import group1.tcss450.uw.edu.picreview.review_service.ConfirmPicFragment;
import group1.tcss450.uw.edu.picreview.review_service.LikeDislikeFragment;
import group1.tcss450.uw.edu.picreview.review_service.LocationPickerFragment;
import group1.tcss450.uw.edu.picreview.search_service.SearchFragment;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;

/**
 * Credentials to SQL database. Use SQLyog or MYSQL workbench to login.
 * Username: demyan15
 * Password: UnunItHo
 */

public class MainActivity   extends     AppCompatActivity

                            implements  LoginFragment.OnFragmentInteractionListener,
                                        RegisterFragment.OnFragmentInteractionListener,
                                        UserAccessFragment.OnFragmentInteractionListener,
                                        MainMenuFragment.OnFragmentInteractionListener,
                                        SearchFragment.OnFragmentInteractionListener,
                                        ConfirmPicFragment.OnFragmentInteractionListener,
                                        CaptionFragment.OnFragmentInteractionListener,
                                        LikeDislikeFragment.OnFragmentInteractionListener,
                                        LocationPickerFragment.OnFragmentInteractionListener

{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Suppressing this because it was causing errors
//        ~  Jared Lowery
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
        {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new MainMenuFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onFragmentInteraction(String toStart) {
        Log.d("Action", "Gets HERE: " + toStart);
        if (toStart.equals(getString(R.string.toStartLoginFragment)))
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null).commit();
        }
        else if (toStart.equals(getString(R.string.toStartRegisterFragment)))
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null).commit();
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

    /**
     * {@inheritDoc}
     */
    public void onFragmentTransition(Frags target)
    {
        FragmentTransaction transaction = null;

        // Prime the transaction to the proper case.
        switch (target) {
            case SEARCH:
                SearchFragment searchFragment = new SearchFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, searchFragment)
                        .addToBackStack(null);
                break;
            case REVIEW:
                // TODO: Change this such that it leads to the camera.
                ConfirmPicFragment pictureFragment = new ConfirmPicFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, pictureFragment)
                        .addToBackStack(null);
                break;
            case USER_ACCESS:
                // TODO: Find more permanent location for this feature.
                UserAccessFragment userAccessFragment = new UserAccessFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, userAccessFragment)
                        .addToBackStack(null);
                break;
            case MAIN_MENU:
                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, mainMenuFragment)
                        .addToBackStack(null);
                break;
            case CONFIRM_PIC:
                ConfirmPicFragment confirmPicFragment = new ConfirmPicFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, confirmPicFragment)
                        .addToBackStack(null);
                break;
            case CAPTION:
                CaptionFragment captionFragment = new CaptionFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, captionFragment)
                        .addToBackStack(null);
                break;
            case LIKE_DISLIKE:
                LikeDislikeFragment likeDislikeFragment = new LikeDislikeFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, likeDislikeFragment)
                        .addToBackStack(null);
                break;
            case LOCATION:
                LocationPickerFragment locationPickerFragment = new LocationPickerFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, locationPickerFragment)
                        .addToBackStack(null);
                break;
            case DATA_TEST:
                // TODO: Add Dema's Test.
                break;
        }

        // Change the fragment, assuming we reached a valid case.
        if (transaction != null) { transaction.commit(); }
    }

    @Override
    public void onFunctionCall(Functions target) 
    {
        // TODO: Implement functionality later.
    }
}
