package com.grsu.guideapp.map

import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderThemeMenuCallback
import java.io.InputStream

enum class Provider(private val path: String) : XmlRenderTheme {

    MAPSFORGE("/assets/custom/custom.xml"),
    OSM("/assets/mapsforge/osmarender.xml"),
    DEFAULT("/assets/mapsforge/default.xml");

    override fun getMenuCallback(): XmlRenderThemeMenuCallback? = null

    override fun getRelativePathPrefix(): String = "/assets/"

    override fun getRenderThemeAsStream(): InputStream? = javaClass.getResourceAsStream(path)

    override fun setMenuCallback(menuCallback: XmlRenderThemeMenuCallback?) {}
}