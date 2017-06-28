package vadevelopment.ideation360.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.fragments.IdeaDeatilFragment;

/**
 * Created by vibrantappz on 6/22/2017.
 */

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {

    private Context context;
    private ArrayList<Comments_Skeleton> arraylist;
    Handler handler, firsthandler, secondhandler, thirddhandler, fourthhandler, fifthhandler, sixhandler;
    int sel_index;
    String type;
    int height;
    private Timer timer;


    public AdapterComment(Context context, ArrayList<Comments_Skeleton> arraylist, int sel_index, String type) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        // int width = displayMetrics.widthPixels;
        this.context = context;
        this.arraylist = arraylist;
        this.sel_index = sel_index;
        this.type = type;
        handler = new Handler();
        firsthandler = new Handler();
        secondhandler = new Handler();
        thirddhandler = new Handler();
        fourthhandler = new Handler();
        fifthhandler = new Handler();
        sixhandler = new Handler();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowcomment, parent, false);
        AdapterComment.ViewHolder viewHolder = new AdapterComment.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterComment.ViewHolder viewHolder, final int position) {

        viewHolder.name.setText(arraylist.get(position).getIdeator_name());
        viewHolder.comment.setText(arraylist.get(position).getComment());
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
        GlideUrl glideUrl = new GlideUrl(arraylist.get(position).getImage(), builder.build());
        Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageview);

        if (type.equalsIgnoreCase("notification")) {
            if (position == sel_index) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] locationOnScreen = new int[2];
                        viewHolder.name.getLocationOnScreen(locationOnScreen);
                        // HandyObjects.showAlert(context, String.valueOf(height));
                        viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fbe755"));
                        IdeaDeatilFragment.scrollview.scrollTo(0, locationOnScreen[1] - (height - 110));
                        firsthandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fcec7a"));
                                secondhandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fdf2a1"));
                                        thirddhandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fdf4b4"));
                                                fourthhandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fef7b7"));
                                                        fifthhandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                viewHolder.ll_outer.setBackgroundColor(Color.parseColor("#fefada"));
                                                                sixhandler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        viewHolder.ll_outer.setBackgroundColor(Color.TRANSPARENT);
                                                                    }
                                                                }, 60);
                                                            }
                                                        }, 60);
                                                    }
                                                }, 50);
                                            }
                                        }, 50);
                                    }
                                }, 50);
                            }
                        }, 50);

                    }
                }, 10);
            }
        }

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, comment;
        private ImageView imageview;
        LinearLayout ll_outer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ll_outer = (LinearLayout) itemLayoutView.findViewById(R.id.ll_outer);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            comment = (TextView) itemLayoutView.findViewById(R.id.comment);
            imageview = (ImageView) itemLayoutView.findViewById(R.id.imageview);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}

