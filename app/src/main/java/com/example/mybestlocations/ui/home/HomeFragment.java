package com.example.mybestlocations.ui.home;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybestlocations.Config;
import com.example.mybestlocations.JSONParser;
import com.example.mybestlocations.Position;
import com.example.mybestlocations.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    ArrayList<Position> data = new ArrayList<Position>();

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Telechargement t = new Telechargement();
                t.execute();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Telechargement extends AsyncTask {
        AlertDialog alert;
        @Override
        protected void onPreExecute() {
            // UI Thread = MainThread
            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeFragment.this.getActivity());
            dialog.setTitle("Téléchargement");
            dialog.setMessage("Veuillez patienter...");

            alert = dialog.create();
            alert.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            // Second Thread
            JSONParser parser = new JSONParser();
            HashMap<String, String> params = new HashMap<String, String>();

            JSONObject result = parser.makeRequest(Config.URL_GETALL);
            Log.e("response", result.toString());

            try {
                int success = result.getInt("success");

                if (success == 0) {
                    String message = result.getString("message");
                    Log.e("message", message);
                }
                else {
                    JSONArray tableau = result.getJSONArray("positions");
                    data.clear();
                    for (int i = 0; i < tableau.length(); i++) {
                        JSONObject ligne = tableau.getJSONObject(i);

                        int idposition=ligne.getInt("idposition");
                        String pseudo=ligne.getString("pseudo");
                        String numero=ligne.getString("numero");
                        String logitude=ligne.getString("logitude");
                        String latitude=ligne.getString("latitude");

                        Position p = new Position(idposition, pseudo, numero, logitude, latitude);
                        data.add(p);
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            // UI Thread
            alert.dismiss();
            ArrayAdapter ad = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
            binding.listView.setAdapter(ad);
        }
    }
}