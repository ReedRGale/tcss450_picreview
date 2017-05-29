package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
import android.net.Uri;
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

import static group1.tcss450.uw.edu.picreview.util.Frags.*;
import static group1.tcss450.uw.edu.picreview.util.Functions.*;


/**
 * This is designed to allow the user to pick a location for the picture.
 * No location by default.
 */
public class LocationPickerFragment     extends     Fragment
                                        implements  View.OnClickListener
{
    /** The activity linked to this fragment. */
    private OnFragmentInteractionListener mListener;

    /** Empty constructor:  required. */
    public LocationPickerFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_picker, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bNoLocation));
        ba.add((Button) v.findViewById(R.id.bNearbyLocations));
        ba.add((Button) v.findViewById(R.id.bPickerBack));
        ba.add((Button) v.findViewById(R.id.bPickerForward));
        ba.add((Button) v.findViewById(R.id.bPickerHome));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /** Functionality for search nearby locations button. */
    public void onSearchLocationPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(PLACE_PICKER); }
    }

    /** Functionality for no location button. */
    public void onNoLocationPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(Functions.UNIMPLEMENTED); }
    }

    /** Functionality for proceeding forward. */
    public void onForwardPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(CONFIRM_REVIEW); }
    }

    /** Functionality for going back to previous step. */
    public void onBackPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(LIKE_DISLIKE); }
    }

    public void onHomePressed() {
        if(mListener != null) {
            mListener.onFragmentTransition(MAIN_MENU);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
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

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bPickerBack:
                onBackPressed();
                break;
            case R.id.bPickerForward:
                onForwardPressed();
                break;
            case R.id.bNoLocation:
                onNoLocationPressed();
                break;
            case R.id.bNearbyLocations:
                onSearchLocationPressed();
                break;
            case R.id.bPickerHome:
                onHomePressed();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener
    {
        void onFragmentTransition(Frags target);
        void onFunctionCall(Functions target);
        void onDataStorage(Frags source, Object data);
    }
}
