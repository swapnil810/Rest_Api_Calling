package com.swapnil.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.swapnil.R;
import com.swapnil.restfullservices.ApiClient;
import com.swapnil.restfullservices.ApiInterface;
import com.swapnil.utills.MyConstants;
import com.swapnil.utills.NetworkAvailablity;
import com.swapnil.utills.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ENABLE = 11;
    private TextView txtHaveAccount;
    private CountryCodePicker countryCodePicker;
    private EditText edtName, edtEmail, edtPhone, edtPasswordSignIn, edtConfirmPasswordSignIn;
    private Button btnSaveUserInfo;
    private String ccp, strName, strEmail, strPhoneNo, strPassword, strConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        onClickListener();
    }

    /**
     * This method is used as performed all click events.
     */
    private void onClickListener() {
        txtHaveAccount.setOnClickListener(this);
        btnSaveUserInfo.setOnClickListener(this);
    }

    /**
     * This method is used to initialed view.
     */
    private void initView() {
        txtHaveAccount = findViewById(R.id.txt_have_account);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtPasswordSignIn = findViewById(R.id.edt_password_sign_in);
        edtConfirmPasswordSignIn = findViewById(R.id.edt_confirm_password_sign_in);
        btnSaveUserInfo = findViewById(R.id.btn_save_user_info);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                //  ccp = countryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_have_account:
                Intent signinPage = new Intent(this, SignInActivity.class);
                startActivity(signinPage);
                break;

            case R.id.btn_save_user_info:
                strName = edtName.getText().toString().trim();
                strEmail = edtEmail.getText().toString().trim();
                strPhoneNo = edtPhone.getText().toString().trim();
                strPassword = edtPasswordSignIn.getText().toString().trim();
                strConfirmPass = edtConfirmPasswordSignIn.getText().toString().trim();
                ccp = countryCodePicker.getSelectedCountryCodeWithPlus();

                if (isValid()) {
                    if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                        signUpVerification(strName, strEmail, ccp, strPhoneNo, strPassword, strConfirmPass);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.please_check_network_no_internet_connection_available), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /*
    Validation checks for entered information by user
     */
    private boolean isValid() {
        if (strName != null && strName.equalsIgnoreCase("")) {
            edtName.setError(getResources().getString(R.string.empty_first_name));
            edtName.requestFocus();
            return false;
        } else if (strEmail != null && strEmail.equalsIgnoreCase("")) {
            edtEmail.setError(getResources().getString(R.string.empty_email));
            edtEmail.requestFocus();
            return false;
        } else if (!MyConstants.isValidEmail(strEmail)) {
            edtEmail.setError(getResources().getString(R.string.please_enter_valid_email_id));
            edtEmail.requestFocus();
            return false;
        } else if (strPhoneNo != null && strPhoneNo.equalsIgnoreCase("")) {
            edtPhone.setError(getResources().getString(R.string.empty_phone_no));
            edtPhone.requestFocus();
            return false;
        } else if (strPhoneNo.length() < 10) {
            edtPhone.setError(getResources().getString(R.string.phone_no_error));
            edtPhone.requestFocus();
            return false;
        } else if (strPassword != null && strPassword.equalsIgnoreCase("")) {
            edtPasswordSignIn.setError(getResources().getString(R.string.empty_pass));
            edtPasswordSignIn.requestFocus();
            return false;
        } else if (strPassword.length() < 6) {
            edtPasswordSignIn.setError(getResources().getString(R.string.valid_pass));
            edtPasswordSignIn.requestFocus();
            return false;
        } else if (strConfirmPass != null && strConfirmPass.equalsIgnoreCase("")) {
            edtConfirmPasswordSignIn.setError(getResources().getString(R.string.empty_pass_confirm));
            edtConfirmPasswordSignIn.requestFocus();
            return false;
        } else if (!strPassword.equals(strConfirmPass)) {
            edtConfirmPasswordSignIn.setError(getResources().getString(R.string.password_do_not));
            edtConfirmPasswordSignIn.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void signUpVerification(String strName, String strEmail, String ccp, String strPhoneNo, String strPassword, String strConfirmPass) {
        MyConstants.showLoaderDialog(SignUpActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.NAME, strName);
        map.put(MyConstants.EMAIL, strEmail);
        map.put(MyConstants.PHONE_NO, strPhoneNo);
        map.put(MyConstants.PASSWORD, strPassword);
        map.put(MyConstants.CONFIRM_PASSWORD, strConfirmPass);
        map.put(MyConstants.COUNTRY_CODE, ccp);
        map.put(MyConstants.DEVICE_TYPE, MyConstants.ANDROID);
        map.put(MyConstants.DEVICE_TOKEN, "null");
        Call<ResponseBody> responseBodyCall = apiService.signup(MyConstants.X_API_KEY, /*MyConstants.AUTH,*/ map);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("sign_up_api", "Response: " + jsonObject);
                        MyConstants.hideLoaderDialog();
                        if (response.body() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
                            if (status.equalsIgnoreCase("200")) {
                                JSONObject jsonObj = jsonObject.optJSONObject(MyConstants.RESULT);
                                String userId = jsonObj.getString(MyConstants.USER_ID);
                                String userName = jsonObj.getString(MyConstants.NAME);
                                String email = jsonObj.getString(MyConstants.EMAIL);
                                String phoneNo = jsonObj.getString(MyConstants.PHONE_NO);
                                String cc = jsonObj.getString(MyConstants.COUNTRY_CODE);
                                String authToken = jsonObj.getString(MyConstants.AUTH_TOKEN);

                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.USER_ID, userId);
                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.NAME, userName);
                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.EMAIL, email);
                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.PHONE_NO, phoneNo);
                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.COUNTRY_CODE, cc);
                                SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(MyConstants.AUTH_TOKEN, authToken);

                                // SharedPreferenceUtility.getInstance(SignUpActivity.this).clearAll();

                            }
                        }
                    } catch (Exception e) {
                        MyConstants.hideLoaderDialog();
                        e.printStackTrace();
                    }
                } else {
                    MyConstants.hideLoaderDialog();
                    if (response.code() == 400) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            if (response.errorBody() != null) {
                                String status = jsonObject.optString(MyConstants.STATUS);
                                String message = jsonObject.optString(MyConstants.MESSAGE);
                                if (status.equalsIgnoreCase("400")) {
                                    Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyConstants.hideLoaderDialog();
                Log.d("sign_up_failure", "sign_up_failure: " + t.getMessage());
            }
        });
    }
}