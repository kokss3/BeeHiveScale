package com.example.koks.beehivescale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.koks.beehivescale.base.DweetDatabase;
import com.example.koks.beehivescale.base.Thing;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private ListView hiveInfo;
    LayoutAdapter layoutAdapter;
    DweetDatabase database;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, NetGetterService.class));
        database = new DweetDatabase(this);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        swipeRefreshLayout = findViewById(R.id.sw_refresh);
        hiveInfo = findViewById(R.id.hive_info);

        getDweetToAdapter();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getDweetToAdapter();
            swipeRefreshLayout.setRefreshing(false);
        });

        hiveInfo.setOnItemLongClickListener((parent, view, position, id) -> {
            Dialog dialog = new Dialog(context);
            Thing longPressedThing = (Thing) parent.getAdapter().getItem(position);
            dialog.setContentView(R.layout.renamer_of_unit);

            TextView originalItem = dialog.findViewById(R.id.originalName);
            EditText changeItem = dialog.findViewById(R.id.renameName);

            Button saveNewName = dialog.findViewById(R.id.save_unit_name);
            Button resetUnitOld = dialog.findViewById(R.id.reset_to_old);
            Button cancelNameUnit = dialog.findViewById(R.id.cancel_unit_name);

            originalItem.setText(longPressedThing.getUnitName());

            saveNewName.setOnClickListener(v -> {
                if (changeItem.getText() != null) {
                    longPressedThing.setAvatar(changeItem.getText().toString());
                    database.insertAvatar(longPressedThing);
                }
                getDweetToAdapter();
                dialog.cancel();
            });

            resetUnitOld.setOnClickListener(v -> {
                database.deleteAvatar(longPressedThing);
                getDweetToAdapter();
                dialog.cancel();
            });

            cancelNameUnit.setOnClickListener(v -> dialog.cancel());
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
                getDweetToAdapter();
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
            if (UnitID.getText() != null) database.insertCreds(UnitID.getText().toString());

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

    private void getDweetToAdapter() {
        List<Thing> tempList = database.getLastDweet().getUnits();

        Collections.sort(tempList, (o1, o2) -> {
            Thing a = o1;
            Thing b = o2;
            return a.getUnitName().compareToIgnoreCase(b.getUnitName());
        });

        layoutAdapter = new LayoutAdapter(context, tempList);
        layoutAdapter.notifyDataSetChanged();
        hiveInfo.setAdapter(layoutAdapter);
    }
}