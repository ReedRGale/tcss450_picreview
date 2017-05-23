/*
 * Group: 1
 * PicReview
 */

package group1.tcss450.uw.edu.picreview.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.login_register_service.UserAccessFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.LoginFragment;
import group1.tcss450.uw.edu.picreview.login_register_service.RegisterFragment;
import group1.tcss450.uw.edu.picreview.review_service.CaptionFragment;
import group1.tcss450.uw.edu.picreview.review_service.ConfirmPicFragment;
import group1.tcss450.uw.edu.picreview.review_service.LikeDislikeFragment;
import group1.tcss450.uw.edu.picreview.review_service.LocationPickerFragment;
import group1.tcss450.uw.edu.picreview.review_service.PicReviewConfirmFragment;
import group1.tcss450.uw.edu.picreview.search_service.SearchFragment;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;

/*
 * Main activity that runs the app.
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
                                        LocationPickerFragment.OnFragmentInteractionListener,
                                        PicReviewConfirmFragment.OnFragmentInteractionListener

{

    /** Request code passed to the PlacePicker intent for identification. */
    private static final int REQUEST_PLACE_PICKER = 1;

    /** Request code passed to camera for identification. */
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    ImageView mImageView = null;

    // Temporary information gleaned from the fragments.
    private Bitmap mTempBitmap = null;

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
        else if (toStart.equals("StartMapsActivity"))
        {
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
//                Toast.makeText(getActivity(), "Google Play Services is not available.",
//                        Toast.LENGTH_LONG)
//                        .show();
            }
//            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
//            startActivity(i);
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
            case USER_ACCESS:
                // TODO: Find more permanent location for this feature.
                UserAccessFragment userAccessFragment = new UserAccessFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, userAccessFragment)
                        .addToBackStack(null);
                break;
            case MAIN_MENU:
                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                transaction = getSupportFragmentManager().beginTransaction();

                // Clear the backstack because if they've been routed to main menu
                // then we probably don't want them going back
                while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStackImmediate();
                }

                transaction.replace(R.id.fragmentContainer, mainMenuFragment)
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
            case CONFIRM_REVIEW:
                PicReviewConfirmFragment picReviewConfirmFragment = new PicReviewConfirmFragment();
                transaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, picReviewConfirmFragment)
                        .addToBackStack(null);
                break;
            case DATA_TEST:
                break;

        }

        // Change the fragment, assuming we reached a valid case.
        if (transaction != null) { transaction.commit(); }
    }

    @Override
    public void onDataStorage(Frags source, Object data)
    {
        // Prime the transaction to the proper case.
        switch (source) {
            case CONFIRM_PIC:
                // Store the bitmap.
                BitmapDrawable temp = (BitmapDrawable) mImageView.getDrawable();
                mTempBitmap = temp.getBitmap();

                // Return a Toast or something to prove it worked.
                Toast.makeText(this,
                        "Temp Bitmap Hash" + mTempBitmap.hashCode(),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

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
                }
                else
                {
                    // TODO: Create a failure dialog box.
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        switch (requestCode)
        {
            case REQUEST_PLACE_PICKER:
                if (resultCode == RESULT_OK)
                {
                    Place place = PlacePicker.getPlace(getApplicationContext(), data);

                    String address = place.getAddress().toString();
                    String location = place.getLatLng().toString();

                    String toastMsg = String.format("Name: %s Address: %s Location: %s", place.getName(), address, location);
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK)
                {
                    // Retrieve image from activity call.
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Change image on confirmPic to the taken picture.
                    if (mImageView == null)
                    {
                        mImageView = (ImageView) findViewById(R.id.testImageView);
                        mImageView.setImageBitmap(imageBitmap);
                    }
                }
                break;
        }
    }


}
