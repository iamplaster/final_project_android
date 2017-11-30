package com.studio.plaster.tweetporter;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class DbCreator extends AsyncTask<Void, Void, TabMainDb> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private TabMainDb tabMainDb;

    public AsyncResponse delegate;
    public AsyncResponseEdit delegateEdit;

    public void setContext(Context context){
        this.context = context;
    }
    public void setListener(AsyncResponse listener){
        delegate = listener;
    }
    public void setListenerEdit(AsyncResponseEdit listener){
        delegateEdit = listener;
    }

    @Override
    protected TabMainDb doInBackground(Void... voids) {
        tabMainDb = Room.databaseBuilder(this.context, TabMainDb.class, "Tab").build();
        return tabMainDb;
    }

    @Override
    protected void onPostExecute(TabMainDb tabMainDb) {
        super.onPostExecute(tabMainDb);
        if(delegate != null){
            delegate.processFinish(tabMainDb);
        }
        if(delegateEdit != null){
            delegateEdit.processFinish(tabMainDb);
        }
    }
}
