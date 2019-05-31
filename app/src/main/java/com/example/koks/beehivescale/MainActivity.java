package com.example.koks.beehivescale;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.DweetDatabase;
import com.example.koks.beehivescale.base.Thing;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    List<Thing> temp = new ArrayList<>();
    private ListView hiveInfo;
    DweetDatabase database;
    LayoutAdapter Layout;

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, netGetterService.class));
        database = new DweetDatabase(this);

        Dweet dweet;

        try {
            System.out.println("Try LastDweet ");
            dweet = database.getLastDweet();
            temp = dweet.getUnits();
        } catch (NullPointerException e) {
            System.out.println(e);
            System.out.println("NullPointException");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        swipeRefreshLayout=findViewById(R.id.sw_refresh);

        hiveInfo = findViewById(R.id.hive_info);
        Layout = new LayoutAdapter(context, temp);
        hiveInfo.setAdapter(Layout);

        swipeRefreshLayout.setOnRefreshListener(
                () -> {

                });
        hiveInfo.setOnItemClickListener((parent, view, position, id) -> {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
        });

        hiveInfo.setOnItemLongClickListener((parent, view, position, id) -> {
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

            Save_name_unit.setOnClickListener(v -> {
                if (changeItem.getText() != null) {
                    Log.e("What to change with", changeItem.getText().toString());
                }

                dialog.cancel();
            });

            Reset_unit_old.setOnClickListener(v -> {
                dialog.cancel();
            });

            Cancel_name_unit.setOnClickListener(v -> dialog.cancel());
            dialog.show();
            return true;
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

        SaveSettings.setOnClickListener(v -> {
            if (UnitID.getText() != null) {
            }
            if (UnitKey.getText() != null) {
            }

            dialog.cancel();
        });

        CanacelSettings.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    private void StartDialogInfo(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_dialog);
        dialog.show();
        Button closeInfoDialog = dialog.findViewById(R.id.closeInfo);
        closeInfoDialog.setOnClickListener(v -> dialog.cancel());
    }
}