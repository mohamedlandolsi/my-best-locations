package com.example.mybestlocations.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mybestlocations.Config; // Add this import

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    protected String doInBackground(String... params) {
        try {
            String pseudo = params[0];
            String numero = params[1];
            String longitude = params[2];
            String latitude = params[3];

            URL url = new URL(Config.URL_ADDPOSITION +
                    "?pseudo=" + URLEncoder.encode(pseudo, "UTF-8") +
                    "&numero=" + URLEncoder.encode(numero, "UTF-8") +
                    "&longitude=" + URLEncoder.encode(longitude, "UTF-8") +
                    "&latitude=" + URLEncoder.encode(latitude, "UTF-8"));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Additional code for handling the connection can be added here

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void savePosition(String pseudo, String numero, String longitude, String latitude) {
        // Implement API call to save the position
        // This might involve creating an AsyncTask or using another background processing method
        
        // Example implementation (you'll need to adapt this to match your existing code pattern)
        new Thread(() -> {
            try {
                URL url = new URL(Config.URL_ADDPOSITION + 
                    "?pseudo=" + URLEncoder.encode(pseudo, "UTF-8") + 
                    "&numero=" + URLEncoder.encode(numero, "UTF-8") + 
                    "&logitude=" + URLEncoder.encode(longitude, "UTF-8") + 
                    "&latitude=" + URLEncoder.encode(latitude, "UTF-8"));
                
                // Make the HTTP request
                // Process the response
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}