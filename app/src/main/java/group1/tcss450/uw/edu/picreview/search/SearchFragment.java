package group1.tcss450.uw.edu.picreview.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;

/**
 * A fragment designed to hold the logic for searching the database for picReviews.
 */
public class SearchFragment     extends     Fragment
                                implements  View.OnClickListener
{
    /** This is the activity that swaps this fragment in and out. */
    private OnFragmentInteractionListener mListener;

    /** These are the strings that hold the query. */
    private String mQuery;
    private EditText eText;

    /** Required empty public constructor */
    public SearchFragment() { }

    /** Will get references to views and add listeners to buttons. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        eText = (EditText) v.findViewById(R.id.eQuery);

        ArrayList<ImageButton> ba = new ArrayList<ImageButton>();

        // Add all buttons.
        ba.add((ImageButton) v.findViewById(R.id.bSearch));

        // Add the listeners.
        for (ImageButton b : ba) { b.setOnClickListener(this); }

        return v;
    }

    /** Method that begins a search for a picReview. */
    public void onSearchPressed()
    {
        if (mListener != null)
        {
            mQuery = eText.getText().toString();
            Log.d("TestString", eText.getText().toString());

            mListener.onDataStorage(SEARCH, mQuery);
            mListener.onFragmentTransition(QUERY);
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

    /** Will call a method that handles searches. */
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.bSearch:
                onSearchPressed();
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
        /** Will be used to start the query fragment. */
        void onFragmentTransition(Frags target);

        /** Will be used to store the query term so that the query fragment can access it. */
        void onDataStorage(Frags source, Object data);
    }
}
