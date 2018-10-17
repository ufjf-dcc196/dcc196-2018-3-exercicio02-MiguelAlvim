package miguel.ex2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Button bttAdd,bttEdit,bttDel;
    EditText txtAdd,txtEdit;
    ListView list;

    LinkedList<String> linkedListNames = new LinkedList<String>();
    LinkedList<Long> linkedListIds = new LinkedList<Long>();
    ArrayAdapter<String> arrayAdapter;

    DBHandler bdHandler;
    SQLiteDatabase bd;
    int listPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttDel = findViewById(R.id.bttDel);
        bttAdd = findViewById(R.id.bttAdd);
        txtAdd = findViewById(R.id.txtAdd);
        bttEdit = findViewById(R.id.bttEdit);
        txtEdit = findViewById(R.id.txtEdit);
        list   = findViewById(R.id.list);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, linkedListNames);
        list.setAdapter(arrayAdapter);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        updateListWithDB();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtEdit.setText(list.getItemAtPosition(position)+"");
                listPosition = position;
            }
        });

        bttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues vals = new ContentValues();
                vals.put("name",txtAdd.getText().toString());
                long id = bd.insert("series", null, vals);
                if(id>-1) {
                    linkedListNames.add(txtAdd.getText().toString());
                    linkedListIds.add(id);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        bttEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues vals = new ContentValues();
                vals.put("name",txtEdit.getText().toString());
                String[] args = {linkedListIds.get(listPosition)+""};
                long id = bd.update("series", vals, "id = ?", args);
                if(id>0){
                    linkedListNames.set(listPosition,txtEdit.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        bttDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] args = {linkedListIds.get(listPosition)+""};
                long id = bd.delete("series", "id = ?", args);
                if(id>0){
                    linkedListNames.remove(listPosition);
                    linkedListIds.remove(listPosition);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void updateListWithDB(){
        String[] cols = {"id","name"};
        Cursor c = bd.query("series", cols, null, null, null,null , null);
        linkedListNames.clear();
        linkedListIds.clear();

        while (c.moveToNext()){
            linkedListNames.add(c.getString(1));
            linkedListIds.add(c.getLong(0));
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
