package com.wellgel.london.Provider.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.Activities.C_DashboardAct;
import com.wellgel.london.Customer.Activities.Ecom_AppointlistDetail;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.SerializeModelClasses.EcomAppoDetailSerial;
import com.wellgel.london.Provider.ModelSerialized.RescheduleAppointment;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.DropDown;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class P_AcceptRejectAct extends AppCompatActivity {
    private ArrayList<String> listSkinColor = new ArrayList<>();
    private ArrayList<String> listNailColor = new ArrayList<>();
    private ArrayList<String> listNailShape = new ArrayList<>();
    RelativeLayout ln_nailColor;
    private P_AcceptRejectAct activity;
    private PreferencesShared shared;
    private TextView nail_shape, near_salon_name, resc_date, resc_time, salonBookingDate, salonBookingTime;
    private ImageView c_booking_detail_navi;
    private ProgressDialog progressDoalog;
    private LinearLayout lay_nail_color, lay_skin_color;
    private CircleImageView c_dash_userhand_image;
    private Button reschedulingBTN;
    private TextView priceText, booking_accept, booking_reject;
    private String priceText_st;
    private int[][] handSamples = new int[][]{new int[]{R.drawable.square_fair_one, R.drawable.square_round_fair_one, R.drawable.round_fair_one, R.drawable.pointed_fair_one, R.drawable.pointed_round_fair_one},
            new int[]{R.drawable.square_fair_two, R.drawable.square_round_fair_two, R.drawable.round_fair_two, R.drawable.pointed_fair_two, R.drawable.pointed_round_fair_two},
            new int[]{R.drawable.square_medium, R.drawable.square_round_medium, R.drawable.round_medium, R.drawable.pointed_medium, R.drawable.pointed_round_medium},
            new int[]{R.drawable.square_black_one, R.drawable.square_round_black_one, R.drawable.round_black_one, R.drawable.pointed_black_one, R.drawable.pointed_round_black_one},
            new int[]{R.drawable.square_black_two, R.drawable.square_round_black_two, R.drawable.round_black_two, R.drawable.pointed_black_two, R.drawable.pointed_round_black_two}};
    private String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__accept_reject);

        ln_nailColor = findViewById(R.id.mainLAyout);

        activity = this;
        shared = new PreferencesShared(activity);
        near_salon_name = findViewById(R.id.near_salon_name);
        nail_shape = findViewById(R.id.nail_shape);
//        near_salon_address = findViewById(R.id.near_salon_address);
        salonBookingDate = findViewById(R.id.salonBookingDate);
        salonBookingTime = findViewById(R.id.salonBookingTime);
        c_booking_detail_navi = findViewById(R.id.c_booking_detail_navi);
        resc_date = findViewById(R.id.resc_date);
        resc_time = findViewById(R.id.resc_time);
        lay_nail_color = findViewById(R.id.lay_nail_color);
        lay_skin_color = findViewById(R.id.lay_skin_color);
        reschedulingBTN = findViewById(R.id.reschedulingBTN);
        c_dash_userhand_image = findViewById(R.id.c_dash_userhand_image);
        priceText = findViewById(R.id.priceText);
        booking_accept = findViewById(R.id.booking_accept);
        booking_reject = findViewById(R.id.booking_reject);

        c_booking_detail_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        priceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatePopupWindowPRice();
            }
        });
        reschedulingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priceText_st = priceText.getText().toString().trim();
                if (priceText_st.isEmpty()) {
                    initiatePopupWindow();
                } else {
                    Intent intent = new Intent(activity, P_RescheduleDateAct.class);
                    intent.putExtra("appo_id", getIntent().getIntExtra("appo_id", 0));
                    intent.putExtra("price", priceText_st);
                    startActivity(intent);
                }

            }
        });
        listNailColor.add(0, "#FFFFFF");
        listNailColor.add(1, "#CC66CC");
        listNailColor.add(2, "#333366");
        listNailColor.add(3, "#009999");
        listNailColor.add(4, "#CC00CC");
        listNailColor.add(5, "#0033FF");
        listNailColor.add(6, "#99FFFF");
        listNailColor.add(7, "#CCFF99");
        listNailColor.add(8, "#006633");
        listNailColor.add(9, "#CC9900");
        listNailColor.add(10, "#33FF00");
        listNailColor.add(11, "#669966");
        listNailColor.add(12, "#666666");
        listNailColor.add(13, "#00FFCC");
        listNailColor.add(14, "#993333");
        listNailColor.add(15, "#990099");
        listNailColor.add(16, "#9999FF");


        listSkinColor.add(0, "#F2D9B7");
        listSkinColor.add(1, "#EFB38A");
        listSkinColor.add(2, "#A07561");
        listSkinColor.add(3, "#795D4C");
        listSkinColor.add(4, "#3D2923");


        listNailShape.add(0, "square");
        listNailShape.add(1, "round");
        listNailShape.add(2, "oval");
        listNailShape.add(3, "stillete");
        listNailShape.add(4, "pointed");


        appointDetailAPI(getIntent().getIntExtra("appo_id", 0));

    }

    public void appointDetailAPI(int appo_id) {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Getting Booking Detail....");
        progressDoalog.show();
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<EcomAppoDetailSerial> call = service.appointmentDetail("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), appo_id);
        call.enqueue(new Callback<EcomAppoDetailSerial>() {
            @Override
            public void onResponse(Call<EcomAppoDetailSerial> call, Response<EcomAppoDetailSerial> response) {

                progressDoalog.dismiss();

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {


                            if (response.body().getData().size() > 0) {


                                near_salon_name.setText(response.body().getData().get(0).getCustomerDetails().get(0).getName());
//                                near_salon_address.setText(response.body().getData().get(0).getCustomerDetails().get(0).getAddress());
                                String currentString = response.body().getData().get(0).getRequestedDatetime();
                                String[] separated = currentString.split(" ");
                                lay_skin_color.setBackgroundColor(Color.parseColor(response.body().getData().get(0).getSkinColor() + ""));
                                lay_nail_color.setBackgroundColor(Color.parseColor(response.body().getData().get(0).getNailPolishColor() + ""));
                                salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));
                                resc_date.setText(parseDateToddMMyyyy(separated[0]));
                                resc_date.setText(parseDateToddMMyyyy(separated[0]));
                                c_dash_userhand_image.setBackgroundColor(Color.parseColor(response.body().getData().get(0).getNailPolishColor() + ""));
                                nail_shape.setText("Nail Shape  " + response.body().getData().get(0).getNailShape());
                                nailShape(c_dash_userhand_image, response.body().getData().get(0).getNailShape());
                                skinColor(c_dash_userhand_image, response.body().getData().get(0).getSkinColor(), response.body().getData().get(0).getNailShape());

                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                SimpleDateFormat requireTime = new SimpleDateFormat("hh:mm");
                                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");
                                Date dt;
                                try {
                                    dt = sdf.parse(separated[1]);
                                    salonBookingTime.setText(sdfs.format(dt));
                                    time = requireTime.format(dt);
                                    resc_time.setText(sdfs.format(dt));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                c_dash_userhand_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        initiatePopupWindow(response.body().getData().get(0).getSkinColor(), response.body().getData().get(0).getNailPolishColor(), response.body().getData().get(0).getNailShape());
                                    }
                                });


                                booking_reject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        priceText_st = priceText.getText().toString().trim();
                                        if (priceText_st.isEmpty()) {
                                            initiatePopupWindow();
                                        } else {
                                            acceptReject(getIntent().getIntExtra("appo_id", 0), priceText_st, "rejected", separated[0] + " " + time);
                                        }
                                    }
                                });
                                booking_accept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        priceText_st = priceText.getText().toString().trim();
                                        if (priceText_st.isEmpty()) {
                                            initiatePopupWindow();
                                        } else {
                                            acceptReject(getIntent().getIntExtra("appo_id", 0), priceText_st, "open", separated[0] + " " + time);
                                        }
                                    }
                                });

                            }
                        } else {


                        }

                    }
                } else {


                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<EcomAppoDetailSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "EEEE dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void initiatePopupWindow(String handcolor, String nailColor, String nailShape) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_pop_up_to_view_hand_image,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            final RelativeLayout apply = layout.findViewById(R.id.mainLAyout);
            final ImageView handImage = layout.findViewById(R.id.hand_image);
            final ImageView close = layout.findViewById(R.id.close);

            apply.setBackgroundColor(Color.parseColor(nailColor + ""));
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            skinColor(handImage, handcolor, nailShape);
//            nailShape(handImage, nailShape);
            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void skinColor(ImageView handImage, String handcolor, String nailshape) {

        int handvalue = 0;
        int nailvalue = 0;
        for (int i = 0; i < listSkinColor.size(); i++) {

            if (handcolor.equals(listSkinColor.get(i)))
                if (handcolor.equals(listSkinColor.get(i)))
                    handvalue = i;
            for (int j = 0; j < listNailShape.size(); j++) {
                if (nailshape.equals(listNailShape.get(j)))
                    nailvalue = j;

            }
        }

        handImage.setImageResource(handSamples[handvalue][nailvalue]);

//        if (handcolor.equalsIgnoreCase(listSkinColor.get(0))) {
//            handImage.setImageResource(handSamples[0][0]);
//            nailShape(handImage, nailColor);
//
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(1))) {
//
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[1][0]);
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(2))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[2][0]);
//
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(3))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[3][0]);
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(4))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[4][0]);
//        }
    }

    private void nailShape(ImageView handImage, String nailshape) {
        if (nailshape.equalsIgnoreCase(listNailShape.get(0))) {
            handImage.setImageResource(handSamples[0][0]);

        } else if (nailshape.equalsIgnoreCase(listNailShape.get(1))) {
            handImage.setImageResource(handSamples[0][1]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(2))) {
            handImage.setImageResource(handSamples[0][2]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(3))) {
            handImage.setImageResource(handSamples[0][3]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(4))) {
            handImage.setImageResource(handSamples[0][4]);
        }
    }


    public static class MyNotificationPublisher extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "" + intent.getAction(), Toast.LENGTH_SHORT);

        }
    }

    public void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_non_nego_price_pop_up,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);

            RelativeLayout main_pop_up = layout.findViewById(R.id.main_pop_up);
            main_pop_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void confirmedBookingPopUp() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_request_confirm,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            TextView back_home = layout.findViewById(R.id.back_home);

            near_salon_name = layout.findViewById(R.id.near_salon_name);
            nail_shape = layout.findViewById(R.id.nail_shape);
            salonBookingDate = layout.findViewById(R.id.salonBookingDate);
            salonBookingTime = layout.findViewById(R.id.salonBookingTime);
            lay_nail_color = layout.findViewById(R.id.lay_nail_color);
            lay_skin_color = layout.findViewById(R.id.lay_skin_color);
            c_dash_userhand_image = layout.findViewById(R.id.c_dash_userhand_image);
            ln_nailColor = layout.findViewById(R.id.lay_nail_color);


            back_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                    startActivity(new Intent(activity, C_DashboardAct.class));
                    finish();
                }
            });

            RelativeLayout main_pop_up = layout.findViewById(R.id.main_pop_up);
            main_pop_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void initiatePopupWindowPRice() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_non_nego_enter_price_pop,
                    (ViewGroup) null);
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            final TextView apply = layout.findViewById(R.id.apply);
            final TextView cancel = layout.findViewById(R.id.cancel);
            final EditText spinnerEdit = layout.findViewById(R.id.spinerValue);


            apply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (spinnerEdit.getText().toString().trim().isEmpty()) {
                        initiatePopupWindow();
                    } else {
                        priceText.setText(spinnerEdit.getText().toString().trim());
                        pw.dismiss();
                    }

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });
            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void acceptReject(int appo_id, String price, String status, String dateTime) {

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Checking appointment....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<RescheduleAppointment> call = service.appointmentUpddateBySalon("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), appo_id + "", dateTime, status, "", price);
        call.enqueue(new Callback<RescheduleAppointment>() {
            @Override
            public void onResponse(Call<RescheduleAppointment> call, Response<RescheduleAppointment> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {


                        if (response.body().getStatus()) {

                            if (status.equals("open")) {
                                confirmedBookingPopUp("", response.body().getData().getAppointments().getAvailableDatetime()
                                        , response.body().getData().getAppointments().getNailPolishColor(),
                                        response.body().getData().getAppointments().getNailShape(),
                                        response.body().getData().getAppointments().getSkinColor());
                            } else dismisFunc();

                        } else {

                        }


                    } else {
                    }
                } else {
                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());

                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(Call<RescheduleAppointment> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }

    public void confirmedBookingPopUp(String name, String availTime, String nailColr, String nailShape, String handColor) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_request_confirm,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            TextView back_home = layout.findViewById(R.id.back_home);

            near_salon_name = layout.findViewById(R.id.near_salon_name);
            nail_shape = layout.findViewById(R.id.nail_shape);
            salonBookingDate = layout.findViewById(R.id.salonBookingDate);
            salonBookingTime = layout.findViewById(R.id.salonBookingTime);
            lay_nail_color = layout.findViewById(R.id.lay_nail_color);
            lay_skin_color = layout.findViewById(R.id.lay_skin_color);
            c_dash_userhand_image = layout.findViewById(R.id.c_dash_userhand_image);

            near_salon_name.setText(name);
//                                near_salon_address.setText(response.body().getData().get(0).getCustomerDetails().get(0).getAddress());
            String[] separated = availTime.split(" ");
            lay_skin_color.setBackgroundColor(Color.parseColor(handColor + ""));
            lay_nail_color.setBackgroundColor(Color.parseColor(nailColr + ""));
            salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));
            c_dash_userhand_image.setBackgroundColor(Color.parseColor(nailColr + ""));
            nail_shape.setText("Nail Shape  " + nailShape);
            nailShape(c_dash_userhand_image, nailShape);
            skinColor(c_dash_userhand_image, handColor, nailShape);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");
            Date dt;
            try {
                dt = sdf.parse(separated[1]);
                salonBookingTime.setText(sdfs.format(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            c_dash_userhand_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    initiatePopupWindow(handColor, nailColr, nailShape);
                }
            });


            back_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismisFunc();
                    pw.dismiss();
                }
            });

            RelativeLayout main_pop_up = layout.findViewById(R.id.main_pop_up);
            main_pop_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismisFunc();
                    pw.dismiss();
                }
            });

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    dismisFunc();
                    pw.dismiss();

                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void dismisFunc() {
        Intent intent = new Intent(activity, C_DashboardAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}