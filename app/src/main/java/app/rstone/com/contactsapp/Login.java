package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static app.rstone.com.contactsapp.Main.MEMPW;
import static app.rstone.com.contactsapp.Main.MEMSEQ;
import static app.rstone.com.contactsapp.Main.MEMTAB;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context __this = Login.this;
        findViewById(R.id.login_btn).setOnClickListener(
            (View v)->{
                ItemExist query = new ItemExist(__this);
                EditText x = findViewById(R.id.input_id);
                EditText y = findViewById(R.id.input_pw);
                query.id = x.getText().toString();
                query.pw = y.getText().toString();
                new Main.StatusService() {
                    @Override
                    public void perform() {
                        if(query.execute()){
                            Toast.makeText(__this,"로그인 성공",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(__this,MemberList.class));
                        }else{
                            Toast.makeText(__this,"로그인 실패",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(__this,Login.class));
                        }
                    }
                }.perform();
            }
        );
    }
    private class LoginQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public LoginQuery(Context __this) {
            super(__this);
            helper = new Main.SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemExist extends LoginQuery{
        String id, pw;
        public ItemExist(Context __this) {
            super(__this);
        }
        public boolean execute(){
            return getDatabase().rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", MEMTAB,MEMSEQ,id,MEMPW,pw),null).moveToNext();
        }
    }
}
