package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
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
 * A fragment to handle confirming whether the image for picReview is pleasing to the user or not.
 */
public class ConfirmPicFragment     extends     Fragment
                                    implements  View.OnClickListener
{
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public ConfirmPicFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_pic, container, false);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bReviewDeny));
        ba.add((Button) v.findViewById(R.id.bReviewConfirm));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /**
     * A method to move to the next fragment in picReview generation.
     */
    public void onYesPressed()
    {
        if (mListener != null)
        {
            /*  Odd case; the bitmap is in the activity right now,
                so no data needs to be passed. */
            mListener.onDataStorage(CONFIRM_PIC, null);
            mListener.onFragmentTransition(CAPTION);
        }
    }

    /**
     * A method to move back to the camera to remake the review.
     */
    public void onNoPressed()
    {
        if (mListener != null) { mListener.onFunctionCall(Functions.TAKE_PICTURE); }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.bReviewConfirm:
                onYesPressed();
                break;
            case R.id.bReviewDeny:
                onNoPressed();
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
        void onFunctionCall(Functions target);
    }
}
