package group1.tcss450.uw.edu.picreview;

// TODO: Figure out how to move elements on the screen.

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment designed to hold the logic for searching the database for picReviews.
 */
public class SearchFragment extends Fragment
{
    // This is the activity that swaps this fragment in and out.
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public SearchFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return a new instance of the fragment.
     */
    public static SearchFragment newInstance(String param1, String param2)
    {

        // TODO: Implement factory method.

        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement if need be.

        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    /**
     * Method that begins a search for a picReview.
     *
     * @param query is the arg used to retrieve info from the database.
     */
    public void onButtonPressed(String query) {

        // TODO: Not yet implemented.

        if (mListener != null) { }
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
