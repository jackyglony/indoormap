package com.indoormap.framework.exception;

/**
 * 
 * ϵͳ�쳣��
 * 
 * @author zhuweiliang
 * @version [�汾��, 2012-8-28]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class ServiceException extends Exception
{
    public enum ERRORCODE
    {
        NETWROKERROE
    }
    
    /**
     * ע������
     */
    private static final long serialVersionUID = 1L;
    
    private ERRORCODE erroeCode;
    
    // ����ID������������Ӧ�ı�����Ϣ��
    private int errorID;
    
    private String exceptionMessage = "";
    
    /**
     * ������������װ
     * 
     * @param ex �������
     */
    public ServiceException(Exception ex)
    {
        super(ex);
        this.exceptionMessage = ex.getMessage();
        
    }
    
    /**
     * ��װ����ŵ����⴦��
     * 
     * @param id ��Ϣid
     * @param ex ���������Ϣ
     */
    public ServiceException(int id, Exception ex)
    {
        super(ex);
        this.exceptionMessage = ex.getMessage();
        this.errorID = id;
    }
    
    /**
     * ��װ����ŵ����⴦��
     * 
     * @param id ��Ϣid
     * @param ex ���������Ϣ
     */
    public ServiceException(ERRORCODE id, Exception ex)
    {
        super(ex);
        this.exceptionMessage = ex.getMessage();
        this.erroeCode = id;
    }
    
    /**
     * ��װ����ŵ����⴦��
     * 
     * @param id ��Ϣid
     */
    public ServiceException(int id)
    {
        this.errorID = id;
    }
    
    /**
     * ��װ����ŵ����⴦�� ,���������
     * 
     * @param id ��Ϣid
     * @param exceptionMessage ������Ϣ
     */
    public ServiceException(ERRORCODE id, String exceptionMessage)
    {
        this.erroeCode = id;
        this.exceptionMessage = exceptionMessage;
    }
    
    /**
     * ��װ����ŵ����⴦�� ,���������
     * 
     * @param id ��Ϣid
     * @param exceptionMessage ������Ϣ
     */
    public ServiceException(int id, String exceptionMessage)
    {
        this.errorID = id;
        this.exceptionMessage = exceptionMessage;
    }
    
    @Override
    public String getMessage()
    {
        return exceptionMessage;
    }
    
    /**
     * ��ȡ����ID
     * 
     * @return String ����ID
     */
    public int getErrorID()
    {
        return this.errorID;
    }
    
  
    
    public ERRORCODE getErroeCode()
    {
        return erroeCode;
    }

    public String getExceptionMessage()
    {
        return exceptionMessage;
    }
    
    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }
    
    /**
     * ��дtoString
     * 
     * @return Classname + message
     */
    public String toString()
    {
        // TODO Auto-generated method stub
        return super.toString() + this.getExceptionMessage();
    }
    
}
