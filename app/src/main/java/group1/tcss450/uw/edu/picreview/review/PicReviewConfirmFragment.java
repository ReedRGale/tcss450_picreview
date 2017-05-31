package group1.tcss450.uw.edu.picreview.review;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.DBUtility;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;
import group1.tcss450.uw.edu.picreview.util.Review;

import static group1.tcss450.uw.edu.picreview.util.Frags.LOCATION;
import static group1.tcss450.uw.edu.picreview.util.Frags.MAIN_MENU;
import static group1.tcss450.uw.edu.picreview.util.Functions.*;
import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 * Last step of the review process. Will validate and save the review to a database.
 */
public class PicReviewConfirmFragment   extends     Fragment
                                        implements  View.OnClickListener
{

    private OnFragmentInteractionListener mListener;

    /** The review to be saved. */
    private Review mReview;

    /** Spinner that appears while review is saving. */
    private ProgressBar waitSpinner;

    /** Empty constructor:  required. */
    public PicReviewConfirmFragment() { }

    /** Will attach listeners to all of the buttons. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pic_review_confirm, container, false);

        waitSpinner = (ProgressBar) v.findViewById(R.id.progressBar);

        ArrayList<Button> ba = new ArrayList<Button>();

        // Add all buttons.
        ba.add((Button) v.findViewById(R.id.bReviewConfirm));
        ba.add((Button) v.findViewById(R.id.bReviewDeny));

        // Add the listeners.
        for (Button b : ba) { b.setOnClickListener(this); }

        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mListener.onFunctionCall(Functions.UPDATE_PREVIEW);
    }

    /** Will proceed with storing the review by handing it off to an asynchronous task.  */
    public void onYesPressed()
    {
        if (mListener != null)
        {
            mReview = (Review) mListener.onDataRetrieval(REVIEW_RETRIEVE);
            AsyncTask<Review, Void, Boolean> task = new PostWebServiceTask();
            task.execute(mReview);
        }
    }

    /** Will return to the previous step. */
    public void onNoPressed()
    {
        if (mListener != null) { mListener.onFragmentTransition(LOCATION); }
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

    /** Will detect what button was clicked and call corresponding method. */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
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
        /* For navigating between LocationPicker & Main menu fragments. */
        void onFragmentTransition(Frags target);

        /* Will be used to display the image taken. */
        void onFunctionCall(Functions target);

        /* Will be used to retrieve the review if the user wants to go ahead and save it.*/
        Object onDataRetrieval(Functions target);
    }

    /**
     * Will hit the php webservice that will save the review into the database.
     */
    private class PostWebServiceTask extends AsyncTask<Review, Void, Boolean> {

        /** Loads the progressBar 90% of the way. */
        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            waitSpinner.incrementProgressBy(90);
        }

        /** Calls a method that will handle the process of contacting the server and saving the review. */
        @Override
        protected Boolean doInBackground(Review... reviews) {
            return DBUtility.saveReview(reviews[0]);
        }

        /** Will increment the progress bar the remaining 10% and notify the user whether the attempt was successfuly. */
        @Override
        protected void onPostExecute(Boolean result) {
            waitSpinner.incrementProgressBy(10);
            waitSpinner.setVisibility(View.GONE);

            if (result)
            {
                Toast.makeText(getContext(), "Review was successfully saved", Toast.LENGTH_LONG).show();
                mListener.onFragmentTransition(MAIN_MENU);
            }
            else
            {
                Toast.makeText(getContext(), "Failed to save Review. Please try again.", Toast.LENGTH_LONG).show();
                waitSpinner.setProgress(0);
            }
        }
    }
}
