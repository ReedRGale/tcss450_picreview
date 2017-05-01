package group1.tcss450.uw.edu.picreview;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
                                            RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
