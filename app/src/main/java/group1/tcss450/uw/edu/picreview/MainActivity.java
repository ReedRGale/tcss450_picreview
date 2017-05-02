package group1.tcss450.uw.edu.picreview;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Credentials to SQL database. Use SQLyog or MYSQL workbench to login.
 * Username: demyan15
 * Password: UnunItHo
 */

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
                                            RegisterFragment.OnFragmentInteractionListener, UserAccessFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new UserAccessFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onFragmentInteraction(String toStart) {
        Log.d("Action", "Gets HERE: " + toStart);
        if (toStart.equals(getString(R.string.toStartLoginFragment)))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).addToBackStack(null).commit();
        }
        else if (toStart.equals(getString(R.string.toStartRegisterFragment)))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RegisterFragment()).addToBackStack(null).commit();
        }
    }
}
