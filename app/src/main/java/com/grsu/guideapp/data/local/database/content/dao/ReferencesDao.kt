package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.data.local.database.content.entities.References
import com.grsu.guideapp.data.local.database.vo.ImagesVO

@Dao
interface ReferencesDao : BaseDao<References> {

    @Query("""
        SELECT c2.id_reference, c2.reference
        FROM references_list AS c1 
        INNER JOIN references_table AS c2 ON c1.id_reference = c2.id_reference
        WHERE c1.id_poi = :poiId AND c2.type_resource = 1 LIMIT 5
    """)
    suspend fun getReferencesByObjectId(poiId: Int): List<ImagesVO>
}