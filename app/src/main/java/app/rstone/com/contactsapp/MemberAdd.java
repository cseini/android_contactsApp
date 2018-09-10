package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        final Context _this = MemberAdd.this;
        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        EditText addr = findViewById(R.id.addr);
        EditText photo = findViewById(R.id.profileName);
        ImageView img = findViewById(R.id.profile);
        findViewById(R.id.imgBtn).setOnClickListener(
                (View v)->{
                    img.setImageDrawable(getResources().getDrawable(
                            getResources().getIdentifier(
                                    this.getPackageName()+":drawable/"+photo.getText().toString(),
                                    null, null
                            ), _this.getTheme()));
                    img.setTag(photo.getText().toString());
                }
        );
        findViewById(R.id.addBtn).setOnClickListener(
                (View v)->{
                    ItemAdd query = new ItemAdd(_this);
                    query.m.phone = ((phone.getText()+"").equals(""))? "" : phone.getText()+"";
                    query.m.email = ((email.getText()+"").equals(""))? "" : email.getText()+"";
                    query.m.addr = ((addr.getText()+"").equals(""))? "" : addr.getText()+"";
                    query.m.name = ((name.getText()+"").equals(""))? "" : name.getText()+"";
                    query.m.photo = (img.getTag().toString().equals(""))? "profile_1" :img.getTag().toString();
                    new Main.StatusService() {
                        @Override
                        public void perform() {
                            query.execute();
                        }
                    }.perform();
                    startActivity(new Intent(_this, MemberList.class));
                }
        );
        findViewById(R.id.listBtn).setOnClickListener(
                (View v)->{
                    this.startActivity(new Intent(_this, MemberList.class));
                }
        );
    }
    private class MemberInsertQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberInsertQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemAdd extends MemberInsertQuery{
        Main.Member m;
        public ItemAdd(Context _this) {
            super(_this);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL(
                    String.format(
                            " INSERT INTO  %s "
                                    + " ( %s , %s , %s , %s , %s , %s ) "
                                    + " VALUES "
                                    + " ( '%s', '%s', '%s', '%s', '%s', '%s' ) "
                            , MEMTAB
                            , MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO
                            , m.name, "1", m.email, m.phone, m.addr, m.photo ));
        }
    }
}

