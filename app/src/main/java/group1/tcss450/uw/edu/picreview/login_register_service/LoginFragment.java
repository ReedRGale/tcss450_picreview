package group1.tcss450.uw.edu.picreview.login_register_service;

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


/**
 * A simple {@link Fragment} subclass.
 * Implements the login functionality.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    /** Object where the user can enter text for username. */
    private EditText user_text;

    /** Object where the user can enter text for password. */
    private EditText pass_text;

    private final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~demyan15/";

    private String returnedMessage;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button button = (Button) v.findViewById(R.id.login_submit_button);
        button.setOnClickListener(this);

        user_text = (EditText) v.findViewById(R.id.login_username_box);
        pass_text = (EditText) v.findViewById(R.id.login_password_box);
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

    @Override
    public void onClick(View v) {

        boolean canSubmit = true;

        if(user_text.length() == 0)
        {
            canSubmit = false;
            user_text.setError(getString(R.string.no_username));
        }
        if (pass_text.length() == 0)
        {
            canSubmit = false;
            pass_text.setError(getString(R.string.no_password));
        }

        if (canSubmit)
        {
            AsyncTask<String, Void, String> task = new PostWebServiceTask();
            task.execute(PARTIAL_URL, user_text.getText().toString(), pass_text.getText().toString());
        }

    }

    /**
     * Will hit the php code that checks with the database to see whether the credentials exist.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "loginService.php";
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
        @Override
        protected void onPostExecute(String result) {
            if (result.equals(getString(R.string.DBSuccessMessage)))
            {
                Toast.makeText(getContext(), "Login was successful", Toast.LENGTH_LONG)
                        .show();
            }
            else
            {
                Toast.makeText(getContext(), "Failed to login. Please re-enter your credentials and try again.", Toast.LENGTH_LONG)
                        .show();
                user_text.setText("");
                pass_text.setText("");
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
        void onFragmentInteraction(String toStart);
    }
}
