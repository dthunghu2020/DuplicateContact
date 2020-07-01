package com.hungdt.test.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.hungdt.test.R;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.Helper;
import com.hungdt.test.utils.MySetting;

public class VipFragment extends Fragment implements BillingProcessor.IBillingHandler {
    private BillingProcessor billingProcessor;
    private boolean readyToPurchase = false;
    private Button btnVip;
    TextView txtMoney;
    LinearLayout lnRateApp, lnClose;
    TextView tvExit;

    ImageView imgStarLight1, imgStarLight2, imgStarLight3, imgStarLight4, imgStarLight5;
    boolean isFromMain = false;
    boolean isEnableClick = true;
    boolean isRate = true;

    CountDownTimer timer;

    public VipFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Ads.initNativeGgFb((LinearLayout) view.findViewById(R.id.lnNative), getActivity(), true);
        //Ads.initBanner(((LinearLayout) view.findViewById(R.id.llBanner)), getActivity(), true);

        billingProcessor = BillingProcessor.newBillingProcessor(getLayoutInflater().getContext().getApplicationContext(), getString(R.string.BASE64), this); // doesn't bind
        billingProcessor.initialize();

        btnVip = view.findViewById(R.id.btnVip);
        txtMoney = view.findViewById(R.id.txtMoney);

        btnVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyToPurchase) {
                    billingProcessor.subscribe(getActivity(), getString(R.string.ID_SUBSCRIPTION));
                } else {
                    Toast.makeText(getLayoutInflater().getContext().getApplicationContext(), "Unable to initiate purchase", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (MySetting.isSubscription(getLayoutInflater().getContext())) {
            btnVip.setVisibility(View.GONE);
            txtMoney.setVisibility(View.GONE);
        }

        //Ads.initNativeGgFb((LinearLayout)findViewById(R.id.lnNative), this, true);

        try {
            if (getActivity().getIntent().hasExtra("isRate")) {
                isRate = getActivity().getIntent().getBooleanExtra("isRate", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getActivity().getIntent().hasExtra("isExit")) {
                isFromMain = getActivity().getIntent().getBooleanExtra("isExit", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        initView(view);
        setData();
        click();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void click() {
        imgStarLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnableClick) {
                    try {
                        if (timer != null) {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isEnableClick = false;
                    MySetting.putRateApp(getContext(), 1);
                    setData();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Helper.feedback(getContext());
                                isEnableClick = true;
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imgStarLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnableClick) {
                    try {
                        if (timer != null) {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isEnableClick = false;
                    MySetting.putRateApp(getContext(), 2);
                    setData();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Helper.feedback(getContext());
                                isEnableClick = true;
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imgStarLight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnableClick) {
                    try {
                        if (timer != null) {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isEnableClick = false;
                    MySetting.putRateApp(getContext(), 3);
                    setData();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Helper.feedback(getContext());
                                isEnableClick = true;
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imgStarLight4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnableClick) {
                    try {
                        if (timer != null) {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isEnableClick = false;
                    MySetting.putRateApp(getContext(), 4);
                    setData();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Helper.callPlayStore(getContext(), getContext().getPackageName());
                                isEnableClick = true;
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        imgStarLight5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnableClick) {
                    try {
                        if (timer != null) {
                            timer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isEnableClick = false;
                    MySetting.putRateApp(getContext(), 5);
                    setData();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Helper.callPlayStore(getContext(), getContext().getPackageName());
                                isEnableClick = true;
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

      /*  lnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });*/
    }

    private void setData() {
        if (MySetting.isRateApp(getContext()) == 0) {
            imgStarLight1.setImageResource(R.drawable.ic_like_off);
            imgStarLight2.setImageResource(R.drawable.ic_like_off);
            imgStarLight3.setImageResource(R.drawable.ic_like_off);
            imgStarLight4.setImageResource(R.drawable.ic_like_off);
            imgStarLight5.setImageResource(R.drawable.ic_like_off);
        } else if (MySetting.isRateApp(getContext()) == 1) {
            imgStarLight1.setImageResource(R.drawable.ic_like_on);
            imgStarLight2.setImageResource(R.drawable.ic_like_off);
            imgStarLight3.setImageResource(R.drawable.ic_like_off);
            imgStarLight4.setImageResource(R.drawable.ic_like_off);
            imgStarLight5.setImageResource(R.drawable.ic_like_off);
        } else if (MySetting.isRateApp(getContext()) == 2) {
            imgStarLight1.setImageResource(R.drawable.ic_like_on);
            imgStarLight2.setImageResource(R.drawable.ic_like_on);
            imgStarLight3.setImageResource(R.drawable.ic_like_off);
            imgStarLight4.setImageResource(R.drawable.ic_like_off);
            imgStarLight5.setImageResource(R.drawable.ic_like_off);
        } else if (MySetting.isRateApp(getContext()) == 3) {
            imgStarLight1.setImageResource(R.drawable.ic_like_on);
            imgStarLight2.setImageResource(R.drawable.ic_like_on);
            imgStarLight3.setImageResource(R.drawable.ic_like_on);
            imgStarLight4.setImageResource(R.drawable.ic_like_off);
            imgStarLight5.setImageResource(R.drawable.ic_like_off);
        } else if (MySetting.isRateApp(getContext()) == 4) {
            imgStarLight1.setImageResource(R.drawable.ic_like_on);
            imgStarLight2.setImageResource(R.drawable.ic_like_on);
            imgStarLight3.setImageResource(R.drawable.ic_like_on);
            imgStarLight4.setImageResource(R.drawable.ic_like_on);
            imgStarLight5.setImageResource(R.drawable.ic_like_off);
        } else if (MySetting.isRateApp(getContext()) == 5) {
            imgStarLight1.setImageResource(R.drawable.ic_like_on);
            imgStarLight2.setImageResource(R.drawable.ic_like_on);
            imgStarLight3.setImageResource(R.drawable.ic_like_on);
            imgStarLight4.setImageResource(R.drawable.ic_like_on);
            imgStarLight5.setImageResource(R.drawable.ic_like_on);
        }


    }

   /* private void closeActivity() {
        finish();
        try {
            if (isFromMain) {
                sendBroadcast(new Intent("exitFromRateApp"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* @Override
    public void onBackPressed() {
        if (isFromMain) super.onBackPressed();
    }*/

    private void initView(View view) {
        lnRateApp = view.findViewById(R.id.lnDialogRateApp);
        imgStarLight1 = view.findViewById(R.id.imgStarLight1Rate);
        imgStarLight2 = view.findViewById(R.id.imgStarLight2Rate);
        imgStarLight3 = view.findViewById(R.id.imgStarLight3Rate);
        imgStarLight4 = view.findViewById(R.id.imgStarLight4Rate);
        imgStarLight5 = view.findViewById(R.id.imgStarLight5Rate);
        lnClose = view.findViewById(R.id.lnExitRateApp);

        click();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onProductPurchased(@Nullable String productId, TransactionDetails details) {
        try {
            Toast.makeText(getLayoutInflater().getContext(), "Thanks for your Purchased!", Toast.LENGTH_SHORT).show();
            MySetting.setSubscription(getLayoutInflater().getContext(), true);
            MySetting.putRemoveAds(getLayoutInflater().getContext(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getContext(), "Unable to process billing", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
        try {
            txtMoney.setText(billingProcessor.getSubscriptionListingDetails(getString(R.string.ID_SUBSCRIPTION)).priceText + "/Month)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
