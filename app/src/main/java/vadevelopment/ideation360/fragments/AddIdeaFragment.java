package vadevelopment.ideation360.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.CameraActivity;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;
import vadevelopment.ideation360.adapter.Adapter_AddIdeaFirst;
import vadevelopment.ideation360.adapter.Adpater_AddIdeaSecond;
import vadevelopment.ideation360.database.ParseOpenHelper;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class AddIdeaFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "AddIdeaFragment";
    private HomeActivity homeactivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String serverstatus;
    private static final int RECORD_REQUEST = 2;
    private static final int REQUEST_CAMERA = 0;
    private static final int LOAD_FROMGALLERY = 1;
    private ArrayList<String> campaignid, ideationname_hashset, cname, campaignname_arraylist;
    private Spinner firstSpinner, secondSpinner;
    private TextView firstspin_maintext, secondspin_maintext;
    private ImageView addphoto_img, record_audio;
    static final int REQUEST_TAKE_PHOTO = 11111;
    private Context context;
    private Dialog recording_dialog;
    private TextView recordingtime;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private long startTime = 0L;
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private File recoded_file;
    private Button savedraft, postidea;
    private EditText et_ideatitle, et_descp;
    SQLiteDatabase database;
    private String selimage_path = "";
    private String recoded_filepath = "";
    private String get_ideatitle, get_ideadescrp, posted_ideaid, uploadphoto_status;
    private SavedIdeas_Skeleton saved_idea;
    boolean spinnerfill;
    ListView first_listview, second_listview;
    private Adapter_AddIdeaFirst adapter_first;
    private Adpater_AddIdeaSecond adpater_second;
    private View linesecond, viewtwo;
    private File imageFile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idea_add, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        context = getActivity();
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.addidea));
        homeactivity.homeicon.setImageResource(R.drawable.cross);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        //  firstSpinner = (Spinner) view.findViewById(R.id.firstSpinner);
        //  secondSpinner = (Spinner) view.findViewById(R.id.secondSpinner);
        savedraft = (Button) view.findViewById(R.id.savedraft);
        postidea = (Button) view.findViewById(R.id.postidea);
        firstspin_maintext = (TextView) view.findViewById(R.id.firstspin_maintext);
        secondspin_maintext = (TextView) view.findViewById(R.id.secondspin_maintext);
        addphoto_img = (ImageView) view.findViewById(R.id.addphoto_img);
        record_audio = (ImageView) view.findViewById(R.id.record_audio);
        et_ideatitle = (EditText) view.findViewById(R.id.et_ideatitle);
        et_descp = (EditText) view.findViewById(R.id.et_descp);
        linesecond = (View) view.findViewById(R.id.linesecond);
        viewtwo = (View) view.findViewById(R.id.viewtwo);
        database = ParseOpenHelper.getInstance(context).getWritableDatabase();

        first_listview = (ListView) view.findViewById(R.id.first_listview);
        second_listview = (ListView) view.findViewById(R.id.second_listview);

        //name = new ArrayList<>();
        // name_final = new ArrayList<>();
        // ideationname = new ArrayList<>();
        ideationname_hashset = new ArrayList<>();
        cname = new ArrayList<>();
        campaignid = new ArrayList<>();
        campaignname_arraylist = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        addphoto_img.setOnClickListener(this);
        record_audio.setOnClickListener(this);
        savedraft.setOnClickListener(this);
        postidea.setOnClickListener(this);
        adapter_first = new Adapter_AddIdeaFirst(getActivity(), ideationname_hashset);
        adpater_second = new Adpater_AddIdeaSecond(getActivity(), campaignname_arraylist);

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            getAllCampaigns();
        }

        firstspin_maintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_listview.setVisibility(View.VISIBLE);
                second_listview.setVisibility(View.INVISIBLE);
                secondspin_maintext.setVisibility(View.INVISIBLE);
                linesecond.setVisibility(View.INVISIBLE);
                et_ideatitle.setVisibility(View.VISIBLE);
                viewtwo.setVisibility(View.VISIBLE);
                et_ideatitle.setEnabled(false);
                //et_ideatitle.setHint("");
            }
        });

        secondspin_maintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_listview.setVisibility(View.INVISIBLE);
                second_listview.setVisibility(View.VISIBLE);
                et_ideatitle.setVisibility(View.INVISIBLE);
                viewtwo.setVisibility(View.INVISIBLE);
            }
        });
        first_listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        second_listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        first_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_ideatitle.setEnabled(true);
                linesecond.setVisibility(View.VISIBLE);
                first_listview.setVisibility(View.INVISIBLE);
                secondspin_maintext.setVisibility(View.VISIBLE);
                firstspin_maintext.setText(ideationname_hashset.get(i).toString());
                campaignname_arraylist.clear();
                Cursor cursor = database.query(ParseOpenHelper.TABLENAME_GETCAMPAIGN, null, ParseOpenHelper.IDEATION_NAME + "=?", new String[]{ideationname_hashset.get(i).toString()}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    campaignname_arraylist.add(cursor.getString(cursor.getColumnIndex("campaign_name")));
                    cursor.moveToNext();
                }
                cursor.close();
                secondspin_maintext.setText(campaignname_arraylist.get(0).toString());
                second_listview.setAdapter(adpater_second);
            }
        });

        second_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                second_listview.setVisibility(View.INVISIBLE);
                et_ideatitle.setVisibility(View.VISIBLE);
                viewtwo.setVisibility(View.VISIBLE);
                secondspin_maintext.setText(campaignname_arraylist.get(i).toString());
            }
        });

        if (getArguments() != null) {
            Log.e("withargument", "withargument");
            saved_idea = getArguments().getParcelable("saved_idea");
            et_ideatitle.setText(saved_idea.getIdea_name());
            et_descp.setText(saved_idea.getIdea_description());
            recoded_filepath = saved_idea.getAudio_path();

            if (recoded_filepath != null && !recoded_filepath.isEmpty()) {
                recoded_file = new File(recoded_filepath);
                record_audio.setImageResource(R.drawable.addvoice_gray);
            }
            if (saved_idea.getImage_path() != null && !saved_idea.getImage_path().isEmpty()) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setFullImageFromFilePath(saved_idea.getImage_path(), addphoto_img);
                    }
                }, 1000);

                imageFile = new File(saved_idea.getImage_path());
            }
        } else {
            Log.e("noargument", "noargument");
        }

    }

    private void getAllCampaigns() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        //  HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.MYCAMPAIGNS + "/" + preferences.getString("ideatorid", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());

                if (serverstatus.equalsIgnoreCase("200")) {
                    try {
                        database.delete("tablegetcampaign", null, null);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jobj_inside = response.getJSONObject(i);
                            cname.add(jobj_inside.getString("Name"));
                            campaignid.add(jobj_inside.getString("CampaignId"));
                            ideationname_hashset.add(jobj_inside.getString("IdeationName"));

                            ContentValues cv = new ContentValues();
                            cv.put("ideation_name", jobj_inside.getString("IdeationName"));
                            cv.put("campaign_name", jobj_inside.getString("Name"));
                            cv.put("campaign_id", jobj_inside.getString("CampaignId"));
                            long id = database.insert("tablegetcampaign", null, cv);
                        }
                        Set<String> hs = new HashSet<>();
                        hs.addAll(ideationname_hashset);
                        ideationname_hashset.clear();
                        ideationname_hashset.addAll(hs);
                        Collections.reverse(ideationname_hashset);

                        first_listview.setAdapter(adapter_first);
                        if (getArguments() != null) {
                            firstspin_maintext.setText(saved_idea.getIdeationsaved_name());
                            // secondspin_maintext.setText(saved_idea.getCampaign());
                        } else {
                            firstspin_maintext.setText(ideationname_hashset.get(0).toString());
                        }

                        Cursor cursor = database.query(ParseOpenHelper.TABLENAME_GETCAMPAIGN, null, ParseOpenHelper.IDEATION_NAME + "=?", new String[]{ideationname_hashset.get(0).toString()}, null, null, null);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            campaignname_arraylist.add(cursor.getString(cursor.getColumnIndex("campaign_name")));
                            cursor.moveToNext();
                        }
                        cursor.close();
                        if (getArguments() != null) {
                            secondspin_maintext.setText(saved_idea.getCampaign());
                        } else {
                            secondspin_maintext.setText(campaignname_arraylist.get(0).toString());
                        }
                        second_listview.setAdapter(adpater_second);
                    } catch (Exception e) {
                    }
                    HandyObjects.stopProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                serverstatus = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addphoto_img:
                check_RequestPermission();
                break;
            case R.id.record_audio:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                            10);
                } else {
                    recording_Dialog();
                }
                break;
            case R.id.savedraft:
                if (getArguments() != null) {
                    if (et_ideatitle.getText().toString().length() == 0) {
                        HandyObjects.showAlert(getActivity(), getActivity().getResources().getString(R.string.enterideatitle));
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put("ideatorid_ofidea", preferences.getString("ideatorid", ""));
                        cv.put("campaign", secondspin_maintext.getText().toString());
                        cv.put("ideationsaved_name", firstspin_maintext.getText().toString());
                        cv.put("idea_name", et_ideatitle.getText().toString());
                        cv.put("idea_description", et_descp.getText().toString());
                        cv.put("image_path", selimage_path);
                        cv.put("audio_path", recoded_filepath);
                        database.update(ParseOpenHelper.TABLE_NAME_SAVEDIDEA, cv,
                                ParseOpenHelper.IDEA_NAME + "=?",
                                new String[]{saved_idea.getIdea_name()});
                        SavedIdeasFragment sf = new SavedIdeasFragment();
                        homeactivity.replaceFragmentHome(sf);
                    }

                } else {
                    if (et_ideatitle.getText().toString().length() == 0) {
                        HandyObjects.showAlert(getActivity(), getActivity().getResources().getString(R.string.enterideatitle));
                    } else {
                        try {
                            ContentValues cv = new ContentValues();
                            cv.put("ideatorid_ofidea", preferences.getString("ideatorid", ""));
                            cv.put("campaign", secondspin_maintext.getText().toString());
                            cv.put("ideationsaved_name", firstspin_maintext.getText().toString());
                            cv.put("idea_name", et_ideatitle.getText().toString());
                            cv.put("idea_description", et_descp.getText().toString());
                            cv.put("image_path", selimage_path);
                            cv.put("audio_path", recoded_filepath);
                            long idd = database.insert("tablesavedidea", null, cv);
                        } catch (Exception e) {
                        }
                        SavedIdeasFragment sf = new SavedIdeasFragment();
                        homeactivity.replaceFragmentHome(sf);
                    }
                }


                break;

            case R.id.postidea:
                get_ideatitle = et_ideatitle.getText().toString();
                get_ideadescrp = et_descp.getText().toString();
                if (get_ideatitle.length() == 0) {
                    HandyObjects.showAlert(getActivity(), getActivity().getResources().getString(R.string.enterideatitle));
                } else if (!HandyObjects.isNetworkAvailable(getActivity())) {
                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                } else {
                    int position = cname.indexOf(secondspin_maintext.getText().toString());
                    String selcampaign_id = campaignid.get(position);
                    //  HandyObjects.showAlert(getActivity(), selcampaign_id);
                    AddIdeaTask(selcampaign_id);
                }
                break;
        }
    }

    private void recording_Dialog() {
        recording_dialog = new Dialog(getActivity());
        recording_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recording_dialog.setCanceledOnTouchOutside(false);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        recording_dialog.show();
        Window window = recording_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        window.setAttributes(wlp);


        recording_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View vview = inflater.inflate(R.layout.dialog_recording, null, false);
        recording_dialog.setContentView(vview);

        TextView start_recording = (TextView) recording_dialog.findViewById(R.id.start_recording);
        TextView stop_recording = (TextView) recording_dialog.findViewById(R.id.stop_recording);
        recordingtime = (TextView) recording_dialog.findViewById(R.id.recordingtime);
        //  txtMsg.setText(getActivity().getResources().getString(R.string.ordernotaccepted));
        Button btncancel = (Button) recording_dialog.findViewById(R.id.btncancel);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != recorder) {
                    try {
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);
                        //  rl_recordingtime.setVisibility(View.GONE);
                        stopRecording();
                    } catch (Exception e) {
                    }
                    //withoutupload_inflate();
                }
                recording_dialog.dismiss();
            }
        });

        start_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        stop_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                //  rl_recordingtime.setVisibility(View.GONE);
                stopRecording();
                recording_dialog.dismiss();
                //HandyObjects.showAlert(context, recoded_file.getAbsolutePath());

                if (recoded_file.getAbsolutePath() != null || !recoded_file.getAbsolutePath().isEmpty()) {
                    record_audio.setImageResource(R.drawable.addvoice_gray);
                }
            }
        });
        recording_dialog.show();
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilename() {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator + "Ideation_Recording");
        f.mkdir();
        // recoded_file = new File(f, "jhjkhdjkashd.mp3");
        recoded_file = new File(f, "recorded" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp3");
        //Toast.makeText(ChatActivity.this,recoded_file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        recoded_filepath = recoded_file.getAbsolutePath();
        return (recoded_file.getAbsolutePath());
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            // AppLog.logString("Error: " + what + ", " + extra);
            Toast.makeText(context, "Error:", Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            //  AppLog.logString("Warning: " + what + ", " + extra);
            Toast.makeText(context, "Warning:", Toast.LENGTH_SHORT).show();
        }
    };

    private void stopRecording() {
        if (null != recorder) {
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
            }
            //withoutupload_inflate();
        }
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (timeInMilliseconds / 1000);
            // timerwith_milisec.setText(String.valueOf(secs));
            int mins = secs / 60;
            secs = secs % 60;
            int hours = mins / 60;
            mins = mins % 60;
            //int milliseconds = (int) (updatedTime % 1000);
            //+ ":" + String.format("%03d", milliseconds)
            //    String timer = "" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
            String timer = "" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
            //set yout textview to the String timer here
            recordingtime.setText(timer);
            customHandler.postDelayed(this, 10);
        }

    };


    void check_RequestPermission() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    android.Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Permission is needed")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{android.Manifest.permission.CAMERA},
                                        REQUEST_CAMERA);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        }
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Permission is needed")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        2);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        } else {
            //  dialog_uploadimage();
            displayMediaPickerDialog();
        }
    }


    private void displayMediaPickerDialog() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        final Dialog mediaDialog = new Dialog(getActivity());
        mediaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mediaDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mediaDialog.setContentView(R.layout.dialog_media_picker);
        LinearLayout approx_lay = (LinearLayout) mediaDialog.findViewById(R.id.approx_lay);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w - 30, (h / 3) - 20);
        approx_lay.setLayoutParams(params);

        TextView options_camera = (TextView) mediaDialog.findViewById(R.id.options_camera);
        options_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
                dispatchTakePictureIntent();
            }
        });
        TextView options_gallery = (TextView) mediaDialog.findViewById(R.id.options_gallery);
        options_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent_gallery, LOAD_FROMGALLERY);
                mediaDialog.dismiss();
            }
        });
        TextView options_cancel = (TextView) mediaDialog.findViewById(R.id.options_cancel);
        options_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
            }
        });
        mediaDialog.show();
    }

    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity) getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(context, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected File createImageFile() throws IOException {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator + "Ideation_AddIdeaImage");
        f.mkdir();
         imageFile = new File(f, "addidea" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity) getActivity();
        activity.setCurrentPhotoPath("file:" + imageFile.getAbsolutePath());
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_FROMGALLERY && data != null) {
            try {
               final Uri selectedImageUri = data.getData();
             /*   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                addphoto_img.setImageBitmap(bitmap);*/

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            addphoto_img.setImageBitmap(bitmap);
                            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator + "Ideation_AddIdeaImage");
                            f.mkdir();
                            imageFile = new File(f, "addidea" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(imageFile);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                            selimage_path = imageFile.getAbsolutePath();
                        } catch (Exception e){}

                    }
                });
               /* CameraActivity activity = (CameraActivity) getActivity();
                activity.setCurrentPhotoPath(imageFile.getAbsolutePath());
                setFullImageFromFilePath(activity.getCurrentPhotoPath(), addphoto_img);*/
            } catch (Exception e) {
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            CameraActivity activity = (CameraActivity) getActivity();
            setFullImageFromFilePath(activity.getCurrentPhotoPath(), addphoto_img);
        } else if (resultCode == getActivity().RESULT_OK && requestCode == RECORD_REQUEST) {
            Uri audioFileUri = data.getData();
            Log.e("ccv", "cfcf");
            // playRecording.setEnabled(true);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        selimage_path = imagePath;
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // HandyObjects.showAlert(getActivity(), imagePath);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] dataa = bos.toByteArray();
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    displayMediaPickerDialog();

                } else {
                    // check_RequestPermission();
                    // Toast.makeText(this, "This permission is neccesary", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    displayMediaPickerDialog();

                } else {
                    // check_RequestPermission();
                    //  Toast.makeText(this, "This permission is neccesary", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 10: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recording_Dialog();
                } else {
                    //User denied Permission.
                }
            }
        }
    }


    private void AddIdeaTask(String selcampid) {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        // get_ideatitle = et_ideatitle.getText().toString();
        //  get_ideadescrp
        postParam.put("CampaignId", selcampid);
        postParam.put("IdeatorId", preferences.getString("ideatorid", ""));
        postParam.put("Title", get_ideatitle);
        postParam.put("Description", get_ideadescrp);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.ADDIDEA, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d(TAG, res.toString());
                        try {
                            final JSONObject response = new JSONObject(res.toString());
                            if (serverstatus.equalsIgnoreCase("200")) {
                                try {
                                    if (getArguments() != null) {
                                        database.delete(ParseOpenHelper.TABLE_NAME_SAVEDIDEA, ParseOpenHelper.IDEA_NAME + "=?", new String[]{saved_idea.getIdea_name()});
                                    }
                                    posted_ideaid = response.getString("IdeaId");
                                    if(imageFile != null){
                                        new Uploadphoto_Task().execute();
                                    }
                                    else if(recoded_file != null){
                                        new UploadRecording_Task().execute();
                                    }


                                } catch (Exception e) {
                                }
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
                            }
                            // HandyObjects.stopProgressDialog();
                        } catch (Exception e) {
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
                HandyObjects.stopProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    class Uploadphoto_Task extends AsyncTask<String, Void, String> {
        String Response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  HandyObjects.startProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                HttpClient httpclient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httppost = new HttpPost(HandyObjects.UPDATE_MEDIA + posted_ideaid + "/" + preferences.getString("ideatorid", "") + "/1");
                httppost.setHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                FileBody bin = new FileBody(imageFile);
                entity.addPart("file", bin);
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost,
                        localContext);
                HttpEntity resEntity = response.getEntity();
                String Response = "";
                Response = EntityUtils.toString(resEntity);
                uploadphoto_status = String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Uploadimage response", Response);
                return Response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (uploadphoto_status.equalsIgnoreCase("200")) {


                    if (recoded_filepath != null && !recoded_filepath.isEmpty()) {
                        new UploadRecording_Task().execute();
                    } else {
                       // HandyObjects.showAlert(context, context.getResources().getString(R.string.pimage_uploadsucc));
                        HandyObjects.stopProgressDialog();
                        HomeFragment hf = new HomeFragment();
                        homeactivity.replaceFragmentHome(hf);
                    }
                } else {
                    HandyObjects.showAlert(context, context.getResources().getString(R.string.servererror));
                    HandyObjects.stopProgressDialog();
                }
            } catch (Exception e) {
            }
        }
    }

    class UploadRecording_Task extends AsyncTask<String, Void, String> {
        String Response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  HandyObjects.startProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                HttpClient httpclient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httppost = new HttpPost(HandyObjects.UPDATE_MEDIA + posted_ideaid + "/" + preferences.getString("ideatorid", "") + "/2");
                httppost.setHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                FileBody bin = new FileBody(recoded_file);
                entity.addPart("file", bin);
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost,
                        localContext);
                HttpEntity resEntity = response.getEntity();
                String Response = "";
                Response = EntityUtils.toString(resEntity);
                uploadphoto_status = String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Uploadimage response", Response);
                return Response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (uploadphoto_status.equalsIgnoreCase("200")) {
                  //  HandyObjects.showAlert(context, "recording success");
                    HandyObjects.stopProgressDialog();
                    HomeFragment hf = new HomeFragment();
                    homeactivity.replaceFragmentHome(hf);
                } else {
                    HandyObjects.showAlert(context, context.getResources().getString(R.string.servererror));
                    HandyObjects.stopProgressDialog();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        spinnerfill = false;
    }
}
