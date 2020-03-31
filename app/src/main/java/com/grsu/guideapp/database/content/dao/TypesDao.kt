package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.database.content.entities.Types
import com.grsu.guideapp.database.vo.TypeItemVO

@Dao
abstract class TypesDao : BaseDao<Types> {

    @Query(
        """
        SELECT id_type, picture, language_ru, language_en, language_cn, language_pl, language_lt
        FROM types
        """
    )
    abstract suspend fun getAll(): List<TypeItemVO>
}