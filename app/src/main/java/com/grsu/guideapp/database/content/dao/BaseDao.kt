package com.grsu.guideapp.database.content.dao

import androidx.room.*

@Dao
@Suppress("unused")
abstract class BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(obj: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(obj: List<T>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    abstract fun update(obj: T)

    /**
     * Update an array of objects from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    abstract fun update(obj: List<T>)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    abstract fun delete(obj: T)

    @Transaction
    fun insertOrUpdate(obj: T) {
        if (insert(obj) == -1L) update(obj)
    }

    @Transaction
    fun insertOrUpdate(objList: List<T>) {
        val insertResult: List<Long> = insert(objList)
        val updateList: MutableList<T> = mutableListOf()
        insertResult.forEach {
            if (insertResult[it.toInt()] == -1L) updateList.add(objList[it.toInt()])
        }
        if (updateList.isNotEmpty()) update(updateList)
    }
}