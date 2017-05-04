package group1.tcss450.uw.edu.picreview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;


/**
 * A fragment that handles the main menu.
 * Currently, there are two things that the main menu must handle:
 * Passing the user to the SearchFragment or to the ReviewFragment.
 */
public class MainMenuFragment extends Fragment
{
    // This is the activity that swaps this fragment in and out.
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public MainMenuFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    /**
     * Method that implements functionality of the search button.
     * Specifically, it takes us to another fragment where we can search for reviews.
     * @param view is presumably the view this came from.
     */
    public void onSearchPressed(View view)
    {
        if (mListener != null) { mListener.onFragmentTransition(SEARCH); }
    }

    /**
     * Method that implements functionality of the review button.
     * Specifically, it takes us to another fragment where we can review items.
     * @param view is presumably the view this came from.
     */
    public void onReviewPressed(View view)
    {
        if (mListener != null) { mListener.onFragmentTransition(REVIEW); }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
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
        /**
         * Transfer from one fragment to another.
         * @param target the target fragment to swap to.
         */
        void onFragmentTransition(Frags target);
    }
}
