package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.*
import com.grsu.guideapp.data.local.database.content.entities.Poi
import com.grsu.guideapp.data.local.database.vo.*

@Dao
interface PoiDao : BaseDao<Poi> {

    @Query("SELECT * FROM poi")
    fun getAll(): List<Poi>

    @Transaction
    @Query(
        """
            SELECT poi.id_poi, poi_language.name, new_table.reference
            FROM poi
            INNER JOIN poi_language ON poi.id_poi = poi_language.id_poi
            LEFT JOIN (
                SELECT poi.id_poi, references_table.reference
		        FROM poi, references_table, references_list
		        WHERE 
		        poi.id_poi = references_list.id_poi AND 
		        references_list.id_reference = references_table.id_reference AND 
		        poi.id_type = :catalogId
		        GROUP BY poi.id_poi
		    ) AS new_table ON new_table.id_poi = poi.id_poi
            WHERE poi.id_type = :catalogId AND poi_language.language = :locale
            """
    )
    fun getAllByCatalogId(catalogId: Int, locale: String): List<ObjectItemVO>

    @Transaction
    @Query(
        """
        SELECT poi.id_poi, poi.location, poi.address, poi.phone, poi.email, poi.link, poi.is_favorite
        FROM poi
        WHERE poi.id_poi = :poiId 
    """
    )
    suspend fun getObjectById(poiId: Int): ObjectVO

    @Transaction
    fun insertOrUpdate(obj: Poi) {
        if (insert(obj) == -1L) update(obj)
    }

    @Transaction
    fun insertOrUpdate(objList: List<Poi>) {
        insert(objList)
            .mapIndexed { index, l -> if (l == -1L) objList[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }

    @Transaction
    @Query(
        """
        SELECT poi.id_poi, poi.id_type, poi.location, poi_language.name, poi.link_icon
        FROM poi
        INNER JOIN poi_language ON poi.id_poi = poi_language.id_poi
        INNER JOIN list_poi ON poi.id_poi = list_poi.id_poi
        WHERE poi_language.language = :locale AND list_poi.id_route = :idRoute
    """
    )
    suspend fun getPoiByIdRoute(idRoute: Int, locale: String): List<PoiVO>

    @Query(
        """
            SELECT pois.id_poi as id, pois.name as name, c2.reference as photo 
            FROM
            (
                SELECT c1.id_poi, c2.name
                FROM poi c1, poi_language c2
                WHERE c1.id_poi = c2.id_poi AND c2.language = :locale AND c1.is_favorite = 1
            ) as pois, references_list c1, references_table c2
            WHERE c1.id_reference = c2.id_reference  AND pois.id_poi = c1.id_poi
            GROUP BY pois.id_poi
        """
    )
    suspend fun getFavouritePoi(locale: String): List<PoiFavoriteVO>

    @Update(entity = Poi::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun changeFavoriteState(poi: ObjectVO): Int

    @Query("""
        SELECT poi_language.id_poi, poi_language.description
        FROM poi_language
        WHERE poi_language.id_poi = :poiId and poi_language.language = :locale
    """)
    suspend fun getDescriptionById(poiId: Int, locale: String): ContentVO
}