package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grsu.guideapp.data.local.database.content.entities.Routes
import com.grsu.guideapp.data.local.database.vo.RouteDetailsVO
import com.grsu.guideapp.data.local.database.vo.RouteFavoriteVO
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


    @Query(
        """
        SELECT routes.duration, routes.distance, routes.southwest, routes.northeast, route_language.description
        FROM routes
        INNER JOIN route_language ON routes.id_route = route_language.id_route
        WHERE routes.id_route = :idRoute AND route_language.language = :locale LIMIT 1
    """
    )
    suspend fun getRouteDetails(idRoute: Int, locale: String): RouteDetailsVO

    @Query(
        """
            SELECT routes.id_route as id, route_language.name as name, routes.photo_reference as photo
            FROM routes
            INNER JOIN route_language ON route_language.id_route = routes.id_route
            WHERE routes.is_favorite = 1 AND route_language.language = :locale
        """
    )
    suspend fun getFavouriteRoutes(locale: String): List<RouteFavoriteVO>

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