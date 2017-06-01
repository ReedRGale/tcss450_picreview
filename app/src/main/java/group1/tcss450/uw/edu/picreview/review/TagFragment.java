package group1.tcss450.uw.edu.picreview.review;

import android.content.Context;
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

import static group1.tcss450.uw.edu.picreview.util.Frags.CAPTION;
import static group1.tcss450.uw.edu.picreview.util.Frags.LIKE_DISLIKE;
import static group1.tcss450.uw.edu.picreview.util.Frags.MAIN_MENU;
import static group1.tcss450.uw.edu.picreview.util.Frags.TAG;

/**
 * A simple Fragment to handle Tagging a Review.
 */
public class TagFragment    extends     Fragment
                            implements  View.OnClickListener
{
    /** The activity linked to this fragment. */
    private OnFragmentInteractionListener mListener;

    /** Empty constructor:  required. */
    public TagFragment() { }

    /** Will add listeners to the buttons. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tag, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bBackTag));
        ba.add((Button) v.findViewById(R.id.bForwardTag));
        ba.add((Button) v.findViewById(R.id.bTagHome));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /** Will go back to the previous step of the review process. */
    public void onBackPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(CAPTION); }
    }

    /**
     * Will go forward to the next step of the review process.
     * If there are tags, adds them.
     */
    public void onForwardPressed()
    {
        if (mListener != null)
        {
            EditText e = (EditText) getActivity().findViewById(R.id.eTags);
            String s = e.getText().toString();

            // Only store tags if they tag it.
            if (!s.equals("")) { mListener.onDataStorage(TAG, s); }
            mListener.onFragmentTransition(LIKE_DISLIKE);
        }
    }

    /** Will take the user to the main menu. */
    public void onHomePressed() {
        if(mListener != null) {
            mListener.onFragmentTransition(MAIN_MENU);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof LikeDislikeFragment.OnFragmentInteractionListener) {
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

    /** Will detect what button was clicked and call corresponding method. */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bBackTag:
                onBackPressed();
                break;
            case R.id.bForwardTag:
                onForwardPressed();
                break;
            case R.id.bTagHome:
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
        /** Will allow user to navigate back to the caption fragment, forward to likedislike
         * ragment, or back to the main menu. */
        void onFragmentTransition(Frags target);

        /** Will be used to help build the Review in MainActivity. */
        void onDataStorage(Frags source, Object data);
    }
}
