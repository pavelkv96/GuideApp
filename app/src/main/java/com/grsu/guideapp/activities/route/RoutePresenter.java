package com.grsu.guideapp.activities.route;

import static com.google.android.gms.maps.model.TileProvider.NO_TILE;
import static com.grsu.guideapp.utils.CryptoUtils.decodeL;
import static com.grsu.guideapp.utils.CryptoUtils.decodeP;
import static com.grsu.guideapp.utils.MapUtils.getDistanceBetween;
import static com.grsu.guideapp.utils.MapUtils.toLocation;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;

public class RoutePresenter extends BasePresenterImpl<RouteView> implements OnFinishedListener,
        RouteContract.RoutePresenter {

    private static final Integer RADIUS = 100;
    private static final String TAG = RoutePresenter.class.getSimpleName();
    private LatLng currentLatLng;
    private List<LatLng> latLngs;
    private Integer currentIndex;
    private List<LatLng> allLatLngs;
    private List<LatLng> turnsList;

    private List<Integer> types = new ArrayList<>();
    private Integer checkedItem;
    private boolean flag;
    private Integer id;

    private RouteView routeView;
    private RouteInteractor routeInteractor;

    public RoutePresenter(RouteView routeView, RouteInteractor routeInteractor) {
        this.routeView = routeView;
        this.routeInteractor = routeInteractor;
    }

    @Override
    public void getId(Integer id) {
        mView.showProgress(null, "Loading...");
        routeInteractor.getRouteById(this, id);
        this.id = id;
    }

    @Override
    public void getProjectionLocation(Location currentLocation) {
        LatLng point = findNearestPointInPolyline(currentLocation);

        routeView.setCurrentPoint(point);

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
    public void setAllPoi(boolean getAll) {
        flag = getAll;
        getAllPoi();
    }

    @Override
    public void getAllPoi() {
        int size = getType().size();
        if (flag && size != 0) {
            routeInteractor.getListPoi(this, id, getType());
        } else {
            getPoi();
        }
    }

    @Override
    public void getPoi() {
        if (currentLatLng != null) {
            getCurrentTurn(toLocation(currentLatLng));
        }
    }

    @Override
    public Tile getTile(int x, int y, int zoom, String provider) {
        return routeInteractor.getTile(this, MapUtils.getTileIndex(zoom, x, y), provider);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        routeView.mapViewSettings(googleMap);
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        allLatLngs = new ArrayList<>();
        latLngs = new ArrayList<>();
        turnsList = new ArrayList<>();

        try {
            for (Line encodeLine : encodePolylines) {
                Integer idLine = encodeLine.getIdLine();

                routeView.setPolyline(decodeL(encodeLine.getPolyline()), idLine);

                LatLng startPoint = decodeP(encodeLine.getStartPoint());
                LatLng endPoint = decodeP(encodeLine.getEndPoint());
                routeView.setPointsTurn(startPoint);
                routeView.setPointsTurn(endPoint);

                List<LatLng> lineList = decodeL(encodeLine.getPolyline());

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
        routeView.removePoi();
        for (Poi poi : poiList) {
            routeView.setPoi(poi);
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
        if (flag) {
            return;
        }

        LatLng shortestDistance = getShortestDistance(turnsList, currentLocation);
        int size = getType().size();

        if (MapUtils.isMoreDistance(RADIUS, currentLocation, shortestDistance) && size != 0) {

            routeInteractor.getListPoi(
                    this,
                    shortestDistance.latitude,
                    shortestDistance.longitude,
                    checkedItem,
                    getType());
        } else {
            routeView.removePoi();
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
}