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
import group1.tcss450.uw.edu.picreview.util.Functions;

import static group1.tcss450.uw.edu.picreview.util.Frags.UNIMPLEMENTED;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PicReviewConfirmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PicReviewConfirmFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public PicReviewConfirmFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic_review_confirm, container, false);
    }

    public void onYesPressed()
    {
        if (mListener != null)  { mListener.onFunctionCall(Functions.UNIMPLEMENTED);  }
    }

    public void onNoPressed()
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
        void onFunctionCall(Functions target);
    }
}
