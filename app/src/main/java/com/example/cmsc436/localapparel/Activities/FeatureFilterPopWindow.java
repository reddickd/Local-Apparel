package com.example.cmsc436.localapparel.Activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.DisplayMetrics;
import java.util.*;
import android.widget.Spinner;
import android.widget.CheckBox;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class FeatureFilterPopWindow extends AppCompatActivity{
    EditText brandInput,cityInput,lowRangeInput,highRangeInput;
    Button filterButton;
    Spinner spin;
    String checkS=null,checkM =null,checkL =null,checkXL = null;
    String checkNew = null, checkUsed = null;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_window);
        brandInput = (EditText)findViewById(R.id.brandInput);
        cityInput = (EditText)findViewById(R.id.cityInput);
        lowRangeInput = (EditText)findViewById(R.id.lowRange);
        highRangeInput = (EditText)findViewById(R.id.highRange);
        filterButton = (Button)findViewById(R.id.filter);

        //Creating the pop up window for filter selection page
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        spin = findViewById(R.id.spinner1);
        String[] items = new String[]{"Jacket", "Coat","Vest","Long Sleeve","Hoodie","Polo","Sweater", "Accessories","Dress","Denim","Shorts","Trousers"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spin.setAdapter(adapter);


        filterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String brandStr = brandInput.getText().toString();
                String cityStr = cityInput.getText().toString();
                String lowRangeInputStr = lowRangeInput.getText().toString();
                String highRangeInputStr = highRangeInput.getText().toString();

                Intent intentSendPackageBack = new Intent();
                intentSendPackageBack.putExtra("brand", brandStr);
                intentSendPackageBack.putExtra("city", cityStr);
                intentSendPackageBack.putExtra("lowRange", lowRangeInputStr);
                intentSendPackageBack.putExtra("highRange", highRangeInputStr);
                ArrayList<String> sizeChecked = new ArrayList<>();

                if(checkS != null)
                    sizeChecked.add(checkS);
                //intentSendPackageBack.putExtra("sizeS", checkS);
                if(checkM != null)
                    sizeChecked.add(checkM);
                //intentSendPackageBack.putExtra("sizeM", checkM);
                if(checkL != null)
                    sizeChecked.add(checkL);
                //intentSendPackageBack.putExtra("sizeL", checkL);
                if(checkXL != null)
                    sizeChecked.add(checkXL);
                //intentSendPackageBack.putExtra("sizeXL", checkXL);
                intentSendPackageBack.putStringArrayListExtra("size",sizeChecked);

                if(checkNew !=null) {
                    intentSendPackageBack.putExtra("new", checkNew);
                } else {
                    intentSendPackageBack.putExtra("used", checkUsed);
                }

                ArrayList<String> categoryPicked  = new ArrayList<>();
                for(int x = 0; x < adapter.getCount(); x++){
                    categoryPicked.add(adapter.getItem(x));
                }
                intentSendPackageBack.putStringArrayListExtra("category",categoryPicked);


                setResult(RESULT_OK,intentSendPackageBack);
                finish();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBoxS:
                if (checked)
                    checkS = "small";
            case R.id.checkBoxM:
                if (checked)
                    checkM ="medium";
            case R.id.checkBoxL:
                if (checked)
                    checkM ="large";
            case R.id.checkBoxXL:
                if (checked)
                    checkM ="xlarge";
                else
                    break;

        }
    }

    public void onCheckboxClickedCondition(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBoxNew:
                if (checked)
                    checkNew = "new";
            case R.id.checkBoxUsed:
                if (checked)
                    checkUsed ="used";
                else
                    break;

        }
    }

}





