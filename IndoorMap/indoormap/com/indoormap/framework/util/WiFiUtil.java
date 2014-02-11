package com.indoormap.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * wifi������
 * 
 * @author lw
 * 
 */
public class WiFiUtil
{
    
    public static String TAG = "WifiUtil";
    
    /** ����WifiManager���� */
    private static WifiManager wifiManager;
    
    private static WifiScanListener wifiListener;
    
    private static Context context;
    
    private static List<ScanResult> apList = new ArrayList<ScanResult>();
    
    public static void init(Context context)
    {
        WiFiUtil.context = context;
        wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    
    public static WifiManager getWifiManager()
    {
        return wifiManager;
    }
    
    /**
     * ��ȡwifi����״̬
     * 
     * @return
     */
    public static boolean isWifiEnabled()
    {
        
        return wifiManager.isWifiEnabled();
        
    }
    
    /**
     * ��ȡWifi����״̬
     * 
     * @return
     */
    public static int getWifiState()
    {
        
        return wifiManager.getWifiState();
        
    }
    
    /**
     * ��ȡWifi������Ϣ
     * 
     * @return
     */
    public static WifiInfo getConnectionInfo()
    {
        return wifiManager.getConnectionInfo();
        
    }
    
    public static int getConnectStatus(int configID)
    {
        int status = wifiManager.getConfiguredNetworks().get(configID).status;
        
        return status;
    }
    
    public static void setWifiMng(WifiManager serviceWifi)
    {
        wifiManager = serviceWifi;
    }
    
    /**
     * ��wifi
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean openWifi()
    {
        
        if (isWifiEnabled())
        {
            return true;
        }
        // LogManager.info(LogConstant.Try_to_open_WIFI, "���Կ���WIFI");
        boolean re = wifiManager.setWifiEnabled(true);
        int num = 0;
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING)
        {
            try
            {
                // Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��
                Thread.sleep(100);
                num++;
                if (num >= 10 * 20)
                {
                 // LogManager.info(LogConstant.REPORT_MSG, "WIFIʧ��");
                    break;
                }
            }
            catch (InterruptedException ie)
            {
            }
        }
     // LogManager.info(LogConstant.WIFI_Open_Success, "WIFI �����ɹ�");
        return true;
    }
    
    /**
     * �ر�wifi
     * 
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void closeWifi()
    {
        closeWifi(false);
    }
    
    /**
     * �ر�wifi
     * 
     * @see [�ࡢ��#��������#��Ա]
     */
    public static void closeWifi(boolean iswait)
    {
        
        // check if WiFi connect
        if (wifiManager.isWifiEnabled() || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING)
        {
         // LogManager.info(LogConstant.WIFI_DISABLED, "���Թر�WIFI");
            wifiManager.setWifiEnabled(false);
            // ����Ҫ�ȴ���ֱ���˳�
            if (!iswait)
            {
                return;
            }
            
            int num = 0;
            while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING)
            {
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
                {
                    break;
                }
                
                try
                {
                    // Ϊ�˱������һֱwhileѭ��������˯��100�����ڼ�⡭��
                    Thread.sleep(100);
                    num++;
                    if (num >= 10 * 5)
                    {
                     // LogManager.info(LogConstant.REPORT_MSG, "�ر�WIFIʧ��");
                        break;
                    }
                }
                catch (InterruptedException ie)
                {
                }
            }
            
         // LogManager.info(LogConstant.WIFI_DISABLED, "�ر�WIFI");
            
        }
        
    }
    
    /**
     * ����ָ����SSID
     * 
     * @param ssid
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean connectWifiBySSID(String ssid)
    {
        
        // LogManager.info(LogConstant.REPORT_MSG, "��ʼ����" +
        // NDCConfigInfo.DefaultSSID);
        
        String defaultssid = "\"" + ssid + "\"";
        
        WifiConfiguration targetConfig = getWifiConfigBySSID(ssid);
        // ����ȵ�û��������Ϣ��������������Ϣ
        if (null == targetConfig)
        {
            /* Create a WifiConfig */
            WifiConfiguration selectedConfig = new WifiConfiguration();
            selectedConfig.SSID = defaultssid;
            selectedConfig.status = WifiConfiguration.Status.ENABLED;
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            int res = wifiManager.addNetwork(selectedConfig);
            wifiManager.saveConfiguration();
            wifiManager.enableNetwork(res, true);
            targetConfig = getWifiConfigBySSID(ssid);
            if (null == targetConfig)
            {
             // LogManager.info(LogConstant.REPORT_MSG, ssid + " has not Configuration");
                return false;
            }
        }
        if (isWifiConnected(ssid))
        {
            return true;
        }
        
        // ����ָ�����ȵ�
        enableNetwork(targetConfig.networkId);
        
        long beginTime = System.currentTimeMillis();
        while (true)
        {
            
            sleep(200);
            
            if (System.currentTimeMillis() - beginTime >= 1000 * 20)
            {
             // LogManager.info(LogConstant.ConnectWifiTimeOut, "�ȵ���볬ʱ");
                return false;
            }
            if (!isWifiEnabled())
            {
                // ���WIFI �رգ����Ƴ�
                return false;
            }
            
            if (isWifiConnected(defaultssid))
            {
                wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "123");
                return true;
            }
            
            // ����ʧ��
            WifiInfo connection = getConnectionInfo();
            if (!ssid.equals(connection.getSSID()))
            {
                enableNetwork(targetConfig.networkId);
            }
            
            if (connection.getSupplicantState() == SupplicantState.DISCONNECTED || connection.getSupplicantState() == SupplicantState.SCANNING)
            {
                
                wifiManager.reconnect();
                
                enableNetwork(targetConfig.networkId);
                
                continue;
                
            }
            
        }
        
        // return false;
        
    }
    
    private static boolean isWifiConnected(String defaultssid)
    {
        defaultssid = defaultssid.replace("\"", "");
        WifiInfo connection = getConnectionInfo();
        // �����ǰwifi ���ӵ���ָ�����ȵ㣬������wifi�ǻ�Ծ״̬������Ϊ����ɹ�
        if (defaultssid.equals(connection.getSSID()) && ConnectivityUtil.isWiFiActive())
        {
         // NdcLog.d(TAG, "cmccWifi isWiFiActive");
            // LogManager.info(LogConstant.REPORT_MSG, "����" + defaultssid +
            // "�ɹ�");
            return true;
        }
        
        return false;
    }
    
    public static boolean enableNetwork(int networkId)
    {
        boolean flag = wifiManager.enableNetwork(networkId, true);
        
        return flag;
    }
    
    /**
     * ����Ϊ��λ <������ϸ����>
     * 
     * @param mm
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void sleep(int mm)
    {
        // ��΢��ʱ�����ӽ������
        try
        {
            Thread.sleep(mm);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * ɨ����Χ���õ�AP
     * 
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static List<ScanResult> getAPListBySsid(String ssid)
    {
        startMonitorWifiScan();
        List<ScanResult> result = new ArrayList<ScanResult>();
        
        try
        {
            openWifi();
            
            result = startScan(ssid);
        }
        finally
        {
            stopMonitorWifiScan();
        }
        
        return result;
    }
    
    /**
     * ����SSID ɸѡ
     * 
     * @param ssid
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static List<ScanResult> getAllAPResult()
    {        
        StringBuffer sb = new StringBuffer();
        
        List<ScanResult> result = new ArrayList<ScanResult>();
        apList = wifiManager.getScanResults();
        if (apList == null)
        {
            // LogManager.info(LogConstant.REPORT_MSG, "level:" + sb.toString());
            return result;
        }
        
        // ����ɨ����
        for (ScanResult scanInfo : apList)
        {            
            sb.append(scanInfo.level).append(",");            
            result.add(scanInfo);
            
        }
        
        // LogManager.info(LogConstant.REPORT_MSG, "l=" + sb.toString());
        return result;
    }
    
    /**
     * ����SSID ɸѡ
     * 
     * @param ssid
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static List<ScanResult> filterAp(String ssid, boolean ischeckLevel)
    {
        
        List<ScanResult> result = new ArrayList<ScanResult>();
        apList = wifiManager.getScanResults();
        if (apList == null)
        {
            return result;
        }
        
        // ����ɨ����
        for (ScanResult scanInfo : apList)
        {
            if (!ssid.equals(scanInfo.SSID))
            {
                continue;
            }
            
            result.add(scanInfo);
        }
        
        return result;
    }
    
    /**
     * ɨ��
     * 
     * @see [�ࡢ��#��������#��Ա]
     */
    public static List<ScanResult> startScan(String ssid)
    {
        wifiListener.listenerSate = -1;
        
        wifiManager.startScan();
        long begintime = System.currentTimeMillis();
        Log.d(TAG, "startScan");
        
        List<ScanResult> result = new ArrayList<ScanResult>();
        int retryNumber = 0;
        
        // �ȴ�ɨ�����
        while (true)
        {
            if (wifiListener.listenerSate == 1 || System.currentTimeMillis() - begintime >= 3500)
            {               
                result = getAllAPResult();
                
                Log.d(TAG, "listenerSate=" + wifiListener.listenerSate + " size=" + result.size() + ":" + retryNumber);
                
                if(result.size() > 0)
                {
                    if(ssid != null)
                    {
                        for (int i = 0; i < result.size(); i++)
                        {
                            ScanResult scanResult = result.get(i);
                            if(scanResult.SSID.equals(ssid))
                            {
                                return result;
                            }
                        }
                    }else{
                        break;
                    }
                }else {
                    
                    // ����һ��
                    if(retryNumber ++ > 1)
                    {
                        break;
                    }
                    
                    sleep(1000);
                    continue;
                }
            }
            
            if (System.currentTimeMillis() - begintime >= 10 * 1000)
            {
                break;
            }
            
            sleep(100);
        }

        Log.d(TAG, "endScan");
                
        return result;
    }
    
    /**
     * ����SSID ��ȡ��������
     * 
     * @param ssid
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static WifiConfiguration getWifiConfigBySSID(String ssid)
    {
        WifiConfiguration targetConfig = null;
        
        List<WifiConfiguration> configs = null;
        
        int loopNum = 0;
        while (loopNum++ < 15)
        {
            configs = wifiManager.getConfiguredNetworks();
            if (configs.size() == 0)
            {
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            }
            else
            {
                break;
            }
        }
        
        for (WifiConfiguration i : configs)
        {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\""))
            {
                targetConfig = i;
                // i.status = WifiConfiguration.Status.DISABLED;
                break;
            }
        }
        return targetConfig;
    }
    
    public static boolean saveEapConfig(String ssid, String bssid, String userName, String passString)
    {
        
     // NdcLog.d(TAG, "Call saveEapConfig");
        
        final String ENTERPRISE_EAP = "PEAP";
        
        /* Create a WifiConfig */
        WifiConfiguration selectedConfig = new WifiConfiguration();
        
        /* AP Name */
        selectedConfig.SSID = "\"" + ssid + "\"";
        selectedConfig.BSSID = bssid;
        /* Key Mgmnt */
        selectedConfig.allowedKeyManagement.clear();
        selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        
        /* Group Ciphers */
        selectedConfig.allowedGroupCiphers.clear();
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        
        /* Pairwise ciphers */
        selectedConfig.allowedPairwiseCiphers.clear();
        selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        
        /* Protocols */
        selectedConfig.allowedProtocols.clear();
        
        selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        
        // selectedConfig.priority = NDCConfigInfo.maxWIFIPriority;
        
        Class[] wcClasses = WifiConfiguration.class.getClasses();
        
        Class wcEnterpriseField = null;
        
        for (Class wcClass : wcClasses)
        {
            
            if (wcClass.getName().indexOf("EnterpriseField") != -1)
            {
                wcEnterpriseField = wcClass;
                break;
            }
        }
        
        Field wcefEap = null, wcefIdentity = null, wcefPassword = null;
        Field[] wcefFields = WifiConfiguration.class.getFields();
        // Dispatching Field vars
        for (Field wcefField : wcefFields)
        {
            
            if (wcefField.getName().equals("eap"))
            {
                wcefEap = wcefField;
            }
            
            // EAP-PEAP ���õ��û�����Ҳ�����ֻ�����
            else if (wcefField.getName().equals("identity"))
            {
                wcefIdentity = wcefField;
            }
            
            // ��֤����
            else if (wcefField.getName().equals("password"))
            {
                wcefPassword = wcefField;
            }
            
        }
        
        Method wcefSetValue = null;
        
        if (wcEnterpriseField != null)
        {
            for (Method m : wcEnterpriseField.getMethods())
            {
                System.out.println("methodName--->" + m.getName());
                if (m.getName().trim().equals("setValue"))
                {
                    wcefSetValue = m;
                    break;
                }
                
            }
        }
        try
        {
            /* EAP Method */
            if (wcEnterpriseField != null)
            {
                wcefSetValue.invoke(wcefEap.get(selectedConfig), ENTERPRISE_EAP);
                
                wcefSetValue.invoke(wcefIdentity.get(selectedConfig), userName);
                
                wcefSetValue.invoke(wcefPassword.get(selectedConfig), passString);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        boolean result = addNetwork(selectedConfig);
        return result;
        
    }
    
    // ���һ�����粢����
    public static boolean addNetwork(WifiConfiguration selectedConfig)
    {
        
        // boolean res1 = wifiManager.setWifiEnabled(true);
        int res = wifiManager.addNetwork(selectedConfig);
     // NdcLog.d("WifiPreference", "add Network returned " + res);
        boolean b = wifiManager.enableNetwork(selectedConfig.networkId, false);
     // NdcLog.d("WifiPreference", "enableNetwork returned " + b);
        boolean c = wifiManager.saveConfiguration();
     // NdcLog.d("WifiPreference", "Save configuration returned " + c);
        boolean d = wifiManager.enableNetwork(res, true);
     // NdcLog.d("WifiPreference", "enableNetwork returned " + d);
        return c;
    }
    
    // ��ʼ����wifiɨ����Ϣ
    public static void startMonitorWifiScan()
    {
        if (null == wifiListener)
        {
            wifiListener = new WifiScanListener();
        }
        
        // ��ʶ��ʼ����
        wifiListener.listenerSate = 0;
        
        context.registerReceiver(wifiListener, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    
    /*
    // ��ʼ����wifiɨ����Ϣ
    public static void startMonitorSUPPLICANT_STATE_CHANGED()
    {
        if (null == wifiListener)
        {
            wifiListener = new WifiScanListener();
        }
        
        // ��ʶ��ʼ����
        wifiListener.listenerSate = 0;
        
        IntentFilter filter1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter1.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter1.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        SystemService.getInstance().registerReceiver(wifiListener, filter1);
        
    }
    
    // ֹͣ����wifiɨ����Ϣ
    public static void stopMonitorSUPPLICANT_STATE_CHANGED()
    {
        context.unregisterReceiver(wifiListener);
    }
    */
    
    // ֹͣ����wifiɨ����Ϣ
    public static void stopMonitorWifiScan()
    {
        context.unregisterReceiver(wifiListener);
    }
    
    static class WifiScanListener extends BroadcastReceiver
    {
        public int listenerSate = -1;
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                
                Log.d(TAG, "Finish Wifi Scan");
                
                // ��ȡɨ��Ľ��
                List<ScanResult> wifiList = wifiManager.getScanResults();
                
                apList = wifiList;
                
                // ��ʶ�Ѿ�ɨ�赽���
                listenerSate = 1;
            }
            
            if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION))
            {
                SupplicantState s = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                
                // LogManager.info(LogConstant.REPORT_MSG, "wifiState2=" +
                // getSupplicantStateText(s) + "|" +
                // WifiInfo.getDetailedStateOf(s));
                
            }
            
        }
        
    }
    
    /**
     * ����ָ����SSID
     * 
     * @param ssid
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    public static boolean connectWifiBySSID1(String ssid)
    {
        
        // LogManager.info(LogConstant.REPORT_MSG, "��ʼ����" +
        // NDCConfigInfo.DefaultSSID);
        
        String defaultssid = "\"" + ssid + "\"";
        
        WifiConfiguration targetConfig = getWifiConfigBySSID(ssid);
        
        // ����ȵ�û��������Ϣ��������������Ϣ
        // �����
        targetConfig = checkConfig(ssid, defaultssid, targetConfig);
        
       //wifiManager.removeNetwork(targetConfig.networkId);
       
        // ����ָ�����ȵ�
        // enableNetwork(targetConfig.networkId);
        
        //sleep(2000);
        
        long beginTime = System.currentTimeMillis();
        while (true)
        {
            
            sleep(200);
            
            if (null == targetConfig)
            {
                targetConfig = checkConfig(ssid, defaultssid, targetConfig);
                
                continue;
            }
            
            if (System.currentTimeMillis() - beginTime >= 1000 * 30)
            {
                return false;
            }
            if (!isWifiEnabled())
            {
                // ���WIFI �رգ����Ƴ�
                return false;
            }
            
            if (isWifiConnected(defaultssid))
            {
                wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "123");
                return true;
            }
            
            // ����ʧ��
            WifiInfo connection = getConnectionInfo();
            if (!ssid.equals(connection.getSSID()))
            {
                enableNetwork(targetConfig.networkId);
            }
            
            if (connection.getSupplicantState() == SupplicantState.DISCONNECTED || connection.getSupplicantState() == SupplicantState.SCANNING)
            {
                
                wifiManager.reconnect();
                
                enableNetwork(targetConfig.networkId);
                
                continue;
                
            }
            
        }
        
        // return false;
        
    }
    
    private static WifiConfiguration checkConfig(String ssid, String defaultssid, WifiConfiguration targetConfig)
    {
        if (null == targetConfig)
        {
            /* Create a WifiConfig */
            WifiConfiguration selectedConfig = new WifiConfiguration();
            selectedConfig.SSID = defaultssid;
            selectedConfig.status = WifiConfiguration.Status.ENABLED;
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            int res = wifiManager.addNetwork(selectedConfig);
            wifiManager.saveConfiguration();
            wifiManager.enableNetwork(res, true);
            targetConfig = getWifiConfigBySSID(ssid);
            if (null == targetConfig)
            {
            }
        }
        return targetConfig;
    }
    
    /**
     * ��ȡ���ʻ�<һ�仰���ܼ���> <������ϸ����>
     * 
     * @param id
     * @return
     * @see [�ࡢ��#��������#��Ա]
     */
    /*
    private static String getLocalString(int id)
    {
        
        return SystemService.getInstance().getString(id);
        
    }
    */
}
