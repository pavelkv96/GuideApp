package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grsu.guideapp.data.local.database.content.entities.Routes
import com.grsu.guideapp.data.local.database.vo.RouteItemVO

@Dao
interface RoutesDao : BaseDao<Routes> {

    @Query("SELECT * FROM routes")
    fun getRoutes(): List<Routes>

    @Query(
        """
        SELECT c1.id_route, c2.name, c1.photo_reference, c1.duration, c1.distance 
        FROM routes c1, route_language c2
        WHERE c1.id_route = c2.id_route AND c2.language = :language AND c1.status <> 0 ORDER BY c1.last_update
        """
    )
    suspend fun getNewRoutes(language: String): List<RouteItemVO>


    @Transaction
    fun insertOrUpdate(obj: Routes) {
        if (insert(obj) == -1L) update(obj)
    }

    @Transaction
    fun insertOrUpdate(objList: List<Routes>) {
        insert(objList)
            .mapIndexed { index, l -> if (l == -1L) objList[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }
}