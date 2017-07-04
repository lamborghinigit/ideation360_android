package vadevelopment.ideation360.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.CameraActivity;
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
    private static final int REQUEST_CAMERA = 0;
    private static final int LOAD_FROMGALLERY = 1;
    static final int REQUEST_TAKE_PHOTO = 11111;
    private ImageView addphoto_img;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String campaign_name, ideation_name, idea_title, idea_discrp;
    private String serverstatus,uploadphoto_status, idea_id, ideator_id;
    private Context context;
    private File imageFile;
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_updateidea, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        context = getActivity();
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.editidea));
        homeactivity.homeicon.setImageResource(R.drawable.cross);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        handler = new Handler();
        firstspin_maintext = (TextView) view.findViewById(R.id.firstspin_maintext);
        secondspin_maintext = (TextView) view.findViewById(R.id.secondspin_maintext);
        et_ideatitle = (EditText) view.findViewById(R.id.et_ideatitle);
        et_descp = (EditText) view.findViewById(R.id.et_descp);
        updateidea = (Button) view.findViewById(R.id.updateidea);
        addphoto_img = (ImageView) view.findViewById(R.id.addphoto_img);
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
            if (!getArguments().getString("idea_imgurl").isEmpty() && getArguments().getString("idea_imgurl") != null) {
                LazyHeaders.Builder builder = new LazyHeaders.Builder()
                        .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                GlideUrl glideUrl = new GlideUrl(getArguments().getString("idea_imgurl"), builder.build());
                Glide.with(getActivity()).load(glideUrl).into(addphoto_img);
            }
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

        addphoto_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_RequestPermission();
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
                                if (imageFile != null) {
                                    new Uploadphoto_Task().execute();
                                }
                                else {
                                    HandyObjects.showAlert(getActivity(), res.getString("Status"));
                                    HandyObjects.stopProgressDialog();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }

                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                HandyObjects.stopProgressDialog();
                                HandyObjects.showAlert(getActivity(), "Badrequest");
                            }

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
                // File photoFile = createImageFile();
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                addphoto_img.setImageBitmap(bitmap);

                saveimage(bitmap);


            } catch (Exception e) {
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            CameraActivity activity = (CameraActivity) getActivity();
            setFullImageFromFilePath(activity.getCurrentPhotoPath(), addphoto_img);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void saveimage(Bitmap bitmap) {
        try {
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
        } catch (Exception e) {
        }
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
        }
    }

    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View

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
                HttpPost httppost = new HttpPost(HandyObjects.UPDATE_MEDIA + getArguments().getString("idea_id") + "/" + preferences.getString("ideatorid", "") + "/1");
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
                        HandyObjects.stopProgressDialog();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    HandyObjects.showAlert(context, context.getResources().getString(R.string.servererror));
                    HandyObjects.stopProgressDialog();
                }
            } catch (Exception e) {
            }
        }
    }

}

