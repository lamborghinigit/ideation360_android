package vadevelopment.ideation360;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vibrantappz on 6/15/2017.
 */

public class HandyObjects {

    public static final String BASE_URL = "https://app.ideation360.com/api/";
    public static final String REGISTER = BASE_URL + "register";
    public static final String LOGIN = BASE_URL + "account";
    public static ProgressDialog progressDialog;

    public static void startProgressDialog(Context con) {
        progressDialog = new ProgressDialog(con);
        progressDialog.setMessage("Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        if (progressDialog != null) {
            try {
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showAlert(final Context ctx, final String text) {
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
                // Call toast.xml file for toast layout
                View toastRoot = inflater.inflate(R.layout.custom_toast, null);
                TextView txtMessage = (TextView) toastRoot.findViewById(R.id.txtMessage);
                txtMessage.setText(text);
                Toast toast = new Toast(ctx);
                toast.setView(toastRoot);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                        0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
