package id.dapur.d4ti3a.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    // Declare Variables
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    EditText mEditText;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String NAME = "name";
    static String LOCATIONNAME = "locationname";
    static String LATITUDE = "latitude";
    static String LONGITUDE = "longitude";
    static String DATE = "date";
    static String FLAG = "flag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Geo Cutomers");



        mEditText = (EditText) findViewById(R.id.search);
        mEditText=(EditText)findViewById(R.id.search);
        listview=(ListView)findViewById(R.id.listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                // TODO Auto-generated method stub
                String gh = listview.getTextFilter().toString();
                mEditText.setText(gh);
            }
        });
        listview.setTextFilterEnabled(true);

        mEditText.addTextChangedListener(new TextWatcher() {

            //Fungsi Search JSON Array
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                ArrayList<HashMap<String, String>> arrayTemplist = new ArrayList<HashMap<String, String>>();
                // TODO Auto-generated method stub
                String searchString = mEditText.getText().toString().toLowerCase(Locale.getDefault());
                Log.d("@w2w2w2w",""+searchString);
                int realtext=searchString.length();
                arrayTemplist.clear();
                for (int i = 0; i < arraylist.size(); i++) {
                    String currentString = arraylist.get(i).get("name").toString();
                    String currentPlaceString = arraylist.get(i).get("locationname").toString();
                    String currentShopString = arraylist.get(i).get("date").toString();
                    if(realtext<=currentString.length()&&realtext<=currentPlaceString.length()&&realtext<=currentShopString.length())
                    {


                        if (searchString.equalsIgnoreCase(currentString.substring(0,realtext)) ||
                                searchString.equalsIgnoreCase(currentShopString.substring(0,realtext)) ||
                                searchString.equalsIgnoreCase(currentPlaceString.substring(0,realtext))) {
                            arrayTemplist.add(arraylist.get(i));
                            adapter = new ListViewAdapter(MainActivity.this, arrayTemplist);
                            listview.setAdapter(adapter);
                        }
                    }
                    else{

                        adapter.notifyDataSetChanged();
                    }


                }}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Execute DownloadJSON AsyncTask
        new DownloadJSON().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.activity_refresh:
                Intent refresh = new Intent(this, MainActivity.class);
                startActivity(refresh);//Start the same Activity
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Mohon Tungggu");
            // Set progressdialog message
            mProgressDialog.setMessage("Sedang Memuat Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<>();
            // Retrieve JSON Objects from the given URL address
            jsonarray = JSONfunctions
                    .getJSONfromURL("http://10.0.0.3:81/delivery/koordinat");

            try {
                // Locate the array  in JSON
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("name", jsonobject.getString("name"));
                    map.put("locationname", jsonobject.getString("locationname"));
                    map.put("latitude", jsonobject.getString("latitude"));
                    map.put("longitude", jsonobject.getString("longitude"));
                    map.put("date", jsonobject.getString("date"));
                    map.put("flag", jsonobject.getString("flag"));
                    // Set the JSON Objects into the array
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            mEditText = (EditText) findViewById(R.id.search);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }
}