package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;

class Logic {

    private static final String TAG = Logic.class.getSimpleName();
    private LatLng currentLatLng;
    private List<LatLng> latLngs;
    private Integer currentIndex;
    private List<LatLng> allLatLngs;
    private List<LatLng> turnsList;

    private static Logic logic = null;

    static Logic getInstance() {
        if (logic == null) {
            logic = new Logic();
        }
        return logic;
    }

    static void detachLogic() {
        logic = null;
    }

    void initialData(List<Line> encodePolylines) {
        allLatLngs = new ArrayList<>();
        latLngs = new ArrayList<>();
        turnsList = new ArrayList<>();

        try {
            for (Line encodeLine : encodePolylines) {
                Integer idLine = encodeLine.getIdLine();

                List<LatLng> geoPointList = CryptoUtils.decodeL(encodeLine.getPolyline());

                LatLng startPoint = CryptoUtils.decodeP(encodeLine.getStartPoint());
                LatLng endPoint = CryptoUtils.decodeP(encodeLine.getEndPoint());

                if (currentLatLng != null) {
                    geoPointList.remove(0);
                    turnsList.add(endPoint);
                } else {
                    currentIndex = 0;
                    currentLatLng = startPoint;
                    turnsList.add(startPoint);
                    turnsList.add(endPoint);
                }

                allLatLngs.addAll(geoPointList);
            }

            for (int i = 0; i < 5; i++) {
                latLngs.add(allLatLngs.get(i));
            }
        } catch (NullPointerException ignored) {
        }
    }

    void detach(int newCenterIndex) {
        LatLng latLng = latLngs.get(newCenterIndex);

        if (newCenterIndex > 2) {
            removeLast(latLng);
        }

        if (newCenterIndex < 2) {
            removePrevious(latLng);
        }
    }

    private void removeLast(LatLng latLng) {
        while (latLngs.indexOf(latLng) - 1 >= 2) {
            latLngs.remove(0);
        }

        //get next points
        if (latLngs.size() == 3) {
            if (currentIndex + 1 < allLatLngs.size()) {
                latLngs.add(allLatLngs.get(currentIndex + 1));
            } else {
                latLngs.add(new LatLng((double) -latLngs.size(), (double) -latLngs.size()));
            }

        }

        if (latLngs.size() == 4) {
            if (currentIndex + 2 < allLatLngs.size()) {
                latLngs.add(allLatLngs.get(currentIndex + 2));
            } else {
                latLngs.add(new LatLng((double) -latLngs.size(), (double) -latLngs.size()));
            }
        }
    }

    private void removePrevious(LatLng latLng) {
        while (latLngs.indexOf(latLng) + 2 < latLngs.indexOf(latLngs.get(latLngs.size() - 1))) {
            latLngs.remove(latLngs.size() - 1);
        }
        //get previous points
        List<LatLng> list = new ArrayList<>();
        if (latLngs.size() == 3) {

            if (currentIndex > 1) {
                list.add(allLatLngs.get(currentIndex - 1));
            } else {
                list.add(new LatLng((double) -latLngs.size(), (double) -latLngs.size()));
            }
            latLngs.addAll(0, list);
            list.clear();
        }

        if (latLngs.size() == 4) {
            if (currentIndex > 2) {
                list.add(allLatLngs.get(currentIndex - 2));
            } else {
                list.add(new LatLng((double) -latLngs.size() - 1,
                        (double) -latLngs.size() - 1));
            }
            latLngs.addAll(0, list);
        }
    }

    LatLng findNearestPointInPolyline(Location currentLocation) {
        LatLng shortestDistance = getShortestDistance(latLngs, currentLocation);

        if (MapUtils.getDistanceBetween(currentLocation, MapUtils.toLocation(shortestDistance))
                > 60) {

            Logs.e(TAG,
                    MapUtils.getDistanceBetween(currentLocation,
                            MapUtils.toLocation(shortestDistance)) + " meters");
            try {
                LatLng latLng = getShortestDistance(allLatLngs, currentLocation);
                int index = allLatLngs.indexOf(latLng) - 2;
                latLngs.clear();
                for (int i = 0; i < 5; i++) {
                    latLngs.add(i, allLatLngs.get(index + i));
                }
                currentLatLng = shortestDistance = latLng;
            } catch (ArrayIndexOutOfBoundsException ignore) {

            }

        }

        if (latLngs.indexOf(shortestDistance) != latLngs.indexOf(currentLatLng)) {
            currentLatLng = shortestDistance;
        }
        return shortestDistance;
    }

    LatLng getShortestDistance(List<LatLng> latLngList, Location currentLocation) {
        Location endLocation = MapUtils.toLocation(latLngList.get(0));
        Float distance = MapUtils.getDistanceBetween(currentLocation, endLocation);
        LatLng latLng = latLngList.get(0);

        for (LatLng point : latLngList) {
            if (MapUtils.isMoreDistance(distance, currentLocation, point)) {
                distance = MapUtils.getDistanceBetween(currentLocation, MapUtils.toLocation(point));
                latLng = point;
            }
        }

        return latLng;
    }

    LatLng getCurrentLatLng() {
        return currentLatLng;
    }

    List<LatLng> getTurnsList() {
        return turnsList;
    }

    List<LatLng> getAllLatLngs() {
        return allLatLngs;
    }

    int getLatLngs(LatLng point) {
        return latLngs.indexOf(point);
    }

    void setCurrentIndex(LatLng point) {
        this.currentIndex = allLatLngs.indexOf(point);
    }
}
