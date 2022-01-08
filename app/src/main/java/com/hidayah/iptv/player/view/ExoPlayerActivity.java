package com.hidayah.iptv.player.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.JsonObject;
import com.hidayah.iptv.BuildConfig;
import com.hidayah.iptv.HidayahTvApplication;
import com.hidayah.iptv.R;
import com.hidayah.iptv.auth.LoginContract;
import com.hidayah.iptv.auth.model.ApiInteractor;
import com.hidayah.iptv.auth.model.ChannelsItem;
import com.hidayah.iptv.auth.model.Data;
import com.hidayah.iptv.data.prefs.AppPreferencesHelper;
import com.hidayah.iptv.player.model.epg.DataItem;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


import static com.hidayah.iptv.utils.Helper.isCurrentShow;

public class ExoPlayerActivity extends FragmentActivity implements EpgFragment.OnScheduleChangeListener {

    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    public static final int KEY_CHANNEL_UP = 11111;
    public static final int KEY_CHANNEL_DOWN = -1111;
    public static final String KEY_CHANNEL = "channel";
    private static final String TAG = "ExoPlayerActivity";
    private static String KEY_CHANNEL_LIST = "list_of_channel";
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ChannelsItem channel;
    private ArrayList<ChannelsItem> channelsItems;
    private boolean startAutoPlay;
    //private ChannelsItem channelsItem;
    private RecyclerView rvEpgGuideList;
    private FrameLayout fragmentContainer;
    private ProgressBar progressBar;
    private ChannelAdapter channelAdapter;
    private MediaSource mediaSource;
    private DataSource.Factory dataSourceFactory;
    private Handler mainHandler;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Handler handlerTime = new Handler(Looper.getMainLooper());
    private Handler multipleClickHandler = new Handler(Looper.getMainLooper());
    private ApiInteractor apiInteractor;
    private static int retryCount=0;
    private int currentChannelItemPos = 0;
    private AppPreferencesHelper preferencesHelper;
    private TextView  tvUserId;
    private String channelToken;
    private boolean disableKeyRepeat;
    private List<com.hidayah.iptv.auth.model.ChannelsItem> channelsItemList = new ArrayList<>();
    private HashMap<Integer, com.hidayah.iptv.auth.model.ChannelsItem> channelMap = new HashMap<Integer, com.hidayah.iptv.auth.model.ChannelsItem>();
    private HashMap<Integer, com.hidayah.iptv.auth.model.ChannelsItem> channelMapByNumber = new HashMap<Integer, com.hidayah.iptv.auth.model.ChannelsItem>();
    private int channelPosition = 0;
    private TextView tvChannelName;
    private TextView tvChannelNumber;
    private LinearLayout llEpgFirstColum;
    private TextView tvFirstHour;
    private TextView tvSecondndHour;
    private TextView tvThirdHour;
    private TextView epg;
    private LayoutParams playerViewLayoutParams;

    private Button btnWatchNow;
    private RelativeLayout viewEpgDescription;
    private TextView epgTitle,epgDescription,epgSubTitle,
            epgDuration,epgChannelInfoTitle,tvCurrentTime,epgTitleLogoSection,epgSubTitleLogoSection,epgVchipRating;
    private RelativeLayout epgListWrapper,rlErorr;
    private String currentUrl;
    private PlayerControlView playerControllView;
    private ImageView ivMiniPlayer;
    private ImageView ivLogo;
    private boolean isMiniPlayerState;
    private DefaultTrackSelector trackSelector;
    private boolean isClosedCaptionEnabled = true;
    private EpgGuideAdapter epgGuideAdapter;
    private int currentChannelId;
    private RelativeLayout rlchannelLogoWrapper;
    private AudioManager audioManager;
    private boolean VolIsMute;
    private static long lastEpgFetchTime = 0;
    private ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> epgChannelItemList = new ArrayList<>();
    private int firstChannelId;
    private EditText edt1,edt2,edt3,edt4;
    private ArrayList<EpgFragment.SimpleChannel> simpleChannelList = new ArrayList<>();
    private SubtitleView subtitleView;
    private int startWindow;
    private long startPosition;
    private Build build;
    private Build.VERSION version;
    private DefaultTrackSelector.Parameters trackSelectorParameters;


    private void loadVod(String title, String url, String preRollTag) {
        Log.d(TAG, "loadVod() called with: title = [" + title + "], url = [" + url + "], preRollTag = [" + preRollTag + "]");
    }

    private void showChannelList(ArrayList<ChannelsItem> channels) {
        //  channelAdapter.addChannels(channels);
    }

    private void loadVod(String title, String vodUrl) {
        //exoPlayer.setTitle(title);
        //exoPlayer.load(vodUrl);
    }

    public static void start(Context context, ChannelsItem channel, ArrayList<ChannelsItem> channels) {
//        Intent starter = new Intent(context, ExoPlayerActivity.class);
//        starter.putExtra(KEY_CHANNEL, channel);
//        starter.putParcelableArrayListExtra(KEY_CHANNEL_LIST, channels);
//        context.startActivity(starter);
    }

    private void setUiFlags() {
        int mUIFlag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
    }

    private void playerInitialization() {
        epgGuideAdapter = new EpgGuideAdapter();
        rlchannelLogoWrapper = findViewById(R.id.rlchannelLogoWrapper);
        ivLogo = findViewById(R.id.ivLogo);
        ivMiniPlayer = findViewById(R.id.ivMiniPlayer);
        rlErorr = findViewById(R.id.rlErorr);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        epgListWrapper = findViewById(R.id.epgListWrapper);
        rvEpgGuideList = findViewById(R.id.rvEpgGuideList);
        fragmentContainer = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.progressUi);
        final LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEpgGuideList.setLayoutManager(layout);
        dataSourceFactory = buildDataSourceFactory();
// Bind the player to the view.
        btnWatchNow = findViewById(R.id.btnWatchNow);
        viewEpgDescription = findViewById(R.id.viewEpgDescription);
        epgTitleLogoSection = findViewById(R.id.epgTitleLogoSection);
        epgSubTitleLogoSection = findViewById(R.id.epgSubTitleLogoSection);
        epgTitle = findViewById(R.id.epgTitle);
        epgSubTitle = findViewById(R.id.epgSubTitle);
        epgDescription = findViewById(R.id.epgDescription);
        epgChannelInfoTitle = findViewById(R.id.epgChannelInfoTitle);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        epgDuration = findViewById(R.id.epgDuration);
        epgVchipRating = findViewById(R.id.epgVchipRating);

        tvChannelNumber = findViewById(R.id.tvChannelNumber);
        tvChannelName = findViewById(R.id.tvChannelName);
        playerView = findViewById(R.id.playerView);
        subtitleView = (SubtitleView) findViewById(R.id.exo_subtitles);
        //playerControllView = findViewById(R.id.playerControllView);
        //playerView.setControllerAutoShow(false);
        //playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS);
        playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());

        btnWatchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLive();
            }
        });

        ivMiniPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMiniPlayer();
                Log.d(TAG, "onClick() called with: ivMiniPlayer = [" + ivMiniPlayer + "]");
            }
        });
    }

    private void toggleMiniPlayer() {
        if(!isMiniPlayerState){
            switchToMiniPlayer();
        }else {
            switchFullScreenPlayer();
        }
    }

    private void switchFullScreenPlayer() {
        if (playerView != null) {
            isMiniPlayerState = false;
            playerView.setLayoutParams(new RelativeLayout.LayoutParams(-1,-1));
            showChannelInfo(false);
        }
    }

    private void switchToMiniPlayer() {
        if (playerView != null) {
            isMiniPlayerState = true;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dpToPixel(480), dpToPixel(270));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            playerView.setLayoutParams(params);
        }
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi/160f));
        return px;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(this);
//        if(appPreferencesHelper.getAppCurrentBaseUrl() == null){
//            readUrlAndSaveBaseUrl();
//        }
/*        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            DefaultTrackSelector.ParametersBuilder builder = new DefaultTrackSelector.ParametersBuilder();
            trackSelectorParameters = builder.build();
            clearStartPosition();
        }*/
        initVar();
        //  startRefreshTokenTimer();
        playerInitialization();
        // checkIntent();
        createUserSession();
        updateTime();
        //switchToMiniPlayer();
    }
    final Runnable r = new Runnable() {
        @Override
        public void run() {
            updateTime();
        }
    };
    private void updateTime() {
        if (tvCurrentTime != null) {
            tvCurrentTime.setText(getCurrentDate());
        }
        handlerTime.postDelayed(r,60*1000);
    }

    private void readUrlAndSaveBaseUrl() {
        try {
            String filename = "rallytv_info.txt";
            File dir = getFilesDir();
            File file = new File(dir, filename);

            Log.d(TAG, "readUrlAndSaveBaseUrl() file location : "+file.getAbsolutePath());
            FileInputStream fin = new FileInputStream(file);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //tv.setText(temp);
            Toast.makeText(getBaseContext(), temp, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
        }
    }

    public static String getSerialNumber() {
        String serialNumber = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserSession() {
        JsonObject jsonObject = new JsonObject();
        String deviceID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String device_ip_address = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        String device_mac_address = wifiManager.getConnectionInfo().getMacAddress();
        String serial_number = getSerialNumber();
        if (serial_number == "unknown") {
            serial_number = "UPTV1942";
        }


//        jsonObject.addProperty("terminal_code", "test");
        jsonObject.addProperty("serial_number", serial_number);
        jsonObject.addProperty("mac", "681DEF1609AA");
        jsonObject.addProperty("local_ip_address", device_ip_address);
        jsonObject.addProperty("app_version", version.RELEASE);
        jsonObject.addProperty("firmware_version", String.valueOf(version.SDK_INT));
        System.out.println(jsonObject);
 //       jsonObject.addProperty("remote_ip", device_ip_address);

        ApiInteractor apiInteractor = new ApiInteractor();
        apiInteractor.getToken(jsonObject, new LoginContract.LoginCompleteListener() {
            @Override
            public void onLoginSuccess(Data data) {
//                getDataManger().setUserRefreshToken(refreshToken);
//                getDataManger().setUserAccessToken(accessToken);
//                getDataManger().setUsername(userName);
                if (data != null) {
                    channelToken = data.getChannelToken();
                    setupChannelData(data.getChannels());
                    Toast.makeText(getApplicationContext(),"Session created: "+ build.SERIAL+deviceID,Toast.LENGTH_SHORT).show();
                    openEpgActivity();
                    rlErorr.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoginFailed(String msg) {
                rlErorr.setVisibility(View.VISIBLE);
                Log.d(TAG, "onLoginFailed() called with: msg = [" + msg + "]");
            }
        });
    }

    private void setupChannelData(List<ChannelsItem> channels) {
        final int lastChannel = preferencesHelper.getLastChannel();
        if (channels != null && !channels.isEmpty()) {
            channelsItemList.clear();
            channelsItemList.addAll(channels);
            firstChannelId = channels.get(0).getChannelId();
            channelMap.clear();
            for (ChannelsItem channel:channels) {
                simpleChannelList.add(new EpgFragment.SimpleChannel("" + channel.getChannelId(), new SpannedString(channel.getChannelName()), channel.getLogoPath(),""+channel.getChannelNumber()));
                channelMap.put(channel.getChannelId(),channel);
                channelMapByNumber.put(channel.getChannelNumber(),channel);
            }
        }
        playContent(findChannelItem(lastChannel),false);
    }

    private  ChannelsItem findChannelItem(int channelId) {
        final int size = channelMap.size();
        if(size > 0 ){
            if(channelId > 0){
                return channelMap.get(channelId);
            }else {
                return channelMap.get(firstChannelId);
            }
        }
        return null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(isMiniPlayerState){
                switchFullScreenPlayer();
            }else {
                return false;
            }
        }

        boolean isDispatched = false;

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i("ButtonTest", "Button pressed: " + String.valueOf(event.getKeyCode()));
            int count = event.getRepeatCount();
            int keyCode = event.getKeyCode();
            String button = "";


            if (keyCode == KeyEvent.KEYCODE_F4 && count == 35) {
                isDispatched = super.dispatchKeyEvent(event);
                button = "SETTINGS";
            } else if (keyCode == KeyEvent.KEYCODE_F3 && count == 35) {
                isDispatched = super.dispatchKeyEvent(event);
                button = "SYSUPDATE";
                
            } else if ((!disableKeyRepeat && ((count <= 36 && event.getRepeatCount() % 10 == 0) || (count > 36 && event.getRepeatCount() % 4 == 0))) || disableKeyRepeat && count == 0) {
                Log.i("ButtonTest", "Button press being processed");
                isDispatched = super.dispatchKeyEvent(event);
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        final int visibility = edt1.getVisibility();
                        if(visibility == View.VISIBLE){
                            redirectToChannelWithTypedNumber();
                        }
                        break;
                    case KeyEvent.KEYCODE_0:
                        //case KeyEvent.KEYCODE_DPAD_CENTER:
                        button = "GUIDE";
                        openEpgActivity();
                        toggleMiniPlayer();
                        break;
                    case KeyEvent.KEYCODE_F3:
                        button = "LIVE";
                        epgListWrapper.setVisibility(View.GONE);
                        rvEpgGuideList.setVisibility(View.GONE);
                        llEpgFirstColum.setVisibility(View.GONE);
                        switchToLive();
                        break;
                    case KeyEvent.KEYCODE_CAPTIONS:
                        boolean currentValue = isClosedCaptionEnabled;
                        isClosedCaptionEnabled = !currentValue;
                        showToast(isClosedCaptionEnabled?"Closed caption enabled":"Closed caption disabled");
                        //setTrackSelectorParam();
                        setCaptions();
                        break;
                    case KeyEvent.KEYCODE_INFO:
                        Log.i("ButtonTest", "Info Button pressed");
                        button = "INFO";
                        showChannelInfo(true);
                        break;
                    case KeyEvent.KEYCODE_LAST_CHANNEL:
                        final int lastChannel = preferencesHelper.getLastChannel();
                        playContent(findChannelItem(lastChannel),false);
                        //button = "LASTCHANNEL";
                        break;
                    case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                        button = "FASTFORWARD";
                        break;
                    case KeyEvent.KEYCODE_MEDIA_REWIND:
                        button = "REWIND";
                        break;
                    case KeyEvent.KEYCODE_F5:
                        button = "REWIND";
                        break;
                    case KeyEvent.KEYCODE_F6:
                        button = "FASTFORWARD";
                        break;
                    case KeyEvent.KEYCODE_CHANNEL_UP:
                        //  case KeyEvent.KEYCODE_DPAD_UP:
                        channelSwitch(KEY_CHANNEL_UP);
                        break;
                    case KeyEvent.KEYCODE_CHANNEL_DOWN:
                        channelSwitch(KEY_CHANNEL_DOWN);
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if(!isMiniPlayerState){
                            channelSwitch(KEY_CHANNEL_DOWN);
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if(!isMiniPlayerState){
                            channelSwitch(KEY_CHANNEL_UP);
                        }
                        break;
                    case KeyEvent.KEYCODE_MUTE:
                        setMute();
                        break;
                    case KeyEvent.KEYCODE_VOLUME_MUTE:
                        setMute();
                        break;
                    case KeyEvent.KEYCODE_VOLUME_UP:
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.STREAM_MUSIC);
                        return true;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.STREAM_MUSIC);
                        return true;

//                    case KeyEvent.KEYCODE_0:
//                        pressDigit(0);
//                        break;
                    case KeyEvent.KEYCODE_1:
                        pressDigit(1);
                        break;
                    case KeyEvent.KEYCODE_2:
                        pressDigit(2);
                        break;
                    case KeyEvent.KEYCODE_3:
                        pressDigit(3);
                        break;
                    case KeyEvent.KEYCODE_4:
                        pressDigit(4);
                        break;
                    case KeyEvent.KEYCODE_5:
                        pressDigit(5);
                        break;
                    case KeyEvent.KEYCODE_6:
                        pressDigit(6);
                        break;
                    case KeyEvent.KEYCODE_7:
                        pressDigit(7);
                        break;
                    case KeyEvent.KEYCODE_8:
                        pressDigit(8);
                        break;
                    case KeyEvent.KEYCODE_9:
                        pressDigit(9);
                        break;
                    case KeyEvent.KEYCODE_F4:
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        Toast.makeText(getApplicationContext(),"Key F4",Toast.LENGTH_SHORT).show();
                        break;
                    case KeyEvent.KEYCODE_S:
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        break;
                    case 0:
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        clearTypedChannelNumber();
                        break;
                    case KeyEvent.KEYCODE_ESCAPE:
                        showExitAlertDialog();
                        break;
                }
            }
            if (button != "") {
                Log.i("ButtonTest", button);
                // this.evaluateJavascript("javascript: window.remoteInput('" + button + "');", null);
            }
        } else {
            isDispatched =  super.dispatchKeyEvent(event);
        }
        return isDispatched || !isMiniPlayerState;
    }

    private void showExitAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure to close application?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private synchronized void pressDigit(int i) {
        if(i>=0){
            channelTypingOptionUi(View.VISIBLE);
            String text1 = edt1.getText().toString();
            String text2 = edt2.getText().toString();
            String text3 = edt3.getText().toString();
            String text4 = edt4.getText().toString();
            if(TextUtils.isEmpty(text1)){
                edt1.setText(String.valueOf(i));
                handler.removeCallbacks(jumpToChannelRunnable);
                handler.postDelayed(jumpToChannelRunnable,5000);
                return;
            }
            if(TextUtils.isEmpty(text2)){
                edt2.setText(String.valueOf(i));
                handler.removeCallbacks(jumpToChannelRunnable);
                handler.postDelayed(jumpToChannelRunnable,5000);
                return;
            }
            if(TextUtils.isEmpty(text3)){
                edt3.setText(String.valueOf(i));
                handler.removeCallbacks(jumpToChannelRunnable);
                handler.postDelayed(jumpToChannelRunnable,5000);
                return;
            }
            if(TextUtils.isEmpty(text4)){
                edt4.setText(String.valueOf(i));
                handler.removeCallbacks(jumpToChannelRunnable);
                handler.postDelayed(jumpToChannelRunnable,5000);
                return;
            }
            //playContent(channelsItemList.get(i),false);
        }
    }
    Runnable jumpToChannelRunnable = new Runnable() {
        @Override
        public void run() {
            redirectToChannelWithTypedNumber();
        }
    };
    private void redirectToChannelWithTypedNumber() {
        try {
            String typedChannelNumber = getTypedChannelNumber();
            if (typedChannelNumber != null) {
                String desireChannelNumber = typedChannelNumber.trim();
                final Integer integer = Integer.valueOf(desireChannelNumber);
                ChannelsItem channelsItem = channelMapByNumber.get(integer);
                if (channelsItem == null) {
                    //ChannelsItem channelsItem = channelMapByNumber.get(integer);
                    Toast.makeText(getApplicationContext(),"Channel Not Found",Toast.LENGTH_SHORT).show();
                }
                playContent(channelsItem,false);
            }
            handler.removeCallbacks(jumpToChannelRunnable);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), typedChannelNumber,Toast.LENGTH_SHORT).show();
        channelTypingOptionUi(View.GONE);
        Log.d(TAG, "redirectToChannelWithTypedNumber() called");
    }
    private String getTypedChannelNumber(){
        String text1 = edt1.getText().toString();
        String text2 = edt2.getText().toString();
        String text3 = edt3.getText().toString();
        String text4 = edt4.getText().toString();
        return text1+text2+text3+text4;
    }

    private void clearTypedChannelNumber(){
        String text1 = edt1.getText().toString();
        String text2 = edt2.getText().toString();
        String text3 = edt3.getText().toString();
        String text4 = edt4.getText().toString();
        if(!TextUtils.isEmpty(text4)){
            edt4.setText("");
            return;
        }
        if(!TextUtils.isEmpty(text3)){
            edt3.setText("");
            return;
        }
        if(!TextUtils.isEmpty(text2)){
            edt2.setText("");
            return;
        }
        if(!TextUtils.isEmpty(text1)){
            edt1.setText("");
            return;
        }
    }
    private void channelTypingOptionUi(int visibility){
        edt1.setVisibility(visibility);
        edt2.setVisibility(visibility);
        edt3.setVisibility(visibility);
        edt4.setVisibility(visibility);
        if(visibility == View.GONE){
            edt1.setText("");
            edt2.setText("");
            edt3.setText("");
            edt4.setText("");
        }
    }

    private void showChannelInfo(boolean isInfoButtonClicked) {
        multipleClickHandler.removeCallbacks(hideChannelInfoRunnable);
        int visibility = rlchannelLogoWrapper.getVisibility();
        if(visibility == View.VISIBLE && isInfoButtonClicked){
            rlchannelLogoWrapper.setVisibility(View.GONE);
        }else {
            rlchannelLogoWrapper.setVisibility(View.VISIBLE);
            multipleClickHandler.postDelayed(hideChannelInfoRunnable,5000);
        }

    }
    final Runnable hideChannelInfoRunnable = new Runnable() {
        @Override
        public void run() {
            rlchannelLogoWrapper.setVisibility(View.GONE);
        }
    };

    private void channelSwitch(int chnnel_btn_id) {
        if(chnnel_btn_id == KEY_CHANNEL_DOWN){
            if (channelsItemList != null) {
                channelPosition--;
                if(channelPosition < 0){
                    channelPosition = channelsItemList.size()-1;
                }
                playContent(channelsItemList.get(channelPosition),false);
            }
        }

        if(chnnel_btn_id == KEY_CHANNEL_UP){
            if (channelsItemList != null) {
                channelPosition++;
                if(channelsItemList.size() <= channelPosition){
                    channelPosition = 0;
                }
                playContent(channelsItemList.get(channelPosition),false);
            }
        }
    }

    public void setMute(){
        if (VolIsMute) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            VolIsMute = false;
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            VolIsMute = true;
        }
    }
    private void switchToLive() {
        viewEpgDescription.setVisibility(View.GONE);
        rvEpgGuideList.setVisibility(View.GONE);
        epgListWrapper.setVisibility(View.GONE);
        // final int lastChannel = preferencesHelper.getLastChannel();
        if(channelsItemList!=null && channelsItemList.size()>0){
            if(currentChannelId > 0){
                playContent(findChannelItem(currentChannelId),false);
            }else {
                playContent(channelsItemList.get(0),false);
            }
        }
        switchFullScreenPlayer();
    }

    private void initVar() {
        initEpgView();
        //tvUserId = findViewById(R.id.tvUserId);
        preferencesHelper = new AppPreferencesHelper(HidayahTvApplication.getInstance());
        retryCount = 0;
        apiInteractor = new ApiInteractor();
        channelAdapter = new ChannelAdapter(new ArrayList<>());
        AnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(channelAdapter);

        alphaAdapter.setInterpolator(new OvershootInterpolator());

        // channelAdapter.setChannelItemClickListener(this);
        // channelPresenter = new ChannelPresenterImpl(new PlayerInteractorImpl(), this);
        //rvEpgGuideList = findViewById(R.id.rvChannelList);

//        rvChannelList.getItemAnimator().setAddDuration(1000);
//        rvChannelList.getItemAnimator().setRemoveDuration(1000);
//        rvChannelList.getItemAnimator().setMoveDuration(1000);
//        rvChannelList.getItemAnimator().setChangeDuration(1000);

    }

    private void initEpgView() {
        llEpgFirstColum = findViewById(R.id.llEpgFirstColum);
        tvFirstHour = findViewById(R.id.tvFirstHour);
        tvSecondndHour = findViewById(R.id.tvSecondndHour);
        tvThirdHour = findViewById(R.id.tvThirdHour);
    }

//    private void checkIntent() {
//        //channelUrl = null;
//        try {
//
//            channel = getIntent().getParcelableExtra(KEY_CHANNEL);
//            if (channel != null) {
//                String slug = channel.getSlug();
//                if (slug != null) {
//                    loadChannelUrl(slug);
//                }else {
//                    finish();
//                }
//            }
//
//            channelsItems = getIntent().getParcelableArrayListExtra(KEY_CHANNEL_LIST);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            //playContent(channelUrl);
//            if (channelsItems != null) {
//                showChannelList(channelsItems);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private String getUrl(String slug) {
        return "http://edge01.iptv.digijadoo.net/live/" + slug + "/playlist.m3u8";
    }

    private void playContent(com.hidayah.iptv.auth.model.ChannelsItem channelsItem, boolean isEpgClicked) {
        if(channelsItem == null)return;
        showChannelInfo(false);
        if(currentChannelId > 0 && currentChannelId != channelsItem.getChannelId()){
            preferencesHelper.saveLastChannel(currentChannelId);
        }
        currentChannelId = channelsItem.getChannelId();
        setChannelLogo(channelsItem);
        releasePlayer();
        initializePlayer(channelsItem.getStreamPath());
        if (epgGuideAdapter != null) {
            epgGuideAdapter.setCurrentShowUI(currentChannelId);
            epgGuideAdapter.notifyDataSetChanged();
        }
        if(!isEpgClicked){
            fetchCurrentEpgData(channelsItem.getChannelId());
        }
        if (channelsItemList != null && !channelsItemList.isEmpty()) {
            channelPosition = channelsItemList.indexOf(channelsItem);
        }

    }

    private void fetchCurrentEpgData(int channelId){
        new ApiInteractor().getCurrentEpgData(channelId, new EpgCurrentGuideListener() {
            @Override
            public void onGetCurrentEpgData(ArrayList<DataItem> dataItemList) {
                try {
                    if (dataItemList != null && !dataItemList.isEmpty()) {
                        final DataItem dataItem = dataItemList.get(0);
                        setUpdateEpgProgramData(dataItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErorr(String msg) {
                Log.d(TAG, "onErorr() called with: msg = [" + msg + "]");
            }
        });
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy h:mm a");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
    private void showEpgDescriptionDat(DataItem dataItem) {
        // llEpgFirstColum.setVisibility(View.GONE);
        //rvEpgGuideList.setVisibility(View.GONE);
        viewEpgDescription.setVisibility(View.VISIBLE);
        epgListWrapper.setVisibility(View.VISIBLE);
        epgTitle.setText(dataItem.getTitle());
//        epgSubTitle.setText(dataItem.getSubTitle());
//
        epgSubTitle.setVisibility(View.GONE);

        //epgChannelInfoTitle.setText(dataItem.getSubTitle());



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        //  format.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat formatter_to = new SimpleDateFormat("h:mm a");
        try {
            Date start = format.parse(dataItem.getStartTime());
            Date end = format.parse(dataItem.getEndTime());
            epgDuration.setText(formatter_to.format(start) + " - "+formatter_to.format(end)+" • "+dataItem.getVchipRating());
        } catch (Exception e) {
            e.printStackTrace();
        }
        epgDescription.setText(dataItem.getDescription());
       // epgVchipRating.setText(dataItem.getVchipRating());

        // setChannelLogo(dataItem);
        //  releasePlayer();
        // initializePlayer(String.valueOf(dataItem.getProgramId()));
    }



    private void setChannelLogo(ChannelsItem channelsItem){
        tvChannelNumber.setText(String.valueOf(channelsItem.getChannelNumber()));
        tvChannelName.setText(channelsItem.getChannelName());
        if (channelsItem.getLogoPath() != null) {
            Glide.with(this).load(channelsItem.getLogoPath()).into(ivLogo);
        }
        epgChannelInfoTitle.setText(channelsItem.getChannelNumber()+" . "+channelsItem.getChannelName());
    }

    private void setUpdateEpgProgramData(DataItem dataItem) {
        if (dataItem != null) {
            epgTitleLogoSection.setVisibility(View.VISIBLE);
            epgSubTitleLogoSection.setVisibility(View.VISIBLE);

            epgTitleLogoSection.setText(dataItem.getTitle());
            epgSubTitleLogoSection.setText(dataItem.getDescription());
        }else {
            epgTitleLogoSection.setVisibility(View.GONE);
            epgSubTitleLogoSection.setVisibility(View.GONE);
        }
    }

    private void setChannelLogo(DataItem dataItem) {
        tvChannelNumber.setText(String.valueOf(dataItem.getProgramId()));
        tvChannelName.setText(dataItem.getTitle());
    }

//    private void loadChannelUrl(String chanelSlug){
//        baseDataInteractor.getChannelUrl(new OnCompleteListener<ChannelUrlRes>() {
//             @Override
//             public void onComplete(boolean isSuccess, ChannelUrlRes result, int code, String errorMsg) {
//                 String url = null;
//                 try {
//                     url = result.getUrl();
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//
//                 if (url != null) {
//                     playContent(url);
//                 }else {
//                     if(code==401&&retryCount<=3){
//                         getAccestoken(true);
//                     }else {
//                         Toast.makeText(getApplicationContext(),"Resource  not found",Toast.LENGTH_SHORT).show();
//                     }
//
//                 }
//
//             }
//         },chanelSlug);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }

    private Player.EventListener playerErrorEventListener = new Player.EventListener() {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (isBehindLiveWindow(error)) {
                // playContent(channelUrl);
                // Re-initialize player at the live edge.
            } else {
                Log.d(TAG, "onPlayerError() called with: e = [" + error + "]");
                // Handle other errors
            }
        }
    };


//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        int visibility = rvChannelList.getVisibility();
//        boolean isVisible = visibility == View.VISIBLE;
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                rvChannelList.setVisibility(View.VISIBLE);
//                if (channelAdapter != null) {
//                    channelAdapter.notifyDataSetChanged();
//                }
//                return true;
//
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                rvChannelList.setVisibility(View.GONE);
//               // rvChannelList.setFocusable(false);
//                return true;
//
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                if(!isVisible){
//                    channelChange(true);
//                }
//                return true;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                if(!isVisible){
//                    channelChange(false);
//                }
//                return true;
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//            case KeyEvent.KEYCODE_ENTER:
//                if(!isVisible){
//                    playerView.showController();
//                }
//                boolean playWhenReady = player.getPlayWhenReady();
//                player.setPlayWhenReady(!playWhenReady);
//                return true;
//            default:
//                return super.onKeyUp(keyCode, event);
//        }
//
//    }

//    private void channelChange(boolean isNext){
//        try {
//            int newItemPosition = isNext?currentChannelItemPos+1:currentChannelItemPos-1;
//            if(newItemPosition>=0||channelsItems.size()-1<=newItemPosition){
//                onChannelLoad(channelsItems.get(newItemPosition),newItemPosition);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /*Started from here*/

    // Internal methods

    private void initializePlayer(String channelUrl) {
        // channelUrl = "https://content.uplynk.com/channel/3324f2467c414329b3b0cc5cd987b6be.m3u8";
        // channelUrl = "https://dal.krctechnologies.net/iptv/vantrix/ltv/Bell_AMI-F_AtopItci_2015_01_12_SD/hls_sd.m3u8";
        try {
            if(channelUrl == null)return;
            currentUrl = channelUrl;
            if (player == null) {
                TrackSelection.Factory trackSelectionFactory;
                trackSelectionFactory = new AdaptiveTrackSelection.Factory();
                trackSelector = new DefaultTrackSelector(trackSelectionFactory);
                setTrackSelectorParam();
                player = ExoPlayerFactory.newSimpleInstance(
                        /* context= */ this, new DefaultRenderersFactory(this), trackSelector);
                player.addListener(new PlayerEventListener());
                player.setPlayWhenReady(startAutoPlay);
                player.setPlayWhenReady(true);
                //  player.addAnalyticsListener(new EventLogger(trackSelector));
                playerView.setPlayer(player);
                playerView.setPlaybackPreparer(playbackPreparer);
                mediaSource = buildMediaSource(Uri.parse(channelUrl));
                player.prepare(mediaSource, false, false);
                player.addTextOutput(new TextOutput() {
                    @Override
                    public void onCues(List<Cue> cues) {
                        subtitleView.onCues(cues);
                    }
                });
            }
            //preload
//            openEpgActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTrackSelectorParam() {
        if (trackSelector != null) {
            trackSelector.setParameters(trackSelector
                    .getParameters().buildUpon().setSelectUndeterminedTextLanguage(isClosedCaptionEnabled).build());
        }
    }


    private PlaybackPreparer playbackPreparer = new PlaybackPreparer() {
        @Override
        public void preparePlayback() {
            if (player != null) {
                player.retry();
            }
        }
    };

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            updateTrackSelectorParameters();
            updateStartPosition();
            player.stop();
//            player.release();
            player = null;
            mediaSource = null;
            //trackSelector = null;
        }
        releaseMediaDrm();
    }

    private void releaseMediaDrm() {
//        if (mediaDrm != null) {
//            mediaDrm.release();
//            mediaDrm = null;
//        }
    }

    private void releaseAdsLoader() {
//        if (adsLoader != null) {
//            adsLoader.release();
//            adsLoader = null;
//            loadedAdTagUri = null;
//            playerView.getOverlayFrameLayout().removeAllViews();
//        }
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
            //   startAutoPlay = true;
            //   startWindow = C.INDEX_UNSET;
            //   startPosition = C.TIME_UNSET;
/*
            clearStartPosition();
            releasePlayer();
            initializePlayer(currentUrl);*/
        }
    }

    private void clearStartPosition() {
       startAutoPlay = true;
       startWindow = C.INDEX_UNSET;
       startPosition = C.TIME_UNSET;
    }

    /**
     * Returns a new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory() {
        return new DefaultHttpDataSourceFactory("hidyah_iptv_player");
    }


    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

//    @Override
//    public void onChannelLoad(ChannelsItem channelsItem, int position) {
//        this.channel = channelsItem;
//       // loadChannelUrl(channelsItem.getSlug());
//        currentChannelItemPos = position;
//        if (channelAdapter != null) {
//            ChannelAdapter.selectedPosition = currentChannelItemPos;
//            channelAdapter.notifyDataSetChanged();
//        }
//    }

    private static int playerRetryCount = 0;

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_BUFFERING) {
                // showControls();
                playerView.hideController();
            }
            if(playbackState == Player.STATE_READY){
                playerRetryCount = 0;
            }

        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
                //player.stop();
                releasePlayer();
                initializePlayer(currentUrl);
                Log.d(TAG, "onPlayerError() called with: isBehindLiveWindow(e) = [" + isBehindLiveWindow(e) + "]");
            }
        }



        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            setCaptions();
        }
    }

    private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage( ExoPlaybackException e) {
            playerRetryCount++;
            String errorString = e.getMessage();
            Log.d(TAG, "getErrorMessage() called with: e = [" + errorString + "]");
            if (errorString != null) {
                boolean unable_to_connect = errorString.contains("Unable to connect");
                if(unable_to_connect){
                    errorString = "Unable to connect to the server.";
                }else {
                    errorString = "Playback Failed.";
                }
            }
            if(playerRetryCount<3){
                clearStartPosition();
                initializePlayer(currentUrl);
            }

            return Pair.create(0, errorString);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
                initializePlayer(currentUrl);
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(currentUrl);
        }
        if (playerView != null) {
            playerView.onResume();
        }
        setUiFlags();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {

            if (playerView != null) {
                playerView.onPause();
            }
            player.stop();
            //releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (preferencesHelper != null) {
            preferencesHelper.saveLastChannel(currentChannelId);
        }
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                releasePlayer();
            }
            releasePlayer();
        }
        if (handler != null) {
            handler.removeCallbacks(runnableResfreshToken);
        }
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.i(logTag + "-DEBUG-" + instanceId, "Activity Restarted");
//        playerViewLayoutParams.width = LayoutParams.MATCH_PARENT;
//        playerViewLayoutParams.height = LayoutParams.MATCH_PARENT;
//        mainHandler.post(() -> playerView.setLayoutParams(playerViewLayoutParams));
//        webView.reload();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            mainHandler.post(() -> player.stop());
//            mainHandler.post(() -> player.release());
            player = null;
            trackSelector = null;
        }
       //webView.loadUrl("about:blank");
          //  releaseAdsLoader();
/*        if (handler != null) {
            handler.removeCallbacks(runnableResfreshToken);
        }*/
    }


    private Runnable runnableResfreshToken = new Runnable() {
        @Override
        public void run() {
            startRefreshTokenTimer();
        }
    };
    private void startRefreshTokenTimer() {
        getAccestoken(false);
        handler.postDelayed(runnableResfreshToken,preferencesHelper.getAccessTokenInterval());
    }

    private void getAccestoken(boolean isExpired) {
        retryCount++;
        apiInteractor.getAccessToken(new LoginContract.LoginCompleteListener() {
            @Override
            public void onLoginSuccess(Data data) {
//              retryCount = 0;
//              if(isExpired){
//                  loadChannelUrl(channel.getSlug());
//              }
//                if (userid != null) {
//                    tvUserId.setText(userid);
//                    tvUserId.setVisibility(View.VISIBLE);
//                }else {
//                    tvUserId.setVisibility(View.GONE);
//                }
//                Log.d(TAG, "onLoginSuccess() called with: refreshToken = [" + refreshToken + "], accessToken = [" + accessToken + "]");
            }

            @Override
            public void onLoginFailed(String msg) {
                if(retryCount<=3){
                    getAccestoken(true);
                }
                Log.d(TAG, "onLoginFailed() called with: msg = [" + msg + "]");
            }
        });
    }
    private void openEpgActivity() {
        long currentTimeMillis = System.currentTimeMillis();
        if((currentTimeMillis - lastEpgFetchTime >= 30*60*1000) || (epgChannelItemList != null && epgChannelItemList.isEmpty())){
            progressBar.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(),"EPG fetching",Toast.LENGTH_SHORT).show();
            final ApiInteractor apiInteractor = new ApiInteractor();
            apiInteractor.getEpgguideData(channelToken, new EpgGuideListener() {
                @Override
                public void onGetEpgData(ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> dataItemList) {

                    if (dataItemList != null && !dataItemList.isEmpty()) {
                        lastEpgFetchTime = currentTimeMillis;
                        epgChannelItemList.clear();
                        epgChannelItemList.addAll(dataItemList);
                        final DataItem dataItem = dataItemList.get(0).data.get(0);
//                        setUpdateEpgProgramData(dataItem);
                        showTvGuideData(epgChannelItemList);
                    }
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(),"EPG fetched dataItemList",Toast.LENGTH_SHORT).show();
   //                 Log.d(TAG, "onGetEpgData() called with: dataItemList = [" + dataItemList.size() + "]");
                }

                @Override
                public void onErorr(String msg) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onErorr() called with: msg = [" + msg + "]");
                }
            });
        }else {
            Log.d(TAG, "openEpgActivity() called cached section");
        }

    }

    private void showTvGuideData(ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> dataItemList) {
        if (dataItemList != null) {
            setFirstColum();
            /**epgGuideAdapter.setChannelItemClickListener(epgItemClickListener);
             epgGuideAdapter.setCurrentShowUI(currentChannelId);
             rvEpgGuideList.swapAdapter(epgGuideAdapter,false);
             epgGuideAdapter.addEpgChannelList(dataItemList);
             //llEpgFirstColum.setVisibility(View.VISIBLE);
             epgListWrapper.setVisibility(View.VISIBLE);
             rvEpgGuideList.setVisibility(View.VISIBLE);*/
            epgListWrapper.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.VISIBLE);
            rvEpgGuideList.setVisibility(View.GONE);
            rlErorr.setVisibility(View.GONE);
            AndroidThreeTen.init(this);
            fragmentContainer.setVisibility(View.VISIBLE);
            final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            EpgFragment epgFragment = new EpgFragment();
            epgFragment.setChannelData(simpleChannelList,dataItemList, this);
            transaction.replace(R.id.fragment_container, epgFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * EpgFragment click event
     */
    @Override
    public void onScheduleClicked(DataItem dataItem) {
        //TODO handel click event
        if(dataItem == null) return;
        final int channelId = dataItem.getChannelId();
        final int intParentChannelId = dataItem.getParenChannelId();
        if(isCurrentShow(dataItem)){
            if(intParentChannelId != -1){
                playContent(findChannelItem(intParentChannelId),true);
            }else {
                playContent(findChannelItem(channelId),true);
            }
            switchFullScreenPlayer();
            setUpdateEpgProgramData(dataItem);
        }else {


            String dtarttime ="";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            //  format.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatter_to = new SimpleDateFormat("h:mm a");
            try {
                Date start = format.parse(dataItem.getStartTime());
                dtarttime = formatter_to.format(start);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Toast.makeText(getApplicationContext(),dataItem.getTitle()+" will start at "+dtarttime, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScheduleSelected(DataItem dataItem) {
        //TODO on selected  show data
        if(dataItem == null) return;
        showEpgDescriptionDat(dataItem);
    }

    long lastUpdateFirstColum = 0;
    private void setFirstColum() {
        long currentTimeMillis = System.currentTimeMillis();
        this.lastUpdateFirstColum = currentTimeMillis;
        SimpleDateFormat dateFormat = new SimpleDateFormat("h a");
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date date = new Date();
        //int minutes = date.getMinutes();
        final Date date1 = new Date(date.getTime());
        final Date date2 = new Date(System.currentTimeMillis() + 1*3600 * 1000);
        final Date date3 = new Date(System.currentTimeMillis() + 2*3600 * 1000);
        final Date date4 = new Date(System.currentTimeMillis() + 3*3600 * 1000);
        String s1 = dateFormat.format(date1);
        String s2 = dateFormat.format(date2);
        String s3 = dateFormat.format(date3);
        String s4 = dateFormat.format(date4);
        tvFirstHour.setText(s1+" - "+s2);
        tvSecondndHour.setText(s2+" - "+s3);
        tvThirdHour.setText(s3+" - "+s4);
    }


    private int epgClickChannelId = -1;
    private EpgItemClickListener epgItemClickListener = new EpgItemClickListener() {
        @Override
        public void onEpgItemClick(DataItem dataItem, int intParentChannelId) {
            final int channelId = dataItem.getChannelId();
            if(isCurrentShow(dataItem)){
                if(intParentChannelId != -1){
                    playContent(findChannelItem(intParentChannelId),true);
                }else {
                    playContent(findChannelItem(channelId),true);
                }
                switchFullScreenPlayer();
                setUpdateEpgProgramData(dataItem);
            }else {
                Toast.makeText(getApplicationContext(),"It will start at "+ dataItem.getStartTime(),Toast.LENGTH_SHORT).show();
            }
//            if(epgClickChannelId == channelId){
//                switchFullScreenPlayer();
//                epgClickChannelId = -1;
//            }else {
//                epgClickChannelId = channelId;
//              //  playContent(dataItem);
//                if(isCurrentShow(dataItem)){
//                    if(intParentChannelId != -1){
//                        playContent(findChannelItem(channelsItemList,intParentChannelId));
//                    }else {
//                        playContent(findChannelItem(channelsItemList,channelId));
//                    }
//
//                    Toast.makeText(getApplicationContext(),"Press again here to fullscreen",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getApplicationContext(),"It will start at "+ dataItem.getStartTime(),Toast.LENGTH_SHORT).show();
//                }
//
//            }
//            multipleClickHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                   epgClickChannelId = -1;
//                }
//            },2000);
        }

        @Override
        public Context getContext() {
            return ExoPlayerActivity.this;
        }

        @Override
        public void onEpgItemFocused(View v, DataItem dataItem, int pos) {
            //  rvEpgGuideList.scrollToPosition(pos);
            showEpgDescriptionDat(dataItem);
            Log.d(TAG, "onEpgItemFocused() called with: dataItem = [" + dataItem + "]");
        }

    };



    public interface EpgGuideListener{
        void onGetEpgData(ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> dataItemList);
        void onErorr(String msg);
    }
    public interface EpgCurrentGuideListener{
        void onGetCurrentEpgData(ArrayList<com.hidayah.iptv.player.model.epg.DataItem> dataItemList);
        void onErorr(String msg);
    }


    public void setCaptions() {
        // if we're playing, we will enable or disable cpations here.
        int rendererIndex = -1;
        int trackGroupIndex = -1;
        int trackIndex = -1;
        TrackGroupArray trackGroups = null;

        if (trackSelector != null) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                int rendererCount = mappedTrackInfo.getRendererCount();
                for (int i = 0; i < rendererCount; i++) {
                    if (mappedTrackInfo.getRendererType(i) == C.TRACK_TYPE_TEXT) {
                        trackGroups = mappedTrackInfo.getTrackGroups(i);
                        if (trackGroups != null) {
                            for (int j = 0; j < trackGroups.length; j++) {
                                TrackGroup group = trackGroups.get(j);
                                if (group != null) {
                                    for (int k = 0; k < group.length; k++) {
                                        Format format = group.getFormat(k);
                                        if (format != null) {
                                            String mimeType = format.sampleMimeType;
                                            if (mimeType != null && mimeType.toLowerCase() == "application/cea-608") {
                                                rendererIndex = i;
                                                trackGroupIndex = j;
                                                trackIndex = k;
                                            }
                                        }
                                        if (rendererIndex >= 0) {
                                            break;
                                        }
                                    }
                                }
                                if (rendererIndex >= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (rendererIndex >= 0) {
                        break;
                    }
                }
            }
            DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();

            DefaultTrackSelector.ParametersBuilder builder = parameters.buildUpon();

            if (rendererIndex >= 0) {
                builder.setRendererDisabled(rendererIndex, !isClosedCaptionEnabled);
                if (isClosedCaptionEnabled) {
                    Log.i("RALLY-LOG", "Turning captions on");
                    Log.i("RALLY-LOG", "Renderer: " + Integer.toString(rendererIndex));
                    Log.i("RALLY-LOG", "Track Group: " + Integer.toString(trackGroupIndex));
                    Log.i("RALLY-LOG", "Track: " + Integer.toString(trackIndex));
                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(trackGroupIndex, trackIndex);
                    //DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(0, 0, 2, 0);
                    //DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(0, 0, C.SELECTION_REASON_MANUAL, 0);
                    builder.setSelectionOverride(rendererIndex, mappedTrackInfo.getTrackGroups(rendererIndex), override);
                } else {
                    builder.clearSelectionOverrides();
                }
                trackSelector.setParameters(builder);
            }
        }
    }

}
