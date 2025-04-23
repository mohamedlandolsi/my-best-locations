package com.example.mybestlocations.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybestlocations.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup button click listeners
        binding.btnEnregistrer.setOnClickListener(view -> saveData());
        binding.btnRetour.setOnClickListener(view -> getActivity().onBackPressed());
        binding.btnMap.setOnClickListener(view -> openMap());

        return root;
    }

    private void saveData() {
        // Get data from form fields
        String pseudo = binding.editPseudo.getText().toString().trim();
        String numero = binding.editNumero.getText().toString().trim();
        String longitude = binding.editLongitude.getText().toString().trim();
        String latitude = binding.editLatitude.getText().toString().trim();

        // Basic validation
        if (pseudo.isEmpty() || numero.isEmpty() || longitude.isEmpty() || latitude.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use ViewModel to save the data
        galleryViewModel.savePosition(pseudo, numero, longitude, latitude);
        
        // You can observe the save result here if needed
        
        Toast.makeText(getContext(), "Position enregistrée", Toast.LENGTH_SHORT).show();
        clearFormFields();
    }

    private void clearFormFields() {
        binding.editPseudo.setText("");
        binding.editNumero.setText("");
        binding.editLongitude.setText("");
        binding.editLatitude.setText("");
    }

    private void openMap() {
        // Implement map functionality here
        Toast.makeText(getContext(), "Ouverture de la carte", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}