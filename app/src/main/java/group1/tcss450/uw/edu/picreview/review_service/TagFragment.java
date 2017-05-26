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
import android.widget.EditText;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.CAPTION;
import static group1.tcss450.uw.edu.picreview.util.Frags.LIKE_DISLIKE;
import static group1.tcss450.uw.edu.picreview.util.Frags.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TagFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TagFragment    extends     Fragment
                            implements  View.OnClickListener
{

    private OnFragmentInteractionListener mListener;

    public TagFragment() { }

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

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /* Will go back to the previous step of the review process. */
    public void onBackPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(CAPTION); }
    }

    /* Will go forward to the next step of the review process. */
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
