package app.rstone.com.contactsapp;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import static app.rstone.com.contactsapp.Main.*;


public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context __this = MemberList.this;
        ItemList query = new ItemList(__this);
        ListView memberList = findViewById(R.id.member_list);
        memberList.setAdapter(new MemberAdapter(__this,(ArrayList<Main.Member>) new ListService() {
            @Override
            public List<Main.Member> perform() {
                return query.execute();
            }
        }.perform()));
        memberList.setOnItemClickListener(
            (AdapterView<?> p, View v, int i, long l)->{
                Intent intent = new Intent(__this,MemberDetail.class);
                Main.Member m = (Main.Member) memberList.getItemAtPosition(i);
                intent.putExtra("seq",m.seq+"");
                startActivity(intent);
            }
        );
        memberList.setOnItemLongClickListener(
            (AdapterView<?> p, View v, int i, long l)->{
                Main.Member m = (Main.Member) memberList.getItemAtPosition(i);
                new AlertDialog.Builder(__this)
                    .setTitle("DELETE")
                    .setMessage("정말로 삭제할까요?")
                    .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(__this,"삭제실행!!",Toast.LENGTH_LONG).show();
                                    ItemDelete queryDelete = new ItemDelete(__this);
                                    queryDelete.id = m.seq+"";
                                queryDelete.execute();
                                startActivity(new Intent(__this,MemberList.class));
                                Toast.makeText(__this,"삭제완료!!",Toast.LENGTH_LONG).show();
                            }
                        })
                    .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(__this,"삭제취소!!",Toast.LENGTH_LONG).show();

                            }
                    }).show();
                return true;
            }
        );
        findViewById(R.id.addBtn).setOnClickListener(
            (View v)->{
                startActivity(new Intent(__this,MemberAdd.class));
            }
        );
    }

    private class ListQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public ListQuery(Context __this) {
            super(__this);
            helper = new Main.SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }

    private class ItemList extends ListQuery {
        public ItemList(Context __this) {super(__this);}
        public ArrayList<Main.Member> execute(){
            ArrayList<Main.Member> list = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery("SELECT * FROM MEMBER",null);
            Main.Member m = null;
            if(c !=null){
                while(c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    list.add(m);
                 }
                Log.d("등록된 회원수가", list.size()+"");
            }else{
                Log.d("등록된 회원이", "없습니다.");
            }
            return list;
        }
    }
    private class deleteQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public deleteQuery(Context __this) {
            super(__this);
            helper = new SQLiteHelper(__this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemDelete extends  deleteQuery {
        String id;
        public ItemDelete(Context __this) {
            super(__this);
        }
        public void execute(){
            getDatabase().execSQL(String.format("DELETE FROM %s WHERE %s LIKE '%s'",MEMTAB,MEMSEQ,id));
        }
    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Main.Member> list;
        LayoutInflater inflater;
        Context _this;

        public MemberAdapter(Context _this,ArrayList<Main.Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(_this);
            this._this = _this;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.profile = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder)v.getTag();
            }
            ItemProfile query = new ItemProfile(_this);
            query.seq = list.get(i).seq+"";
            holder.profile
                    .setImageDrawable(
                            getResources().getDrawable(
                                    getResources().getIdentifier(
                                            _this.getPackageName()+":drawable/"
                                                    + (new Main.RetrieveService() {
                                                @Override
                                                public Object perform() {
                                                    return query.execute();
                                                }
                                            }.perform())
                                            , null, null
                                    ), _this.getTheme()
                            )
                    );
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profile;
        TextView name,phone;
    }
    private class MemberProfileQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public MemberProfileQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemProfile extends MemberProfileQuery{
        String seq;
        public ItemProfile(Context _this) {
            super(_this);
        }
        public String execute(){
            Cursor c = getDatabase()
                    .rawQuery(String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' "
                            , MEMPHOTO, MEMTAB, MEMSEQ, seq),null);
            String result = "";
            if(c != null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(MEMPHOTO));
                }
            }
            return result;
        }
    }
}
