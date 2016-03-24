package com.example.amar.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity{

    private ProgressDialog pDialog;



    // URL to get contacts JSON
    private static String url = "http://www.speechify.in/internship/android_task.php";

    // JSON Node names
    private static final String TAG_RECEIPE = "recipe_data";
    private static final String TAG_ING = "ingredient_name";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";


    // contacts JSONArray
    JSONArray rec = null;
    JSONArray ingredient=null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> rList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
       setActionBar(toolbar);

        rList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();




        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
//
            HashMap<String,String> i=rList.get(position);

                String ingredient=i.get(TAG_ING);
                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleActivity.class);

                in.putExtra(TAG_NAME, name);
               in.putExtra(TAG_ING, ingredient);

                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetContacts().execute();

    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    rec = jsonObj.getJSONArray(TAG_RECEIPE);

                    // looping through All Contacts
                    for (int i = 0; i < rec.length(); i++) {
                        JSONObject c = rec.getJSONObject(i);

                      //  String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        name="\n"+(i+1)+". "+name;
                       // Log.d("name:", name);

                        ingredient = c.getJSONArray("ingredient_data");
                       // Log.d("ingredient:", ingredient.toString());
                        String ing="";
                        for(int j=0;j<ingredient.length();j++) {
                            JSONObject obj = ingredient.getJSONObject(j);

                            ing=ing+obj.getString(TAG_ING)+"\n";

                        }
                      HashMap<String, String> contact = new HashMap<String, String>();



                        contact.put(TAG_ING, ing);
                        contact.put(TAG_NAME, name);

                        // adding contact to contact list
                        rList.add(contact);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();




            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, rList,
                    R.layout.list_item, new String[] { TAG_NAME
                     }, new int[] { R.id.name
                    });

            setListAdapter(adapter);
        }

    }

}