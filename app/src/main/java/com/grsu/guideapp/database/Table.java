package com.grsu.guideapp.database;

public interface Table {

    interface Lines {

        String table_name = "lines";

        String id_line = "id_line";
        String start_point = "start_point";
        String end_point = "end_point";
        String polyline = "polyline";
        String audio_reference = "audio_reference";
    }

    interface Routes {
        String table_name = "routes";

        String id_route = "id_route";
        String short_name = "short_name";
        String full_name = "full_name";
        String duration = "duration";
        String distance = "distance";
        String short_description = "short_description";
        String full_description = "full_description";
        String reference_photo_route = "reference_photo_route";
        String southwest = "southwest";
        String northeast = "northeast";
    }

    interface ListLines{
        String table_name = "list_lines";

        String id = "id";
        String id_route = "id_route";
        String id_line = "id_line";
        String sequence = "sequence";


    }

}
