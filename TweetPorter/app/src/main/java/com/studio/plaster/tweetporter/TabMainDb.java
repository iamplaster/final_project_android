package com.studio.plaster.tweetporter;



import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.os.Parcelable;

import com.studio.plaster.tweetporter.modeldb.TabListDb;

@Database(entities = {TabListDb.class}, version = 1)
public abstract class TabMainDb extends RoomDatabase {

    public abstract TabListDAO getTabListDAO();


}
