package com.levrite.globustestproject.Activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.levrite.globustestproject.Adapter.RVAdapter;
import com.levrite.globustestproject.Database.DBContentProvider;
import com.levrite.globustestproject.Database.DBHelper;
import com.levrite.globustestproject.Model.Company;
import com.levrite.globustestproject.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RVAdapter mRVAdapter;
    private ArrayList<Company> companies = new ArrayList<Company>();
    private String[] projection = {DBHelper.COMPANY_COLUMN_ID, DBHelper.COMPANY_COLUMN_NAME, DBHelper.COMPANY_COLUMN_POSITION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerViewDragDropManager dragMgr = new RecyclerViewDragDropManager();

        dragMgr.setInitiateOnMove(false);
        dragMgr.setInitiateOnLongPress(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRVAdapter = new RVAdapter(populateCompanies(), this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(dragMgr.createWrappedAdapter(mRVAdapter));

        dragMgr.attachRecyclerView(mRecyclerView);

    }

    protected ArrayList<Company> populateCompanies() {


        Cursor cursor = getContentResolver().query(DBContentProvider.CONTENT_URI, projection, null, null, DBHelper.COMPANY_COLUMN_POSITION + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getShort(cursor.getColumnIndex(DBHelper.COMPANY_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COMPANY_COLUMN_NAME));
                int positionTable = cursor.getShort(cursor.getColumnIndex(DBHelper.COMPANY_COLUMN_POSITION));
                Company company = new Company(id, name, positionTable);
                companies.add(company);

            } while (cursor.moveToNext());
        }

        return companies;
    }


}
