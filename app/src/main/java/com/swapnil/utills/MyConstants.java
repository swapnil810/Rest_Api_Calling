package com.swapnil.utills;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.stetho.okhttp3.BuildConfig;
//import com.swapnil.BuildConfig;
import com.swapnil.R;

public class MyConstants {
    public static final String DESCRIPTION = "description";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String DEVICE_TYPE = "device_type";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String X_API_KEY = "2e7793e8e6580ba8b80fc457829590c6";
    public static final String AUTH = "#";
    public static final String ANDROID = "android";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String COUNTRY_CODE = "country_code";
    public static final String NAME = "name";
    public static final String PHONE_NO = "phone_number";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String USER_ID = "user_id";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String GENDER = "gender";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String PICTURE_TYPE = "picture_type";
    public static final String BASE_64_STRING = "base64_string";
    public static final String FRONT_SIDE_PIC = "front_side_pic";
    public static final String RIGHT_SIDE_PIC = "right_side_pic";
    public static final String LEFT_SIDE_PIC = "left_side_pic";
    public static final String BACK_SIDE_PIC = "back_side_pic";
    public static final String BACK_GROUND_SIDE_PIC = "back_ground_pic";
    public static final String CATEGORY_IMAGE = "category_image";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String TRY_ON_IMAGE = "try_on_image";
    public static final String PRODUCT_IMAGE = "product_image";
    public static final String CATEGORY_ID = "category_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String BG_IMAGE = "bg_image";
    public static final String FRONT_VIEW_IMAGE = "front_view_image";
    public static final String SIDE_VIEW_IMAGE = "side_view_image";
    public static final String MODEL_3D_BODY = "model_3d_body";
    public static final String MULTIWIDTHS = "multiwidths";
    public static final String FILTER_TYPE = "filter_type";
    public static final String BRAND = "brand";
    public static final String SIZE = "size";
    public static final String COLOR = "color";
    public static final String MIN_PRICE = "min_price";
    public static final String MAX_PRICE = "max_price";
    public static final String ERROR = "error";
    public static final String DATA = "data";
    public static final String NECK = "neck";
    public static final String CHEST = "chest";
    public static final String HAND = "hand";
    public static final String WAIST = "waist";
    public static final String LEG = "leg";
    public static final String HIPS = "neck";
    public static final String MY_FILE_PATH = "my_file_path";
    public static final String PROFILE_PIC = "profile_pic";
    public static Dialog progressDialog;

    /**
     * This method is used to changed status bar colour.
     *
     * @param activity
     */
    public static void changesStatusBarColour(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_top_bar));
        }
    }

    /**
     * This method is used to changed status bar colour white.
     *
     * @param activity
     */
    public static void changesStatusBarColourWhite(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }

    /**
     * This method is used to changed status bar colour white.
     *
     * @param activity
     */
    public static void changesStatusBarColourGray(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(activity.getResources()
                    .getColor(R.color.app_top_bar));
        }
    }


    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    /**
     * this method will initialize all dialogs.
     */
    public static void showLoaderDialog(Context context) {
        // if (progressDialog == null)
        progressDialog = new Dialog(context);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void hideLoaderDialog() {
        progressDialog.dismiss();
    }


    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int size = listAdapter.getCount();
            for (int i = 0; i < size; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//            if (Constants.LOGGING)
            if (BuildConfig.DEBUG)
                Log.d("tyty", "Utility : setListViewHeightBasedOnChildren : total height: " + totalHeight);
            return totalHeight;
        }
        return 0;
    }

    /**
     * This method is used to dynamically change the ListView height
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

