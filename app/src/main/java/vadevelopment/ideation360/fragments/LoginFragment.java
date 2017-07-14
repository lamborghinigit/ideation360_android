package vadevelopment.ideation360.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.SplashActivity;
import vadevelopment.ideation360.database.ParseOpenHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginRegister_Containor containor;
    private TextView bottomForgot;
    private Button loginApp;
    private EditText et_email, et_pwd;
    private String get_pwd, get_email, serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String TAG = "LoginFragment";
    private SQLiteDatabase database;
    private NestedScrollView rlmain;
    Handler handler;
    private ImageView back;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        containor = (LoginRegister_Containor) getActivity();
        bottomForgot = (TextView) view.findViewById(R.id.bottomForgot);
        loginApp = (Button) view.findViewById(R.id.loginApp);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        back = (ImageView) view.findViewById(R.id.back);
        get_email = et_email.getText().toString().trim();
        get_pwd = et_pwd.getText().toString().trim();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        database = ParseOpenHelper.getInstance(getActivity()).getWritableDatabase();
        rlmain = (NestedScrollView) view.findViewById(R.id.rlmain);
        handler = new Handler();

        bottomForgot.setOnClickListener(this);
        loginApp.setOnClickListener(this);
        containor.firstLinear.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlmain.scrollTo(0, 20);
                    }
                }, 10);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottomForgot:
                ForgotPwdFragment fp = new ForgotPwdFragment();
                containor.replaceFragment(fp);
                break;
            case R.id.loginApp:
                get_email = et_email.getText().toString().trim();
                get_pwd = et_pwd.getText().toString().trim();

                if (get_email.length() == 0) {
                    // et_email.setError("Field is Empty");
                    // et_email.requestFocus();
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.enterusernameorpwd));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()) {
                    //  et_email.setError("Enter valid Email");
                    HandyObjects.showAlert(getActivity(), "Invalid email");
                    et_email.requestFocus();
                } else if (get_pwd.length() == 0) {
                    //et_pwd.setError("Field is blank");
                    // et_pwd.requestFocus();
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.enterusernameorpwd));
                } else if (!HandyObjects.isNetworkAvailable(getActivity())) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                } else {
                    makeJsonObjReq();
                }


                /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();*/
                break;
        }
    }

    private void makeJsonObjReq() {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Username", get_email);
        postParam.put("Email", get_email);
        postParam.put("Password", get_pwd);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.LOGIN, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d(TAG, res.toString());
                        try {
                            final JSONObject response = new JSONObject(res.toString());
                            if (serverstatus.equalsIgnoreCase("200")) {
                                try {
                                    if (response.getString("Status") != null && !response.getString("Status").isEmpty()) {
                                        JSONArray asignments_array = response.getJSONArray("Assignments");
                                        if (asignments_array.length() > 0) {
                                            if (asignments_array.length() == 1) {
                                                JSONObject jobj_first = asignments_array.getJSONObject(0);
                                                editor.putString("clientid", jobj_first.getString("ClientId"));
                                                editor.putString("ideatorid", jobj_first.getString("IdeatorId"));
                                                //HandyObjects.showAlert(getActivity(),jobj_first.getString("IdeatorId"));
                                                editor.putString("loginwith", "indi");
                                                editor.commit();
                                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else if (asignments_array.length() > 1) {
                                                database.delete("assignments", null, null);
                                                for (int i = 0; i < asignments_array.length(); i++) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put("company_name", asignments_array.getJSONObject(i).getString("CompanyName"));
                                                    cv.put("client_id", asignments_array.getJSONObject(i).getString("ClientId"));
                                                    cv.put("ideator_id", asignments_array.getJSONObject(i).getString("IdeatorId"));
                                                    long idd = database.insert("assignments", null, cv);
                                                    Log.e("table", String.valueOf(idd));
                                                }
                                                editor.putString("loginwith", "multi");
                                                editor.commit();
                                                MultipleAssig_Fragment mult_frg = new MultipleAssig_Fragment();
                                                containor.replaceFragment(mult_frg);
                                                //HandyObjects.showAlert(getActivity(), "store data greater than 1");
                                            }
                                        } else {
                                            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.noclient_avalibale));
                                        }
                                    } else {
                                        //HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
                                    }

                                } catch (Exception e) {
                                }
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                // HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
                                HandyObjects.showAlert(getActivity(), getResources().getString(R.string.invalidusername));
                            }
                            HandyObjects.stopProgressDialog();
                        } catch (Exception e) {
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                HandyObjects.showAlert(getActivity(), getResources().getString(R.string.invalidusername));
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                serverstatus = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

        // Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
