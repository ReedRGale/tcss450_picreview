package group1.tcss450.uw.edu.picreview.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;
import group1.tcss450.uw.edu.picreview.util.Globals;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;
import static group1.tcss450.uw.edu.picreview.util.Functions.*;


/**
 * Sets up the main menu.
 * Currently, there are two things that the main menu must handle:
 * Passing the user to the SearchFragment or to the ReviewFragment.
 */
public class MainMenuFragment   extends     Fragment
                                implements  View.OnClickListener
{
    /** This is the activity that swaps this fragment in and out. */
    private OnFragmentInteractionListener mListener;


    // Buttons that we need to change visibility of.

    /** Review button linked to its respective functionality. */
    private Button mReview;

    /** MyReviews button linked to its respective functionality. */
    private Button mMyReviews;

    /** Login button linked to its respective functionality. */
    private Button mLogin;

    /** Logout button linked to its respective functionality. */
    private Button mLogout;


    /** Required empty public constructor */
    public MainMenuFragment() {}

    /** Inflates the view, gets references to some views, and adds listeners to buttons. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Save all buttons whose states can change.
        mReview = (Button) v.findViewById(R.id.bReview);
        mMyReviews = (Button) v.findViewById(R.id.my_reviews_button);
        mLogin = (Button) v.findViewById(R.id.bTempUserAccess);
        mLogout = (Button) v.findViewById(R.id.bLogout);

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bSearch));
        ba.add((Button) v.findViewById(R.id.bReview));
        ba.add((Button) v.findViewById(R.id.bTempUserAccess));
        ba.add((Button) v.findViewById(R.id.my_reviews_button));
        ba.add((Button) v.findViewById(R.id.bLogout));

        // If user is logged in, then they can view their own reviews
        if (Globals.CURRENT_USERNAME.length() != 0) enableReview();
        else enableLogin();

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /** Will detect what button was clicked and invokes corresponding method. */
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.bSearch:
                onSearchPressed();
                break;
            case R.id.bReview:
                onReviewPressed();
                break;
            case R.id.bTempUserAccess:
                onUserAccessPressed();
                break;
            case R.id.my_reviews_button:
                onViewMyReviewsPressed();
                break;
            case R.id.bLogout:
                onLogoutPressed();
                break;
        }
    }

    /**
     * Method that implements functionality of the search button.
     * Specifically, it takes us to another fragment where we can search for reviews.
     */
    private void onSearchPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(SEARCH); }
    }

    /**
     * Method that implements functionality of the review button.
     * Specifically, it takes us to another fragment where we can review items.
     */
    private void onReviewPressed()
    {
        if (mListener != null)
        {
            mListener.onFunctionCall(TAKE_PICTURE);
        }
    }

    /**
     * Method that launches functionality for registering/login.
     */
    private void onUserAccessPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(USER_ACCESS); }
    }

    /**
     * Method that implements functionality of the logout button.
     */
    private void onLogoutPressed()
    {
        if (mListener != null)
        {
            Globals.CURRENT_USERNAME = "";
            mListener.onDataStorage(MAIN_MENU, Globals.CURRENT_USERNAME);
            enableLogin();
            mListener.onFragmentTransition(MAIN_MENU);
        }
    }

    /** Enable review functionality, disabling login. */
    private void enableReview()
    {
        mReview.setVisibility(View.VISIBLE);
        mMyReviews.setVisibility(View.VISIBLE);
        mLogout.setVisibility(View.VISIBLE);
        mLogin.setVisibility(View.INVISIBLE);
    }

    /** Enable login functionality, disabling review. */
    private void enableLogin()
    {
        mReview.setVisibility(View.INVISIBLE);
        mMyReviews.setVisibility(View.INVISIBLE);
        mLogout.setVisibility(View.INVISIBLE);
        mLogin.setVisibility(View.VISIBLE);
    }

    /**
     * Method that launches fragment for getting data from our database.
     */
    private void onViewMyReviewsPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(MY_REVIEWS); }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener
    {
        /** For navigating between fragments. */
        void onFragmentTransition(Frags target);

        /** Will be used to store username locally on the device. */
        void onDataStorage(Frags source, Object data);

        /** Will be used to start the camera. */
        void onFunctionCall(Functions target);
    }
}
