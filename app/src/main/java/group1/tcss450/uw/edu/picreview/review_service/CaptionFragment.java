package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;
import static group1.tcss450.uw.edu.picreview.util.Frags.CAPTION;
import static group1.tcss450.uw.edu.picreview.util.Frags.CONFIRM_PIC;
import static group1.tcss450.uw.edu.picreview.util.Frags.LIKE_DISLIKE;
import static group1.tcss450.uw.edu.picreview.util.Frags.UNIMPLEMENTED;


/**
 * A simple {@link Fragment} subclass.
 * Allows user to add a caption to their review.
 */
public class CaptionFragment    extends     Fragment
                                implements  View.OnClickListener
{
    /** The activity linked to this fragment. */
    private OnFragmentInteractionListener mListener;

    /** Empty constructor:  required. */
    public CaptionFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_caption, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bCaptionBack));
        ba.add((Button) v.findViewById(R.id.bCaptionForward));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /** Will go back to the previous step of the review process. */
    public void onBackPressed()
    {
        if (mListener != null) {  mListener.onFragmentTransition(CONFIRM_PIC); }
    }

    /**
     * Will go forward to the next step of the review process.
     * If there is a caption to save, it will save it. */
    public void onForwardPressed()
    {
        if (mListener != null)
        {
            mListener.onDataStorage( CAPTION,
                                     getActivity().findViewById(R.id.eCaption));
            mListener.onFragmentTransition(TAG);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.bCaptionForward:
                onForwardPressed();
                break;
            case R.id.bCaptionBack:
                onBackPressed();
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
        void onDataStorage(Frags source, Object data);
    }
}
