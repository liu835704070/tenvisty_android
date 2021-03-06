package com.tws.commonlib.bean;

import android.content.Context;
import android.graphics.Bitmap;

import com.tws.commonlib.App;
import com.tws.commonlib.R;
import com.tws.commonlib.base.CameraClient;
import com.tws.commonlib.base.TwsTools;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface IMyCamera {
    public enum CameraP2PType{
        TutkP2P ,
        HichipP2P
    }
    public enum Supllier{
        UnKnown ,
        AN ,
        FB,
        HX
    }
    public interface TaskExecute {
        void onPosted(IMyCamera camera,Object data);
    }
    public static String DEFAULT_PASSWORD = "admin";
    public static String NO_USE_UID = "00000000000000000000";
    public String getNickName();
    public  void setNickName(String nickName);
    public  String getUid();
    public  String getAccount();
    public  String getPassword();
    public  void  setPassword(String password);
    public  int  getEventCount();
    public  float getVideoRatio(Context context);
    public  void  setVideoRatio(Context context,float ratio);
    public  boolean isFirstLogin();
    public  boolean isPlaying();
    public CameraState getState();
    public int getEventNum(int eventType);
    public void setEventNum(int eventNum,int eventType);
    public int refreshEventNum(Context context,int eventType);
    public int clearEventNum(Context context,int eventType);
    public String getSoftVersion();
    public  void setSoftVersion(String version);
    //只连接，不登录
    public void connect();
    public Bitmap getSnapshot();
    public void setSnapshot(Bitmap snapshot);
    public void asyncSnapshot(final TaskExecute te, final int channel);
    public void start();
    public void asyncStart(final TaskExecute ex);
    public void stop();
    public void asyncStop(final TaskExecute ex);
    public void asyncWakeUp(final TaskExecute ex);
    public void startVideo();
    public void asyncStartVideo(final TaskExecute te);
    public void stopRecording(final int avChannel);
    public void asyncStopRecording(final int avChannel);
    public  void  startRecording(String file,int channel);
    public void sendIOCtrl(final int avChannel, final int type, final byte[] data);
    public void asyncSendIOCtrl(final int avChannel, final int type, final byte[] data);
    public void stopVideo();
    public void asyncStopVideo(final TaskExecute te);
    public void startAudio();
    public void asyncStartAudio(final TaskExecute te);
    public void stopAudio();
    public void asyncStopAudio(final TaskExecute te);
    public void startSpeak();
    public void asyncStartSpeak(final TaskExecute te);
    public void stopSpeak();
    public void asycnStopSpeak(final TaskExecute te);
    public void ptz(int type);
    public void asyncPtz(final int type);
    public boolean shouldPush(int evtType,long evtTime);
    public void openPush(final CameraClient.ServerResultListener2 succListner, final CameraClient.ServerResultListener2 errorListner);
    public void closePush(Context context);
    public  boolean  isPushOpen();
    public  boolean  setPushOpen(boolean open);
    public void remove(Context context);
    public void save(Context context);
    public boolean sync2Db(Context context);
    public boolean isExist();
    public boolean isSessionConnected();
    public boolean isConnected();
    public boolean isDisconnect();
    public boolean isPasswordWrong();
    public boolean isConnecting();
    public  boolean isNotConnect();
    public boolean isSleeping();
    public boolean isWakingUp();
    public void saveSnapShot(final int channel, final String subFolder, final String fileName, final TaskExecute te);
    public int getVideoQuality();
    public void setVideoQuality(int videoQuality);
    public  int getCameraModel();
    public  int setCameraModel(int mode);

    public  String getCameraStateDesc();
    public  int getCameraStateBackgroundColor();
    public int getIntId();
    public  int getTotalSDSize();
    public  void  setTotalSDSize(int total);
    public String getCustomTypeVersion();

    public void setCustomTypeVersion(String customTypeVersion);
    public String getVendorTypeVersion();

    public void setVendorTypeVersion(String vendorTypeVersion);

    public String getSystemTypeVersion();

    public void setSystemTypeVersion(String systemTypeVersion);

    public  long getDatabaseId();
    public  void  setDatabaseId(long dbId);
    public void registerIOTCListener(IIOTCListener listener);
    public void unregisterIOTCListener(IIOTCListener listener);
    public  void registerPlayStateListener(IPlayStateListener listener);
    public  void unregisterPlayStateListener(IPlayStateListener listener);
    public  CameraP2PType getP2PType();
    public void registerDownloadListener(IDownloadCallback listener) ;
    public void unregisterDownloadListener(IDownloadCallback listener);
    public  boolean supportCable();
    public  boolean supportBattery();
    public  Supllier getSupplier();
    public String getModelName();
    public void setModelName(String modelName);
    public class MyCameraFactory{
        public  static MyCameraFactory instance;
        public static synchronized MyCameraFactory shareInstance(){
            if(instance == null){
                instance = new MyCameraFactory();
            }
            return instance;
        }
        public IMyCamera createCamera(String nickName, String uid, String account, String pwd) {
            IMyCamera camera = null;
            if(uid.length() == 17){
                camera = new HichipCamera(App.getContext(), nickName, uid, account,pwd);
            }
            else{
                camera = new MyCamera(nickName,uid,account,pwd);
            }
            return camera;
        }
    }

    public BatteryStatus getBatteryStatus();

    public boolean isDefaultFunc();

    public void setFunctionFlag(Context context, byte[] functionFlag) ;

    public boolean hasSetFunctionFlag();

    public boolean hasPTZ(Context context);

    public boolean hasListen(Context context);

    public boolean hasPreset(Context context);

    public boolean hasZoom(Context context);

    public boolean hasSDSlot(Context context);

}
