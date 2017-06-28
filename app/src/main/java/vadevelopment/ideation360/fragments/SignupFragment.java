package vadevelopment.ideation360.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import retrofit2.Call;
import retrofit2.Callback;
import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.database.ParseOpenHelper;
import vadevelopment.ideation360.rest.ApiClient;
import vadevelopment.ideation360.rest.ApiInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class SignupFragment extends Fragment {

    private static String TAG = "SignupFragment";
    private LoginRegister_Containor containor;
    private Button signup;
    public ApiInterface apiService;
    private EditText et_fname, et_lname, et_companyname, et_email, et_pwd;
    private CheckBox checkbox;
    private String get_firstname, get_lastname, get_cmpnyname, get_email, get_pwd, serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        containor = (LoginRegister_Containor) getActivity();
        containor.firstLinear.setVisibility(View.VISIBLE);
        signup = (Button) view.findViewById(R.id.signup);
        et_fname = (EditText) view.findViewById(R.id.et_fname);
        et_lname = (EditText) view.findViewById(R.id.et_lname);
        et_companyname = (EditText) view.findViewById(R.id.et_companyname);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        database = ParseOpenHelper.getInstance(getActivity()).getWritableDatabase();

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        containor.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_firstname = et_fname.getText().toString().trim();
                get_lastname = et_lname.getText().toString().trim();
                get_cmpnyname = et_companyname.getText().toString().trim();
                get_email = et_email.getText().toString().trim();
                get_pwd = et_pwd.getText().toString().trim();

                if (get_firstname.length() == 0) {
                   /* et_fname.setError("Field is Empty");
                    et_fname.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.allfieldsmandatory));
                } else if (get_lastname.length() == 0) {
                  /*  et_lname.setError("Field is Empty");
                    et_lname.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.allfieldsmandatory));
                } else if (get_cmpnyname.length() == 0) {
                   /* et_companyname.setError("Field is Empty");
                    et_companyname.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.allfieldsmandatory));
                } else if (get_email.length() == 0) {
                   /* et_email.setError("Field is Empty");
                    et_email.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.allfieldsmandatory));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(get_email).matches()) {
                    /*et_email.setError("Enter valid Email");
                    et_email.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), "Invalid email");
                    et_email.requestFocus();
                } else if (get_pwd.length() == 0) {
                   /* et_pwd.setError("Field is blank");
                    et_pwd.requestFocus();*/
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.allfieldsmandatory));
                } else if (checkbox.isChecked() == false) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.select_termcondition));
                } else if (!HandyObjects.isNetworkAvailable(getActivity())) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                } else {
                    makeJsonObjReq();
                }
            }
        });
    }


    private void makeJsonObjReq() {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Username", get_email);
        postParam.put("Email", get_email);
        postParam.put("Password", get_pwd);
        postParam.put("FirstName", get_firstname);
        postParam.put("LastName", get_lastname);
        postParam.put("CompanyName", get_cmpnyname);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.REGISTER, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        if (serverstatus.equalsIgnoreCase("200")) {
                            try {
                                if (!response.getString("Status").isEmpty()) {
                                    JSONArray asignments_array = response.getJSONArray("Assignments");
                                    if (asignments_array.length() > 0) {
                                        if (asignments_array.length() == 1) {
                                            JSONObject jobj_first = asignments_array.getJSONObject(0);
                                            editor.putString("clientid", jobj_first.getString("ClientId"));
                                            editor.putString("ideatorid", jobj_first.getString("IdeatorId"));
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

                                }

                            } catch (Exception e) {
                            }
                        } else if (serverstatus.equalsIgnoreCase("400")) {
                            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
                        }
                        HandyObjects.stopProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
               // HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
                HandyObjects.stopProgressDialog();
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

