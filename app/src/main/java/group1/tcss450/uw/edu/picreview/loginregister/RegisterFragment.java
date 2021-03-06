package group1.tcss450.uw.edu.picreview.loginregister;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group1.tcss450.uw.edu.picreview.R;
import group1.tcss450.uw.edu.picreview.util.Frags;
import group1.tcss450.uw.edu.picreview.util.Globals;

import static group1.tcss450.uw.edu.picreview.util.Frags.MAIN_MENU;
import static group1.tcss450.uw.edu.picreview.util.Frags.REGISTER;


/**
 * A simple {@link Fragment} subclass.
 * Implements the registration functionality.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    /** Objects where text can be entered. */
    private EditText user_box;
    private EditText pass_box;
    private EditText confirm_pass_box;

    /** The link to to folder where our php scripts are contained. */
    private final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~demyan15/";


    public RegisterFragment() {
        // Required empty public constructor
    }

    /** Gets references to some views and attaches listeners to submit button. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button submit_button = (Button) v.findViewById(R.id.register_submit_button);
        submit_button.setOnClickListener(this);

        user_box = (EditText) v.findViewById(R.id.register_username_box);
        pass_box = (EditText) v.findViewById(R.id.register_password_box);
        confirm_pass_box = (EditText) v.findViewById(R.id.register_confirm_password_box);

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

    /** Will check the input to make sure it is valid before attempting to register by passing data off to an AsyncTask. */
    @Override
    public void onClick(View v) {

        boolean canSubmit = true;

        if (user_box.length() == 0)
        {
            canSubmit = false;
            user_box.setError(getString(R.string.no_username));
        }
        else if (user_box.length() < 4 || user_box.length() > 25)
        {
            canSubmit = false;
            user_box.setError(getString(R.string.short_username));
        }


        if (pass_box.length() == 0)
        {
            canSubmit = false;
            pass_box.setError(getString(R.string.no_password));
        }
        else if (pass_box.length() < 4 || pass_box.length() > 25)
        {
            canSubmit = false;
            pass_box.setError(getString(R.string.short_password));
        }
        else if (confirm_pass_box.length() == 0)
        {
            canSubmit = false;
            confirm_pass_box.setError(getString(R.string.no_confirm_pass));
        }
        else if (!confirm_pass_box.getText().toString().equals(pass_box.getText().toString()))
        {
            canSubmit = false;
            confirm_pass_box.setError(getString(R.string.no_pass_match));
        }

        if (user_box.getText().toString().equals(pass_box.getText().toString()))
        {
            canSubmit = false;
            user_box.setError(getString(R.string.pass_username_match));

        }

        if(canSubmit)
        {
            AsyncTask<String, Void, String> task = new PostWebServiceTask();
            task.execute(PARTIAL_URL, user_box.getText().toString(), pass_box.getText().toString());
        }
    }

    /**
     * Will hit the php code that checks with the database to see whether the credentials can be registered.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {

        /* Name of php file that handles registration attempts. */
        private final String SERVICE = "registerService.php";

        /* Will be used to temporarily house the new username. */
        private String new_username = Globals.CURRENT_USERNAME;

        /* Will send a request to the server and return the response. */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                new_username = strings[1];
                String data = URLEncoder.encode("Username", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("Password", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        /** Determines whether the registration was successful, if so it saves the username locally and takes the user to the main menu; else it notifies the user of failure. */
        @Override
        protected void onPostExecute(String result) {

            if (result.equals(getString(R.string.DBSuccessMessage)))
            {
                Globals.CURRENT_USERNAME = new_username;
                Toast.makeText(getContext(), "Registration was successful", Toast.LENGTH_LONG)
                        .show();
                mListener.onDataStorage(REGISTER, new_username);
                mListener.onFragmentTransition(MAIN_MENU);

            }
            else
            {
                Toast.makeText(getContext(), "Failed to register. Please try again with different credentials.", Toast.LENGTH_LONG)
                        .show();
            }
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
        /** Will be used to start the main menu fragment. */
        void onFragmentTransition(Frags target);

        /** Will be used to store credentials on locally on the device. */
        void onDataStorage(Frags source, Object data);
    }
}
