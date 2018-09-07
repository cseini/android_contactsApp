package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static app.rstone.com.contactsapp.Main.MEMADDR;
import static app.rstone.com.contactsapp.Main.MEMEMAIL;
import static app.rstone.com.contactsapp.Main.MEMNAME;
import static app.rstone.com.contactsapp.Main.MEMPHONE;
import static app.rstone.com.contactsapp.Main.MEMPHOTO;
import static app.rstone.com.contactsapp.Main.MEMPW;
import static app.rstone.com.contactsapp.Main.MEMTAB;

public class MemberAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);
        final Context __this = MemberAdd.this;
        findViewById(R.id.confirmBtn).setOnClickListener(
                (View v)->{
                    ItemAdd addQuery = new ItemAdd(__this);
                    TextView name = findViewById(R.id.textName);
                    TextView pw = findViewById(R.id.textPw);
                    TextView email = findViewById(R.id.textEmail);
                    TextView phone = findViewById(R.id.textPhone);
                    TextView addr = findViewById(R.id.textAddress);
                    ImageView profile = findViewById(R.id.profile);
                    addQuery.m.name = name.getText().toString();
                    addQuery.m.pw = pw.getText().toString();
                    addQuery.m.email = email.getText().toString();
                    addQuery.m.phone = phone.getText().toString();
                    addQuery.m.addr = addr.getText().toString();
                    addQuery.execute();
                    startActivity(new Intent(__this,MemberList.class));
                }
        );
        findViewById(R.id.cancelBtn).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(__this,MemberList.class));
                }
        );
    }
    private class addQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public addQuery(Context __this) {
            super(__this);
            helper = new Main.SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemAdd extends addQuery {
        Main.Member m;
        public ItemAdd(Context __this) {
            super(__this);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL(String.format(
                " INSERT INTO %s "+
                        " (%s, %s, %s, %s, %s, %s) VALUES " +
                        " ('%s', '%s', '%s', '%s', '%s', '%s') ",
                MEMTAB, MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO,
                m.name, m.pw, m.email, m.phone, m.addr, "profile_1"));
        }
    }
}

