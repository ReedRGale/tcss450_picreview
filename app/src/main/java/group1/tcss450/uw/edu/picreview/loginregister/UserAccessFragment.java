package group1.tcss450.uw.edu.picreview.loginregister;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;

import static group1.tcss450.uw.edu.picreview.util.Frags.*;


/**
 * Basically a sort of menu for user access to the app.
 */
public class UserAccessFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public UserAccessFragment() {
        // Required empty public constructor
    }

    /* Attaches listeners to the buttons. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_access, container, false);

        Button b = (Button) v.findViewById(R.id.login_button);
        b.setOnClickListener(this);

        b = (Button) v.findViewById(R.id.register_button);
        b.setOnClickListener(this);

        return v;
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

    /* Will determine what button was clicked and open the corresponding fragment. */
    @Override
    public void onClick(View v) {
        // Will determine which fragment to start
        if (v.getId() == R.id.login_button)
        {
            mListener.onFragmentTransition(LOGIN);
        }
        else if (v.getId() == R.id.register_button)
        {
            mListener.onFragmentTransition(REGISTER);
        }
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
        void onFragmentTransition(Frags target);
    }
}
