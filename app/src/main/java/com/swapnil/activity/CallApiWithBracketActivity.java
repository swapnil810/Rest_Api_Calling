package com.swapnil.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swapnil.R;
import com.swapnil.restfullservices.ApiClient;
import com.swapnil.restfullservices.ApiInterface;
import com.swapnil.utills.MyConstants;
import com.swapnil.utills.SharedPreferenceUtility;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CallApiWithBracketActivity extends AppCompatActivity {
    private String strSwapnilId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api_with_bracket);
        strSwapnilId = SharedPreferenceUtility.getInstance(this).getString("SWAPNIL_ID");
    }

    /**
     * This method is used to call api for pass id in {} retrofit.
     */
    private void callingDemoApi() {
        final ProgressDialog progressDialog = new ProgressDialog(CallApiWithBracketActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.please_wait)); // set message
        progressDialog.show();

        ApiInterface apiSerivce = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiSerivce.getSwapnilDetailsById(MyConstants.X_API_KEY, MyConstants.AUTH, strSwapnilId);
        responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("update_data_patent", "Response: " + jsonObject);
                        if (response.body() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
                            if (status.equalsIgnoreCase("200")) {
                                JSONObject result = jsonObject.optJSONObject(MyConstants.RESULT);
                                String name = result.optString(MyConstants.NAME);
                                String email = result.optString(MyConstants.EMAIL);
                                String gender = result.optString(MyConstants.GENDER);
                                String height = result.optString(MyConstants.HEIGHT);
                                String weight = result.optString(MyConstants.WEIGHT);

                            } else if (status.equalsIgnoreCase("400")) {
                                Toast.makeText(CallApiWithBracketActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (response.errorBody() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
                            Log.d("swapnil_detail", "Error: " + message);
                            if (status.equalsIgnoreCase("400")) {
                                Toast.makeText(CallApiWithBracketActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CallApiWithBracketActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }
}