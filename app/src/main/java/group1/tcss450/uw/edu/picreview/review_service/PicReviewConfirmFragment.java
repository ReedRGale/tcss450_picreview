package group1.tcss450.uw.edu.picreview.review_service;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    /* The review to be saved. */
    private Review mReview;

    /* Spinner that appears while review is saving. */
    private ProgressBar waitSpinner;

    /** Empty constructor:  required. */
    public PicReviewConfirmFragment() { }

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
        void onFragmentTransition(Frags target);
        Object onDataRetrieval(Functions target);
    }

    /**
     * Will hit the php webservice that will save the review into the database.
     */
    private class PostWebServiceTask extends AsyncTask<Review, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            for (int i = 0; i < 90; i++)
            {
                waitSpinner.incrementProgressBy(1);
            }
        }

        @Override
        protected Boolean doInBackground(Review... reviews) {
            return DBUtility.saveReview(reviews[0]);
        }

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

            Log.d("RESPONSE", result.toString());
        }
    }
}
