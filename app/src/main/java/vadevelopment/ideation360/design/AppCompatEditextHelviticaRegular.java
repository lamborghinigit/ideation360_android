package vadevelopment.ideation360.design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by india on 6/12/2017.
 */

public class AppCompatEditextHelviticaRegular extends AppCompatEditText {
    public AppCompatEditextHelviticaRegular(Context context) {
        super(context);
        setCustomFont(context);
    }

    public AppCompatEditextHelviticaRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public AppCompatEditextHelviticaRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }



    public void setCustomFont(Context context){
        Typeface face = Typeface.createFromAsset(context.getAssets(), "helvetica_normal.ttf");
        setTypeface(face);
    }

}
