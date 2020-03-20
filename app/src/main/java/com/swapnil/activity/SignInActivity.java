package com.swapnil.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    boolean checked;
    CheckBox chk;
    private TextView txtForgotPass, txtDontHaveAccount;
    private Button btnLogin;
    private EditText edtEmail, edtPasswordSignIn;
    private String strEmail, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        MyConstants.changesStatusBarColour(this);
        initView();
        onClickListener();
    }

    /**
     * This method is used as performed all click events.
     */
    private void onClickListener() {
        txtForgotPass.setOnClickListener(this);
        txtDontHaveAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    /**
     * This method is used to initialed view.
     */
    private void initView() {
        edtEmail = findViewById(R.id.edt_email);
        edtPasswordSignIn = findViewById(R.id.edt_password_sign_in);
        txtForgotPass = findViewById(R.id.txt_forgot_pass);
        txtDontHaveAccount = findViewById(R.id.txt_dont_have_account);
        btnLogin = findViewById(R.id.btn_login);

        chk = findViewById(R.id.chk_terms_condition);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = ((CheckBox) v).isChecked();
                if (checked) {
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPasswordSignIn.getText().toString().trim();
                if (isValid()) {
                    if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                        signInVerification(strEmail, strPassword);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.please_check_network_no_internet_connection_available), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.txt_forgot_pass:
               /* Intent forgotPassPage = new Intent(this, ForgotPasswordActivity.class);
                startActivity(forgotPassPage);*/
                break;

            case R.id.txt_dont_have_account:
                Intent signupPage = new Intent(this, SignUpActivity.class);
                startActivity(signupPage);
                break;
        }
    }

    /*
    Validation checks for entered information by user
     */
    private boolean isValid() {
        if (strEmail != null && strEmail.equalsIgnoreCase("")) {
            edtEmail.setError(getResources().getString(R.string.empty_email));
            edtEmail.requestFocus();
            return false;
        } else if (!MyConstants.isValidEmail(strEmail)) {
            edtEmail.setError(getResources().getString(R.string.please_enter_valid_email_id));
            edtEmail.requestFocus();
            return false;
        } else if (strPassword != null && strPassword.equalsIgnoreCase("")) {
            edtPasswordSignIn.setError(getResources().getString(R.string.empty_pass));
            edtPasswordSignIn.requestFocus();
            return false;
        } else if (checked == false) {
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.check_box), Toast.LENGTH_SHORT).show();
            edtPasswordSignIn.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    /**
     * This method is used to calling webservice of sign in screen.
     *
     * @param strEmail
     * @param strPassword
     */
    private void signInVerification(String strEmail, String strPassword) {
        MyConstants.showLoaderDialog(SignInActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.EMAIL, strEmail);
        map.put(MyConstants.PASSWORD, strPassword);
        map.put(MyConstants.DEVICE_TYPE, MyConstants.ANDROID);
        map.put(MyConstants.DEVICE_TOKEN, "hjhhjhkhjhkhjk");
        Call<ResponseBody> responseBodyCall = apiService.login(MyConstants.X_API_KEY, map);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("sign_in_api", "Response: " + jsonObject);
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
                                String weight = jsonObj.getString(MyConstants.WEIGHT);
                                String height = jsonObj.getString(MyConstants.HEIGHT);

                                SharedPreferenceUtility.getInstance(SignInActivity.this).putBoolean("LogIn", true);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.USER_ID, userId);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.NAME, userName);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.EMAIL, email);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.PHONE_NO, phoneNo);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.COUNTRY_CODE, cc);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.AUTH_TOKEN, authToken);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.WEIGHT, weight);
                                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(MyConstants.HEIGHT, height);

                               /* Intent intent = new Intent(SignInActivity.this, CustomPinActivity.class);
                                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                                intent.putExtra(MyConstants.USER_ID, userId);
                                startActivity(intent);*/

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

                                Toast.makeText(SignInActivity.this, "" + message, Toast.LENGTH_SHORT).show();

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
                Log.d("sign_in_failure", "sign_in_failure: " + t.getMessage());
            }
        });
    }
}
