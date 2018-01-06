package com.tws.commonlib.bean;

import com.tws.commonlib.App;
import com.tws.commonlib.MainActivity;
import com.tws.commonlib.view.AppUpdateView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TwsDataValue {
    public static final int CAMERA_MAX_LIMITS = 64;

    public final static String EXTRA_KEY_UID = "camera_uid";
    public final static String ACTION_CAMERA_INIT_END = "camera_init_end";
    public final static String ACTION_CAMERA_REFRESH = "camera_refresh";
    public final static String ACTION_CAMERA_REFRESH_ONE_ITEM = "camera_refresh_one_item";
    public final static int HANDLE_MESSAGE_SESSION_STATE = 0x90000001;
    public final static int HANDLE_MESSAGE_CHANNEL_STATE = 0x90000002;
    public final static int HANDLE_MESSAGE_STS_CHANGE_STREAMINFO = 0x90000003;
    public final static int HANDLE_MESSAGE_IO_RESP = 0x90000004;
    public final static String DEFAULT_PASSWORD = "admin";
    public final static String SNAP_DIR = "snapshot";
    public final static String RECORDING_DIR = "videorecording";
    public final static String Remte_RECORDING_DIR = "remote";
    public final static String NTP_SERVER = "poolll.ntp.org";
    public static String[][] TimeZoneField = new String[][]{{"Etc/GMT-12", "GMT-12:00", "0"}, {"Pacific/Apia", "GMT-11:00", "1"}, {"Pacific/Honolulu", "GMT-10:00", "0"}, {"America/Anchorage", "GMT-9:00", "1"}, {"America/Los_Angeles", "GMT-8:00", "1"}, {"America/Denver", "GMT-7:00", "1"}, {"America/Tegucigalpa", "GMT-7:00", "1"}, {"America/Phoenix", "GMT-7:00", "0"}, {"America/Saskatchewan", "GMT-6:00", "1"}, {"America/Mexico_City", "GMT-6:00", "1"}, {"America/Chicago", "GMT-6:00", "0"}, {"America/Costa_Rica", "GMT-6:00", "0"}, {"America/Indianapolis", "GMT-5:00", "1"}, {"America/New_York", "GMT-5:00", "1"}, {"America/Bogota", "GMT-5:00", "0"}, {"America/Caracas", "GMT-4:30", "0"}, {"America/Santiago", "GMT-4:00", "1"}, {"America/Montreal", "GMT-4:00", "1"}, {"America/St_Johns", "GMT-3:30", "1"}, {"America/Thule", "GMT-3:00", "1"}, {"America/Buenos_Aires", "GMT-3:00", "0"}, {"America/Sao_Paulo", "GMT-3:00", "1"}, {"Atlantic/South_Georgia", "GMT-2:00", "1"}, {"Atlantic/Cape_Verde", "GMT-1:00", "0"}, {"Atlantic/Azores", "GMT-1:00", "1"}, {"Europe/Dublin", "GMT+0:00", "1"}, {"Africa/Casablanca", "GMT+0:00", "0"}, {"Europe/Amsterdam", "GMT+1:00", "1"}, {"Europe/Belgrade", "GMT+1:00", "1"}, {"Europe/Brussels", "GMT+1:00", "1"}, {"Europe/Warsaw", "GMT+1:00", "1"}, {"Africa/Lagos", "GMT+1:00", "0"}, {"Europe/Athens", "GMT+2:00", "1"}, {"Europe/Bucharest", "GMT+2:00", "1"}, {"Africa/Cairo", "GMT+2:00", "1"}, {"Africa/Harare", "GMT+2:00", "0"}, {"Europe/Helsinki", "GMT+2:00", "1"}, {"Asia/Jerusalem", "GMT+2:00", "1"}, {"Asia/Baghdad", "GMT+3:00", "0"}, {"Asia/Kuwait", "GMT+3:00", "0"}, {"Europe/Moscow", "GMT+3:00", "1"}, {"Africa/Nairobi", "GMT+3:00", "0"}, {"Asia/Tehran", "GMT+3:30", "1"}, {"Asia/Dubai", "GMT+4:00", "0"}, {"Asia/Baku", "GMT+4:00", "1"}, {"Asia/Kabul", "GMT+4:30", "0"}, {"Asia/Yekaterinburg", "GMT+5:00", "1"}, {"Asia/Karachi", "GMT+5:00", "0"}, {"Asia/Calcutta", "GMT+5:30", "0"}, {"Asia/Katmandu", "GMT+5:45", "0"}, {"Asia/Novosibirsk", "GMT+6:00", "1"}, {"Asia/Dhaka", "GMT+6:00", "0"}, {"Asia/Astana", "GMT+6:00", "0"}, {"Asia/Rangoon", "GMT+6:30", "0"}, {"Asia/Bangkok", "GMT+7:00", "0"}, {"Asia/Krasnoyarsk", "GMT+7:00", "1"}, {"Asia/Hong_Kong", "GMT+8:00", "0"}, {"Asia/Irkutsk", "GMT+8:00", "1"}, {"Asia/Kuala_Lumpur", "GMT+8:00", "0"}, {"Australia/Perth", "GMT+8:00", "0"}, {"Asia/Taipei", "GMT+8:00", "0"}, {"Asia/Tokyo", "GMT+9:00", "0"}, {"Asia/Seoul", "GMT+9:00", "0"}, {"Asia/Yakutsk", "GMT+9:00", "1"}, {"Australia/Adelaide", "GMT+9:30", "1"}, {"Australia/Brisbane", "GMT+10:00", "0"}, {"Australia/Sydney", "GMT+10:00", "1"}, {"Pacific/Guam", "GMT+10:00", "0"}, {"Australia/Hobart", "GMT+10:00", "1"}, {"Asia/Vladivostok", "GMT+10:00", "1"}, {"Asia/Magadan", "GMT+11:00", "1"}, {"Pacific/Auckland", "GMT+12:00", "1"}, {"Pacific/Fiji", "GMT+12:00", "1"}, {"Pacific/Tongatapu", "GMT+13:00", "0"}};
    public static List<MyCamera> cameraList = null;
    public static String XGToken = "";
    public static String UMToken = "";
    public static int[] SensValues = new int[]{90, 70, 50, 30, 0};

    public synchronized static List<MyCamera> cameraList() {
        if (cameraList == null) {
            cameraList = new ArrayList<MyCamera>();
            AppUpdateView.initCameraList(App.getContext());
            // MainActivity.initCamera(GlobalConfig.GetInstance(MyApp.GetApp()).getAppContext());
        }
        return cameraList;
    }

    public synchronized static List<MyCamera> cameraList(boolean strong) {
        if (cameraList == null || strong) {
            cameraList = new ArrayList<MyCamera>();
            AppUpdateView.initCameraList(App.getContext());
            // MainActivity.initCamera(GlobalConfig.GetInstance(MyApp.GetApp()).getAppContext());
        }
        return cameraList;
    }
}