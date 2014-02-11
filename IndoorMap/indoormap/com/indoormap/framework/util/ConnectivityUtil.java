package com.indoormap.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

/**
 * 
 * �����ж� WIFI EDGE �Ƿ����
 * 
 * @author zhuweiliang
 * @version [�汾��, 2012-10-21]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class ConnectivityUtil
{
    
    private static ConnectivityManager connectivity;
    
    public static void init(Context context)
    {
        connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    /**
     * �жϵ�ǰwifi�Ƿ�����״̬
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isWiFiActive()
    {
        
        NetworkInfo wifiNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
        if (wifiNetInfo != null)
        {
            if (wifiNetInfo.isConnected())
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * �жϵ�ǰwifi�Ƿ�����״̬
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean isEdgeActive()
    {
        
        NetworkInfo wifiNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        
        if (wifiNetInfo != null)
        {
            if (wifiNetInfo.isConnected())
            {
                Log.d("TAG", "isEdgeActive TRUE");
                return true;
            }
        }
        
        return false;
    }
    
    // �ж�wifi ����״̬
    public static String getWifiConnectState()
    {
        
        NetworkInfo wifiNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
        if (null != wifiNetInfo)
        {
            State wifi = wifiNetInfo.getState();
            String state = wifi.toString(); // ��ʾwifi����״̬
            
            return state;
        }
        
        return null;
    }
    
}
