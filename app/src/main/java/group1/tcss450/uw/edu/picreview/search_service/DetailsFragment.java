package group1.tcss450.uw.edu.picreview.search_service;

// TODO: How do we want to approach this problem?
// TODO: Design an XML format for this.

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group1.tcss450.uw.edu.picreview.R;


/**
 * A fragment designed to handle presenting extra detail of a fragment.
 */
public class DetailsFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public DetailsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of a DetailsFragment.
     */
    public static DetailsFragment newInstance(String param1, String param2)
    {
        // TODO: Finish this if it needs completion.

        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this if needed.

        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    /**
     * This method should handle enlarging the picReview icon when pressed.
     * @param uri is a remnant of being auto-generated.
     */
    public void onEnlargePressed(Uri uri)
    {
        // TODO: Figure out the type of info that needs to be pushed into this, if anything.

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
