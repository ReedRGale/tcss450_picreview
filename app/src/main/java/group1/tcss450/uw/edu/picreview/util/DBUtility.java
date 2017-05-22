package group1.tcss450.uw.edu.picreview.util;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.review_service.DisplayReviewsFragment;

/**
 * Provides methods with which to access the database.
 * NOTE: These methods should be called asynchronously
 */
public class DBUtility {

    private final static String SERVICE = "http://cssgate.insttech.washington.edu/~demyan15/";

    /* Will take a review passed in as an argument and will save it to the database. */
    public static boolean saveReview(Review theReview)
    {

        JSONObject jsonReview = new JSONObject();
        try {

            // Since caption is optional, check whether there is one for this review
            if (theReview.getCaption() != null)
            {
                jsonReview.put("Caption", theReview.getCaption());
            }
            else
            {
                jsonReview.put("Caption", "null");
            }

            // There won't be any comments since the review does not exist yet
            jsonReview.put("Comments", "null");
            jsonReview.put("Tag", theReview.getTag());
            jsonReview.put("Likes", theReview.getLikes());
            jsonReview.put("Dislikes", theReview.getDislikes());

            // Serialize bitMap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            byte[] imageAsBytes = null;
            oos = new ObjectOutputStream(baos);
            oos.writeObject(theReview.getImage());
            imageAsBytes = baos.toByteArray();
            String stringImage = new String(imageAsBytes);
            jsonReview.put("Image", stringImage);

            // Serialize Location
            String stringLocation;
            if (theReview.getLocation() != null)
            {
                Parcel p = Parcel.obtain();
                theReview.getLocation().writeToParcel(p, 0);
                final byte[] b = p.marshall();
                p.recycle();
                stringLocation = new String(b);
            }
            else
            {
                stringLocation = "null";
            }
            jsonReview.put("Location", stringLocation);

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        // Send the JSON to our webservice
        String response = "";
        HttpURLConnection urlConnection = null;
        try {
            URL urlObject = new URL(SERVICE + "makeReview.php");
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            String data = URLEncoder.encode("Username", "UTF-8") +
                    "=" + URLEncoder.encode("John", "UTF-8") +
                    "&" + URLEncoder.encode("jsonReview", "UTF-8") +
                    "=" + URLEncoder.encode(jsonReview.toString(), "UTF-8");
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
            return false;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }


        return true;
    }

    /* Will get all reviews belonging to the given user. */
    public static List<Review> getReviewsByUsername(String theUsername)
    {

        return null;
    }

    /* Will get all reviews containing a part of this tag. */
    public static List<Review> getReviewsByTag(String theTag)
    {
        return null;
    }

    /* Will get all reviews for this location. */
    public static List<Review> getReviewsByLocation(Location theLocation) { return null; }

}
