package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;


/**
 * A fragment to handle confirming whether the image for picReview is pleasing to the user or not.
 */
public class ConfirmPicFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public ConfirmPicFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Determine if we need to do this.

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_pic, container, false);
    }

    /**
     * A method to move to the next fragment in picReview generation.
     *
     * @param uri is an artifact of autogeneration.
     */
    public void onYesPressed(Uri uri)
    {
        // TODO: Not yet implemented.
        if (mListener != null) {
            mListener.onFragmentTransition(UNIMPLEMENTED);
        }
    }

    /**
     * A method to move back to the camera to remake the review.
     *
     * @param uri is an artifact of autogeneration.
     */
    public void onNoPressed(Uri uri)
    {
        // TODO: Not yet implemented.
        if (mListener != null) {
            mListener.onFragmentTransition(UNIMPLEMENTED);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentTransition(Frags target);
    }
}
