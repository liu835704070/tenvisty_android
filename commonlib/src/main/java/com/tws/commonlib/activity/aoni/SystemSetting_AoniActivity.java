package com.tws.commonlib.activity.aoni;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.hichip.base.HiLog;
import com.tutk.IOTC.AVIOCTRLDEFs;
import com.tutk.IOTC.Camera;
import com.tws.commonlib.MainActivity;
import com.tws.commonlib.R;
import com.tws.commonlib.activity.BaseActivity;
import com.tws.commonlib.base.MyConfig;
import com.tws.commonlib.base.TwsProgressDialog;
import com.tws.commonlib.base.TwsToast;
import com.tws.commonlib.base.TwsTools;
import com.tws.commonlib.bean.IIOTCListener;
import com.tws.commonlib.bean.IMyCamera;
import com.tws.commonlib.bean.TwsDataValue;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 添加摄像机的界面
 *
 * @author Administrator
 */
public class SystemSetting_AoniActivity extends BaseActivity implements IIOTCListener {

    private String dev_uid;
    private IMyCamera camera;
    private static final int UPDATE_GET_INFO = 0x101;
    boolean isUpdating = false;
    int updateState = -1;
    int resetState = -1;
    int rebootState = -1;
    AVIOCTRLDEFs.SMsgAVIoctrlDeviceInfoResp deviceInfo;
    String deviceVersion = null;
    private static final int UPDATA_STATE_NONE = 0;
    private static final int UPDATA_STATE_CHECKING = 1;
    private static final int GET_UPDATE_VERSION_NUM = 0X9999;
    private static final int GET_UPDATE_VERSION_DATA = 0X10000;

    private int updateStatus = UPDATA_STATE_NONE;

    private static long updateTime = 0;

    private String redirectAddr = null;
    private boolean send = false;
    private boolean isCheckFm = false;
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_system_setting_aoni);
        dev_uid = this.getIntent().getExtras().getString(TwsDataValue.EXTRA_KEY_UID);
        for (IMyCamera _camera : TwsDataValue.cameraList()) {
            if (_camera.getUid().equalsIgnoreCase(dev_uid)) {
                camera = _camera;
                break;
            }
        }
        this.setTitle(getResources().getString(R.string.title_camera_setting_system));
        initView();
        camera.registerIOTCListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
    }


    public void doClickLL(View view) {

    }


    @Override
    public void receiveFrameData(IMyCamera camera, int avChannel, Bitmap bmp) {

    }

    @Override
    public void receiveFrameInfo(IMyCamera camera, int avChannel, long bitRate, int frameRate, int onlineNm, int frameCount, int incompleteFrameCount) {

    }

    @Override
    public void receiveSessionInfo(IMyCamera camera, int resultCode) {
        Message msg = new Message();
        msg.what = resultCode;
        // sessionHandler.sendMessage(msg);
    }

    @Override
    public void receiveChannelInfo(IMyCamera camera, int avChannel, int resultCode) {

    }

    @Override
    public void receiveIOCtrlData(IMyCamera camera, int avChannel, int avIOCtrlMsgType, byte[] data) {
        Bundle bundle = new Bundle();
        bundle.putInt("sessionChannel", avChannel);
        bundle.putByteArray("data", data);

        Message msg = new Message();
        msg.what = avIOCtrlMsgType;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void initSendAudio(IMyCamera paramCamera, boolean paramBoolean) {

    }

    @Override
    public void receiveOriginalFrameData(IMyCamera paramCamera, int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3) {

    }

    @Override
    public void receiveRGBData(IMyCamera paramCamera, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {

    }

    @Override
    public void receiveRecordingData(IMyCamera paramCamera, int avChannel, int paramInt1, String path) {

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");

            switch (msg.what) {
                case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_DEVINFO_RESP: {
                    deviceInfo = new AVIOCTRLDEFs.SMsgAVIoctrlDeviceInfoResp(data);
                    //edt_mode.setText(model);
                    String strBinaryVersion = Integer.toBinaryString(deviceInfo.version);
                    while (strBinaryVersion.length() < 32) {
                        strBinaryVersion = "0" + strBinaryVersion;
                    }
                    deviceVersion = "";
                    for (int i = 0; i < 4; i++) {
                        deviceVersion += Integer.parseInt(strBinaryVersion.substring(i * 8, i * 8 + 8), 2) + "";
                        if (i != 3) {
                            deviceVersion += ".";
                        }
                    }
                    if(!isCheckFm){
                        return;
                    }
                    isCheckFm = false;

                    isUpdate = false;

                    for (UpdateInfo upinfo : mListUpdataInfo) {

                        HiLog.v("version:" + deviceVersion);
                        String[] b_new = upinfo.ver.split("\\.");
                        String[] b_old = deviceVersion.split("\\.");

                        if (b_new.length == b_old.length) {
                            for (int i = 0; i < b_new.length; i++) {

                                if (i == b_new.length - 1) {

                                    String[] last_new_array = b_new[i].split("-");
                                    String[] last_old_array = b_old[i].split("-");


                                    HiLog.v("last_new_array:" + last_new_array.length + "  last_old_array:" + last_old_array.length);
                                    int newi = 0;
                                    if (last_new_array.length >= 1) {
                                        newi = Integer.parseInt(last_new_array[0]);
                                    }
                                    //string to int erro

                                    int oldi = 0;
                                    if (last_old_array.length >= 1) {
                                        oldi = Integer.parseInt(last_old_array[0]);
                                    }


                                    HiLog.v("newi:" + newi + "  oldi:" + oldi);
                                    if (newi > oldi) {
                                        updateInfo = upinfo;
                                        isUpdate = true;
                                    }
                                } else {
                                    if (!b_old[i].equals(b_new[i])) {
                                        HiLog.v(i + "b_new:" + b_new[i]);
                                        HiLog.v(i + "b_old:" + b_old[i]);
                                        isUpdate = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (isUpdate) {
                            break;
                        }
                    }
                    //				if(progressDialog != null) {
                    if (isUpdate) {

                        HiLog.v("ThreadCheckRedirect  start");
                        if (updateInfo == null) return;

                        redirectAddr = updateInfo.url;
                        ThreadCheckRedirect tr = new ThreadCheckRedirect();
                        tr.start();
                    } else {
                        dismissLoadingProgress();
                        showAlert(getString(R.string.dialog_msg_new_firmware_already_latest));
                        //	dismissLoadingProgress();
                        updateStatus = UPDATA_STATE_NONE;
                    }
                    //				}


                }
                break;
                case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_REBOOT_SYSTEM_RESP: {
                    AVIOCTRLDEFs.SMsgAVIoctrlRusultResp resp = new AVIOCTRLDEFs.SMsgAVIoctrlRusultResp(data);
                    if (resp.result == 0) {
                        rebootState = 1;
                        dismissLoadingProgress();
                        back2Activity(MainActivity.class);
                    } else {
                        dismissLoadingProgress();
                        showAlert(getString(R.string.alert_reboot_fail));
                    }
                    break;
                }
                case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SET_UPRADE_RESP: {
                    if (data[0] != 0) {
                        dismissLoadingProgress();
                        showAlert(getString(R.string.dialog_msg_new_firmware_updating_fail));
                    } else {
                        updateState = 1;
                        dismissLoadingProgress();
                        back2Activity(MainActivity.class);
                    }
                    break;
                }
                case GET_UPDATE_VERSION_NUM:

                    HiLog.e("UPDATE RETURN");
                    if (msg.arg1 == 1) {
                        updateInfo = (UpdateInfo) msg.obj;
                        send = true;
                        isCheckFm = true;
                        camera.sendIOCtrl(0, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_DEVINFO_REQ, new byte[0]);
                    }

                    break;
                case GET_UPDATE_VERSION_DATA:

                    //dismissLoadingProgress();
                    updateStatus = UPDATA_STATE_NONE;
                    dismissLoadingProgress();
                    showYesNoDialog(R.string.dialog_msg_new_firmware,R.string.prompt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    if (System.currentTimeMillis() - updateTime <= 180000)
                                        return;

                                    //				String dl = updateInfo.url+updateInfo.ver+".exe";
                                    String dl = redirectAddr;
                                    byte[] byt = new byte[128];
                                    byte[] bSvr = dl.getBytes();
                                    //				Arrays.fill(byt, (byte)0);
                                    int len = bSvr.length > 128 ? 128 : bSvr.length;
                                    System.arraycopy(bSvr, 0, byt, 0, len);
                                    HiLog.v("dl:" + dl);
                                    updateTime = System.currentTimeMillis();
                                    send = true;
                                    showLoadingProgress();
                                    camera.sendIOCtrl(0,AVIOCTRLDEFs.IOTYPE_USER_IPCAM_REMOTE_UPGRADE_REQ,AVIOCTRLDEFs.SMsgAVIoctrlRemoteUpgradeReq.parseContent(updateInfo.ver,dl));

                                    break;
                            }
                        }
                    });
                    break;
                case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_REMOTE_UPGRADE_RESP:
                    AVIOCTRLDEFs.SMsgAVIoctrlRemoteUpgradeResp resp = new AVIOCTRLDEFs.SMsgAVIoctrlRemoteUpgradeResp(data);
                    if(resp.result == 0){
                        showAlert(getString(R.string.dialog_msg_new_firmware_update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                back2Activity(MainActivity.class);
                            }
                        });
                    }
                    else if(resp.result == 1){
                        dismissLoadingProgress();
                        showAlert(getString(R.string.dialog_msg_new_firmware_already_latest), getString(R.string.prompt).toString(), true, null);
                        //	dismissLoadingProgress();
                        updateStatus = UPDATA_STATE_NONE;
                    }
                    else if(resp.result == -1){
                        dismissLoadingProgress();
                        showAlert(getString(R.string.dialog_msg_new_firmware_upgrading));
                        back2Activity(MainActivity.class);
                        //	dismissLoadingProgress();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private List<SystemSetting_AoniActivity.UpdateInfo> mListUpdataInfo = new ArrayList<SystemSetting_AoniActivity.UpdateInfo>();

    private class ThreadHttpResqust extends Thread {
        public ThreadHttpResqust() {

        }

        public void run() {
            //GlobalConfig.GetInstance((MyApp) SystemSettingActivity.this.getApplicationContext()).getAppName()
            //gokeupdate.mytenvis.org115.29.190.131
            if (MyConfig.getAppName().equalsIgnoreCase("testapp")) {
                downloadUrl = "http://115.29.190.131/%s/%s/an_update.html?p=%s";
            }
            String paras = "";
            try {
                String p = String.format("uid=%s&fmver=%s", camera.getUid(), SystemSetting_AoniActivity.this.deviceVersion != null ? SystemSetting_AoniActivity.this.deviceVersion : "");
                paras = "a" + TwsTools.getBase64(p) + "=";
            } catch (Exception ex) {

            }

            String url = String.format(downloadUrl, MyConfig.getAppName(),camera.getModelName(), paras);


            //	        String urldown = null;
            //			String ver = null;
            HttpResponse httpResponse = null;
            try {

                HttpGet httpGet = new HttpGet(url);
                httpResponse = new DefaultHttpClient().execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {

                    String result = EntityUtils.toString(httpResponse.getEntity());
                    HiLog.v("result:" + result);
                    try {
                        JSONObject resultJson = new JSONObject(result);
                        JSONArray listArray = resultJson.getJSONArray("list");
                        for (int i = 0; i < listArray.length(); i++) {
                            JSONObject jsonObj = listArray.getJSONObject(i);
                            String u = jsonObj.getString("url");
                            String v = jsonObj.getString("ver");
                            SystemSetting_AoniActivity.UpdateInfo updateInfo = new SystemSetting_AoniActivity.UpdateInfo(u, v);
                            mListUpdataInfo.add(updateInfo);

                            HiLog.v("url:" + u + "     ver:" + v);


                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (mListUpdataInfo.size() > 0) {
                        Message msg = new Message();
                        msg.what = GET_UPDATE_VERSION_NUM;
                        msg.arg1 = 1;
                        //	        			msg.obj = new UpdateInfo(urldown, ver);
                        handler.sendMessage(msg);
                        return;
                    }


                }

                Message msg = new Message();
                msg.what = GET_UPDATE_VERSION_NUM;

                msg.arg1 = 0;
                handler.sendMessage(msg);
            } catch (Exception e) {

            }
            /**        HiCam最后版本           **/


        }
    }

    //	private String host = "58.64.153.34";
    //	private String get = "/V7.1.4.1.11.exe";


    private class ThreadCheckRedirect extends Thread {
        public void run() {


            Socket socket = null;
            try {
                //	        	InetAddress address = new InetAddress();
                //	        	InetAddress addr = InetAddress.getByName("www.baidu.com");

                //	            socket = new Socket(addr,80);

                if (updateInfo == null) {

                }


                System.out.println("updateInfo:" + updateInfo);
                System.out.println("updateInfo.url:" + updateInfo.url);

                String host = null;
                int port = 80;
                socket = new Socket();            //此时Socket对象未绑定本地端口,并且未连接远程服务器
                socket.setReuseAddress(true);


                //        	    Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
                //    	        Matcher matcher = p.matcher(updateInfo.url);
                //    	        if (matcher.find()) {
                //    	        	host = matcher.group();
                //    	            System.out.println("host:"+host);
                //    	        }
                //

                String host_temp = null;
                Pattern p2 = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");
                Matcher matcher2 = p2.matcher(updateInfo.url);
                if (matcher2.find()) {
                    host_temp = matcher2.group();
                    System.out.println("host2:" + host_temp);
                }

                //    	        if(host_temp == null)
                //    	        	return;

                // 如果
                if (host_temp.contains(":") == false) {
                    host = host_temp;
                } else {

                    String[] ipPortArr = host_temp.split(":");
                    host = ipPortArr[0];

                    port = Integer.parseInt(ipPortArr[1]);
                    System.out.println("---00port:      " + ipPortArr[0]);
                    System.out.println("---port:    " + port);
                }


                SocketAddress remoteAddr = new InetSocketAddress(host, port);
                socket.connect(remoteAddr);

                //向服务器端第一次发送字符串
                DataOutputStream doc = new DataOutputStream(socket.getOutputStream());

                //向服务器端第二次发送字符串


                String sendhead = "GET " + updateInfo.url+ " HTTP/1.1\r\n";
                sendhead += "Accept: */*\r\n";
                sendhead += "Accept-Language: zh-cn\r\n";
                sendhead += "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)\r\n";
                sendhead += "Host: " + host + "\r\n";
                sendhead += "Connection: Keep-Alive\r\n";
                sendhead += "\r\n";

                System.out.println("sendhead:" + sendhead);
                doc.write(sendhead.getBytes());

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                while (true) {
                    String str = in.readLine();
                    System.out.println("in.readLine:" + str);

                    if (str.contains("HTTP/")) {
                        //	                	if (str.contains("200 OK")) {
                        //	                		break;
                        //	                	}
                        if (str.contains("302") || str.contains("301")) {
                            System.out.println("------------ 301 302 Found----------------");
                        } else {
                            break;
                        }
                    }

                    if (str.contains("Location:")) {

                        String newhttp = str.substring(9).trim();
                        System.out.println("1------------ newhttp----------------:" + newhttp);

                        redirectAddr = str.substring(9).trim();
                        break;
                    }
                }

                doc.close();
                in.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }

                Message msg = new Message();
                msg.what = GET_UPDATE_VERSION_DATA;
                msg.arg1 = 1;
                handler.sendMessage(msg);

            }

        }
    }

    private void checkUpdate() {

        new SystemSetting_AoniActivity.ThreadHttpResqust().start();

        updateStatus = UPDATA_STATE_CHECKING;
    }

    public String getFirmInfo(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        String result = null;
        try {
            httpResponse = new DefaultHttpClient().execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    result = EntityUtils.toString(httpResponse.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //private static String downloadUrl = "http://192.168.1.240/test/goke_update.html";
    private static String downloadUrl = "http://update.wificam.org/%s/%s/an_update.html?p=%s";//��������ַ
    //	private static String downloadUrl = "http://58.64.153.34/V7.1.4.1.11.exe";//��������ַ

    //	"http://58.64.153.34/V7.1.4.1.11.exe"

    private SystemSetting_AoniActivity.UpdateInfo updateInfo;

    private class UpdateInfo {
        String url;
        String ver;

        public UpdateInfo(String u, String v) {
            url = u;
            ver = v;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.unregisterIOTCListener(this);
        }
        dismissLoadingProgress();
    }

    public void goSetting(View view) {
        Intent intent = new Intent();
        intent.putExtras(this.getIntent());
        if (view.getId() == R.id.ll_system_reboot) {
            //重启
            showYesNoDialog(R.string.dialog_msg_reboot_camera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            showLoadingProgress(getString(R.string.process_rebooting));
                            rebootState = 0;
                            // Yes button clicked
                            camera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_REBOOT_SYSTEM_REQ, new byte[0]);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            });
        } else if (view.getId() == R.id.ll_system_update) {
            updateState = 0;
            //升级固件
            showLoadingProgress(getString(R.string.process_upgrading_check), true, 90000, new TwsProgressDialog.OnTimeOutListener() {
                @Override
                public void onTimeOut(TwsProgressDialog dialog) {
                    updateState = -1;
                    TwsToast.showToast(SystemSetting_AoniActivity.this, getString(R.string.toast_connect_timeout));
                }
            });
            checkUpdate();
        }
    }

}
