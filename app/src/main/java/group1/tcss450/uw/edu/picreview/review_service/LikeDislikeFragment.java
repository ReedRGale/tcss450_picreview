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

import static group1.tcss450.uw.edu.picreview.util.Frags.UNIMPLEMENTED;


/**
 * A simple {@link Fragment} subclass designed to handle adding the like or dislike
 * piece of information to the PicReview object.
 */
public class LikeDislikeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LikeDislikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_dislike, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onLikePressed(Uri uri) {
        if (mListener != null) { mListener.onFragmentTransition(UNIMPLEMENTED); }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onDislikePressed(Uri uri) {
        if (mListener != null) { mListener.onFragmentTransition(UNIMPLEMENTED); }
    }

    public void onBackPressed(Uri uri) {
        if (mListener != null) { mListener.onFragmentTransition(UNIMPLEMENTED); }
    }

    public void onForwardPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(UNIMPLEMENTED); }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener
    {
        void onFragmentTransition(Frags target);
    }
}
