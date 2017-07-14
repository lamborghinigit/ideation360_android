package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.Skeleton.People_Skeleton;
import vadevelopment.ideation360.Skeleton.Ratings_Skeleton;
import vadevelopment.ideation360.adapter.AdapterComment;
import vadevelopment.ideation360.adapter.AdapterHome;


/**
 * Created by vibrantappz on 6/22/2017.
 */

public class IdeaDeatilFragment extends Fragment {

    private static String TAG = "IdeaDeatilFragment";
    private HomeActivity homeactivity;
    private TextView text_ideatitle, text_firstcampaign, text_firstideation, text_description, noof_rating, username, useremail, date;
    private String ideatorid, ideaid, serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RatingBar ratingBarbig, ratingBarsmall;
    private ArrayList<Comments_Skeleton> comments_arraylist;
    private ArrayList<String> commnt_list;
    private RecyclerView comments_recyclerview;
    private AdapterComment adapter;
    public static EditText et_comment;
    public static NestedScrollView scrollview;
    ImageView image, commentedimage, fillimage;
    private Handler handler;
    MediaPlayer mediaPlayer;
    LinearLayout ll_playrecording;
    String imageurl = "";
    File recoded_file;
    private String audio_url = "";
    File file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_ideadetail, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();

        homeactivity.hometoptext.setText(getResources().getString(R.string.idea));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        homeactivity.settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        handler = new Handler();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        scrollview = (NestedScrollView) view.findViewById(R.id.scrollview);
        text_ideatitle = (TextView) view.findViewById(R.id.text_ideatitle);
        text_firstcampaign = (TextView) view.findViewById(R.id.text_firstcampaign);
        text_firstideation = (TextView) view.findViewById(R.id.text_firstideation);
        text_description = (TextView) view.findViewById(R.id.text_description);
        noof_rating = (TextView) view.findViewById(R.id.noof_rating);
        username = (TextView) view.findViewById(R.id.username);
        useremail = (TextView) view.findViewById(R.id.useremail);
        date = (TextView) view.findViewById(R.id.date);
        image = (ImageView) view.findViewById(R.id.image);
        fillimage = (ImageView) view.findViewById(R.id.fillimage);
        commentedimage = (ImageView) view.findViewById(R.id.commentedimage);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        ratingBarbig = (RatingBar) view.findViewById(R.id.ratingBarbig);
        ratingBarsmall = (RatingBar) view.findViewById(R.id.ratingBarsmall);
        ll_playrecording = (LinearLayout) view.findViewById(R.id.ll_playrecording);

        comments_recyclerview = (RecyclerView) view.findViewById(R.id.comments_recyclerview);
        comments_recyclerview.setVisibility(View.VISIBLE);
        comments_recyclerview.setHasFixedSize(false);
        comments_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        // mLayoutManager.scrollToPosition(5);

        comments_recyclerview.setLayoutManager(mLayoutManager);
        comments_recyclerview.setItemAnimator(new DefaultItemAnimator());
        comments_recyclerview.setNestedScrollingEnabled(false);

        if (getArguments() != null) {
            ideaid = getArguments().getString("ideaid");
            ideatorid = getArguments().getString("ideatorid");
            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
            GlideUrl glideUrl_comm = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" + preferences.getString("ideatorid", ""), builder.build());
            GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" + ideatorid, builder.build());
            Glide.with(getActivity()).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(image);
            Glide.with(getActivity()).load(glideUrl_comm).diskCacheStrategy(DiskCacheStrategy.NONE).into(commentedimage);
            if (!HandyObjects.isNetworkAvailable(getActivity())) {
                HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
            } else {
                IdeaDetail_Task(ideaid, ideatorid, getArguments().getString("date"));
            }
        }
        ratingBarbig.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                RateIdea(ideaid, preferences.getString("ideatorid", ""), String.valueOf(Math.round(v)));
            }
        });

        et_comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_comment.setRawInputType(InputType.TYPE_CLASS_TEXT);

        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!HandyObjects.isNetworkAvailable(getActivity())) {
                        HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                    } else {
                        SubmitComment(et_comment.getText().toString(), ideaid, ideatorid, getArguments().getString("date"));
                    }
                }
                return false;
            }
        });

        homeactivity.settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateIdea_Fragment update_frgm = new UpdateIdea_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("idea_id", ideaid);
                bundle.putString("ideator_id", ideatorid);
                bundle.putString("ideation_name", text_firstideation.getText().toString());
                bundle.putString("campaign_name", text_firstcampaign.getText().toString());
                bundle.putString("idea_title", text_ideatitle.getText().toString());
                bundle.putString("idea_discrp", text_description.getText().toString());
                bundle.putString("idea_imgurl", imageurl);
                bundle.putString("audio_url", audio_url);
                update_frgm.setArguments(bundle);
                homeactivity.replaceFragmentHome(update_frgm);
            }
        });

        ll_playrecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri fileUri = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider", recoded_file);
                intent.setDataAndType(fileUri, "audio/mp3");
                startActivity(intent);
            }
        });

        noof_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ratings_Fragment rating_frgm = new Ratings_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("ideaid", ideaid);
                bundle.putString("ideatorid", ideatorid);
                rating_frgm.setArguments(bundle);
                homeactivity.replaceFragmentHome(rating_frgm);
            }
        });


    }

    private void IdeaDetail_Task(String ideaid, final String ideatorid, String datee) {
        date.setText(datee);
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        final JsonObjectRequest req = new JsonObjectRequest(HandyObjects.IDEADETAIL + "/" + ideaid + "/" + ideatorid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Idea detail", response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        text_ideatitle.setText(response.getString("Title"));
                        text_firstcampaign.setText(response.getString("CampaignTitle"));
                        text_firstideation.setText(response.getString("IdeationName"));
                        text_description.setText(response.getString("Description"));
                        ratingBarbig.setRating(Float.parseFloat(response.getString("RatingValueByCurrentIdeator")));
                        ratingBarsmall.setRating(Float.parseFloat(response.getString("RatingMeanValue")));
                        noof_rating.setText(response.getString("NrOfRatings"));
                        if (response.getString("IsEditable").equalsIgnoreCase("true")) {
                            homeactivity.settingicon.setVisibility(View.VISIBLE);
                            homeactivity.settingicon.setImageResource(R.drawable.editicon);
                        }
                        JSONArray jarry_media = response.getJSONArray("Media");
                        if (jarry_media.length() == 0) {
                            fillimage.setVisibility(View.GONE);
                            ll_playrecording.setVisibility(View.GONE);
                        } else if (jarry_media.length() == 1) {
                            if (jarry_media.getJSONObject(0).getString("MediaType").equalsIgnoreCase("Photo")) {
                                fillimage.setVisibility(View.VISIBLE);
                                LazyHeaders.Builder builder = new LazyHeaders.Builder()
                                        .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                                GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(0).getString("IdeaMediaId"), builder.build());
                                Glide.with(getActivity()).load(glideUrl).into(fillimage);
                                imageurl = "https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(0).getString("IdeaMediaId");
                            } else if (jarry_media.getJSONObject(0).getString("MediaType").equalsIgnoreCase("VoiceMemo")) {
                                audio_url = "https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(0).getString("IdeaMediaId");

                                if (checkFile(jarry_media.getJSONObject(0).getString("IdeaMediaId")).exists()) {
                                    recoded_file = checkFile(jarry_media.getJSONObject(0).getString("IdeaMediaId"));
                                    ll_playrecording.setVisibility(View.VISIBLE);
                                } else {
                                    new DownloadAudio().execute(jarry_media.getJSONObject(0).getString("IdeaMediaId"));
                                }
                            }
                        } else if (jarry_media.length() == 2) {
                            if (jarry_media.getJSONObject(0).getString("MediaType").equalsIgnoreCase("Photo")) {
                                fillimage.setVisibility(View.VISIBLE);
                                LazyHeaders.Builder builder = new LazyHeaders.Builder()
                                        .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                                GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(0).getString("IdeaMediaId"), builder.build());
                                Glide.with(getActivity()).load(glideUrl).into(fillimage);
                                imageurl = "https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(0).getString("IdeaMediaId");
                            }
                            if (jarry_media.getJSONObject(1).getString("MediaType").equalsIgnoreCase("VoiceMemo")) {
                                audio_url = "https://app.ideation360.com/api/getmedia/" + response.getString("IdeaId") + "/" + jarry_media.getJSONObject(1).getString("IdeaMediaId");
                                if (checkFile(jarry_media.getJSONObject(1).getString("IdeaMediaId")).exists()) {
                                    recoded_file = checkFile(jarry_media.getJSONObject(1).getString("IdeaMediaId"));
                                    ll_playrecording.setVisibility(View.VISIBLE);
                                } else {
                                    new DownloadAudio().execute(jarry_media.getJSONObject(1).getString("IdeaMediaId"));
                                }
                            }
                        }

                        JSONArray jarry = response.getJSONArray("Comments");
                        comments_arraylist = new ArrayList<>();
                        commnt_list = new ArrayList<>();
                        comments_arraylist.clear();
                        commnt_list.clear();
                        for (int i = 0; i < jarry.length(); i++) {
                            Comments_Skeleton sske = new Comments_Skeleton();
                            sske.setComment_id(jarry.getJSONObject(i).getString("IdeaCommentId"));
                            sske.setIdeator_name(jarry.getJSONObject(i).getString("IdeatorName"));
                            sske.setImage("https://app.ideation360.com/api/getprofileimage/" + jarry.getJSONObject(i).getString("IdeatorId"));
                            sske.setComment(jarry.getJSONObject(i).getString("Comment"));
                            comments_arraylist.add(sske);
                            commnt_list.add(jarry.getJSONObject(i).getString("IdeaCommentId"));
                        }
                        Collections.reverse(comments_arraylist);
                        Collections.reverse(commnt_list);
                        if (getActivity() != null && getArguments().getString("from").equalsIgnoreCase("notification")) {
                            int index = commnt_list.indexOf(getArguments().getString("IdeaCommentId"));
                            adapter = new AdapterComment(getActivity(), comments_arraylist, index, "notification");
                        } else {
                            adapter = new AdapterComment(getActivity(), comments_arraylist, 0, "normal");
                        }
                        comments_recyclerview.setAdapter(adapter);
                        getprofile(ideatorid, response);
                    }
                } catch (Exception e) {
                }
                HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private File checkFile(String mediaid) {
       File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator + "Ideation");
        f.mkdir();
        File file = new File(f, mediaid + ".mp3");
       /* try {
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            file = File.createTempFile(mediaid, ".mp3", storageDir);
        } catch (Exception e) {
        }*/
        return file;
    }

    private void getprofile(final String ideatorid, final JSONObject response_ratings) {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        //   HandyObjects.startProgressDialog(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.GETPROFILE + "/" + ideatorid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("getprofile", response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        username.setText(response.getString("FirstName") + " " + response.getString("LastName"));
                        useremail.setText(response.getString("Email"));
                       /* JSONArray jarry = response_ratings.getJSONArray("Ratings");
                        ratings_arraylist = new ArrayList<>();
                        for (int i = 0; i < jarry.length(); i++) {
                            Ratings_Skeleton rske = new Ratings_Skeleton();
                            rske.setName(jarry.getJSONObject(i).getString("IdeatorName"));
                            rske.setIdeatorid(jarry.getJSONObject(i).getString("IdeatorId"));
                            rske.setValue(jarry.getJSONObject(i).getString("Value"));
                            ratings_arraylist.add(rske);
                        }*/
                    }
                } catch (Exception e) {
                }
                //HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    class DownloadAudio extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(audio_url);
                get.setHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                playMp3(is, strings[0]);
            } catch (Exception e) {
            }
            return null;
        }
    }


    private void playMp3(InputStream is, String finalname) {
        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator + "Ideation");
            f.mkdir();
            recoded_file = new File(f, finalname + ".mp3");
           /* try {
                File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                recoded_file = File.createTempFile(finalname, ".mp3", storageDir);
            } catch (Exception e) {
            }*/
            FileOutputStream output = new FileOutputStream(recoded_file.getAbsolutePath());
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.close();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_playrecording.setVisibility(View.VISIBLE);
            }
        });

    }


    private void SubmitComment(String gettext, final String ideaid, final String ideatorid, final String date) {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("IdeaId", ideaid);
        postParam.put("IdeatorId", preferences.getString("ideatorid", ""));
        postParam.put("Comment", gettext);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.ADDCOMMENT, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.e("submitcomment", res.toString());
                        try {
                            if (serverstatus.equalsIgnoreCase("200")) {
                                et_comment.setText("");
                                HandyObjects.stopProgressDialog();
                                if (res.getString("Status").equalsIgnoreCase("COMMENT_ADDED")) {
                                    HandyObjects.showAlert(getActivity(), getActivity().getResources().getString(R.string.cmntadded));
                                    IdeaDetail_Task(ideaid, ideatorid, date);
                                }
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                HandyObjects.stopProgressDialog();
                                HandyObjects.showAlert(getActivity(), "Error 400");
                            }

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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

    private void RateIdea(final String ideaid, final String ideatorid, String rated_value) {
        HandyObjects.stopProgressDialog();
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("IdeaId", ideaid);
        postParam.put("IdeatorId", ideatorid);
        postParam.put("Value", rated_value);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.RATE_IDEA, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.e("submitcomment", res.toString());
                        try {
                            if (serverstatus.equalsIgnoreCase("200")) {
                                ratingBarbig.setRating(Float.parseFloat(res.getString("RatingValueByCurrentIdeator")));
                                ratingBarsmall.setRating(Float.parseFloat(res.getString("AverageRating")));
                                noof_rating.setText(res.getString("NrOfRatings"));
                                HandyObjects.stopProgressDialog();
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                HandyObjects.stopProgressDialog();
                                HandyObjects.showAlert(getActivity(), "Error 400");
                            }

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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