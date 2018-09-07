package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static app.rstone.com.contactsapp.Main.*;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context __this = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        Log.d("넘어온 seq 값 :::", seq);
        String seq2 = intent.getStringExtra("seq");
        Log.d("넘어온 seq2 값 :::", seq2);
        findViewById(R.id.listBtn).setOnClickListener(
                (View v) -> {
                    startActivity(new Intent(__this, MemberList.class));
                }
        );

        ItemRetrieve query = new ItemRetrieve(__this);
        query.id = seq2;
        Main.Member m = (Main.Member) new RetrieveService() {
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(this.getPackageName() + ":drawable/" + m.photo, null, null), __this.getTheme()));
        TextView name = findViewById(R.id.name);
        name.setText(m.name);
        TextView phone = findViewById(R.id.phone);
        phone.setText(m.phone);
        TextView addr = findViewById(R.id.addr);
        addr.setText(m.addr);
        TextView email = findViewById(R.id.email);
        email.setText(m.email);
        findViewById(R.id.updateBtn).setOnClickListener(
                (View v) -> {
                    Intent moveUpdate = new Intent(__this, MemberUpdate.class);
                    moveUpdate.putExtra("spec", m.seq + "," + m.photo + "," + m.name +
                            "," + m.email + "," + m.phone + "," + m.addr);
                    startActivity(moveUpdate);
                }
        );
        findViewById(R.id.callBtn).setOnClickListener(
                (View v) -> {

                }
        );
        findViewById(R.id.dialBtn).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+m.phone)));
                }
        );
    }
    private class RetriveQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public RetriveQuery(Context __this) {
            super(__this);
            helper = new Main.SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }

    private class ItemRetrieve extends RetriveQuery{
        String id;
        public ItemRetrieve(Context __this) {super(__this);}
        public Main.Member execute(){
            Cursor c = getDatabase().rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'",MEMTAB, MEMSEQ,id),null);
            Main.Member m = null;
            while(c.moveToNext()){
                m = new Main.Member();
                m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                m.name = c.getString(c.getColumnIndex(MEMNAME));
                m.addr = c.getString(c.getColumnIndex(MEMADDR));
                m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                m.pw = c.getString(c.getColumnIndex(MEMPW));
                m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
            }
        return m;
        }
    }
}
