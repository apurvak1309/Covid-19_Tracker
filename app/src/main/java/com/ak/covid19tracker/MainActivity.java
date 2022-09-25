package com.ak.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<District> districtList;
    Adapter adapter;
    private static String JSON_URL="https://data.covid19india.org/v2/state_district_wise.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.districtList);
        districtList=new ArrayList<>();
        extract();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }
    private void filter(String text) {
        ArrayList<District> filteredlist = new ArrayList<District>();
        for (District item : districtList) {
            if (item.getDistrict().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }
    private void extract() {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject distrctObject = response.getJSONObject(i);
                        JSONArray jsonChild = distrctObject.getJSONArray("districtData");
                        District district = new District();
                        district.setDistrict(distrctObject.getString("state").toString());
                        int total=0;
                        for (int k = 0; k < jsonChild.length(); k++){
                            JSONObject obj =  jsonChild.getJSONObject(k);
                            int confirmed=obj.getInt("confirmed");
                            total+=confirmed;
                            district.setCases(Integer.toString(total));
                        }
                        districtList.add(district);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject distrctObject = response.getJSONObject(i);
                        JSONArray jsonChild = distrctObject.getJSONArray("districtData");
                        for (int k = 0; k < jsonChild.length(); k++){
                            JSONObject obj =  jsonChild.getJSONObject(k);
                            District district = new District();
                            district.setDistrict(obj.getString("district").toString());
                            district.setCases(obj.getString("confirmed"));
                            districtList.add(district);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter=new Adapter(getApplicationContext(),districtList);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}