package com.example.maacilaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Object;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.json.JSONObject.*;


public class MainActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 4 characters
                    "$");

    private static final Pattern PHONENUMBER_PATTERN =
            Pattern.compile("^" +"(?:\\+62)?0?8\\d{2}(\\d{8})$");

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText address;
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText password;
    private com.google.android.material.button.MaterialButton registerButton;
    private static String URL_REGIST = "http://192.168.0.6/android_register_login/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
                //Regist();
            }
        });
    }



    private boolean validatefirstName() {
        String usernameInput = firstName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            firstName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 10) {
            firstName.setError("name too long");
            return false;
        } else {
            firstName.setError(null);
            return true;
        }
    }

    private boolean validatelastName() {
        String usernameInput = lastName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            lastName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 10) {
            lastName.setError("name too long");
            return false;
        } else {
            lastName.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        String usernameInput = address.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            address.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            address.setError("address too long");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatephoneNumber() {
        String phoneNumberInput = phoneNumber.getText().toString().trim();

        if (phoneNumberInput.isEmpty()) {
            phoneNumber.setError("Field can't be empty");
            return false;
        } else if (phoneNumberInput.length() > 13) {
            phoneNumber.setError("number too long");
            return false;
        } else if (!PHONENUMBER_PATTERN.matcher(phoneNumberInput).matches()) {
            phoneNumber.setError("Phone Number Invalid");
            return false;
        }else {
            phoneNumber.setError(null);
            return true;
        }
    }

    public void confirmInput() {
        if (!validatefirstName() | !validatelastName() | !validateAddress()| !validateEmail() |
                !validatePassword() | !validatephoneNumber()) {
            return;
        }


        final String firstName = this.firstName.getText().toString().trim();
        final String lastName = this.lastName.getText().toString().trim();
        final String address = this.address.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password  = this.password.getText().toString().trim();
        final String phoneNumber = this.phoneNumber.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(MainActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Register Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Register Error" + error, Toast.LENGTH_SHORT).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("address", address);
                params.put("email", email);
                params.put("password", password);
                params.put("phoneNumber", phoneNumber);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //private void Regist(){ }


}

