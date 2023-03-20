# SigmaInteractive SDK

## Requirement: Android minSdk 21, Exoplayer 2.17.+

### I. Init SDK

Add [sigma-interactive.aar](https://github.com/truongnguyen1804/sigma-interactive-android-tv-example/tree/main/libs)  file to libs folder.

Add to app/build.gradle:

```
dependencies {
   ...
   implementation files('../libs/sigma-interactive.aar')

}
```

### II. Use

1. Init SigmaInteractive sdk to project (**I**).

2. Create Interactive webview 

   ```
   _interactiveWebview = new SigmaWebView(this, new SetPlayerListener() {
       @Override
       public View onSetPlayer() {
           return playerView;
       }
   });
   ```

   

2. Add event listening when id3 starts parsing to send data to interactive sdk

   [SigmaRendererFactory](https://github.com/phamngochai123/sigma-interactive-sdk-example/blob/mobile-android/app/src/main/java/com/example/sigmainteractive/SigmaRendererFactory.java) see in demo

   ```java
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
   
   ```

3. Add event listening when id3 returns the right timer to send data to interactive sdk

   ```java
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
   ```

4. Create SigmaWebViewCallback to listen for events from interactive sdk.

   5.1 In onReady function send json string data to interactive sdk (required)

   ```java
   _interactiveWebview.sendOnReadyBack(new Gson().toJson(getUserOption(token, currrentChannelId)));
   ```

   5.2 In the onKeyDown function listen to the key events returned from the webview and handle those events.

   ```java
   
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
   ```

   5.3 The onReload function is called when the token expires, sending back json string data to the interactive sdk
   
   ```java
   	_interactiveWebview.sendOnReadyBack(new Gson().toJson(getUserOption(token, currrentChannelId)));
   ```
   
   

#### 



