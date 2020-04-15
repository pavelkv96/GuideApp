package com.grsu.guideapp.data.local.database.utils

import androidx.annotation.VisibleForTesting
import com.grsu.guideapp.data.local.database.content.entities.*
import com.grsu.guideapp.data.remote.pojo.*
import com.grsu.guideapp.data.remote.pojo.Objects
import com.grsu.guideapp.utils.CryptoUtils
import com.grsu.guideapp.utils.Status
import com.grsu.guideapp.utils.TypeResource
import com.grsu.guideapp.utils.extensions.toMD5
import java.util.*

object ContentConverter {

    @VisibleForTesting
    fun insert_Lines(turn: Turn): Lines {
        return Lines(
            0,
            CryptoUtils.encodeP(turn.start.location.latLng),
            CryptoUtils.encodeP(turn.end.location.latLng),
            turn.polyline
        )
    }

    @VisibleForTesting
    fun insert_ListLines(id_route: Long, lines: List<Long>): MutableList<ListLines>? {
        if (lines.isEmpty()) return null
        val list = mutableListOf<ListLines>()
        for (i in lines.indices)
            list.add(ListLines(lines[i], id_route, i))
        return list
    }

    @VisibleForTesting
    fun insert_ListPoi(id_route: Long, id_poi: Long): ListPoi {
        val distance = Math.random().let {
            val start = 70
            val end = 1000
            start + it * (end - start)
        }

        return ListPoi(id_route, id_poi, "", distance)
    }

    @VisibleForTesting
    fun insert_Marker(hashUrl: String, byteImage: ByteArray): Marker {
        return Marker(hashUrl, byteImage, Date())
    }

    @VisibleForTesting
    fun insert_part_POI(abstract: Objects): Poi? {
        val categories = abstract.categories
        if (categories.isEmpty()) return null

        return Poi(
            abstract.id,
            CryptoUtils.encodeP(abstract.location.latLng),
            abstract.icon.toMD5(),
            categories[0],
            Date(0),
            Date()
        )
    }

    @VisibleForTesting
    fun insert_full_POI(abstract: AbstractObject): Poi? {
        val poi = abstract.poiObject ?: return null
        val content = poi.content ?: return null
        val details = poi.property ?: return null
        return Poi(
            abstract.id,
            CryptoUtils.encodeP(details.location.latLng),
            details.icon.toMD5(),
            content.category!!.id,
            abstract.timestamp.updatedAt,
            Date(),
            0,
            details.address?.ru,
            details.email.getOrNull(0),
            details.href.getOrNull(0),
            details.phone.mobile.getOrNull(0),
            0
        )
    }

    @VisibleForTesting
    fun insert_PoiLanguage(id_poi: Long, language: Content): List<PoiLanguage> {
        val name = language.name.full
        val about = language.about.full
        return mutableListOf<PoiLanguage>().apply {
            add(PoiLanguage(id_poi, "ru", name.ru, about.ru))
            add(PoiLanguage(id_poi, "en", name.en, about.en))
            add(PoiLanguage(id_poi, "cn", name.cn, about.cn))
            add(PoiLanguage(id_poi, "pl", name.pl, about.pl))
            add(PoiLanguage(id_poi, "lt", name.lt, about.lt))
        }
    }

    @VisibleForTesting
    fun insert_References(url: String, typeResource: TypeResource = TypeResource.UNDEFINED): References {
        return References(url.toMD5(), url, typeResource)
    }

    @VisibleForTesting
    fun insert_ReferencesList(id_poi: Long, urlReferences: String): ReferencesList {
        return ReferencesList(id_poi, urlReferences.toMD5())
    }

    @VisibleForTesting
    fun insert_RouteLanguage(id_route: Long, language: Content): List<RouteLanguage> {
        val name = language.name.full
        val about = language.name.full
        return mutableListOf<RouteLanguage>().apply {
            add(RouteLanguage(id_route, "ru", name.ru, about.ru))
            add(RouteLanguage(id_route, "en", name.en, about.en))
            add(RouteLanguage(id_route, "cn", name.cn, about.cn))
            add(RouteLanguage(id_route, "pl", name.pl, about.pl))
            add(RouteLanguage(id_route, "lt", name.lt, about.lt))
        }
    }

    @VisibleForTesting
    fun insert_Route(abstract: AbstractObject, status: Status = Status.UNDEFINED): Routes? {
        val route = abstract.routeObject ?: return null
        val content = route.content ?: return null
        val data = route.data ?: return null

        return Routes(
            abstract.id,
            0,
            0,
            content.images.getOrNull(0)?.href,
            CryptoUtils.encodeP(data.limLeft.latLng),
            CryptoUtils.encodeP(data.limRight.latLng),
            maxOf(abstract.timestamp.updatedAt, data.date.updatedAt),
            Date(),
            status,
            0
        )
    }

    @VisibleForTesting
    fun insert_Types(type: Category, picture: ByteArray): Types {
        val id = type.id
        val name = type.name
        return Types(id, picture, name.ru, name.en, name.cn, name.lt, name.pl, 1)
    }
}