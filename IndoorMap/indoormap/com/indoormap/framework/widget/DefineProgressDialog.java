package com.indoormap.framework.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.indoormap.R;

/**
 * 
 * �Զ��� ������
 * 
 * @author zhuweiliang
 * @version [�汾��, 2013-4-23]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class DefineProgressDialog extends ProgressDialog
{
    private String message;
    
    private TextView define_progress_msg;
    
    private TextView hideText;
    
    private OnKeyListener onKeyListener;
    
    public DefineProgressDialog(Context context)
    {
        super(context);
        message = "��������...";// TODO Auto-generated constructor stub
    }
    
    public DefineProgressDialog(Context context, String message)
    {
        super(context);
        this.message = message;
    }
    
    public DefineProgressDialog(Context context, String message, OnKeyListener onKeyListener)
    {
        super(context);
        this.message = message;
        this.onKeyListener = onKeyListener;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.define_progress_msg);
        define_progress_msg = (TextView)findViewById(R.id.define_progress_msg);
        define_progress_msg.setText(message);
        hideText = (TextView)findViewById(R.id.hide_text);
        if (onKeyListener == null)
        {
            this.setOnKeyListener(new OnKeyListener()
            {
                
                @Override
                public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2)
                {
                    if (arg1 == KeyEvent.KEYCODE_SEARCH || arg1 == KeyEvent.KEYCODE_BACK)
                    {
                        return true;
                    }
                    else
                    {
                        return false; // Ĭ�Ϸ��� false
                    }
                }
                
            });
        }
        else
        {
            
            this.setOnKeyListener(onKeyListener);
        }
    }
    
    public void updateMsg(String msg)
    {
        if (null != define_progress_msg)
        {
            
            define_progress_msg.setText(msg);
        }
        
    }
    
    public void enableHideText()
    {
        
        hideText.setVisibility(View.VISIBLE);
        
    }
    
    public void updateHideMsg(String msg)
    {
        
        hideText.setText(msg);
        
    }
    
    public String getHideMsg()
    {
        
        return hideText.getText().toString();
        
    }
    
    @Override
    public void show()
    {
        // TODO Auto-generated method stub
        super.show();
        this.setCancelable(false);
    }
}
