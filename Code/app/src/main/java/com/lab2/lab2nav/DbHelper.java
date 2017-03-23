package com.lab2.lab2nav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "Address.db";
    private static final String TABLE_NAME = "address";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PROPERTY = "property";
    private static final String COLUMN_STREET = "street";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_ZIP = "zip";
    private static final String COLUMN_LOAN = "loan";
    private static final String COLUMN_APR = "apr";
    private static final String COLUMN_MONTHLY = "monthly";
    private static final String LAT = "latitude";
    private static final String LON = "longitude";

    public static final String SQL_CREATE =
            "create table " + TABLE_NAME + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_PROPERTY + " text, " +
                    COLUMN_STREET + " text, " +
                    COLUMN_CITY + " text, " +
                    COLUMN_STATE + " text, " +
                    COLUMN_ZIP + " text, " +
                    COLUMN_LOAN + " real, " +
                    COLUMN_APR + " real, " +
                    COLUMN_MONTHLY + " real, " +
                    LAT + " text, "+
                    LON+" text);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists address;");
        onCreate(db);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    public boolean insertData(String property, String street, String city, String state, String zip, double loan, double apr, double monthly) {
        SQLiteDatabase db = this.getWritableDatabase();

        //adding to validate address - Rohan
        String location = apr + ", " + street + ", " + city + ", " + state + ", " + zip + ", USA";
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(mContext);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //addressQueue.add(latLng);
                //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                ContentValues contentValues = new ContentValues();
                System.out.println("*************INSERTING**************" + monthly);
                contentValues.put("property", property);
                contentValues.put("street", street);
                contentValues.put("city", city);
                contentValues.put("state", state);
                contentValues.put("zip", zip);
                contentValues.put("loan", loan);
                contentValues.put("apr", apr);
                contentValues.put("monthly", monthly);
                contentValues.put("latitude",String.valueOf(latLng.latitude));
                contentValues.put("longitude",String.valueOf(latLng.longitude));
                long id = db.insert(TABLE_NAME, null, contentValues);
                return true;

            } else {
                Toast.makeText(mContext, "Invalid City", Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> mapData = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resultSet = db.rawQuery("Select * from address",null);
        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            /*Log.d("ADDRESS : city",resultSet.getString(resultSet.getColumnIndex("city")));
            Log.d("ADDRESS : lat :",resultSet.getString(resultSet.getColumnIndex("latitude")));
            Log.d("ADDRESS : lon :",resultSet.getString(resultSet.getColumnIndex("longitude")));*/
            mapData.add(
                            resultSet.getString(resultSet.getColumnIndex("id"))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("property")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("property")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("street")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("street")))+","+
                                            (resultSet.getString(resultSet.getColumnIndex("city")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("city")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("state")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("state")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("zip")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("zip")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("loan")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("loan")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("apr")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("apr")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("monthly")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("monthly")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("latitude")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("latitude")))+","+
                                    (resultSet.getString(resultSet.getColumnIndex("longitude")).trim().length()==0?"#":resultSet.getString(resultSet.getColumnIndex("longitude"))));
            resultSet.moveToNext();
        }
        resultSet.close();
        return mapData;
    }

}
