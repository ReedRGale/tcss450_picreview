/*
 * Group: 1
 * PicReview
 */

package group1.tcss450.uw.edu.picreview.main;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.login_register_service.UserAccessFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.LoginFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.RegisterFragment;
import group1.tcss450.uw.edu.picreview.review_service.CaptionFragment;
import group1.tcss450.uw.edu.picreview.review_service.LikeDislikeFragment;
import group1.tcss450.uw.edu.picreview.review_service.LocationPickerFragment;
import group1.tcss450.uw.edu.picreview.review_service.PicReviewConfirmFragment;
import group1.tcss450.uw.edu.picreview.review_service.TagFragment;
import group1.tcss450.uw.edu.picreview.search_service.MyReviewsFragment;
import group1.tcss450.uw.edu.picreview.search_service.QueryFragment;
import group1.tcss450.uw.edu.picreview.search_service.SearchFragment;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;
import group1.tcss450.uw.edu.picreview.util.Globals;
import group1.tcss450.uw.edu.picreview.util.Review;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;
import static group1.tcss450.uw.edu.picreview.util.Frags.LOCATION;

/**
 * Main activity used when running the app.
 */
public class MainActivity   extends     AppCompatActivity

                            implements  LoginFragment.OnFragmentInteractionListener,
                                        MyReviewsFragment.OnFragmentInteractionListener,
                                        RegisterFragment.OnFragmentInteractionListener,
                                        UserAccessFragment.OnFragmentInteractionListener,
                                        MainMenuFragment.OnFragmentInteractionListener,
                                        SearchFragment.OnFragmentInteractionListener,
                                        CaptionFragment.OnFragmentInteractionListener,
                                        TagFragment.OnFragmentInteractionListener,
                                        LikeDislikeFragment.OnFragmentInteractionListener,
                                        LocationPickerFragment.OnFragmentInteractionListener,
                                        PicReviewConfirmFragment.OnFragmentInteractionListener,
                                        QueryFragment.OnFragmentInteractionListener

{
    // Constants.

    /** Request code passed to the PlacePicker intent for identification. */
    private static final int REQUEST_PLACE_PICKER = 1;

    /** Request code passed to camera for identification. */
    private static final int REQUEST_IMAGE_CAPTURE = 2;


    // Prefs saved between instances.

    private SharedPreferences mPrefs;


    // Intermediary information gleaned from other activities.

    /** ImageView used when creating a new Review */
    private ImageView mImageView = null;

    /** Place used when creating a new Review */
    private Place mPlace = null;

    /** The review just before storage */
    private Review mTempReview = null;


    // Temporary information gleaned from the fragments.

    /** Bitmap used when creating a new Review */
    private Bitmap mTempBitmap = null;

    /** Caption used when creating a new Review */
    private String mTempCaption = null;

    /** Tags used when creating a new Review */
    private String[] mTempTags = null;

    /**  Value to determine whether the user likes or dislikes the place reviewed.
        positive:   Good
        zero:       Unchanged--probably an error
        negative:   Bad                             */
    private int mTempLD = 0;

    /** Location used when creating a new Review */
    private Location mTempLocation = null;

    /** Location used when creating a new Review */
    private String[] mQuery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences(getString(R.string.PREF_SESSION), Context.MODE_PRIVATE);
        Globals.CURRENT_USERNAME = mPrefs.getString(getString(R.string.PREF_USERNAME), "");

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

    /**
     * A method to generally handle moving from one fragment to another.
     * @param target the target fragment to swap to.
     */
    @Override
    public void onFragmentTransition(Frags target)
    {
        FragmentTransaction transaction = null;

        // Prime the transaction to the proper case.
        switch (target)
        {
            case SEARCH:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                // Clear all the way back to the MAIN_MENU fragment.
                // If this is called from the error dialog, then we don't want that error
                // on the stack. It creates an infinite loop.
                while (getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                transaction.replace(R.id.fragmentContainer, new SearchFragment())
                        .addToBackStack(null);
                break;
            case USER_ACCESS:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragmentContainer, new UserAccessFragment())
                        .addToBackStack(null);
                break;
            case MAIN_MENU:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in);
                // Clear the backstack because if they've been routed to main menu
                // then we probably don't want them going back
                while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStackImmediate();
                }

                transaction.replace(R.id.fragmentContainer, new MainMenuFragment())
                        .addToBackStack(null);
                break;
            case CAPTION:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new CaptionFragment())
                        .addToBackStack(null);
                break;
            case TAG:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new TagFragment())
                        .addToBackStack(null);
                break;
            case LIKE_DISLIKE:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new LikeDislikeFragment())
                        .addToBackStack(null);
                break;
            case LOCATION:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new LocationPickerFragment())
                        .addToBackStack(null);
                break;
            case CONFIRM_REVIEW:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new PicReviewConfirmFragment())
                        .addToBackStack(null);
                break;
            case LOGIN:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new LoginFragment())
                        .addToBackStack(null);
                break;
            case REGISTER:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new RegisterFragment())
                        .addToBackStack(null);
                break;
            case MY_REVIEWS:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new MyReviewsFragment())
                        .addToBackStack(null);
                break;
            case QUERY:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragmentContainer, new QueryFragment())
                        .addToBackStack(null);
                break;
        }

        // Change the fragment, assuming we reached a valid case.
        if (transaction != null) { transaction.commit(); }
    }

    /**
     * Method used to give data from the activity to a Fragment
     * @param target is the fragment to send the data.
     * @return is data to give to the fragment.
     */
    @Override
    public Object onDataRetrieval(Functions target)
    {
        Object theData = null;

        // Prime the transaction to the proper case.
        switch (target) {
            case REVIEW_RETRIEVE:
                // Prime the review.
                mTempReview = new Review();
                mTempReview.setCaption(mTempCaption);
                mTempReview.setImage(mTempBitmap);
                mTempReview.setLocation(mTempLocation);
                mTempReview.setReviewType(mTempLD);

                Log.d(this.toString(), mTempReview.getLocation().toString());

                if (mTempTags != null)
                {
                    mTempReview.setTags(Arrays.asList(mTempTags));
                }
                else
                {
                    mTempReview.setTags(new ArrayList<String>());
                }

                // Defaults.
                mTempReview.setLikes(0);
                mTempReview.setDislikes(0);
                mTempReview.setComments(new ArrayList<String>());

                theData = mTempReview;
                mImageView = null;

                break;
            case QUERY_RETRIEVE:
                // Return the query.
                theData = mQuery;
                break;
        }

        return theData;
    }

    /**
     * A fragment to handle temporary data storage.
     * @param source this is the source fragment. Tells us what data is being sent in.
     * @param data is the data to store.
     */
    @Override
    public void onDataStorage(Frags source, Object data)
    {
        // Prime the transaction to the proper case.
        switch (source) {
            case CAPTION:
                // Store the caption.
                EditText tempET = (EditText) data;
                mTempCaption = tempET.getText().toString();
                break;
            case TAG:
                // Parse the string passed in for tags.
                mTempTags = parseHashTags((String) data);
                break;
            case LIKE_DISLIKE:
                // Store the positive/negative data.
                mTempLD = (int) data;
                break;
            case LOCATION:
                // Milk the information.
                Location tempLoc = new Location("");
                tempLoc.setLatitude(mPlace.getLatLng().latitude);
                tempLoc.setLongitude(mPlace.getLatLng().longitude);
                mTempLocation = tempLoc;
                break;
            case SEARCH:
                // Store the positive/negative data.
                mQuery = parseHashTags((String) data);
                Log.d("The Parsed String", mQuery[0]);
                break;
            case MAIN_MENU:
            case LOGIN:
            case REGISTER:
                saveToSharedPrefs((String) data);
                break;
        }
    }

    /**
     * This is a general method to call functionality in other,
     * external, sources.
     * @param target the target functionality to retrieve.
     */
    @Override
    public void onFunctionCall(Functions target)
    {
        // Prime the transaction to the proper case.
        switch (target) {
            case TAKE_PICTURE:
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    {
                        startActivityForResult( takePictureIntent,
                                REQUEST_IMAGE_CAPTURE);
                    }

                    onFragmentTransition(CAPTION);
                }
                else
                {
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder .setMessage("Failed to take picture...")
                            .setPositiveButton( "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) { } })
                            .show();
                }
                break;
            case PLACE_PICKER:
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    //showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
//                GooglePlayServicesUtil
//                        .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case UPDATE_PREVIEW:
                attachToImageView();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        // Check which request we're responding to
        switch (requestCode)
        {
            case REQUEST_PLACE_PICKER:
                if (resultCode == RESULT_OK)
                {
                    mPlace = PlacePicker.getPlace(getApplicationContext(), data);
                    onDataStorage(LOCATION, null);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK && data != null)
                {
                    // Retrieve image from activity call.
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Change image on confirmPic to the taken picture.
                    if (mImageView == null) { mTempBitmap = imageBitmap; }
                }
                break;
        }
    }

    /* Helper Methods */

    /**
     * Helper method to turn a string of hashes into a string array.
     * @param theParsableString is the string peppered with hash tags.
     * @return is the parsed string.
     */
    private static String[] parseHashTags(String theParsableString)
    {
		/* **** Split initial string. **** */

        Log.d("ParseHash", "in Function...");

		if (theParsableString.contains("#"))
        {
            String[] hashArray = theParsableString.split("#");
            int cull = 0;
            for (int i = 0; i < hashArray.length; i++)
            {
                hashArray[i] = hashArray[i].trim();
                if (hashArray[i].equals("")) { cull++; }
            }

            Log.d("ParseHash", "Cull: " + cull);

            /* **** Split initial string. **** */

            /* **** Cull off random hashes. **** */

            String[] culledArray = new String[hashArray.length - cull];
            int j = 0;
            for (int i = 0; i < hashArray.length && j < culledArray.length; i++)
            {
                if (!hashArray[i].equals(""))
                {
                    culledArray[j] = hashArray[i];
                    j++;
                }
            }
            /* **** Cull off random hashes. **** */

            return culledArray;
        }
        else
        {
            // Just return theParsableString as the tag.
            return new String[] { theParsableString };
        }
    }

    /** Store the user to prefs so they can still be logged in. */
    private void saveToSharedPrefs(String username)
    {
        mPrefs.edit().putString(getString(R.string.PREF_USERNAME), username).apply();
    }

    /** Attaches the bitmap to the imageView. */
    private void attachToImageView()
    {
        mImageView = (ImageView) findViewById(R.id.ivPreview);
        mImageView.setImageBitmap(mTempBitmap);
    }
}
