package vadevelopment.ideation360.design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by india on 6/12/2017.
 */

public class AppCompatButtonHelviticaBold extends AppCompatButton {

    public AppCompatButtonHelviticaBold(Context context) {
        super(context);
        setCustomFont(context);
    }

    public AppCompatButtonHelviticaBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public AppCompatButtonHelviticaBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }




    public void setCustomFont(Context context){
        Typeface face = Typeface.createFromAsset(context.getAssets(), "helvetica_bold.ttf");
        setTypeface(face);
    }
    
}
