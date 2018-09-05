package com.grsu.guideapp.fragments.test;

import static com.google.android.gms.maps.model.TileProvider.NO_TILE;
import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeLL;
import static com.grsu.guideapp.utils.Crypto.decodeP;
import static com.grsu.guideapp.utils.Crypto.decodePL;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.test.TestContract.TestInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;

public class TestPresenter extends BasePresenterImpl<TestViews> implements OnFinishedListener,
        TestContract.TestPresenter {

    private static final Integer RADIUS = 100;
    private static final String TAG = TestPresenter.class.getSimpleName();
    private LatLng currentLatLng;
    private List<LatLng> latLngs;
    private Integer currentIndex;
    private List<LatLng> allLatLngs;
    private Polyline polyline;
    private List<LatLng> turnsList;
    private List<LatLng> poly;

    private List<Integer> types = new ArrayList<>();
    private Integer checkedItem;


    private TestViews testViews;
    private TestInteractor testInteractor;

    public TestPresenter(TestViews testViews, TestInteractor testInteractor) {
        this.testViews = testViews;
        this.testInteractor = testInteractor;
    }

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading...");
        testInteractor.getRouteById(this, id);
    }

    @Override
    public void getLocation(Location currentLocation) {
        LatLng point = findNearestPointInPolyline(currentLocation);

        if (polyline == null) {
            poly = new ArrayList<>();
            poly.add(point);
            polyline = testViews.setPolyline(poly.get(0));
        } else {
            poly.add(point);
            polyline.setPoints(poly);
        }

        currentIndex = allLatLngs.indexOf(point);

        Integer index = latLngs.indexOf(point);
        detach(index);

        getCurrentTurn(MapUtils.toLocation(currentLatLng));
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
            getCurrentTurn(MapUtils.toLocation(currentLatLng));
        }
    }

    @Override
    public Tile getTile(int x, int y, int zoom, String provider) {
        return testInteractor.getTile(this, MapUtils.getTileIndex(zoom, x, y), provider);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        testViews.mapViewSettings(googleMap);
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        allLatLngs = new ArrayList<>();
        latLngs = new ArrayList<>();
        turnsList = new ArrayList<>();

        try {
            for (Line encodeLine : setData1()) {
                Integer idLine = encodeLine.getIdLine();

                testViews.setPolyline(decodeLL(encodeLine.getPolyline()), idLine);

                LatLng startPoint = decodePL(encodeLine.getStartPoint());
                LatLng endPoint = decodePL(encodeLine.getEndPoint());
                testViews.setPoints(startPoint);
                testViews.setPoints(endPoint);

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
        testViews.removeMarkers();
        for (Poi poi : poiList) {
            testViews.setGetPoints(poi);
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

            testInteractor.getListPoi(
                    this,
                    shortestDistance.latitude,
                    shortestDistance.longitude,
                    checkedItem,
                    getType());
        } else {
            testViews.removeMarkers();
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

        if (latLngs.indexOf(shortestDistance) != latLngs.indexOf(currentLatLng)) {
            currentLatLng = shortestDistance;
        }
        return shortestDistance;
    }

    private LatLng getShortestDistance(List<LatLng> latLngList, Location currentLocation) {
        Location endLocation = MapUtils.toLocation(latLngList.get(0));
        Float distance = MapUtils.getDistanceBetween(currentLocation, endLocation);
        LatLng latLng = latLngList.get(0);

        for (LatLng point : latLngList) {
            if (MapUtils.isMoreDistance(distance, currentLocation, point)) {
                distance = MapUtils.getDistanceBetween(currentLocation, MapUtils.toLocation(point));
                latLng = point;
            }
        }

        Logs.e(TAG, distance + " meters");
        return latLng;
    }


    private static List<Line> setData1() {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(1, "ahtfI{iopC", "iitfIenopC", "ahtfI{iopCIa@G]G[EWGU"));
        lines.add(new Line(2, "iitfIenopC", "iotfIulopC", "iitfIenopCG@MBOBSBUDSBSDSBQBSDMB"));
        lines.add(new Line(3, "iotfIulopC", "wotfI_jopC", "iotfIulopC@NAh@MZ"));
        lines.add(new Line(4, "wotfI_jopC", "sdufIacopC", "wotfI_jopCQDQDQDOBQDQDQDQBODQDQDQBQDQDQDOBQDQDQDQDQDODQDQDQFQDODQDQDQDQDODQDQDQDQDQDMD"));
        lines.add(new Line(5, "sdufIacopC", "qcufI{nnpC", "sdufIacopC@\\?\\@\\@\\@\\@\\?^@\\@\\@\\@\\?^@\\@\\@\\?\\@\\@\\@\\@^@f@"));
        lines.add(new Line(6, "qcufI{nnpC", "e_tfIginpC", "qcufI{nnpCNHPHNHPHNFPHPHNHNHPHNHRHRJLFLFPJNHPHNHPHLFRDPDPBNDPBRCPCRCPCRETCTEVCNCPAPCPARCPCPCN?PAP?PAP?P?P?T?T?V@T@P@P@PBV@\\@X@XBRCTCLA"));
        lines.add(new Line(7, "e_tfIginpC", "qgtfI_hopC", "e_tfIginpCG[G[G[EYG[G[G]GYE[GYG[G[G[E[EYG[E[G_@Ic@G[Ia@Ic@G_@Ga@Ke@Ia@Ie@Ic@Ia@G]Ic@Ic@"));
        return lines;
    }
}
