package group1.tcss450.uw.edu.picreview.search;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.DBUtility;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Functions;
import group1.tcss450.uw.edu.picreview.util.Globals;
import group1.tcss450.uw.edu.picreview.util.Review;

import static group1.tcss450.uw.edu.picreview.util.Functions.QUERY_RETRIEVE;

/**
 * Will get and display reviews matching the search term.
 */
public class QueryFragment extends Fragment
{
    /** This is the activity that swaps this fragment in and out. */
    private OnFragmentInteractionListener mListener;

    /** Variables needed to set up the recyclerView. */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Review> mDataset;

    /** Progress bar to show while loading reviews. */
    private ProgressBar waitSpinner;

    /** The search terms. */
    private String[] mQuery;

    /** Required empty public constructor */
    public QueryFragment() { }

    /** Will get references to view and start the process of getting reviews matching search term. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_query, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvQuery);
        waitSpinner = (ProgressBar) v.findViewById(R.id.progressBarQuery);

        // use a linear layout manager
        LinearLayoutManager tempManager = new LinearLayoutManager(getContext());
        tempManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager = tempManager;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mQuery = (String[]) mListener.onDataRetrieval(QUERY_RETRIEVE);

        mDataset = new ArrayList<Review>();
        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        AsyncTask<Void, Void, List<Review>> task = new PostWebServiceTask();
        task.execute();

        return v;
    }

    /** The list object which holds the reviews. */
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        /** View object that serves as a template for the review. */
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView mImage;
            public TextView mCaption;
            public TextView mLikes;
            public TextView mDislikes;
            public TextView mTags;
            public TextView mLocation;
            public TextView mPosition;
            public TextView mUser;

            /** Gets references to all of the views and attaches listeners to the buttons. */
            public ViewHolder(View itemView) {
                super(itemView);
                mImage = (ImageView) itemView.findViewById(R.id.review_photo);
                mCaption = (TextView) itemView.findViewById(R.id.caption);
                mLikes = (TextView) itemView.findViewById(R.id.likes);
                mDislikes = (TextView) itemView.findViewById(R.id.dislikes);
                mTags = (TextView) itemView.findViewById(R.id.tags);
                mLocation = (TextView) itemView.findViewById(R.id.location);
                mPosition = (TextView) itemView.findViewById(R.id.hiddenView);
                mUser = (TextView) itemView.findViewById(R.id.user);

                Button b = (Button) itemView.findViewById(R.id.bLike);
                b.setOnClickListener(this);

                b = (Button) itemView.findViewById(R.id.bDislike);
                b.setOnClickListener(this);

            }

            /** Determines whether button clicked was a like or dislike, updates corresponding number, and starts the appropriate service. */
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bLike)
                {
                    int index = Integer.parseInt(mPosition.getText().toString());
                    Review review = mDataset.get(index);
                    int likes = review.getLikes();
                    review.setLikes(++likes);
                    mLikes.setText("" + review.getLikes());
                    AsyncTask<Review, Void, Boolean> task = new PostUpdateLikesWebServiceTask();
                    task.execute(review);

                }
                else if (v.getId() == R.id.bDislike)
                {
                    int index = Integer.parseInt(mPosition.getText().toString());
                    Review review = mDataset.get(index);
                    int dislikes = review.getDislikes();
                    review.setDislikes(++dislikes);
                    mDislikes.setText("" + review.getDislikes());
                    AsyncTask<Review, Void, Boolean> task = new PostUpdateDislikesWebServiceTask();
                    task.execute(review);
                }
            }

        }


        // Doesn't really do anything
        public MyAdapter(List<Review> myDataset) {
            mDataset = myDataset;
        }

        /** Sets up the viewHolder to use our layout. */
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            View v = LayoutInflater .from(parent.getContext())
                                    .inflate(R.layout.review_view,
                                             parent,
                                             false);
            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            return vh;
        }

        /** Replaces the contents of views (invoked by the layout manager) */
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mImage.setImageBitmap(mDataset.get(position).getImage());
            holder.mCaption.setText("Caption: " + mDataset.get(position).getCaption());
            holder.mLikes.setText("" + mDataset.get(position).getLikes());
            holder.mLikes.setTextColor(Color.GREEN);
            holder.mDislikes.setText("" +  mDataset.get(position).getDislikes());
            holder.mDislikes.setTextColor(Color.RED);
            String tags = "Tags: ";
            for (int i = 0; i < mDataset.get(position).getTag().size(); i++)
            {
                tags += "#" + mDataset.get(position).getTag().get(i) + " ";
            }
            holder.mTags.setText(tags);
            holder.mPosition.setText("" + position);
            holder.mUser.setText("By: " + mDataset.get(position).getUser());
            holder.mLocation.setText("Location: " + mDataset.get(position).getmAddress());
        }

        /** Return the size of the dataset (invoked by the layout manager). */
        @Override
        public int getItemCount() {
            if (mDataset != null)
            {
                return mDataset.size();
            }
            else
            {
                return 0;
            }
        }
    }

    /**
     * Will hit the php webservice that will get the reviews needed.
     */
    private class PostWebServiceTask extends AsyncTask<Void, Void, List<Review>>
    {

        /** Loads the progressBar 90% of the way. */
        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            waitSpinner.incrementProgressBy(90);
        }

        /** Calls a method that will handle the process of contacting the server and getting reviews. */
        @Override
        protected List<Review> doInBackground(Void... params)
        {
            // Collect all review candidates.
            List<List<Review>> tempReviews = new ArrayList<List<Review>>();

            for (String s : mQuery) { tempReviews.add(DBUtility.getReviewsByUsername(s)); }
            for (String s : mQuery) { tempReviews.add(DBUtility.getReviewsByTag(s)); }

            // Add all temporary reviews here now.
            List<Review> finalReviews = new ArrayList<Review>();

            for (List<Review> l : tempReviews) { finalReviews.addAll(l); }

            return finalReviews;
        }

        /** Will increment the progress bar the remaining 10% and update the set in the RecyclerView or let the user
         * know that there are no reviews and take them back to the main menu.
         */
        @Override
        protected void onPostExecute(List<Review> resultSet) {
            waitSpinner.incrementProgressBy(10);
            waitSpinner.setVisibility(View.GONE);

            // Get all user reviews first
            if (resultSet.size() != 0)
            {
                mAdapter = new MyAdapter(resultSet);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder .setMessage("No reviews found...")
                        .setPositiveButton( "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        mListener.onFragmentTransition(Frags.SEARCH);
                                    }
                                })
                        .show();
            }

        }
    }

    /**
     * Will hit the php webservice that will update the number of likes for this review.
     */
    private class PostUpdateLikesWebServiceTask extends AsyncTask<Review, Void, Boolean> {

        /** Loads the progressBar 90% of the way. */
        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            waitSpinner.incrementProgressBy(90);
        }

        /** Calls a method that handles the interaction with the server. */
        @Override
        protected Boolean doInBackground(Review... reviews) {
            return DBUtility.updateReview(Globals.LIKE_FIELD, reviews[0]);
        }

        /** Will increment the progress bar the remaining 10%. */
        @Override
        protected void onPostExecute(Boolean result) {
            waitSpinner.incrementProgressBy(10);
            waitSpinner.setVisibility(View.GONE);
        }
    }

    /**
     * Will hit the php webservice that will update the number of dislikes for this review.
     */
    private class PostUpdateDislikesWebServiceTask extends AsyncTask<Review, Void, Boolean> {

        /** Loads the progressBar 90% of the way. */
        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            waitSpinner.incrementProgressBy(90);
        }

        /** Calls a method that handles the interaction with the server. */
        @Override
        protected Boolean doInBackground(Review... reviews) {
            return DBUtility.updateReview(Globals.DISLIKE_FIELD, reviews[0]);
        }

        /** Will increment the progress bar the remaining 10%. */
        @Override
        protected void onPostExecute(Boolean result) {
            waitSpinner.incrementProgressBy(10);
            waitSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnFragmentInteractionListener) {
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
    public interface OnFragmentInteractionListener
    {

        /** Used to retrieve query information. */
        Object onDataRetrieval(Functions target);

        /** Used to take the user back to search when no reviews are found. */
        void onFragmentTransition(Frags target);
    }
}
