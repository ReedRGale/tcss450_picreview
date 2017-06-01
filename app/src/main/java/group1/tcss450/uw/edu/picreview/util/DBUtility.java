package group1.tcss450.uw.edu.picreview.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods with which to access the database.
 * NOTE: These methods should be called asynchronously
 */
public class DBUtility {

    /** The url to the webservice which will interact with the DB to save the review. */
    private final static String SERVICE = "http://cssgate.insttech.washington.edu/~demyan15/";

    /** Will take a review passed in as an argument and will save it to the database. */
    public static boolean saveReview(Review theReview)
    {
        boolean success = true;
        JSONObject jsonReview = new JSONObject();
        try {
            // Serialize everything that needs to be serialized
            String SerializedComments = (theReview.getComments() != null) ? serializeStringList(theReview.getComments()) : null;
            String SerializedTags = (theReview.getTag() != null) ? serializeStringList(theReview.getTag()) : null;
            String SerializedImage = (theReview.getImage() != null) ? serializeBitmap(theReview.getImage()) : null;
            String SerializedLocation = (theReview.getLocation() != null) ? serializeLocation(theReview.getLocation()) : null;


            // Check whether information is valid
            if (theReview.getCaption() == null || SerializedComments == null || SerializedTags == null || SerializedImage == null)
            {
                success = false;
            }

            // Check whether likes/dislikes are at 0, like they should be for a new review, and whether the review
            // has been denoted as a positive or negative one.
            if (theReview.getLikes() != 0 || theReview.getDislikes() != 0 || theReview.getReviewType() == 0)
            {
                success = false;
            }

            // If information is valid, load review into JSONObject
            if (success)
            {
                jsonReview.put("Caption", theReview.getCaption());
                jsonReview.put("Comments", SerializedComments);
                jsonReview.put("Tags", SerializedTags);
                jsonReview.put("Image", SerializedImage);
                if (SerializedLocation != null)
                {
                    jsonReview.put("Location", SerializedLocation);
                }
                else
                {
                    jsonReview.put("Location", "none");
                }
                jsonReview.put("Likes", theReview.getLikes());
                jsonReview.put("Dislikes", theReview.getDislikes());
                jsonReview.put("ReviewType", theReview.getReviewType());
            }
            else
            {
                Log.d("Error", "Review information is invalid");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            success = false;
        }

        if (success)
        {
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
                        "=" + URLEncoder.encode(Globals.CURRENT_USERNAME, "UTF-8") +
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

                JSONObject jsonResponse = new JSONObject(response);
                if (!jsonResponse.has("returnValue") || (jsonResponse.has("returnValue") && jsonResponse.getInt("returnValue") != 1)) success = false;
                Log.d("Error", jsonResponse.toString());
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("Error", "Error saving review. " + response);
                success = false;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }


        return success;
    }

    /** Will get all reviews belonging to the given user. */
    public static List<Review> getReviewsByUsername(String theUsername)
    {
        boolean ConnectionSuccess = true;
        List<Review> reviews = new ArrayList<>();

        // Get reviews in JSON form
        String response = "";
        HttpURLConnection urlConnection = null;
        try {
            URL urlObject = new URL(SERVICE + "getUserReviews.php");
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            String data = URLEncoder.encode("Username", "UTF-8") +
                    "=" + URLEncoder.encode(theUsername, "UTF-8");
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
            Log.d("Error", "Error Getting review by username. " + response);
            ConnectionSuccess = false;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        if (ConnectionSuccess)
        {
            try {
                // Convert response to JSON Object
                JSONObject returnedObject = new JSONObject(response);
                if (returnedObject.has("returnValue") && returnedObject.getInt("returnValue") == 1 && returnedObject.has("arrayToReturn")) {
                    JSONArray JSONreviews = returnedObject.getJSONArray("arrayToReturn");

                    // Go through reviews and create review objects
                    for (int i = 0; i < JSONreviews.length(); i++)
                    {
                        boolean CreationSuccess = true;
                        JSONObject JsonReviewObject = (JSONObject) JSONreviews.get(i);
                        if (!JsonReviewObject.has("caption") || !JsonReviewObject.has("comments") || !JsonReviewObject.has("tags")
                                || !JsonReviewObject.has("username") || !JsonReviewObject.has("image") || !JsonReviewObject.has("location")
                                || !JsonReviewObject.has("likes") || !JsonReviewObject.has("dislikes") || !JsonReviewObject.has("ReviewType")) {
                            CreationSuccess = false;
                            Log.d("Error", "JSONObject did not contain a required field");
                        }
                        else
                        {
                            List<String> deserializedTagList = (List<String>) deserializeString(JsonReviewObject.getString("tags"));
                            List<String> deserializedCommentList = (List<String>) deserializeString(JsonReviewObject.getString("comments"));
                            Bitmap deserializedImage = deserializeBitmap(JsonReviewObject.getString("image"));

                            Location deserializedLocation = (!JsonReviewObject.getString("location").equals("null")) ? deserializeLocation(JsonReviewObject.getString("location")) : null;

                            if (deserializedImage == null) CreationSuccess = false;

                            if (CreationSuccess)
                            {
                                Review review = new Review(JsonReviewObject.getString("caption"), deserializedImage, deserializedLocation, JsonReviewObject.getInt("likes"),
                                        JsonReviewObject.getInt("dislikes"), JsonReviewObject.getInt("ReviewType"));
                                review.setComments(deserializedCommentList);
                                review.setTags(deserializedTagList);
                                review.setUser(JsonReviewObject.getString("username"));
                                review.setMyId(Integer.parseInt(JsonReviewObject.getString("id")));
                                review.setLocation(deserializedLocation);
                                if (deserializedLocation != null)
                                {
                                    review.setmAddress(getAddress(deserializedLocation));
                                }
                                else {
                                    review.setmAddress("No Location Given");
                                }

                                reviews.add(review);
                            }
                            else
                            {
                                Log.d("Error", "Unable to build Review");

                            }
                        }
                    }

                }

            } catch (JSONException e) {
                Log.d("Error", "Error thrown while building review");
                e.printStackTrace();
            }
        }

        return reviews;
    }

    /* Will get all reviews containing this tag. */
    public static List<Review> getReviewsByTag(String theTag)
    {
        List<Review> reviews = new ArrayList<>();

        String response = getAllJSonReviews();
        if (response != null)
        {
            try {
                // Convert response to JSON Object
                JSONObject returnedObject = new JSONObject(response);
                if (returnedObject.has("returnValue") && returnedObject.getInt("returnValue") == 1 && returnedObject.has("arrayToReturn")) {
                    JSONArray JSONreviews = returnedObject.getJSONArray("arrayToReturn");

                    // Go through reviews and create review objects
                    for (int i = 0; i < JSONreviews.length(); i++)
                    {
                        boolean CreationSuccess = true;
                        JSONObject JsonReviewObject = (JSONObject) JSONreviews.get(i);
                        if (!JsonReviewObject.has("caption") || !JsonReviewObject.has("comments") || !JsonReviewObject.has("tags")
                                || !JsonReviewObject.has("username") || !JsonReviewObject.has("image") || !JsonReviewObject.has("location")
                                || !JsonReviewObject.has("likes") || !JsonReviewObject.has("dislikes") || !JsonReviewObject.has("ReviewType")) {
                            CreationSuccess = false;
                            Log.d("Error", "JSONObject did not contain a required field");
                        }
                        else
                        {
                            // First, check whether tags match so that we don't do any extra work if we don't need this review
                            boolean meetsCriteria = false;
                            List<String> deserializedTagList = (List<String>) deserializeString(JsonReviewObject.getString("tags"));
                            Log.d("SIZE: ", "" + deserializedTagList.size());
                            for (int j = 0; j < deserializedTagList.size(); j++)
                            {
                                if (deserializedTagList.get(j).equals(theTag))
                                {
                                    meetsCriteria = true;
                                    break;
                                }
                            }

                            if (meetsCriteria)
                            {
                                List<String> deserializedCommentList = (List<String>) deserializeString(JsonReviewObject.getString("comments"));
                                Bitmap deserializedImage = deserializeBitmap(JsonReviewObject.getString("image"));
                                Location deserializedLocation = (!JsonReviewObject.getString("location").equals("none")) ? deserializeLocation(JsonReviewObject.getString("location")) : null;

                                if (deserializedImage == null) CreationSuccess = false;

                                if (CreationSuccess)
                                {
                                    Review review = new Review(JsonReviewObject.getString("caption"), deserializedImage, deserializedLocation, JsonReviewObject.getInt("likes"),
                                            JsonReviewObject.getInt("dislikes"), JsonReviewObject.getInt("ReviewType"));
                                    review.setComments(deserializedCommentList);
                                    review.setTags(deserializedTagList);
                                    review.setUser(JsonReviewObject.getString("username"));
                                    review.setMyId(Integer.parseInt(JsonReviewObject.getString("id")));
                                    review.setLocation(deserializedLocation);
                                    if (deserializedLocation != null)
                                    {
                                        review.setmAddress(getAddress(deserializedLocation));
                                    }
                                    else {
                                        review.setmAddress("No Location Given");
                                    }
                                    reviews.add(review);
                                }
                                else
                                {
                                    Log.d("Error", "Unable to build Review");
                                }
                            }

                        }
                    }

                }

            } catch (JSONException e) {
                Log.d("Error", "Error thrown while building review");
                e.printStackTrace();
            }
        }


        return reviews;
    }

    /** Will get all reviews for this location. */
    public static List<Review> getReviewsByLocation(Location theLocation)
    {
        List<Review> reviews = new ArrayList<>();

        String response = getAllJSonReviews();
        if (response != null)
        {
            try {
                // Convert response to JSON Object
                JSONObject returnedObject = new JSONObject(response);
                if (returnedObject.has("returnValue") && returnedObject.getInt("returnValue") == 1 && returnedObject.has("arrayToReturn")) {
                    JSONArray JSONreviews = returnedObject.getJSONArray("arrayToReturn");

                    // Go through reviews and create review objects
                    for (int i = 0; i < JSONreviews.length(); i++)
                    {
                        boolean CreationSuccess = true;
                        JSONObject JsonReviewObject = (JSONObject) JSONreviews.get(i);
                        if (!JsonReviewObject.has("caption") || !JsonReviewObject.has("comments") || !JsonReviewObject.has("tags")
                                || !JsonReviewObject.has("username") || !JsonReviewObject.has("image") || !JsonReviewObject.has("location")
                                || !JsonReviewObject.has("likes") || !JsonReviewObject.has("dislikes") || !JsonReviewObject.has("ReviewType")) {
                            CreationSuccess = false;
                            Log.d("Error", "JSONObject did not contain a required field");
                        }
                        else
                        {
                            // First, check whether tags match so that we don't do any extra work if we don't need this review
                            boolean meetsCriteria = false;
                            Location deserializedLocation = (!JsonReviewObject.getString("location").equals("none")) ? deserializeLocation(JsonReviewObject.getString("location")) : null;
                            if (deserializedLocation != null && deserializedLocation.getLongitude() == theLocation.getLongitude() && deserializedLocation.getLatitude() == theLocation.getLatitude())
                            {
                                meetsCriteria = true;
                            }


                            if (meetsCriteria)
                            {
                                List<String> deserializedTagList = (List<String>) deserializeString(JsonReviewObject.getString("tags"));
                                List<String> deserializedCommentList = (List<String>) deserializeString(JsonReviewObject.getString("comments"));
                                Bitmap deserializedImage = deserializeBitmap(JsonReviewObject.getString("image"));

                                if (deserializedImage == null) CreationSuccess = false;

                                if (CreationSuccess)
                                {
                                    Review review = new Review(JsonReviewObject.getString("caption"), deserializedImage, deserializedLocation, JsonReviewObject.getInt("likes"),
                                            JsonReviewObject.getInt("dislikes"), JsonReviewObject.getInt("ReviewType"));
                                    review.setComments(deserializedCommentList);
                                    review.setTags(deserializedTagList);
                                    review.setUser(JsonReviewObject.getString("username"));
                                    review.setLikes(JsonReviewObject.getInt("likes"));
                                    review.setDislikes(JsonReviewObject.getInt("dislikes"));
                                    review.setReviewType(JsonReviewObject.getInt("ReviewType"));
                                    review.setMyId(Integer.parseInt(JsonReviewObject.getString("id")));
                                    review.setLocation(deserializedLocation);
                                    if (deserializedLocation != null)
                                    {
                                        review.setmAddress(getAddress(deserializedLocation));
                                    }
                                    else {
                                        review.setmAddress("No Location Given");
                                    }
                                    reviews.add(review);
                                }
                                else
                                {
                                    Log.d("Error", "Unable to build Review");
                                }
                            }

                        }
                    }

                }

            } catch (JSONException e) {
                Log.d("Error", "Error thrown while building review");
                e.printStackTrace();
            }
        }


        return reviews;


    }

    /** Will update a field in the review in DB. */
    public static boolean updateReview(int field, Review theReview)
    {
        boolean success = true;
        JSONObject jsonReview = new JSONObject();

        String SerializedComments = (theReview.getComments() != null) ? serializeStringList(theReview.getComments()) : null;
        try {
            jsonReview.put("Comments", SerializedComments);
            jsonReview.put("Likes", theReview.getLikes());
            jsonReview.put("Dislikes", theReview.getDislikes());
            jsonReview.put("id", theReview.getMyId());

            String theField = null;
            if (field == Globals.LIKE_FIELD)
            {
                theField = "likes";
            }
            else if (field == Globals.DISLIKE_FIELD)
            {
                theField = "dislikes";
            }
            else if (field == Globals.COMMENTS_FIELD)
            {
                theField = "comments";
            }

            if (theField != null)
            {
                jsonReview.put("update", theField);
            }
            else
            {
                success = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error", "Crashed when created JSON for updating");
            success = false;
        }

        if (success)
        {
            Log.d("Error", "JSON OBJECT CREATED: " +jsonReview.toString());
            // Send the JSON to our webservice
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(SERVICE + "updateReview.php");
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("jsonReview", "UTF-8") +
                        "=" + URLEncoder.encode(jsonReview.toString(), "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

                JSONObject jsonResponse = new JSONObject(response);
                if (!jsonResponse.has("returnValue") || (jsonResponse.has("returnValue") && jsonResponse.getInt("returnValue") != 1)) success = false;
                Log.d("Error", jsonResponse.toString());
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("Error", "Error saving review. " + response);
                success = false;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }
        else
        {
            Log.d("Error", "Unable to update review");
        }

        return success;
    }

    /**
    Will get all reviews from the webservice and return the JSon it receives.
     */
    private static String getAllJSonReviews()
    {
        // Get reviews in JSON form
        String response = "";
        HttpURLConnection urlConnection = null;
        try {
            URL urlObject = new URL(SERVICE + "getAllReviews.php");
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.flush();
            InputStream content = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
        } catch (Exception e) {
            response = null;
            Log.d("Error", "Error Getting all reviews: " + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    // Serialization Methods

    /** Will serialize a list of strings. */
    private static String serializeStringList(Object o)
    {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(o);
            so.flush();
            return new String(Base64.encode(bo.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            Log.d("Exception", "Thrown");
            e.printStackTrace();
        }
        return null;
    }

    /** Will deserialize a string. */
    private static Object deserializeString(String theString)
    {
        try {
            byte b[] = Base64.decode(theString.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Will serialize a BitMap. */
    private static String serializeBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * Will deserialize a Bitmap object.
     * @param encodedString The serialized Bitmap
     * @return bitmap (from given string)
     */
    private static Bitmap deserializeBitmap(String encodedString)
    {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.d("Image", "Height" + bitmap.getHeight() + " Width: " + bitmap.getWidth());
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
        }
        return null;
    }

    /** Will serialize a Location object. */
    private static String serializeLocation(Location location)
    {
        Double[] latLong = new Double[2];
        latLong[0] = location.getLatitude();
        latLong[1] = location.getLongitude();

        return serializeStringList(latLong);
    }

    /** Will deserialize a serialized location into a Location object. */
    private static Location deserializeLocation(String theLocation)
    {
        Double[] latLong = (Double[]) deserializeString(theLocation);
        Location l = new Location("");
        l.setLatitude(latLong[0]);
        l.setLongitude(latLong[1]);
        return l;
    }

    /** Will return the address and name of the location given. */
    private static String getAddress(Location theLocation)
    {
        boolean success = true;
        StringBuilder address = new StringBuilder();
        if (theLocation != null)
        {
            String SERVICE = "http://maps.googleapis.com/maps/api/geocode/json?";
                // Send the JSON to our webservice
                String response = "";
                HttpURLConnection urlConnection = null;
                try {
                    String data = URLEncoder.encode("latlng", "UTF-8") +
                            "=" + URLEncoder.encode("" + theLocation.getLatitude(), "UTF-8") +
                            "," + URLEncoder.encode("" + theLocation.getLongitude(), "UTF-8") +
                            "&" + URLEncoder.encode("sensor", "UTF-8") +
                            "=" + URLEncoder.encode("true", "UTF-8");
                    URL urlObject = new URL(SERVICE + data);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.flush();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                    Log.d("LINK", SERVICE + data);

                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray locations = jsonResponse.getJSONArray("results");
                    if (locations.length() > 1)
                    {
                        JSONObject location = locations.getJSONObject(0);
                        address.append(location.get("formatted_address").toString());
                    }
                    else
                    {
                        success = false;
                    }
                } catch (Exception e) {
                    response = "Unable to connect, Reason: "
                            + e.getMessage();
                    Log.d("Error", "Error getting locations. " + response);
                    success = false;
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }

            }
        else
        {
            success = false;
        }

        if (!success) address.append("No location given");

        return address.toString();

    }
}
