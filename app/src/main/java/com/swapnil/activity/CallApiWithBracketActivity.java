package com.swapnil.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.swapnil.R;
import com.swapnil.restfullservices.ApiClient;
import com.swapnil.restfullservices.ApiInterface;
import com.swapnil.utills.MyConstants;
import com.swapnil.utills.SharedPreferenceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApiWithBracketActivity extends AppCompatActivity {
    private String strSwapnilId;

    private ArrayList<ConnectionModel> arrayListResponse = new ArrayList<>();
    private Type arrayToList = new TypeToken<ArrayList<ConnectionModel>>() {
    }.getType();

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

    //===============================================Pass JsonObject data & calling api =======================================================//


 /*   {
        "contact_details" : [
        {
            "facebook_id" : "2049794321830791",
                "device_contact_name" : "Swapnil Sharma"
        },
        {
            "facebook_id" : "2576919922572662",
                "device_contact_name" : "Canopus Rise"
        }
 ]
    }*/


    public JsonArray jsonRequest() {
        JsonArray jsonArray = new JsonArray();
        try {
            for (int i = 0; i < arrayListResponse.size(); i++) {
                int b = arrayListResponse.get(i).getIsConnected();
                if (b == 1) {
                    JsonObject jsonObject1 = new JsonObject();
                    if (arrayListResponse.get(i).getUserId() != null)
                        jsonObject1.addProperty("user_id", arrayListResponse.get(i).getUserId());

                    jsonObject1.addProperty("device_contact_name", arrayListResponse.get(i).getDeviceContactName());

                    if (arrayListResponse.get(i).getFacebook_id() != null)
                        jsonObject1.addProperty("facebook_id", arrayListResponse.get(i).getFacebook_id());
                    jsonArray.add(jsonObject1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void addContactApi() {
        MyConstants.showLoaderDialog(CallApiWithBracketActivity.this);
        String authToken = SharedPreferenceUtility.getInstance(CallApiWithBracketActivity.this).getString(MyConstants.AUTH_TOKEN);
        String strUserId = SharedPreferenceUtility.getInstance(CallApiWithBracketActivity.this).getString(strSwapnilId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        JsonObject postData = new JsonObject();
        postData.add("contact_details", jsonRequest());
        Call<ResponseBody> responseBodyCall = apiService.addContact(MyConstants.X_API_KEY,
                MyConstants.AUTH, strUserId, authToken, postData);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    MyConstants.hideLoaderDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (response.body() != null) {
                            JSONArray resultArray = jsonObject.optJSONArray(MyConstants.RESULT);
                            arrayListResponse = new Gson().fromJson(resultArray.toString(), arrayToList);
                        }

                    } catch (Exception e) {
                        MyConstants.hideLoaderDialog();
                        e.printStackTrace();
                        if (e.toString().equalsIgnoreCase("java.lang.IllegalArgumentException: Path must not be empty.")) {
                        }
                    }
                } else if (response.code() == 400) {
                    MyConstants.hideLoaderDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (response.errorBody() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
                            Toast.makeText(CallApiWithBracketActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            if (status.equalsIgnoreCase("4000")) {
                                Toast.makeText(CallApiWithBracketActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyConstants.hideLoaderDialog();
                    }
                } else if (response.code() == 401) {
                    MyConstants.hideLoaderDialog();
                } else {
                    MyConstants.hideLoaderDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (response.errorBody() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
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
                MyConstants.hideLoaderDialog();
                Toast.makeText(CallApiWithBracketActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    //===============================================Using @FormUrlEncoded Format calling retrofit2 api=======================================================//

    public void requestCode(String requestCode) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> obj = new HashMap<>();
        obj.put(MyConstants.CODE, requestCode);
        Call<ResponseBody> result = apiInterface.CheckEcodeApi(obj);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("code_Api", "Response: " + jsonObject);
                        if (response.body() != null) {
                            String code = jsonObject.optString("code");
                            String status = jsonObject.optString(MyConstants.STATUS);
                            if (code.equalsIgnoreCase("200")) {
                                JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                                String codeCopy = jsonObject1.optString("codeCopy");

                            } else if (code.equalsIgnoreCase("400")) {
                                String message2 = jsonObject.optString(MyConstants.MESSAGE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        if (response.errorBody() != null) {
                            String status = jsonObject.optString(MyConstants.STATUS);
                            String message = jsonObject.optString(MyConstants.MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    Toast.makeText(CallApiWithBracketActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}