package group1.tcss450.uw.edu.picreview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A fragment to handle confirming whether the image for picReview is pleasing to the user or not.
 */
public class ConfirmPicFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public ConfirmPicFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of a ConfirmPicFragment.
     */
    public static ConfirmPicFragment newInstance()
    {
        // TODO: Determine if we need a factory method for this.
        /* My guess is we might need one, so that when we make one, we just
        *  pass in an image. Speaking of which, how do we pass around image
        *  files? */

        ConfirmPicFragment fragment = new ConfirmPicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
            mListener.onFragmentInteraction(uri);
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
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
