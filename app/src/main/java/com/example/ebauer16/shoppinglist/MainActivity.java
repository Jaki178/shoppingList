package com.example.ebauer16.shoppinglist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private TreeMap<String, List<Article>> map = new TreeMap<>();
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private String key;
    private ArrayList<Article> articleList = new ArrayList<>();
    private Article article;
    private String articleName;
    private String amount;
    private List<String> items = new ArrayList<>();
    private ArrayAdapter adapter;
    private EditText articleValue;
    private EditText amountValue;
    private EditText storeName;
    private File FILE = new File("shoppinglist");
    private Gson gson = new Gson();
    private static final int RQ_WRITE_STORAGE = 12345;
    private List<String> spinnerList = new ArrayList<>();
    private List<Article> testtest = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        spinner = findViewById(R.id.spinner);
        articleValue = findViewById(R.id.articleName);
        amountValue = findViewById(R.id.amountArticle);
        storeName = findViewById(R.id.storeName);
        initCombo(spinner);
        if(!map.isEmpty()){
            load(FILE.getName());
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                items.clear();
                key = spinner.getSelectedItem().toString();
                if(!articleList.isEmpty()) {
                   /* for (int j = 0; j < map.keySet().size(); j++) {
                        articleList.add((Article) map.get(key));
                    } */
                    testtest.add((Article) map.get(key));
                    for (int j = 0; j < map.keySet().size(); j++) {
                        String line = testtest.get(j).toString();
                        String[] array = line.split(";");
                        articleName = array[0];
                        amount = array[1];
                        items.add(articleName + " " + amount);
                       /* String line = articleList.get(j).toString();
                        String[] array = line.split(";");
                        articleName = array[0];
                        amount = array[1];
                        items.add(articleName + " " + amount);
                            */
                    }
                    testtest.clear();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bindAdapterToListView(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.addArticle:
                addNewArticle();
                break;
            case R.id.addStore:
                addNewStore();
                break;
            case R.id.save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewArticle(){
        final View vDialog = getLayoutInflater().inflate(R.layout.addarticledialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Add new article");
        alert.setView(vDialog);
        alert.setPositiveButton("Ok", (dialog, which) -> handleArticleDialog(vDialog));
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void handleArticleDialog(View vDialog) {
        EditText articleName_txt = vDialog.findViewById(R.id.articleName);
        EditText amountArticle_txt = vDialog.findViewById(R.id.amountArticle);
        articleName = articleName_txt.getText().toString();
        amount = amountArticle_txt.getText().toString();
        Article newArticle = new Article(articleName, amount);
        key = spinner.getSelectedItem().toString();
        items.add(articleName + " " + amount);
        articleList.add(newArticle);
        map.put(key, articleList);
    }

    private void addNewStore(){
        final View vDialog = getLayoutInflater().inflate(R.layout.addstoredialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Add new store");
        alert.setView(vDialog);
        alert.setPositiveButton("Ok", (dialog, which) -> handleStoreDialog(vDialog));
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void handleStoreDialog(View vDialog) {
        EditText newStore_txt = vDialog.findViewById(R.id.storeName);
        String newStore = newStore_txt.getText().toString();
        spinnerList.add(newStore);
        map.put(newStore, null);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void save(){
        try {
            gson.toJson(map, new FileWriter(FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initCombo(Spinner spinner){
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(spinnerAdapter);
    }

    private void bindAdapterToListView(ListView lv){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }

    private void load(String filename){
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = Environment.getExternalStorageDirectory();
        String path = outFile.getAbsolutePath();
        String fullPath = path + File.separator + filename;
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fullPath)));
            out.print(map);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("TAG", e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void printInput(View view){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String [] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, RQ_WRITE_STORAGE);
        } else {
            load(FILE.getName());
        }
    }
}
