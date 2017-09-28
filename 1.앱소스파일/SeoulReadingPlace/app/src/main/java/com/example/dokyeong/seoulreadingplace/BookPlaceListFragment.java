package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by DoKyeong on 2016. 8. 23..
 */
public class BookPlaceListFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{
    // 현재위치 잡을수있으면 해당구 출력
    // 현재위치 잡을수없으면 종로구 출력
    private Context mContext_System;
    private RecyclerView recyclerView_BookPlaceList;
    private Spinner spinner_AddressGu;
    private FloatingActionButton floatingActionButton_Map;

    private ArrayList<BookPlace> mArrayList_BookPlaces;
    private ArrayList<String> mArrayList_BookPlaceNames;
    private BookPlaceListAdapter mAdapter_BookPlaceList;

    private GoogleApiClient mGoogleApiClient_Gps;
    private Location m_Location;
    private LocationRequest m_LocationRequest;
    private int mInt_Count = 0;
    private int mInt_Selection = 0;

    private String[] mArray_AddressGu = {"강서구", "양천구", "구로구", "영등포구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구",
            "광진구", "성동구", "중구", "동대문구", "중랑구", "노원구", "도봉구", "강북구", "은평구", "서대문구", "종로구", "성북구"};

    int[] mArray_Pictures = {
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
            R.drawable.bookplace095, R.drawable.bookplace096,  R.drawable.bookplace098,
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

    public BookPlaceListFragment(){

    }

    public void onAttach(Context context){
        super.onAttach(context);

        mContext_System = context;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        mArrayList_BookPlaces = (ArrayList<BookPlace>)(getArguments().getSerializable("BOOKPLACES"));

        mGoogleApiClient_Gps = new GoogleApiClient.Builder(mContext_System)
                .addConnectionCallbacks(BookPlaceListFragment.this)
                .addOnConnectionFailedListener(BookPlaceListFragment.this)
                .addApi(LocationServices.API)
                .build();

        View view_One = layoutInflater.inflate(R.layout.fragment_bookplacelist, container, false);

        AddressGuAdapter adapter_AddressGu = new AddressGuAdapter(mContext_System, mArray_AddressGu);

        spinner_AddressGu = (Spinner)view_One.findViewById(R.id.spinner_bookplacelist_addressgu);
        spinner_AddressGu.setAdapter(adapter_AddressGu);
        spinner_AddressGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<BookPlace> arrayList_BookPlaces = new ArrayList<BookPlace>();
                ArrayList<Integer> arrayList_Pictures = new ArrayList<Integer>();
                for(int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++){
                    if(parent.getItemAtPosition(position).toString().equals(mArrayList_BookPlaces.get(int_Index).getAddressGu())){
                        arrayList_BookPlaces.add(mArrayList_BookPlaces.get(int_Index));
                        arrayList_Pictures.add(mArray_Pictures[int_Index]);
                    }
                }
                mAdapter_BookPlaceList = new BookPlaceListAdapter(mContext_System, arrayList_BookPlaces, arrayList_Pictures);
                recyclerView_BookPlaceList.setAdapter(mAdapter_BookPlaceList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView_BookPlaceList = (RecyclerView)view_One.findViewById(R.id.recyclerview_bookplacelist_bookplacelist);
        recyclerView_BookPlaceList.setHasFixedSize(true);
        recyclerView_BookPlaceList.setLayoutManager(new LinearLayoutManager(mContext_System));
        //mAdapter_BookPlaceList = new BookPlaceListAdapter(mContext_System, mArrayList_BookPlaces);
        //recyclerView_BookPlaceList.setAdapter(mAdapter_BookPlaceList);

        floatingActionButton_Map = (FloatingActionButton)view_One.findViewById(R.id.floatingactionbutton_bookplacelist_map);
        floatingActionButton_Map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                LocationManager locationManager = (LocationManager)mContext_System.getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }else{
                    new TedPermission(mContext_System)
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
            Toast.makeText(mContext_System, "권한 거부를 하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient_Gps.isConnected()) {
            mGoogleApiClient_Gps.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext_System, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext_System, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        Geocoder geocoder_AddressGu = new Geocoder(mContext_System, Locale.KOREA);
        List<Address> list_Address;
        String string_Content = "";
        try{
            if(geocoder_AddressGu != null){
                list_Address = geocoder_AddressGu.getFromLocation(double_Latitude, double_Longitude, 1);

                if(list_Address != null && list_Address.size() > 0){
                    Address address = list_Address.get(0);

                    String string_AddressLine = "";
                    String string_AdminArea = "";
                    String string_CountryName = "";
                    String string_FeatureName = "";
                    String string_Locality = "";
                    String string_Premises = "";
                    String string_SubLocality = "";
                    String string_SubThoroughfare = "";
                    String string_Thoroughfare = "";

                    string_AddressLine = address.getAddressLine(0);
                    string_AdminArea = address.getAdminArea(); // 서울특별시
                    string_Locality = address.getLocality(); // 구

                    string_Content = string_AddressLine + "," + string_AdminArea + "," + string_CountryName + "," +
                            string_FeatureName + "," + string_Locality + "," + string_Premises + "," + string_SubLocality + "," +
                            string_SubThoroughfare+ "," + string_Thoroughfare;

                    if(string_AdminArea.equals("서울특별시")){
                        for(int int_OuterIndex = 0; int_OuterIndex < mArray_AddressGu.length; int_OuterIndex++){
                            if(string_Locality.equals(mArray_AddressGu[int_OuterIndex])){
                                //Toast.makeText(mContext_System, mArray_AddressGu[int_OuterIndex], Toast.LENGTH_SHORT).show();
                                spinner_AddressGu.setSelection(int_OuterIndex);
                                mGoogleApiClient_Gps.disconnect();
                                break;
                            }
                        }
                    }else{
                        Toast.makeText(mContext_System, "서울이 아닙니다.", Toast.LENGTH_SHORT).show();
                        mInt_Count = mInt_Count + 1;
                        if (mInt_Count == 5 && mGoogleApiClient_Gps.isConnected()) {
                            mGoogleApiClient_Gps.disconnect();
                        }
                    }
                }
            }
        }catch(IOException e){

        }

    }
}
