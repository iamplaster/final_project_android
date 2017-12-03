package com.studio.plaster.tweetporter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.studio.plaster.tweetporter.adapter.EditAdpater;
import com.studio.plaster.tweetporter.model.TabList;

import java.util.List;

public class EditActivity extends AppCompatActivity implements AsyncResponseEdit{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private GetInfoObject getInfoObject;
    private TabList tabList;
    private List<String> keywords;
    private EditAdpater editAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Topic");
        getInfoObject = new GetInfoObject();
        getInfoObject.setListenerEdit(this);

        DbCreator dbCreator = new DbCreator();
        dbCreator.setContext(this);
        dbCreator.setListenerEdit(this);
        dbCreator.execute();


        Intent intent = getIntent();
        this.tabList = intent.getParcelableExtra("tablist");

        keywords = this.tabList.getKeywordList();
        FloatingActionButton addButton = findViewById(R.id.fabAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTabDialog();
            }
        });

        ImageButton delButton = findViewById(R.id.editDelButton);
        if(delButton != null){
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.corner_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.editnametopic_button:
                editNameTabDialog();
                return true;
            case R.id.addtopic_button:
                addTabDialog();
                return true;
            case R.id.deletetab_button_edit:
                getInfoObject.deleteTab(this.tabList,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editNameTabDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.edit_name_popup, null);
        final EditText name = prompt.findViewById(R.id.editNameTab);
        name.setText(tabList.getName());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tabList.setName(name.getText().toString());
                getInfoObject.updateTab(tabList);
                setResult(RESULT_OK);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void addTabDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.add_keyword_popup, null);
        final EditText keyword = prompt.findViewById(R.id.editAddKeyword);
        final RadioButton radioUser = prompt.findViewById(R.id.radioUser);
        final RadioButton radioKeyword = prompt.findViewById(R.id.radioKeyword);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(prompt);



        alertDialogBuilder.setCancelable(false).setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(radioUser.isChecked()){
                            keywords.add("@"+keyword.getText().toString());
                        }
                        if(radioKeyword.isChecked()){
                            keywords.add("#"+keyword.getText().toString());
                        }
                        tabList.setKeywordList(keywords);
                        getInfoObject.updateTab(tabList);
                        setResult(RESULT_OK);

                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable) && radioKeyword.isChecked()){
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    @Override
    public void updateFinish() {
        loadRecycler();

    }

    @Override
    public void processFinish(TabMainDb output) {
        getInfoObject.setDb(output);
        loadRecycler();
    }

    @Override
    public void delTabFinish(String result, int lastPosition) {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void delTopic(List<String> keywords) {
        tabList.setKeywordList(keywords);
        getInfoObject.updateTab(tabList);
        setResult(RESULT_OK);
    }


    public void loadRecycler(){
        RecyclerView recyclerView = findViewById(R.id.editRecycler);
        editAdpater = new EditAdpater();
        editAdpater.setListener(this);
        editAdpater.setKeywords(keywords);
        recyclerView.setAdapter(editAdpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
