package group1.tcss450.uw.edu.picreview.review_service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;

/**
 * A simple {@link Fragment} subclass.
 * Will display a list of reviews based on the list given it.
 */
public class DisplayReviewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    /* The view of this fragment. */
    LinearLayout mView;

    /** The activity linked to this fragment. */
    public DisplayReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = (LinearLayout) inflater.inflate(R.layout.fragment_display_reviews, container, false);

        // List Reviews
        if (getArguments() != null)
        {
            ArrayList<String> reviews = (ArrayList<String>) getArguments().getSerializable("Reviews");
            for (String review: reviews)
            {
                TextView revView = new TextView(getActivity());
                revView.setText(review);
                mView.addView(revView);
            }
        }

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                    //+ " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
