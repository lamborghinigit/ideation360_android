package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/24/2017.
 */

public class UpdateIdea_Fragment extends Fragment {

    private static String TAG = "UpdateIdea_Fragment";
    private HomeActivity homeactivity;
    private TextView firstspin_maintext, secondspin_maintext;
    private EditText et_descp, et_ideatitle;
    private Button updateidea;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String campaign_name, ideation_name, idea_title, idea_discrp;
    private String serverstatus, idea_id, ideator_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_updateidea, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.editidea));
        homeactivity.homeicon.setImageResource(R.drawable.cross);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        firstspin_maintext = (TextView) view.findViewById(R.id.firstspin_maintext);
        secondspin_maintext = (TextView) view.findViewById(R.id.secondspin_maintext);
        et_ideatitle = (EditText) view.findViewById(R.id.et_ideatitle);
        et_descp = (EditText) view.findViewById(R.id.et_descp);
        updateidea = (Button) view.findViewById(R.id.updateidea);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        if (getArguments() != null) {
            idea_id = getArguments().getString("idea_id");
            ideator_id = getArguments().getString("ideator_id");
            ideation_name = getArguments().getString("ideation_name");
            campaign_name = getArguments().getString("campaign_name");
            idea_title = getArguments().getString("idea_title");
            idea_discrp = getArguments().getString("idea_discrp");
            firstspin_maintext.setText(ideation_name);
            secondspin_maintext.setText(campaign_name);
            et_ideatitle.setText(idea_title);
            et_descp.setText(idea_discrp);
        }


        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        updateidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HandyObjects.isNetworkAvailable(getActivity())) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                } else {
                    UpdateIdea();
                }
            }
        });
    }

    private void UpdateIdea() {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("IdeaId", idea_id);
        postParam.put("IdeatorId", ideator_id);
        postParam.put("Title", idea_title);
        postParam.put("Description", idea_discrp);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.UPDATEIDEA, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.e("submitcomment", res.toString());
                        try {
                            if (serverstatus.equalsIgnoreCase("200")) {
                                getActivity().getSupportFragmentManager().popBackStack();
                                HandyObjects.showAlert(getActivity(), res.getString("Status"));
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                HandyObjects.showAlert(getActivity(), "Badrequest");
                            }
                            HandyObjects.stopProgressDialog();
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.stopProgressDialog();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
