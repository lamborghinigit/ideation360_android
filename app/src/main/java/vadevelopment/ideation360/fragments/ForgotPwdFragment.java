package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class ForgotPwdFragment extends Fragment {

    private LoginRegister_Containor containor;
    private Button restpwd_btn;
    private EditText et_email;
    private String get_email, serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String TAG = "ForgotPwdFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_forgotpwd, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        containor = (LoginRegister_Containor) getActivity();
        containor.firstLinear.setVisibility(View.VISIBLE);
        et_email = (EditText) view.findViewById(R.id.et_email);
        restpwd_btn = (Button) view.findViewById(R.id.restpwd_btn);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        containor.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        restpwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_email = et_email.getText().toString().trim();
                if (get_email.length() == 0) {
                    et_email.setError("Field is Empty");
                    et_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()) {
                    et_email.setError("Enter valid Email");
                    et_email.requestFocus();
                } else if (!HandyObjects.isNetworkAvailable(getActivity())) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                } else {
                    ForgotPwd_Task();
                }
            }
        });
    }


    private void ForgotPwd_Task() {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("UsernameOrEmail", get_email);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.FRGTPWD, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        if (serverstatus.equalsIgnoreCase("200")) {
                            try {
                                if (response.getString("Email") != null && !response.getString("Email").isEmpty()) {
                                    et_email.setText("");
                                    HandyObjects.showAlert(getActivity(), response.getString("Message"));
                                } else {
                                    HandyObjects.showAlert(getActivity(), response.getString("Message"));
                                }
                            } catch (Exception e) {
                            }
                        } else if (serverstatus.equalsIgnoreCase("400")) {
                            HandyObjects.showAlert(getActivity(), "forgot pwd error");
                        }
                        HandyObjects.stopProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

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
