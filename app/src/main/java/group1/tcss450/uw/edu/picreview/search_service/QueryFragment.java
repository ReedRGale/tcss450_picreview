package group1.tcss450.uw.edu.picreview.search_service;


import android.content.Context;
import android.content.DialogInterface;
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

import static group1.tcss450.uw.edu.picreview.util.Frags.*;
import static group1.tcss450.uw.edu.picreview.util.Functions.QUERY_RETRIEVE;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment
{
    /** This is the activity that swaps this fragment in and out. */
    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar waitSpinner;
    private String[] mQuery;

    /** Required empty public constructor */
    public QueryFragment() { }


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
        AsyncTask<Void, Void, List<Review>> task = new PostWebServiceTask();
        task.execute();

        return v;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        private List<Review> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private ImageView mImage;
            private TextView mCaption;

            private ViewHolder(View itemView) {
                super(itemView);
                mImage = (ImageView) itemView.findViewById(R.id.review_photo);
                mCaption = (TextView) itemView.findViewById(R.id.caption);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Review> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
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

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mImage.setImageBitmap(mDataset.get(position).getImage());
            holder.mCaption.setText(mDataset.get(position).getCaption());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    /**
     * Will hit the php webservice that will get the reviews needed.
     */
    private class PostWebServiceTask extends AsyncTask<Void, Void, List<Review>>
    {

        @Override
        protected void onPreExecute() {
            waitSpinner.setVisibility(View.VISIBLE);
            waitSpinner.incrementProgressBy(90);
        }

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
                builder .setMessage("You haven't made any reviews...")
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
        Object onDataRetrieval(Functions target);
        void onFragmentTransition(Frags target);
    }
}
