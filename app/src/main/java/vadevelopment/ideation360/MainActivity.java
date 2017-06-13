package vadevelopment.ideation360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initViews();
    }

    private void initViews() {
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent intent = new Intent(MainActivity.this, LoginRegister_Containor.class);
                intent.putExtra("from","login");
                startActivity(intent);
                //finish();
                break;
            case R.id.signup:
                Intent intent_signup = new Intent(MainActivity.this, LoginRegister_Containor.class);
                intent_signup.putExtra("from","signup");
                startActivity(intent_signup);
                break;

        }
    }
}
