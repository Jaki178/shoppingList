package com.example.ebauer16.shoppinglist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private ListView list;
    private TreeMap<String, List<Article>> map = new TreeMap<>();
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private String key;
    private ArrayList<Article> articleList;
    private Article article;
    private String articleName;
    private String amount;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter adapter;
    private EditText articleValue;
    private EditText amountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        spinner = findViewById(R.id.spinner);
        articleValue = findViewById(R.id.articleName);
        amountValue = findViewById(R.id.amountArticle);
        initCombo(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                articleList.clear();
                items.clear();
                key = spinner.toString();
                for (int j = 0; j < map.get(key).size(); j++){
                    articleList.add((Article) map.get(key));
                }
                for (int j = 0; j < articleList.size(); j++){
                    String line = articleList.get(j).toString();
                    String [] array = line.split(";");
                    articleName = array[0];
                    amount = array[1];
                    items.add(articleName + " " + amount);
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
        alert.setPositiveButton("Ok", (dialog, which) -> handleArticleDialog(vDialog));
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void handleArticleDialog(View vDialog) {
        articleName = article.getText();
        amount = article.getAmount(); // Nicht sicher, dass das richtige zurückgeliefert wird, da die Methoden von article aufgerufen werden.
        Article newArticle = new Article(articleName, amount);
        key = spinner.toString();
        items.add(articleName + " " + amount);
        articleList.add(newArticle);
        map.put(key, articleList);
    }

    private void addNewStore(){
        final View vDialog = getLayoutInflater().inflate(R.layout.addstoredialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Add new store");
        alert.setPositiveButton("Ok", (dialog, which) -> handleStoreDialog(vDialog));
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void handleStoreDialog(View vDialog) {

    }

    private void save(){

    }

    private void initCombo(Spinner spinner){
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void bindAdapterToListView(ListView lv){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }
}
