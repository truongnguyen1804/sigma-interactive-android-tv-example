package com.tdm.tdminteractive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.tdm.sigmainteractivetvsdk.SetPlayerListener;
import com.tdm.sigmainteractivetvsdk.SigmaWebView;
import com.tdm.sigmainteractivetvsdk.SigmaWebViewCallback;
import com.tdm.sigmainteractivetvsdk.SigmaWebviewInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PlayerActivity extends AppCompatActivity {
    PlayerView playerView;

    ExoPlayer exoPlayer;
    private static final String KEY_AUTH = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWQiOiJhZG1pbiIsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE2NDEzNTYyNTl9.WBRTuqhjBvzHTWCSorkVWeGeRcDFZUHzkGekDGtuZqg";

    private MediaItem mediaItem;

    private String[][] urlChannels = {
            {"c9c2ebfb-2887-4de6-aec4-0a30aa848915", "http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"}
            , {"32a55ed3-4ee1-42f8-819a-407b54a39923", "http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"}
            , {"60346597-8ed9-48de-bd4d-8546d0070c7c", "http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"}
            , {"22e1fdb6-8d10-4193-8411-562c7104aa2b", "http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"}};

    private BodyTest[] bodyTests = {
            new BodyTest("user", "0352153688", "default-app", new BodyTest.UserData("0352153688", "user", "android")),
            new BodyTest("user", "0352153666", "default-app", new BodyTest.UserData("0352153666", "user", "android")),
            new BodyTest("user", "0352153677", "default-app", new BodyTest.UserData("0352153677", "user", "android")),
            new BodyTest("user", "0352153688", "default-app", new BodyTest.UserData("0352153688", "user", "android"))
    };

    private String[] idUser = {"", "0352153666", "0352153677", "0352153688"};

    private SigmaWebviewInterface _interactiveWebview;
    private RelativeLayout interactiveLayout;
    private int currentIndexChannel = 0;
    private int currentIndexInteractive = 0;
    private String currrentChannelId = "";

    private Button btnGuest, btn1, btn2, btn3, btnChannel1, btnChannel2, btnChannel3, btnChannel4;

    private LinearLayout layoutViewGroup;

    SharedPreferences sharedPreferences;

    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("shaTest", MODE_PRIVATE);
        config();
    }

    public void config() {

        btnGuest = findViewById(R.id.btn_guest);
        btn1 = findViewById(R.id.btn_u_1);
        btn2 = findViewById(R.id.btn_u_2);
        btn3 = findViewById(R.id.btn_u3);

        btnChannel1 = findViewById(R.id.btn_channel_1);
        btnChannel2 = findViewById(R.id.btn_channel_2);
        btnChannel3 = findViewById(R.id.btn_channel_3);
        btnChannel4 = findViewById(R.id.btn_channel_4);

        interactiveLayout = findViewById(R.id.interactive_layout);
        layoutViewGroup = findViewById(R.id.layout_group);
        playerView = findViewById(R.id.player);

        currentIndexChannel = 0;
        currentIndexInteractive = 0;
        initPlayer(currentIndexChannel);
        action();
    }

    private void action() {
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInteractive(0);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInteractive(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInteractive(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInteractive(3);
            }
        });
        btnChannel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(0);
            }
        });
        btnChannel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(1);
            }
        });
        btnChannel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(2);
            }
        });
        btnChannel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer(3);
            }
        });

        btnGuest.setNextFocusUpId(R.id.sigma_webview);
        btn1.setNextFocusUpId(R.id.sigma_webview);
        btn2.setNextFocusUpId(R.id.sigma_webview);
        btn3.setNextFocusUpId(R.id.sigma_webview);
        btnChannel1.setNextFocusUpId(R.id.sigma_webview);
        btnChannel2.setNextFocusUpId(R.id.sigma_webview);
        btnChannel3.setNextFocusUpId(R.id.sigma_webview);
        btnChannel4.setNextFocusUpId(R.id.sigma_webview);
        ((WebView) _interactiveWebview).setNextFocusDownId(R.id.btn_guest);

    }

    private void setVisibleViewGroup() {
        if (layoutViewGroup.getVisibility() == View.VISIBLE) {
            layoutViewGroup.setVisibility(View.GONE);
            if (_interactiveWebview != null) {
                ((WebView) _interactiveWebview).setFocusable(true);
                ((WebView) _interactiveWebview).requestFocus();
            }
        } else {
            layoutViewGroup.setVisibility(View.VISIBLE);
        }
    }

    private void initPlayer(int index) {

        currentIndexChannel = index;

        String channelId = urlChannels[currentIndexChannel][0];
        String url = urlChannels[currentIndexChannel][1];

        currrentChannelId = channelId;

        initInteractive(currentIndexInteractive);

        DefaultRenderersFactory renderersFactory = new SigmaRendererFactory(this, metadata -> {
            if (metadata != null) {
                for (int i = 0; i < metadata.length(); i++) {
                    Metadata.Entry entry = metadata.get(i);
                    if (entry instanceof TextInformationFrame) {
                        String des = ((TextInformationFrame) entry).description;
                        String value = ((TextInformationFrame) entry).value;
                        if (des.toUpperCase().equals("TXXX")) {
                            _interactiveWebview.sendID3TagInstant(value);
                        }
                    }
                }
            }
        });

        exoPlayer = new ExoPlayer.Builder(this, renderersFactory).build();


        exoPlayer.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onMetadata(EventTime eventTime, Metadata metadata) {
                Log.d("checkListener", new Gson().toJson(metadata));
                if (metadata != null) {
                    for (int i = 0; i < metadata.length(); i++) {
                        Metadata.Entry entry = metadata.get(i);
                        if (entry instanceof TextInformationFrame) {
                            String des = ((TextInformationFrame) entry).description;
                            String value = ((TextInformationFrame) entry).value;
                            if (des.toUpperCase().equals("TXXX")) {
                                _interactiveWebview.sendID3Tag(value);
                            }
                        }
                    }
                }
            }
        });


        mediaItem = new MediaItem.Builder()
                .setUri(url)
                .setClippingConfiguration(new MediaItem.ClippingConfiguration.Builder().build()).build();

        playerView.setUseController(false);

        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(new Player.Listener() {

            @Override
            public void onPlayerError(PlaybackException error) {
                Log.d("onPlayerError", "errorCode: " + error.getMessage());
                if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                    exoPlayer.seekToDefaultPosition();
                    exoPlayer.prepare();
                } else {
                    // Handle other errors.
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Log.d("onIsPlayingChanged", isPlaying + "");
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {

            }

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                Log.d("exoPlayer", "onPlayWhenReadyChanged" + reason);

            }
        });
        playerView.setPlayer(exoPlayer);

    }

    private void initInteractive(int index) {
        currentIndexInteractive = index;
        ((RelativeLayout) interactiveLayout).removeAllViews();
        _interactiveWebview = new SigmaWebView(this, new SetPlayerListener() {
            @Override
            public View onSetPlayer() {
                return playerView;
            }
        });

        ((RelativeLayout) interactiveLayout)
                .addView(((WebView) _interactiveWebview), RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ((WebView) _interactiveWebview).setId(R.id.sigma_webview);
        ((WebView) _interactiveWebview).bringToFront();
        _interactiveWebview.setActivity(PlayerActivity.this);

        token = "";
        if (currentIndexInteractive != 0) {
            if (sharedPreferences.getString(idUser[currentIndexInteractive], "").length() > 0) {
                token = sharedPreferences.getString(idUser[currentIndexInteractive], "");

            } else {
                RequestQueue queue = Volley.newRequestQueue(PlayerActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, "https://dev-livestream.gviet.vn/api/interactive/v1/users/gen-token",
                        response -> {
                            Log.d("StringRequest", "success: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                sharedPreferences.edit().putString(idUser[currentIndexInteractive], jsonObject.optString("token", "")).apply();
                                initInteractive(currentIndexInteractive);
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                    Log.d("StringRequest", "error: " + error.networkResponse.statusCode + "\n" + new Gson().toJson(error));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> header = new HashMap<>();
                        header.put("authorization", KEY_AUTH);
                        return header;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        String body = new Gson().toJson(bodyTests[currentIndexInteractive]);
                        Log.d("checkBody", body);

                        try {
                            return body == null ? null : body.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", body, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                };

                queue.add(request);
            }

        }

        _interactiveWebview.setCallback(new SigmaWebViewCallback() {
            @Override
            public void onReady() {
                Log.d("_interactiveWebview", "onReady");
                ((WebView) _interactiveWebview).setFocusable(true);
                ((WebView) _interactiveWebview).requestFocus();
                _interactiveWebview.sendOnReadyBack(new Gson().toJson(getUserOption(token, currrentChannelId)));
            }

            @Override
            public void fullReload() {
                _interactiveWebview.sendOnReadyBack(new Gson().toJson(getUserOption(token, currrentChannelId)));
            }


            @Override
            public void onKeyDown(int code) {
                Log.d("_interactiveWebview", "onKeyDown " + code);

                KeyEvent event = null;

                switch (code) {
                    case SigmaWebView.KEYCODE_UP:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP);
                        break;
                    case SigmaWebView.KEYCODE_DOWN:
                        btnGuest.requestFocus();
                        ((WebView) _interactiveWebview).setFocusable(true);
                        ((WebView) _interactiveWebview).clearFocus();
                        break;
                    case SigmaWebView.KEYCODE_LEFT:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
                        break;
                    case SigmaWebView.KEYCODE_RIGHT:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case SigmaWebView.KEYCODE_ENTER:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
                        break;
                    case SigmaWebView.KEYCODE_ESCAPE:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
                        break;
                    case SigmaWebView.KEYCODE_0:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_0);
                        break;
                    case SigmaWebView.KEYCODE_1:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1);
                        break;
                    case SigmaWebView.KEYCODE_2:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_2);
                        break;
                    case SigmaWebView.KEYCODE_3:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_3);
                        break;
                    case SigmaWebView.KEYCODE_4:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_4);
                        break;
                    case SigmaWebView.KEYCODE_5:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_5);
                        break;
                    case SigmaWebView.KEYCODE_6:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_6);
                        break;
                    case SigmaWebView.KEYCODE_7:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7);
                        break;
                    case SigmaWebView.KEYCODE_8:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_8);
                        break;
                    case SigmaWebView.KEYCODE_9:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_9);
                        break;
                    case SigmaWebView.KEYCODE_PAGEUP:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_PAGE_UP);
                        break;
                    case SigmaWebView.KEYCODE_PAGEDOWN:
                        event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_PAGE_DOWN);
                        break;
                }

                if (event != null) {
                    PlayerActivity.this.onKeyDown(event.getKeyCode(), event);
                }
            }

            @Override
            public void onOverlayShow() {
                Log.d("_interactiveWebview", "onOverlayShow");
            }

            @Override
            public void onOverlayHide() {
                Log.d("_interactiveWebview", "onOverlayHide");
            }

            @Override
            public void onSetSession(String s) {

                Log.d("_interactiveWebview", "onSetSession " + s);

            }

            @Override
            public void onConfigChange(String s) {
                Log.d("_interactiveWebview", "onConfigChange " + s);
            }

            @Override
            public void onPanelShowed(boolean b) {
                Log.d("_interactiveWebview", "onPanelShowed " + b);
            }

            @Override
            public void overlayConfigChange(String s) {
                Log.d("_interactiveWebview", "overlayConfigChange " + s);
            }

            @Override
            public void onShowOverlay(String s) {
                Log.d("_interactiveWebview", "onShowOverlay " + s);
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDownMain", keyCode + "");

        return super.onKeyDown(keyCode, event);

    }

    private SigmaWebView.UserOptions getUserOption(String token, String channelId) {
        return new SigmaWebView.UserOptions(token, channelId,
                "default-app", "", true, "ArrowLeft", true, "");
    }

    public static boolean isKeyBack(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                return true;
        }
        return false;
    }


    public static class BodyTest {
        String role;
        String id;
        String appId;
        UserData userData;

        public BodyTest(String role, String id, String appId, UserData userData) {
            this.role = role;
            this.id = id;
            this.appId = appId;
            this.userData = userData;
        }

        public static class UserData {
            String id;
            String role;
            String platform;

            public UserData(String id, String role, String platform) {
                this.id = id;
                this.role = role;
                this.platform = platform;
            }
        }
    }


}