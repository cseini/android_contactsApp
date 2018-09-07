package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import static app.rstone.com.contactsapp.Main.*;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        Intent intent = this.getIntent();
        final Context __this = MemberUpdate.this;
        String[] spec = getIntent().getStringExtra("spec").split(",");
        ImageView profile = findViewById(R.id.profileImg);
        EditText name = findViewById(R.id.textName);
        name.setHint(spec[2]);
        EditText email = findViewById(R.id.changeEmail);
        email.setHint(spec[3]);
        EditText phone = findViewById(R.id.changePhone);
        phone.setHint(spec[4]);
        EditText addr = findViewById(R.id.changeAddress);
        addr.setHint(spec[5]);
        profile.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(this.getPackageName()+":drawable/"+spec[1],null,null),__this.getTheme()));

        findViewById(R.id.confirmBtn).setOnClickListener(
                (View v)->{
                    ItemUpdate query = new ItemUpdate(__this);
                    query.m.seq = Integer.parseInt(spec[0]);
                    query.m.name = (name.getText().toString().equals(""))?spec[2]:name.getText().toString();
                    query.m.email = (email.getText().toString().equals(""))?spec[3]:email.getText().toString();
                    query.m.phone = (phone.getText().toString().equals(""))?spec[4]:phone.getText().toString();
                    query.m.addr = (addr.getText().toString().equals(""))?spec[5]:addr.getText().toString();
                    new StatusService() {
                        @Override
                        public void perform() {
                            query.execute();
                        }
                    }.perform();
                    query.execute();
                    Intent update = new Intent(__this,MemberDetail.class);
                    update.putExtra("seq",spec[0]);
                    startActivity(update);
                }
        );
        findViewById(R.id.cancelBtn).setOnClickListener(
                (View v)->{
                    Intent moveDetail = new Intent(__this,MemberDetail.class);
                    moveDetail.putExtra("seq",spec[0]);
                    startActivity(moveDetail);
                }
        );
    }
    private class UpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context __this) {
            super(__this);
            helper = new Main.SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {return helper.getWritableDatabase();}
    }
    private class ItemUpdate extends UpdateQuery{
        Main.Member m ;
        public ItemUpdate(Context __this) {
            super(__this);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL((String.format(
                    "UPDATE %s SET %s='%s', %s='%s',%s='%s', %s='%s' WHERE %s like '%s' ",
                    MEMTAB, MEMNAME, m.name, MEMEMAIL, m.email, MEMPHONE, m.phone, MEMADDR, m.addr, MEMSEQ, m.seq)));
        }
    }

}

