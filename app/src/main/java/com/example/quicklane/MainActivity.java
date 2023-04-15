package com.example.quicklane;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public static final String verifyuser = "";
    TextView fronterusername;
    String passusername;
    String emailrave;
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;
    BottomNavigationView navigationView;


    final int amount_1 = 5000;


    String narration = "Quicklane Payment";
    String txRef;
    String country = "UG";
    String currency = "UGX";

    final String publicKey = "Your flutterwave public key"; //Get your public key from your account
    final String encryptionKey = "Your flutterwave encryptionkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fetchApi();
        Hover.initialize(this);
        ImageButton momo = findViewById(R.id.imageButton3);
        ImageButton weatherbtn = findViewById(R.id.imageButton4);
        ImageButton map = findViewById(R.id.imageButton2);
        ImageButton btn_scan = findViewById(R.id.btn_Scan);
        ImageButton button = findViewById(R.id.imageButton);
        Intent intent = getIntent();
        passusername = intent.getStringExtra(Login.usernameprofile);

        fronterusername = findViewById(R.id.usernamefront);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");


        reference.child(passusername).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                fronterusername.setText(passusername);
                emailrave = String.valueOf(dataSnapshot.child("email"));
            }
        });



        momo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new HoverParameters.Builder(MainActivity.this)
                        .request("4dc42e43")
                        .buildIntent();
                startActivityForResult(i, 0);
            }
        });


        weatherbtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                makePayment(amount_1);
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentIntentClientSecret != null)
                    paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                            new PaymentSheet.Configuration("Quicklane", configuration));
            }
        });

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);


        btn_scan.setOnClickListener(V -> {
            scanCode();
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("https://www.google.com/maps/");
                mapIntent.setData(data);
                startActivity(Intent.createChooser(mapIntent, "Launching Map"));
            }
        });

        navigationView = findViewById(R.id.nav);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuHome:
                        break;
                    case R.id.menuHistory:
                        startActivity(new Intent(MainActivity.this, Navigation.class));
                        break;
                    case R.id.menuNotification:
                        startActivity(new Intent(MainActivity.this, weather.class));
                        break;
                    case R.id.menuProfile:
                        Intent profileIntent = new Intent(MainActivity.this, UserProfile.class);
                        profileIntent.putExtra(verifyuser, passusername);
                        startActivity(profileIntent);
                        break;
                }
                return true;
            }
        });

    }

    public void makePayment(int amount) {
        txRef = emailrave + "" + UUID.randomUUID().toString();

        /*
        Create instance of RavePayManager
         */
        new RavePayManager(this).setAmount(amount)
                .setCountry(country)
                .setCurrency(currency)
                .setEmail(emailrave)
                .setfName(passusername)
                .setNarration(narration)
                .setPublicKey(publicKey)
                .setEncryptionKey(encryptionKey)
                .setTxRef(txRef)
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .acceptGHMobileMoneyPayments(true)
                .onStagingEnv(false).
                allowSaveCardFeature(true)
                .initialize();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "You have successfully paid UGX 5000 as your Toll Gate fees " , Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR ", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "RavePay Payment Cancelled" , Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchApi() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://demo.codeseasy.com/apis/stripe/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on the flush");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose payment method");
            builder.setPositiveButton("Pay with bank", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (paymentIntentClientSecret != null)
                        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                                new PaymentSheet.Configuration("Codes Easy", configuration));
                }
            }).show();

            builder.setNegativeButton("MomoPay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new HoverParameters.Builder(MainActivity.this)
                            .request("4dc42e43")
                            .buildIntent();
                    startActivityForResult(i, 0);
                }
            }).show();

            builder.setNeutralButton("RavePay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makePayment(amount_1);
                }
            }).show();
        }
    });


}
