package vadevelopment.ideation360.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by online computers on 11/19/2015.
 */
public class ParseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "IDEATION_360";
    public static final int VERSION = 1;
    private static ParseOpenHelper mInstance = null;
    public static final String TABLE_NAME_ASSIGNMENTS = "assignments";
    public static final String COMPANY_NAME = "company_name";
    public static final String CLIENT_ID = "client_id";
    public static final String IDEATOR_ID = "ideator_id";

    public static final String TABLE_NAME_SAVEDIDEA = "tablesavedidea";
    public static final String IDEATORID_OFIDEA = "ideatorid_ofidea";
    public static final String CAMPAIGN = "campaign";
    public static final String IDEATIONSAVED_NAME = "ideationsaved_name";
    public static final String IDEA_NAME = "idea_name";
    public static final String IDEA_DESCRIPTION = "idea_description";
    public static final String IMAGEFILE_PATH = "image_path";
    public static final String AUDIOFILE_PATH = "audio_path";

    public static final String TABLENAME_GETCAMPAIGN = "tablegetcampaign";
    public static final String IDEATION_NAME = "ideation_name";
    public static final String CAMPAIGN_NAME = "campaign_name";
    public static final String CAMPAIGN_ID = "campaign_id";


    public synchronized static ParseOpenHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new ParseOpenHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public ParseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        /*mInstance = this.getWritableDatabase();*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table assignments(company_name TEXT,client_id TEXT,ideator_id TEXT);");
        db.execSQL("create table tablesavedidea(ideatorid_ofidea TEXT,campaign TEXT,ideationsaved_name TEXT,idea_name TEXT,idea_description TEXT,image_path TEXT,audio_path TEXT);");
        db.execSQL("create table tablegetcampaign(ideation_name TEXT,campaign_name TEXT,campaign_id TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table" + TABLE_NAME_ASSIGNMENTS);
        db.execSQL("drop table" + TABLE_NAME_SAVEDIDEA);
        db.execSQL("drop table" + TABLENAME_GETCAMPAIGN);
        onCreate(db);
    }
}