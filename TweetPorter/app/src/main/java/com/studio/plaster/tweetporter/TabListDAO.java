package com.studio.plaster.tweetporter;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.studio.plaster.tweetporter.modeldb.TabListDb;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface TabListDAO {
    @Insert(onConflict = IGNORE)
    void insert(TabListDb tabListDb);

    @Update
    void update(TabListDb tabListDb);

    @Delete
    void delete(TabListDb tabListDb);

    @Query("SELECT * FROM TabListDb")
    List<TabListDb> getAll();


}
