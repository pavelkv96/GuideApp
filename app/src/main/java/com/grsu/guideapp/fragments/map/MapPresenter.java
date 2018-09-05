package com.grsu.guideapp.fragments.map;

import static com.google.android.gms.maps.model.TileProvider.NO_TILE;
import static com.grsu.guideapp.utils.Crypto.decodeLL;
import static com.grsu.guideapp.utils.Crypto.decodePL;
import static com.grsu.guideapp.utils.MapUtils.getDistanceBetween;
import static com.grsu.guideapp.utils.MapUtils.toLocation;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.map.MapContract.MapInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;

public class MapPresenter extends BasePresenterImpl<MapViews> implements OnFinishedListener,
        MapContract.MapPresenter {

    private static final Integer RADIUS = 100;
    private static final String TAG = MapPresenter.class.getSimpleName();
    private LatLng currentLatLng;
    private List<LatLng> latLngs;
    private Integer currentIndex;
    private List<LatLng> allLatLngs;
    private List<LatLng> turnsList;

    private List<Integer> types = new ArrayList<>();
    private Integer checkedItem;


    private MapViews mapViews;
    private MapInteractor mapInteractor;

    public MapPresenter(MapViews mapViews, MapInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading...");
        mapInteractor.getRouteById(this, id);
    }

    @Override
    public void getLocation(Location currentLocation) {
        LatLng point = findNearestPointInPolyline(currentLocation);

        mapViews.setCurrentPoint(point);

        currentIndex = allLatLngs.indexOf(point);

        Integer index = latLngs.indexOf(point);
        detach(index);

        getCurrentTurn(toLocation(currentLatLng));
    }

    @Override
    public void setRadius(String radius) {
        checkedItem = Integer.valueOf(radius);
    }

    @Override
    public void setType(List<Integer> typesObjects) {
        types = typesObjects;
    }

    @Override
    public List<Integer> getType() {
        return types;
    }

    @Override
    public void getMarkers() {
        if (currentLatLng != null) {
            getCurrentTurn(toLocation(currentLatLng));
        }
    }

    @Override
    public Tile getTile(int x, int y, int zoom, String provider) {
        return mapInteractor.getTile(this, MapUtils.getTileIndex(zoom, x, y), provider);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapViews.mapViewSettings(googleMap);
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        allLatLngs = new ArrayList<>();
        latLngs = new ArrayList<>();
        turnsList = new ArrayList<>();

        try {
            for (Line encodeLine : encodePolylines) {
                Integer idLine = encodeLine.getIdLine();

                mapViews.setPolyline(decodeLL(encodeLine.getPolyline()), idLine);

                LatLng startPoint = decodePL(encodeLine.getStartPoint());
                LatLng endPoint = decodePL(encodeLine.getEndPoint());
                mapViews.setPointsTurn(startPoint);
                mapViews.setPointsTurn(endPoint);

                List<LatLng> lineList = decodeLL(encodeLine.getPolyline());

                if (currentLatLng != null) {
                    lineList.remove(0);
                    turnsList.add(endPoint);
                } else {
                    currentIndex = 0;
                    currentLatLng = startPoint;
                    turnsList.add(endPoint);
                    turnsList.add(startPoint);
                }

                allLatLngs.addAll(lineList);
            }

            for (int i = 0; i < 5; i++) {
                latLngs.add(allLatLngs.get(i));
            }
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }

    @Override
    public void onFinished1(List<Poi> poiList) {
        mapViews.removeMarkers();
        for (Poi poi : poiList) {
            mapViews.setPoi(poi);
        }
    }

    @Override
    public Tile onFinished(byte[] tile) {
        if (tile == null) {
            return NO_TILE;
        }

        return new Tile(256, 256, tile);
    }

    private void getCurrentTurn(Location currentLocation) {
        LatLng shortestDistance = getShortestDistance(turnsList, currentLocation);
        int size = getType().size();

        if (MapUtils.isMoreDistance(RADIUS, currentLocation, shortestDistance) && size != 0) {

            mapInteractor.getListPoi(
                    this,
                    shortestDistance.latitude,
                    shortestDistance.longitude,
                    checkedItem,
                    getType());
        } else {
            mapViews.removeMarkers();
        }
    }

    private void detach(int newCenterIndex) {
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

    private LatLng findNearestPointInPolyline(Location currentLocation) {
        LatLng shortestDistance = getShortestDistance(latLngs, currentLocation);

        if (getDistanceBetween(currentLocation, toLocation(shortestDistance)) > 60) {

            Logs.e(TAG,
                    getDistanceBetween(currentLocation, toLocation(shortestDistance)) + " meters");
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

    private LatLng getShortestDistance(List<LatLng> latLngList, Location currentLocation) {
        Location endLocation = toLocation(latLngList.get(0));
        Float distance = getDistanceBetween(currentLocation, endLocation);
        LatLng latLng = latLngList.get(0);

        for (LatLng point : latLngList) {
            if (MapUtils.isMoreDistance(distance, currentLocation, point)) {
                distance = getDistanceBetween(currentLocation, toLocation(point));
                latLng = point;
            }
        }

        return latLng;
    }


    private static List<Line> setData1() {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(1, "ahtfI{iopC", "iitfIenopC", "ahtfI{iopCIa@G]G[EWGU"));
        lines.add(new Line(2, "iitfIenopC", "iotfIulopC", "iitfIenopCG@MBOBSBUDSBSDSBQBSDMB"));
        lines.add(new Line(3, "iotfIulopC", "wotfI_jopC", "iotfIulopC@NAh@MZ"));
        lines.add(new Line(4, "wotfI_jopC", "sdufIacopC",
                "wotfI_jopCQDQDQDOBQDQDQDQBODQDQDQBQDQDQDOBQDQDQDQDQDODQDQDQFQDODQDQDQDQDODQDQDQDQDQDMD"));
        lines.add(new Line(5, "sdufIacopC", "qcufI{nnpC",
                "sdufIacopC@\\?\\@\\@\\@\\@\\?^@\\@\\@\\@\\?^@\\@\\@\\?\\@\\@\\@\\@^@f@"));
        lines.add(new Line(6, "qcufI{nnpC", "e_tfIginpC",
                "qcufI{nnpCNHPHNHPHNFPHPHNHNHPHNHRHRJLFLFPJNHPHNHPHLFRDPDPBNDPBRCPCRCPCRETCTEVCNCPAPCPARCPCPCN?PAP?PAP?P?P?T?T?V@T@P@P@PBV@\\@X@XBRCTCLA"));
        lines.add(new Line(7, "e_tfIginpC", "qgtfI_hopC",
                "e_tfIginpCG[G[G[EYG[G[G]GYE[GYG[G[G[E[EYG[E[G_@Ic@G[Ia@Ic@G_@Ga@Ke@Ia@Ie@Ic@Ia@G]Ic@Ic@"));
        return lines;
    }
}
