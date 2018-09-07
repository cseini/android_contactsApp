package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context __this = Main.this;
        findViewById(R.id.login_btn).setOnClickListener(
            (View v)->{
                SQLiteHelper helper = new SQLiteHelper(__this);
                startActivity(new Intent(__this,Login.class));
            }
        );

    }
    static class Member{int seq;String name,pw,email,phone,addr,photo;}
    static interface StatusService{public void perform();}
    static interface ListService{public List<?> perform();}
    static interface RetrieveService{public Object perform();}
    static String DBNAME = "rstone.db";
    static String MEMTAB = "MEMBER";
    static String MEMSEQ = "SEQ";
    static String MEMNAME = "NAME";
    static String MEMPW = "PW";
    static String MEMEMAIL = "EMAIL";
    static String MEMPHONE = "PHONE";
    static String MEMADDR = "ADDR";
    static String MEMPHOTO = "PHOTO";
    static abstract class QueryFactory{
        Context __this;

        public QueryFactory(Context __this) {
            this.__this = __this;
        }
        public abstract SQLiteDatabase getDatabase();

    }
    static class SQLiteHelper extends SQLiteOpenHelper{

        public SQLiteHelper(Context context) {
            super(context, DBNAME, null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql=String.format(
                " CREATE TABLE IF NOT EXISTS %s "+
                " (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT) ",
                MEMTAB, MEMSEQ ,MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO
            );
            Log.d( "실행할 쿼리 :: " ,sql);
            db.execSQL(sql);
            Log.d("========================", "create 쿼리실행완료");
            String[] names = {"예진우","구승효","예선우","이노을","주경문"};
            String[] emails = {"jinu@sangkuk.com","koo@sangkuk.com","woo@sangkuk.com","noel@sangkuk.com","jookm@sangkuk.com"};
            for(int i=1;i<=5;i++){
                db.execSQL(String.format(
                        " INSERT INTO %s "+
                                " (%s, %s, %s, %s, %s, %s) VALUES " +
                                " ('%s', '%s', '%s', '%s', '%s', '%s') ",
                        MEMTAB, MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO,
                        names[i-1], "1", emails[i-1], "010-0000-000" + i, "신촌 "+i+"번길", "profile_"+i
                ));
            }
            Log.d("========================", "insert 쿼리실행완료!");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ MEMTAB);
            onCreate(db);
        }
    }
}
