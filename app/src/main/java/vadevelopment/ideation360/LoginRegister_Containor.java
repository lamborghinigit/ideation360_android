package vadevelopment.ideation360;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import vadevelopment.ideation360.fragments.LoginFragment;
import vadevelopment.ideation360.fragments.LoginRegister_Fragment;
import vadevelopment.ideation360.fragments.SignupFragment;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class LoginRegister_Containor extends FragmentActivity {

    public ImageView back;
    public RelativeLayout firstLinear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        initViews();
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.back);
        firstLinear = (RelativeLayout) findViewById(R.id.firstLinear);
        LoginRegister_Fragment lf = new LoginRegister_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.loginreg_containor, lf).commit();
    }

    public void replaceFragment(Fragment fm) {
        getSupportFragmentManager().beginTransaction().replace(R.id.loginreg_containor, fm).addToBackStack(null).commit();
    }
}
