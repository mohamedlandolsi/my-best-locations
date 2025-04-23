package com.example.mybestlocations.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mybestlocations.Config;
import com.example.mybestlocations.JSONParser;
import com.example.mybestlocations.Position;
import com.example.mybestlocations.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    ArrayList<Position> data = new ArrayList<Position>();
    private FragmentHomeBinding binding;
    private AlertDialog alert;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        return root;
    }

    private void loadData() {
        // Show loading dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Téléchargement");
        dialog.setMessage("Veuillez patienter...");
        alert = dialog.create();
        alert.show();

        Log.d(TAG, "Starting data load process");
        
        // Use ExecutorService instead of AsyncTask
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Background work
                JSONParser parser = new JSONParser();
                HashMap<String, String> params = new HashMap<String, String>();
                
                try {
                    Log.d(TAG, "Making network request to: " + Config.URL_GETALL);
                    JSONObject result = parser.makeRequest(Config.URL_GETALL);
                    
                    if (result == null) {
                        showError("Network request failed - null response");
                        return;
                    }
                    
                    Log.d(TAG, "Response received: " + result.toString());

                    try {
                        int success = result.getInt("success");
                        Log.d(TAG, "Success value: " + success);

                        if (success == 0) {
                            String message = result.getString("message");
                            Log.e(TAG, "API error: " + message);
                            showError("API returned error: " + message);
                        } else {
                            JSONArray tableau = result.getJSONArray("positions");
                            Log.d(TAG, "Found " + tableau.length() + " positions");
                            
                            data.clear();
                            for (int i = 0; i < tableau.length(); i++) {
                                JSONObject ligne = tableau.getJSONObject(i);

                                int idposition = ligne.getInt("idposition");
                                String pseudo = ligne.getString("pseudo");
                                String numero = ligne.getString("numero");
                                String logitude = ligne.getString("logitude");
                                String latitude = ligne.getString("latitude");

                                Position p = new Position(idposition, pseudo, numero, logitude, latitude);
                                data.add(p);
                                Log.d(TAG, "Added position: " + p.toString());
                            }
                            
                            // Add this to see if data is populated
                            Log.d(TAG, "Total positions loaded: " + data.size());
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                        showError("Error parsing data: " + e.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception in network call", e);
                    showError("Network error: " + e.getMessage());
                }

                // Update UI on the main thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (alert != null && alert.isShowing()) {
                                alert.dismiss();
                            }
                            
                            if (binding != null) {
                                if (data.isEmpty()) {
                                    Toast.makeText(getActivity(), "No positions found", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Create a custom adapter to properly display the positions
                                    ArrayAdapter<Position> adapter = new ArrayAdapter<Position>(
                                            getActivity(), 
                                            android.R.layout.simple_list_item_1, 
                                            data);
                                    binding.listView.setAdapter(adapter);
                                    
                                    Log.d(TAG, "ListView adapter set with " + data.size() + " items");
                                    Toast.makeText(getActivity(), data.size() + " positions loaded", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e(TAG, "Binding is null when updating UI");
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Activity is null when updating UI");
                }
            }
        });
        executor.shutdown();
    }

    private void showError(final String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (alert != null && alert.isShowing()) {
                        alert.dismiss();
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error: " + message);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alert != null && alert.isShowing()) {
            alert.dismiss();
        }
        binding = null;
    }
}