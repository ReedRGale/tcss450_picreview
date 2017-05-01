package group1.tcss450.uw.edu.picreview;

import android.content.Context;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    /** Objects where text can be entered. */
    private EditText user_box;
    private EditText pass_box;
    private EditText confirm_pass_box;

    private final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~demyan15/db/";

    private String returnedMessage;

    private boolean success = false;

    public RegisterFragment() {
        // Required empty public constructor
    }


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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onClick(View v) {

        boolean canSubmit = true;

        if (user_box.length() == 0)
        {
            canSubmit = false;
            user_box.setError(getString(R.string.no_username));
        }

        if (pass_box.length() == 0)
        {
            canSubmit = false;
            pass_box.setError(getString(R.string.no_password));
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

        if(canSubmit)
        {
            AsyncTask<String, Void, String> task = new PostWebServiceTask();
            task.execute(PARTIAL_URL, user_box.getText().toString(), pass_box.getText().toString());
            if (success)
            {
                // Do something
            }
        }
    }

    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "registerService.php";
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
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG)
                    .show();
            // Something wrong with the network or the URL.
/*            if (result.startsWith("Unable to")) {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }*/
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
