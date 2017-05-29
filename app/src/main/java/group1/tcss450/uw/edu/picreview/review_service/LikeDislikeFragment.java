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

import static group1.tcss450.uw.edu.picreview.util.Frags.CAPTION;
import static group1.tcss450.uw.edu.picreview.util.Frags.LIKE_DISLIKE;
import static group1.tcss450.uw.edu.picreview.util.Frags.LOCATION;
import static group1.tcss450.uw.edu.picreview.util.Frags.MAIN_MENU;
import static group1.tcss450.uw.edu.picreview.util.Frags.TAG;
import static group1.tcss450.uw.edu.picreview.util.Functions.*;


/**
 * A simple {@link Fragment} subclass designed to handle adding the like or dislike
 * piece of information to the PicReview object.
 */
public class LikeDislikeFragment    extends     Fragment
                                    implements  View.OnClickListener
{
    /** The activity linked to this fragment. */
    private OnFragmentInteractionListener mListener;

    /** The forward button. */
    private Button mForward;

    /** Empty contructor:  required. */
    public LikeDislikeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_like_dislike, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        mForward = (Button) v.findViewById(R.id.bForwardLikeDislike);
        mForward.setEnabled(false);
        mForward.setVisibility(View.INVISIBLE);

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bBackLikeDislike));
        ba.add((Button) v.findViewById(R.id.bForwardLikeDislike));
        ba.add((Button) v.findViewById(R.id.bLike));
        ba.add((Button) v.findViewById(R.id.bDislike));
        ba.add((Button) v.findViewById(R.id.bHomeLikeDislike));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /**
     * Will notify the proper method of the user's choice.
     * Should enable the forward button.
     */
    public void onLikePressed()
    {
        if (mListener != null)
        {
            mForward.setEnabled(true);
            mForward.setVisibility(View.VISIBLE);
            mListener.onDataStorage(LIKE_DISLIKE, 1);
        }
    }

    /**
     * Will notify the proper method of the user's choice.
     * Should enable the forward button.
     */
    public void onDislikePressed()
    {
        if (mListener != null)
        {
            mForward.setEnabled(true);
            mListener.onDataStorage(LIKE_DISLIKE, -1);
        }
    }

    /** Will go back to the previous step of the review process. */
    public void onBackPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(TAG); }
    }

    /** Will go forward to the next step of the review process. */
    public void onForwardPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(LOCATION); }
    }

    /** Return to the home screen and abort the review process. */
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
            case R.id.bBackLikeDislike:
                onBackPressed();
                break;
            case R.id.bForwardLikeDislike:
                onForwardPressed();
                break;
            case R.id.bLike:
                onLikePressed();
                break;
            case R.id.bDislike:
                onDislikePressed();
                break;
            case R.id.bHomeLikeDislike:
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
        void onDataStorage(Frags source, Object data);
    }
}
