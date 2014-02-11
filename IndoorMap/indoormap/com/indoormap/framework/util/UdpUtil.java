package com.indoormap.framework.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import android.util.Log;

public class UdpUtil
{
    
    // ��ʱʱ���趨
    private static int udpRecvWaitTime = 5000;
	private static final String CODE_MODE = "UTF-8";
    
    // ����UDP����
    public static String send(String ip, int port, Map<String, String> postParam)
    {
        if (null == postParam)
        {
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        for (String key : postParam.keySet())
        {
            sb.append(key).append("=").append(postParam.get(key)).append("&");
        }
        if (sb.length() > 1)
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return send(ip, port, sb.toString(), false);
    }
    
    // ����UDP����
    public static String send(String ip, int port, String content)
    {
        return send(ip, port, content, false);
    }
    
    // ����UDP����
    private static String send(String ip, int port, String content, boolean isrepeat)
    {
        
        DatagramSocket datagramSocket = null;
        String resp = null;
        try
        {
            datagramSocket = new DatagramSocket();
            
            // ��������
            sendData(ip, port, content, datagramSocket);
           // NdcLog.d(TAG, "SEND DATA:" + port);
            
            // ��ȡ��Ӧ
            resp = getResponse(datagramSocket);
           // NdcLog.d(TAG, "RESP DATA:" + port);
            
        }
        catch (IOException e)
        {
            
           // NdcLog.d(TAG, "IOException:" + e.getMessage());
            
            if (isrepeat)
            {
                resp = null;
            }
            else
            {
                // NdcLog.d(TAG, "START REPEAT:" + port);
                // ����
                return send(ip, port, content, true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != datagramSocket)
            {
                datagramSocket.close();
            }
            
        }
        
        return resp;
    }
    
    /**
     * ��ȡ��Ӧ���
     * 
     * @param ds
     * @return
     * @throws SocketException
     * @throws IOException
     * @see [�ࡢ��#��������#��Ա]
     */
    private static String getResponse(DatagramSocket ds)
        throws SocketException, IOException
    {
        byte[] recevieData = new byte[1024*64];
        DatagramPacket recvPacket = new DatagramPacket(recevieData, recevieData.length);
        
        // �趨��ʱʱ��
        ds.setSoTimeout(4000);
        
        // ��������
        ds.receive(recvPacket);
        
        ByteBuffer buffer = ByteBuffer.allocate(recvPacket.getLength());
        
        buffer.put(recvPacket.getData(), 0, recvPacket.getLength());
        
        // ����
        // byte data[] = DecodeComm.getObjectByte(buffer.array());
        // String resp = HexUtil.bytestoString(data);
        
        // String resp = HexUtil.bytestoString(buffer.array());
        
        // String resp = buffer.toString();

        // String resp = HexUtil.EncodeUtf8ByteToString(buffer.array());
		String resp = new String(buffer.array(), CODE_MODE);
        
        return resp;
    }
    
    /**
     * ������Ϣ
     * 
     * @param ip ���͵�IP
     * @param port �˿�
     * @param content ����
     * @param ds
     * @throws UnknownHostException
     * @throws IOException
     * @see [�ࡢ��#��������#��Ա]
     */
    private static void sendData(String ip, int port, String content, DatagramSocket ds)
        throws UnknownHostException, IOException
    {
        InetAddress serverAddr = InetAddress.getByName(ip);
        
        Log.d("TAG", content);
		byte data[] = content.getBytes(CODE_MODE);
        
        // byte sendMesag[] = EncodeComm.packagesStream(data);
        byte sendMesag[] = data;
        
        DatagramPacket dp = new DatagramPacket(sendMesag, sendMesag.length, serverAddr, port);
        ds.setSoTimeout(udpRecvWaitTime);
        ds.send(dp);
    }
    
}
