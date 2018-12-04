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
import android.widget.AdapterView;
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
import android.widget.AdapterView.OnItemSelectedListener;
import com.google.firebase.auth.FirebaseUser;
public class FeatureFilterPopWindow extends AppCompatActivity implements OnItemSelectedListener{
    EditText brandInput,lowRangeInput,highRangeInput,radius;
    Button filterButton;
    Spinner spin;
    String checkS=null,checkM =null,checkL =null,checkXL = null;
    String checkNew = null, checkUsed = null;
    ArrayAdapter<String> adapter;
    ArrayList<String> categoryPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_window);
        brandInput = (EditText)findViewById(R.id.brandInput);

        lowRangeInput = (EditText)findViewById(R.id.lowRange);
        highRangeInput = (EditText)findViewById(R.id.highRange);
        filterButton = (Button)findViewById(R.id.filter);
        radius = (EditText)findViewById(R.id.radiusInput);
        categoryPicked  = new ArrayList<String>();
        //Creating the pop up window for filter selection page
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        spin = findViewById(R.id.spinner1);
        spin.setOnItemSelectedListener(this);
        String[] items = new String[]{"","Jacket", "Coat","Vest","Long Sleeve","Shirt","Hoodie","Polo","Sweater", "Accessories","Dress","Denim","Shorts","Trousers"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);


        filterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String brandStr = brandInput.getText().toString();

                String lowRangeInputStr = lowRangeInput.getText().toString();
                String highRangeInputStr = highRangeInput.getText().toString();
                String radiusInput = radius.getText().toString();

                Intent intentSendPackageBack = new Intent();
                intentSendPackageBack.putExtra("brand", brandStr);

                intentSendPackageBack.putExtra("lowRange", lowRangeInputStr);
                intentSendPackageBack.putExtra("highRange", highRangeInputStr);
                intentSendPackageBack.putExtra("radius", radiusInput);

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
                if(!sizeChecked.isEmpty())
                    intentSendPackageBack.putStringArrayListExtra("size",sizeChecked);

                if(checkNew !=null) {
                    intentSendPackageBack.putExtra("new", checkNew);
                }
                if(checkUsed !=null) {
                    intentSendPackageBack.putExtra("used", checkUsed);
                }
                /*
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                */

                //Log.d("Hi2","Got in here");
                //for(int x = 0; x < adapter.getCount(); x++){
                //   Log.d("Hi2","Got in here333");
                //Log.d("Bye",adapter.getItem(x));
                //   categoryPicked.add(adapter.getItem(x));
                //  Log.d("Hi2","Added:"+ adapter.getItem(x));
                //}
                if(!categoryPicked.isEmpty()){
                    //Log.d("Hi2","SHOULD GO IN HERE" + categoryPicked.get(0));
                    //Log.d("Hi2","SHOULD GO IN HERE" + categoryPicked.size());
                    categoryPicked.remove(0);
                    intentSendPackageBack.putStringArrayListExtra("category",categoryPicked);
                }


                setResult(RESULT_OK,intentSendPackageBack);
                finish();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        categoryPicked.add(item);
        // Showing selected spinner item

    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();


        switch(view.getId()) {
            case R.id.checkBoxS:
                if (checked)
                    checkS = "S";
                break;
            case R.id.checkBoxM:
                if (checked)
                    checkM ="M";
                break;
            case R.id.checkBoxL:
                if (checked)
                    checkM ="L";
                break;
            case R.id.checkBoxXL:
                if (checked)
                    checkM ="XL";
                else
                    break;

        }
    }

    public void onCheckboxClickedCondition(View view) {
        //CheckBox newCheck = (CheckBox)view.findViewById(R)
        boolean checked = ((CheckBox) view).isChecked();


        switch(view.getId()) {
            case R.id.checkBoxNew:
                if (checked)
                    checkNew = "New";
                break;
            case R.id.checkBoxUsed:
                if (checked)
                    checkUsed ="Used";
                else
                    break;

        }
    }

}





