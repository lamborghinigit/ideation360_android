package vadevelopment.ideation360.design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by india on 6/12/2017.
 */

public class HelviticaBold extends AppCompatTextView {

    public HelviticaBold(Context context) {
        super(context);
        setCustomFont(context);
    }

    public HelviticaBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public HelviticaBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }


    public void setCustomFont(Context context){
        Typeface face = Typeface.createFromAsset(context.getAssets(), "helvetica_bold.ttf");
        setTypeface(face);
    }


}