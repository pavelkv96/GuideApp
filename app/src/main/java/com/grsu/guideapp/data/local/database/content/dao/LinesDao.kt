package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.data.local.database.content.entities.Lines
import com.grsu.guideapp.data.local.database.vo.LinesVO

@Dao
interface LinesDao : BaseDao<Lines>{

    @Query(
        """
        SELECT list_lines.sequence, lines.start_point, lines.end_point, lines.polyline
        FROM lines
        INNER JOIN list_lines ON lines.id_line = list_lines.id_line
        WHERE list_lines.id_route = :idRoute ORDER BY list_lines.sequence
    """
    )
    suspend fun getLines(idRoute: Int): List<LinesVO>
}