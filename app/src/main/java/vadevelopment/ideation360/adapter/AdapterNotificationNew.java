package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.Skeleton.NotificationNew_Skeleton;
import vadevelopment.ideation360.fragments.IdeaDeatilFragment;

/**
 * Created by vibrantappz on 6/24/2017.
 */

public class AdapterNotificationNew extends RecyclerView.Adapter<AdapterNotificationNew.ViewHolder> {

    private Context context;
    private ArrayList<NotificationNew_Skeleton> arraylist;
    private HomeActivity homeActivity;

    public AdapterNotificationNew(Context context, ArrayList<NotificationNew_Skeleton> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        homeActivity = ((HomeActivity) context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterNotificationNew.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rownotif_new, parent, false);
        AdapterNotificationNew.ViewHolder viewHolder = new AdapterNotificationNew.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterNotificationNew.ViewHolder viewHolder, final int position) {

        if (arraylist.get(position).getActivityType().equalsIgnoreCase("ADDCOMMENT") || arraylist.get(position).getActivityType().equalsIgnoreCase("addcomment")) {
            viewHolder.discription.setText(arraylist.get(position).getIdeatorName() + " " + context.getResources().getString(R.string.hascommented) + " " + "'" + arraylist.get(position).getIdeaTitle() + "'");
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("RATEDIDEA") || arraylist.get(position).getActivityType().equalsIgnoreCase("ratedidea")) {
            viewHolder.discription.setText(arraylist.get(position).getIdeatorName() + " " + " " + context.getResources().getString(R.string.gaveyouridea) + "'" + arraylist.get(position).getIdeaTitle() + "'" + " " + context.getResources().getString(R.string.stars));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("IDEACLUSTERED") || arraylist.get(position).getActivityType().equalsIgnoreCase("IdeaClustered")) {
            viewHolder.discription.setText(context.getResources().getString(R.string.YourIdea) + " '" + arraylist.get(position).getIdeaTitle() + "," + " " + context.getResources().getString(R.string.wasaddedtothecluster));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("INEXPERIMENT") || arraylist.get(position).getActivityType().equalsIgnoreCase("InExperiment")) {
            viewHolder.discription.setText(context.getResources().getString(R.string.YourIdea) + " '" + arraylist.get(position).getIdeaTitle() + "" + context.getResources().getString(R.string.isinanexperiment));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("SUCCESSFULEXPERIMENT") || arraylist.get(position).getActivityType().equalsIgnoreCase("SuccessfulExperiment")) {
            viewHolder.discription.setText(context.getResources().getString(R.string.YourIdea) + " '" + arraylist.get(position).getIdeaTitle() + ", " + context.getResources().getString(R.string.wasinanexperiment));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("UNSUCCESSFULEXPERIMENT") || arraylist.get(position).getActivityType().equalsIgnoreCase("UnSuccessfulExperiment")) {
            viewHolder.discription.setText(context.getResources().getString(R.string.YourIdea) + " '" + arraylist.get(position).getIdeaTitle() + "' " + context.getResources().getString(R.string.wasinanexperiment));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("INPROJECT") || arraylist.get(position).getActivityType().equalsIgnoreCase("InProject")) {
            viewHolder.discription.setText(context.getResources().getString(R.string.CongratsyourIdea) + "'" + arraylist.get(position).getIdeaTitle() + ", " + context.getResources().getString(R.string.isnowpartofaproject));
        } else if (arraylist.get(position).getActivityType().equalsIgnoreCase("INFREEZEBOX") || arraylist.get(position).getActivityType().equalsIgnoreCase("InFreezeBox")) {
            viewHolder.discription.setText("'" + arraylist.get(position).getIdeaTitle() + "' " + context.getResources().getString(R.string.freezebox));
        }

        String[] d = arraylist.get(position).getActivityDate().split("T");
        String[] datesplit = d[0].split("-");
        viewHolder.date.setText(datesplit[0] + "." + datesplit[1] + "." + datesplit[2]);

        viewHolder.ll_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arraylist.get(position).getActivityType().equalsIgnoreCase("ADDCOMMENT")) {
                    IdeaDeatilFragment ideadetail_frg = new IdeaDeatilFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "notification");
                    bundle.putString("IdeaCommentId", arraylist.get(position).getIdeaCommentId());
                    bundle.putString("ideaid", arraylist.get(position).getIdeaId());
                    bundle.putString("ideatorid", arraylist.get(position).getIdeatorId());
                    ideadetail_frg.setArguments(bundle);
                    homeActivity.replaceFragmentHome(ideadetail_frg);
                } else {
                    IdeaDeatilFragment ideadetail_frg = new IdeaDeatilFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "notif");
                    bundle.putString("ideaid", arraylist.get(position).getIdeaId());
                    bundle.putString("ideatorid", arraylist.get(position).getIdeatorId());
                    ideadetail_frg.setArguments(bundle);
                    homeActivity.replaceFragmentHome(ideadetail_frg);
                }
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView discription, date;
        public LinearLayout ll_outer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            discription = (TextView) itemLayoutView.findViewById(R.id.discription);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            ll_outer = (LinearLayout) itemLayoutView.findViewById(R.id.ll_outer);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}


