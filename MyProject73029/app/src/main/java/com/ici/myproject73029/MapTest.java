package com.ici.myproject73029;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ici.myproject73029.items.Show;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class MapTest extends AppCompatActivity{
    private MapView mapView;
    private TextView venue;
    private List<Address> list=null;
    private MapPOIItem marker;
    private String strVenus;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        mapView=new MapView(this);
        final Geocoder geocoder=new Geocoder(this);
        viewGroup=findViewById(R.id.map_view);
        venue=findViewById(R.id.item_venue);
        Intent intent=getIntent();
        strVenus=intent.getStringExtra("venue");

        try {
            list=geocoder.getFromLocationName(strVenus,100);
            Log.d("위도경도",list.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list!=null){
            if(list.size()==0){
                Toast.makeText(this,"주소 입력",Toast.LENGTH_SHORT).show();
            }else{
                double lat=list.get(0).getLatitude();
                double lon=list.get(0).getLongitude();
                MapPoint mapPoint=MapPoint.mapPointWithGeoCoord(lat,lon);
                mapView.setMapCenterPoint(mapPoint,true);
                viewGroup.addView(mapView);
                marker=new MapPOIItem();
                marker.setItemName(strVenus);
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                mapView.addPOIItem(marker);
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        viewGroup.removeView(mapView);
    }

}