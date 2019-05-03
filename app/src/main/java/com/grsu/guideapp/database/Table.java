package com.grsu.guideapp.database;

public interface Table {

    int NOT_DOWNLOAD = 0;
    int HAVE_UPDATE = 1;
    int DOWNLOAD = 2;

    interface Lines {

        String table_name = "lines";

        String id_line = "id_line";
        String start_point = "start_point";
        String end_point = "end_point";
        String polyline = "polyline";
    }

    interface ListLines {

        String table_name = "list_lines";

        String id = "id";
        String id_route = "id_route";
        String id_line = "id_line";
        String sequence = "sequence";

    }

    interface ListPoi {

        String name_table = "list_poi";

        String id_list = "id_list";
        String id_route = "id_route";
        String id_point = "id_point";
        String id_poi = "id_poi";
        String distance = "distance";
    }

    interface POI {

        String name_table = "poi";

        String id_poi = "id_poi";
        String location = "location";
        String id_type = "id_type";
        String audio_reference = "audio_reference";
        String photo_reference = "photo_reference";
        String link = "link";
        String last_update = "last_update";
        String last_download = "last_download";
    }

    interface PoiLanguage {

        String name_table = "poi_language";

        String id_translate = "id_translate";
        String id_poi = "id_poi";
        String type = "type";
        String language_ru = "language_ru";
        String language_en = "language_en";
        String language_cn = "language_cn";
        String language_lt = "language_lt";
        String language_pl = "language_pl";
    }

    interface Routes {

        String table_name = "routes";

        String id_route = "id_route";
        String duration = "duration";
        String distance = "distance";
        String reference_photo_route = "reference_photo_route";
        String southwest = "southwest";
        String northeast = "northeast";
        String last_update = "last_update";
        String last_download = "last_download";
        String is_full = "is_full";
    }

    interface RoutesLanguage {

        String name_table = "routes_language";

        String id_translate = "id_translate";
        String id_route = "id_route";
        String type = "type";
        String language_ru = "language_ru";
        String language_en = "language_en";
        String language_cn = "language_cn";
        String language_lt = "language_lt";
        String language_pl = "language_pl";
    }

    interface Types {

        String name_table = "types";

        String id_type = "id_type";
        String language_ru = "language_ru";
        String language_en = "language_en";
        String language_cn = "language_cn";
        String language_lt = "language_lt";
        String language_pl = "language_pl";
        String icon_type = "icon_type";
        String is_checked = "is_checked";
    }
}
