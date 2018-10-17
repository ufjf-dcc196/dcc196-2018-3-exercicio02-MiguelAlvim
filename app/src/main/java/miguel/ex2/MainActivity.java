package miguel.ex2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Arrays;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Button bttAdd;
    EditText txtAdd;
    ListView list;

    LinkedList<String> linkedList = new LinkedList<String>();
    ArrayAdapter<String> arrayAdapter;

    DBHandler bdHandler;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttAdd = findViewById(R.id.bttAdd);
        txtAdd = findViewById(R.id.txtAdd);
        list   = findViewById(R.id.list);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,linkedList);
        list.setAdapter(arrayAdapter);

        bdHandler = new DBHandler(getApplicationContext());
        bd = bdHandler.getReadableDatabase();

        String[] cols = {"id","name"};
        Cursor c = bd.query("series", cols, null, null, null,null , null);

        while (c.moveToNext()){
            linkedList.add(c.getString(1));
            arrayAdapter.notifyDataSetChanged();
        }

        bttAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkedList.add(txtAdd.getText().toString());
                ContentValues vals = new ContentValues();
                vals.put("name",txtAdd.getText().toString());
                if(bd.insert("series", null, vals)>-1) {
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}
