package com.levrite.globustestproject.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.levrite.globustestproject.Database.DBContentProvider;
import com.levrite.globustestproject.Model.Company;
import com.levrite.globustestproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 13.02.2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> implements DraggableItemAdapter<RVAdapter.ViewHolder> {

    private ArrayList<Company> mCompanyList;
    private Context mContext;
    private int newID;
    private int currentID;

    public RVAdapter(ArrayList<Company> companyList, Context context) {
        setHasStableIds(true);
        mCompanyList =  companyList;
        mContext = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Company companyItem = mCompanyList.get(position);
        holder.companyName.setText(companyItem.name);

    }

    @Override
    public int getItemCount() {
        return mCompanyList.size();
    }

     @Override
    public long getItemId(int position) {
        return mCompanyList.get(position).id;

    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Company companyMovedItem = mCompanyList.remove(fromPosition);
        mCompanyList.add(toPosition, companyMovedItem);
        notifyItemMoved(fromPosition, toPosition);
        updateDatabase(fromPosition, toPosition, companyMovedItem.id);


    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    /**
     * Bad function
     * @param currentId
     * @param nextId
     * @param itemId
     */
    public void updateDatabase(int currentId, int nextId, final int itemId){

        currentID = currentId;
        newID = nextId;

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                ContentValues contentValues = new ContentValues();

                if(currentID < newID){

                    for(int i = currentID; i < newID; i++){

                        contentValues.put("newID", i);
                        contentValues.put("currentID", i+1);
                        mContext.getContentResolver().update(DBContentProvider.CONTENT_URI,contentValues, null,null);

                    }


                } else if(currentID > newID){

                    for(int i = currentID; i > newID; i--){

                        contentValues.put("newID", i);
                        contentValues.put("currentID", i-1);
                        mContext.getContentResolver().update(DBContentProvider.CONTENT_URI,contentValues, null,null);


                    }

                }

                contentValues.put("newID", newID);
                contentValues.put("currentID", itemId);

                mContext.getContentResolver().update(DBContentProvider.CONTENT_URI,contentValues, "id",null);


                return null;
            }
        }.execute();

    }


    public static class ViewHolder extends AbstractDraggableItemViewHolder{

        private TextView companyName;

        public ViewHolder(View itemView) {

            super(itemView);
            companyName = (TextView) itemView.findViewById(R.id.companyText);

        }

    }

}
