package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by DoKyeong on 2016. 8. 23..
 */
public class BookPlaceMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    private Context m_Context;
    private MapView mapView_Map;
    private GoogleMap mGoogleMap_Map;
    private ImageButton imageButton_Search;
    private Spinner spinner_AddressGu;
    private Spinner spinner_BookPlaceName;
    private FloatingActionButton floatingActionButton_Gps;

    private ArrayList<BookPlace> mArrayList_BookPlaces;
    private ArrayList<String> mArrayList_BookPlaceNames;
    private ArrayAdapter<String> mArrayAdapter_BookPlaceNames;
    private GoogleApiClient mGoogleApiClient_Gps;
    private Location m_Location;
    private LocationRequest m_LocationRequest;

    public BookPlaceMapFragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        m_Context = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        mArrayList_BookPlaces = (ArrayList<BookPlace>) (getArguments().getSerializable("BOOKPLACES"));

        mGoogleApiClient_Gps = new GoogleApiClient.Builder(m_Context)
                .addConnectionCallbacks(BookPlaceMapFragment.this)
                .addOnConnectionFailedListener(BookPlaceMapFragment.this)
                .addApi(LocationServices.API)
                .build();

        View view_One = layoutInflater.inflate(R.layout.fragment_bookplacemap, container, false);
        MapsInitializer.initialize(this.getActivity());
        floatingActionButton_Gps = (FloatingActionButton)view_One.findViewById(R.id.floatingactionbutton_bookplacemap_location);
        mapView_Map = (MapView) view_One.findViewById(R.id.mapview_bookplacemap_bookplacemap);
        mapView_Map.onCreate(savedInstanceState);
        mapView_Map.getMapAsync(this);

        spinner_AddressGu = (Spinner) view_One.findViewById(R.id.spinner_bookplacemap_addressgu);
        spinner_BookPlaceName = (Spinner) view_One.findViewById(R.id.spinner_bookplacemap_bookplacename);

        String[] array_AddressGu = {"강서구", "양천구", "구로구", "영등포구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구",
                "광진구", "성동구", "중구", "동대문구", "중랑구", "노원구", "도봉구", "강북구", "은평구", "서대문구", "종로구", "성북구"};
        AddressGuAdapter adapter_AddressGu = new AddressGuAdapter(m_Context, array_AddressGu);

        spinner_AddressGu.setAdapter(adapter_AddressGu);
        spinner_AddressGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mArrayList_BookPlaceNames = new ArrayList<String>();
                for (int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++) {
                    if (parent.getItemAtPosition(position).toString().equals(mArrayList_BookPlaces.get(int_Index).getAddressGu())) {
                        mArrayList_BookPlaceNames.add(mArrayList_BookPlaces.get(int_Index).getName());

                        BookPlaceNameAdapter adapter_BookPlaceName = new BookPlaceNameAdapter(m_Context, mArrayList_BookPlaceNames);

                        spinner_BookPlaceName.setAdapter(adapter_BookPlaceName);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_BookPlaceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int int_Position, long id) {
                Double double_Latitude = 0.0;
                Double double_Longitude = 0.0;

                for (int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++) {
                    if (parent.getItemAtPosition(int_Position).toString().equals(mArrayList_BookPlaces.get(int_Index).getName())) {
                        double_Latitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLatitude());
                        double_Longitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLongitude());
                        break;
                    }
                }

                LatLng latLng_Location = new LatLng(double_Latitude, double_Longitude);
                mGoogleMap_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_Location, 15));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatingActionButton_Gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view_One) {
                LocationManager locationManager = (LocationManager)m_Context.getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }else{
                    new TedPermission(m_Context)
                            .setPermissionListener(permissionListener)
                            .setRationaleMessage("현재 위치를 알기 위해서는 위치 권한이 필요합니다.")
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                }
            }
        });

        return view_One;
    }

    public PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            mGoogleApiClient_Gps.connect();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> arrayList) {
            Toast.makeText(m_Context, "권한 거부를 하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mapView_Map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient_Gps.isConnected()) {
            mGoogleApiClient_Gps.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView_Map.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView_Map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView_Map.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView_Map.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap_Map) {
        mGoogleMap_Map = googleMap_Map;

        int[] array_Pictures = {
                R.drawable.bookplace021, R.drawable.bookplace022, R.drawable.bookplace023, R.drawable.bookplace024,
                R.drawable.bookplace025, R.drawable.bookplace026, R.drawable.bookplace027, R.drawable.bookplace028,
                R.drawable.bookplace029, R.drawable.bookplace030,
                R.drawable.bookplace031, R.drawable.bookplace032, R.drawable.bookplace033, R.drawable.bookplace034,
                R.drawable.bookplace035, R.drawable.bookplace036, R.drawable.bookplace037, R.drawable.bookplace038,
                R.drawable.bookplace039, R.drawable.bookplace040,
                R.drawable.bookplace041, R.drawable.bookplace042, R.drawable.bookplace043, R.drawable.bookplace044,
                R.drawable.bookplace045, R.drawable.bookplace046, R.drawable.bookplace047, R.drawable.bookplace048,
                R.drawable.bookplace049, R.drawable.bookplace050,
                R.drawable.bookplace051, R.drawable.bookplace052, R.drawable.bookplace053, R.drawable.bookplace054,
                R.drawable.bookplace055, R.drawable.bookplace056, R.drawable.bookplace057, R.drawable.bookplace058,
                R.drawable.bookplace059, R.drawable.bookplace060,
                R.drawable.bookplace061, R.drawable.bookplace062, R.drawable.bookplace063, R.drawable.bookplace064,
                R.drawable.bookplace065, R.drawable.bookplace066, R.drawable.bookplace067, R.drawable.bookplace068,
                R.drawable.bookplace069, R.drawable.bookplace070,
                R.drawable.bookplace071, R.drawable.bookplace072, R.drawable.bookplace073, R.drawable.bookplace074,
                R.drawable.bookplace075, R.drawable.bookplace076, R.drawable.bookplace077, R.drawable.bookplace078,
                R.drawable.bookplace080,
                R.drawable.bookplace081, R.drawable.bookplace082, R.drawable.bookplace083, R.drawable.bookplace084,
                R.drawable.bookplace085, R.drawable.bookplace086, R.drawable.bookplace087, R.drawable.bookplace088,
                R.drawable.bookplace089, R.drawable.bookplace090,
                R.drawable.bookplace091, R.drawable.bookplace092, R.drawable.bookplace094,
                R.drawable.bookplace095, R.drawable.bookplace096, R.drawable.bookplace098,
                R.drawable.bookplace099, R.drawable.bookplace100,
                R.drawable.bookplace101, R.drawable.bookplace001, R.drawable.bookplace093,
                R.drawable.bookplace002, R.drawable.bookplace003, R.drawable.bookplace004,
                R.drawable.bookplace005, R.drawable.bookplace006, R.drawable.bookplace007, R.drawable.bookplace008,
                R.drawable.bookplace009, R.drawable.bookplace010,
                R.drawable.bookplace011, R.drawable.bookplace012, R.drawable.bookplace013, R.drawable.bookplace014,
                R.drawable.bookplace015, R.drawable.bookplace016, R.drawable.bookplace017, R.drawable.bookplace018,
                R.drawable.bookplace019, R.drawable.bookplace020,
                R.drawable.bookplace079, R.drawable.bookplace120,
                R.drawable.bookplace102, R.drawable.bookplace103, R.drawable.bookplace104,
                R.drawable.bookplace105, R.drawable.bookplace106, R.drawable.bookplace107, R.drawable.bookplace108,
                R.drawable.bookplace097, R.drawable.bookplace109, R.drawable.bookplace110,
                R.drawable.bookplace111, R.drawable.bookplace112, R.drawable.bookplace113, R.drawable.bookplace114,
                R.drawable.bookplace115, R.drawable.bookplace116, R.drawable.bookplace117, R.drawable.bookplace118,
                R.drawable.bookplace119,
                R.drawable.bookplace121, R.drawable.bookplace122, R.drawable.bookplace123,

                R.drawable.bookplace124, R.drawable.bookplace125, R.drawable.bookplace126, R.drawable.bookplace127,
                R.drawable.bookplace128, R.drawable.bookplace129, R.drawable.bookplace130, R.drawable.bookplace131,
                R.drawable.bookplace132, R.drawable.bookplace133, R.drawable.bookplace134, R.drawable.bookplace135,
                R.drawable.bookplace136, R.drawable.bookplace137, R.drawable.bookplace138, R.drawable.bookplace140,
                R.drawable.bookplace140, R.drawable.bookplace141, R.drawable.bookplace142, R.drawable.bookplace143,
                R.drawable.bookplace144, R.drawable.bookplace145, R.drawable.bookplace146, R.drawable.bookplace147,
                R.drawable.bookplace148, R.drawable.bookplace149, R.drawable.bookplace150, R.drawable.bookplace151,
                R.drawable.bookplace152, R.drawable.bookplace153, R.drawable.bookplace154, R.drawable.bookplace155,
                R.drawable.bookplace156, R.drawable.bookplace157, R.drawable.bookplace158, R.drawable.bookplace159,
                R.drawable.bookplace160, R.drawable.bookplace161};

        for (int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++) {
            String string_Name = mArrayList_BookPlaces.get(int_Index).getName();
            Double double_Latitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLatitude());
            Double double_Longitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLongitude());

            LatLng latLng_Location = new LatLng(double_Latitude, double_Longitude);

            MarkerOptions markerOptions_BookPlace = new MarkerOptions();
            markerOptions_BookPlace.title(string_Name);
            markerOptions_BookPlace.position(latLng_Location);
            if (mArrayList_BookPlaces.get(int_Index).getCategory() == 0) {
                markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_library_48dp));
            } else {
                markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bookstore_48dp));
            }
            MarkerTag markerTag_One = new MarkerTag(mArrayList_BookPlaces.get(int_Index), array_Pictures[int_Index]);
            Marker marker_One = googleMap_Map.addMarker(markerOptions_BookPlace);
            marker_One.setTag(markerTag_One);
        }

        mGoogleMap_Map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker_One) {
                MarkerTag markerTag_One = (MarkerTag) marker_One.getTag();

                Intent intent_BookPlaceDetail = new Intent(m_Context, BookPlaceDetailActivity.class);
                intent_BookPlaceDetail.putExtra("BOOKPLACE", markerTag_One.getBookPlace());
                intent_BookPlaceDetail.putExtra("PICTURE", markerTag_One.getPicture());
                ((Activity) m_Context).startActivity(intent_BookPlaceDetail);
                return false;
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(m_Context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_Context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        m_LocationRequest = LocationRequest.create();
        m_LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        m_LocationRequest.setInterval(5000);
        m_LocationRequest.setFastestInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient_Gps, m_LocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient_Gps.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Double double_Latitude = location.getLatitude();
        Double double_Longitude = location.getLongitude();

        LatLng latLng_Location = new LatLng(double_Latitude, double_Longitude);
        mGoogleMap_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_Location, 15));


    }
}
