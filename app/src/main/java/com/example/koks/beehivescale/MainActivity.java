package com.example.koks.beehivescale;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    private Common produceAPI = new Common(context);
    private SavingFiles fileSaver = new SavingFiles(context);
    private ListView hiveInfo;
    private ArrayList<String> units = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    //archive for raw stram from dweet.io
    private final static String RAW_MEMORY = "raw_output.txt";

    //archive file for thing and key
    private static final String FILE_NAME_ADDRESS_ITEM = "Settings_info_thing.txt";
    private static final String FILE_NAME_ADDRESS_KEY = "Settings_info_key.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SavingInfo saveID = new SavingInfo(context);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        swipeRefreshLayout=findViewById(R.id.sw_refresh);
        hiveInfo = findViewById(R.id.hive_info);
        produceAPI = new Common(context);
        new GetUnitData().execute(produceAPI.apiRequestNotKeyed());
        startService(new Intent(this, netGetterService.class));

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new GetUnitData().execute(produceAPI.apiRequestNotKeyed());
                    }
                }
        );
        hiveInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ArrayList e = (ArrayList<String>) parent.getAdapter().getItem(position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                graphDay dialogFragment = new graphDay();

                dialogFragment.setID((String) e.get(0));
                dialogFragment.show(ft, "dialog");
            }
        });

        hiveInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ArrayList<String> e;
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.renamer_of_unit);
                Button Save_name_unit = dialog.findViewById(R.id.save_unit_name);
                Button Reset_unit_old = dialog.findViewById(R.id.reset_to_old);
                Button Cancel_name_unit = dialog.findViewById(R.id.cancel_unit_name);
                final TextView originalItem = dialog.findViewById(R.id.originalName);
                final EditText changeItem = dialog.findViewById(R.id.renameName);
                e = (ArrayList<String>) parent.getAdapter().getItem(position);
                originalItem.setText(e.get(0));

                Save_name_unit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (changeItem.getText() != null) {
                            saveID.addNewUnitID(e.get(0), changeItem.getText().toString());
                            Log.e("What to change with",changeItem.getText().toString());
                        }

                        new GetUnitData().execute(produceAPI.apiRequestNotKeyed());
                        dialog.cancel();
                    }
                });

                Reset_unit_old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveID.addNewUnitID(e.get(0), e.get(0));
                        new GetUnitData().execute(produceAPI.apiRequestNotKeyed());
                        dialog.cancel();
                    }
                });

                Cancel_name_unit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Refresh:
                new GetUnitData().execute(produceAPI.apiRequestNotKeyed());
                return true;
            case R.id.Credentials:
                startDialogCreds();
                return true;
            case R.id.Info:
                StartDialogInfo();
                return true;
            case R.id.about:
                startDialogAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }

    private void startDialogCreds() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.credentials_dialog);
        final EditText UnitID = dialog.findViewById(R.id.username);
        final EditText UnitKey = dialog.findViewById(R.id.password);
        Button SaveSettings = dialog.findViewById(R.id.save_settings);
        Button CanacelSettings = dialog.findViewById(R.id.cancel_setting);

        SaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UnitID.getText() != null) {
                    fileSaver.saveFile(FILE_NAME_ADDRESS_ITEM, UnitID.getText().toString());
                    produceAPI.changeThing(UnitID.getText().toString());
                }
                if (UnitKey.getText() != null) {
                    fileSaver.saveFile(FILE_NAME_ADDRESS_KEY, UnitKey.getText().toString());
                    produceAPI.changeKey(UnitKey.getText().toString());
                }

                dialog.cancel();
                new GetUnitData().execute(UnitID.getText().toString());
            }
        });

        CanacelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void StartDialogInfo(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_dialog);
        dialog.show();
        Button closeInfoDialog = dialog.findViewById(R.id.closeInfo);
        closeInfoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private class GetUnitData extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String currID;
        String mass;
        String voltage;
        netGetter http;

        private ArrayList<String> unitOnePart(String ID, String mass_old, String voltage_old) {
            ArrayList<String> finalOut = new ArrayList<>();
            if (mass_old.contains(",")) mass = mass_old.substring(0, mass_old.indexOf("\""));
            mass = mass_old.replace(";", "");
            voltage = voltage_old.replace(";", "");

            finalOut.add(ID);
            finalOut.add(mass);
            finalOut.add(voltage);
            return finalOut;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd.setTitle(R.string.wait);
           // pd.show();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String stream;
            String urlString = params[0];
            http = new netGetter(getApplicationContext());
            stream = http.getHTTPData(urlString);

            //check if receiving of dweets is successful
            if (http.itHasConnection()) {
                fileSaver.saveFile(RAW_MEMORY, stream);
            }else {
                //read last succesful dweet from file
                stream = fileSaver.readFile(RAW_MEMORY);
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            createJSON takeInput;
            String timestamp;
            if (!http.itHasConnection()) {
                Toast.makeText(getApplicationContext(),
                        R.string.NO_CONECTIVITY_ALEART, LENGTH_LONG).show();
            }
            layoutAdapter myInflater;
            ArrayList<ArrayList<String>> temp = new ArrayList<>();
            super.onPostExecute(s);
            if (s != null) {
                try {
                    //extract JSONObject
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray with = jsonObj.getJSONArray("with");
                    JSONObject content = with.getJSONObject(0);

                    //get time string for further archiving
                    //for now it is just local variable with nothing to do
                    timestamp = content.toString();
                    timestamp = timestamp.substring(timestamp.indexOf("created\":\""), timestamp.indexOf("Z")-4);
                    timestamp = timestamp.replace("created\":\"","");
                    Log.e("Time of recovery",timestamp);

                    //start backup
                    takeInput = new createJSON(getApplicationContext(), timestamp);
                    takeInput.run();

                    JSONObject contents = content.getJSONObject("content");

                    //Iterate keys
                    Iterator<String> allUnits = contents.keys();

                    //run through all unit list
                    while (allUnits.hasNext()) {
                        currID = allUnits.next();
                        units.add(currID);
                        String idValues = contents.getString(currID);

                        if (idValues.contains("[")) {
                            idValues = idValues.substring(2, idValues.indexOf(",") - 1);
                        }
                            int postitionOfSeparator = idValues.indexOf(";");
                            voltage = idValues.substring(0, postitionOfSeparator);
                            mass = idValues.substring(postitionOfSeparator + 1);
                            temp.add(unitOnePart(currID, mass, voltage));
                            takeInput.putUnitData(currID,mass);
                        }
                    takeInput.saveLists();

                    //inflate listView
                    myInflater = new layoutAdapter(getApplicationContext(), temp);
                    hiveInfo.setAdapter(myInflater);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pd.dismiss();
        }
     }
}