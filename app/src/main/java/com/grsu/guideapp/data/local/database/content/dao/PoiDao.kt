package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grsu.guideapp.data.local.database.content.entities.Poi
import com.grsu.guideapp.data.local.database.vo.DetailsObjectVO
import com.grsu.guideapp.data.local.database.vo.ObjectItemVO

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

    @Query(
        """
        SELECT c1.location, c2.description, c1.address, c1.phone, c1.email, c1.link
        FROM poi AS c1
        INNER JOIN poi_language AS c2 ON c1.id_poi = c2.id_poi
        WHERE c1.id_poi = :poiId AND language = :locale 
    """
    )
    suspend fun getObjectById(poiId: Int, locale: String): DetailsObjectVO.ObjectVO

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
}