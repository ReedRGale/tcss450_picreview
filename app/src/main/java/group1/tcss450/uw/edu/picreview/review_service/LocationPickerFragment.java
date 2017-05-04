package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;


/**
 * A simple {@link Fragment} subclass.
 * This is designed to allow the user to pick a location for the picture.
 * No location by default.
 */
public class LocationPickerFragment     extends     Fragment
                                        implements  View.OnClickListener
{

    private OnFragmentInteractionListener mListener;

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
        ba.add((Button) v.findViewById(R.id.bMyLocation));
        ba.add((Button) v.findViewById(R.id.bNearbyLocations));
        ba.add((Button) v.findViewById(R.id.bPickerBack));
        ba.add((Button) v.findViewById(R.id.bPickerForward));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }


    public void onMyLocationPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(Functions.UNIMPLEMENTED); }
    }

    public void onSearchLocationPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(Functions.UNIMPLEMENTED); }
    }

    public void onNoLocationPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(Functions.UNIMPLEMENTED); }
    }

    public void onForwardPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(CONFIRM_REVIEW); }
    }

    public void onBackPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(LIKE_DISLIKE); }
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
            case R.id.bMyLocation:
                onMyLocationPressed();
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
    }
}
