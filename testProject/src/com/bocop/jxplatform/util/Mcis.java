package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
public class Mcis {
	String mstrCsp;
	String tranCode;
	// 报文类型 1
	String msg_type;
	public static int lenmsg_type = 1;
	// 交易同异部标志 1
	String tran_async;
	public static int lentran_async = 1;
	// 交易码 10
	String ext_trancode;
	public static int lenext_trancode = 10;
	// 路由类型 2
	String route_type;
	public static int lenroute_type = 2;

	// 路由字符串 20 左对齐 20位
	String route_code;
	public static int lenroute_code = 20;
	// 系统标示 2 中银开放平台标示
	String channel_id;
	public static int lenchannel_id = 2;
	// 交易日期 8
	String tran_date;
	public static int lentran_date = 8;
	// 交易时间 6
	String tran_time;
	public static int lentran_time = 6;

	// 前端流水号 16
	String traceno;
	public static int lentraceno = 16;
	// 原交易日期 8
	String old_trandate;
	public static int lenold_trandate = 8;
	// 原交易时间 6
	String old_trantime;
	public static int lenold_trantime = 6;
	// 原前端流水号 16
	String old_traceno;
	public static int lenold_traceno = 16;

	// 后台返回流水号 8
	String ap_traeno;
	public static int lenap_traeno = 8;
	// 后台返回码 7
	String ret_code;
	public static int lenret_code = 7;
	// 柜员 7
	String teller;
	public static int lenteller = 7;
	// 备用域 13
	String filler;
	public static int lenfiller = 13;
	// 报文体长度 9(05)
	String data_len;
	public static int lendata_len = 5;
	// 报文体
	String ret_data;

	String code;

	public Mcis(String strCsp,String tranCode) {
		this.mstrCsp = strCsp;
		this.tranCode = tranCode;
	}
	
	public Mcis(String strCsp) {
		this.mstrCsp = strCsp;
	}
	public Mcis() {
	}

	public byte[] getMcis() throws UnsupportedEncodingException {
		msg_type = "0";
		tran_async = "0";
		ext_trancode = "E109150001";
		route_type = "04";
		route_code = String.format("%1$-20s", "00015");
		channel_id = "38";
		// 获取当前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		tran_date = format.format(new Date(System.currentTimeMillis()));

		SimpleDateFormat format_h = new SimpleDateFormat("HHmmss");
		tran_time = format_h.format(new Date(System.currentTimeMillis()));
		// 预留空格16
		traceno = String.format("%-16s", "");
		// 预留空格8
		old_trandate = String.format("%-8s", "");
		// 预留空格6
		old_trantime = String.format("%-6s", "");
		// 预留空格16
		old_traceno = String.format("%-16s", "");
		// 预留空格8
		ap_traeno = String.format("%-8s", "");
		// 预留空格7
		ret_code = String.format("%-7s", "");
		// 预留空格7
		teller = String.format("%-7s", "");
		// 预留空格13
		filler = String.format("%-13s", "");

		int cspLen = ("EB4024" + mstrCsp).length();
		String strMcis = URLDecoder.decode(tranCode + mstrCsp, "UTF-8");
		data_len = String.format("%1$05d", cspLen);
		StringBuilder sb = new StringBuilder();
		sb.append(msg_type);
		sb.append(tran_async);
		sb.append(ext_trancode);
		sb.append(route_type);
		sb.append(route_code);
		sb.append(channel_id);
		sb.append(tran_date);
		sb.append(tran_time);
		sb.append(traceno);
		sb.append(old_trandate);
		sb.append(old_trantime);
		sb.append(old_traceno);
		sb.append(ap_traeno);
		sb.append(ret_code);
		sb.append(teller);
		sb.append(filler);
		sb.append(data_len);
//		return URLDecoder.decode(sb.toString(), "GBK") + strMcis;
		return (sb.toString() + strMcis).getBytes("GBK");
	}

	//获取返回包
    public String recData(byte[] data) throws UnsupportedEncodingException
    {
    	String recdata = new String(data,"GBK");
    	// Log.i(recdata);
        int index = 0;
        msg_type = recdata.substring(index, index + lenmsg_type);
        index = index + lenmsg_type;
        
        Log.i("tag",index + "msg_type:" + msg_type);

        tran_async = recdata.substring(index, index + lentran_async);
        index = index + lentran_async;
        
        Log.i("tag",index + "tran_async:" + tran_async);

        ext_trancode = recdata.substring(index, index + lenext_trancode);
        index = index + lenext_trancode;
        
        Log.i("tag",index + "ext_trancode:" + ext_trancode);

        route_type = recdata.substring(index,index +  lenroute_type);
        index = index + lenroute_type;
        
        Log.i("tag",index + "route_type:" + route_type);

        route_code = recdata.substring(index, index + lenroute_code);
        index = index + lenroute_code;
        
        Log.i("tag",index + "route_code:" + route_code);

        channel_id = recdata.substring(index,index +  lenchannel_id);
        index = index + lenchannel_id;
        
        Log.i("tag",index + "channel_id:" + channel_id);

        tran_date = recdata.substring(index,index +  lentran_date);
        index = index + lentran_date;
        
        Log.i("tag",index + "tran_date:" + tran_date);

        tran_time = recdata.substring(index,index +  lentran_time);
        index = index + lentran_time;
        
        Log.i("tag",index + "tran_time:" + tran_time);
        
        traceno = recdata.substring(index,index + index +  lentraceno);
        index = index + lentraceno;
        
        Log.i("tag",index + "traceno:" + traceno);
        
        old_trandate = recdata.substring(index, index + lenold_trandate);
        index = index + lenold_trandate;
        
        Log.i("tag",index + "old_trandate:" + old_trandate);
        
        old_trantime = recdata.substring(index, index + lenold_trantime);
        index = index + lenold_trantime;
        
        Log.i("tag",index + "old_trantime:" + old_trantime);
        
        old_traceno = recdata.substring(index, index + lenold_traceno);
        index = index + lenold_traceno;
        
        Log.i("tag",index + "old_traceno:" + old_traceno);
        
        ap_traeno = recdata.substring(index, index + lenap_traeno);
        index = index + lenap_traeno;
        
        Log.i("tag",index + "ap_traeno:" + ap_traeno);
        
        ret_code = recdata.substring(index,index +  lenret_code);
        index = index + lenret_code;
        
        Log.i("tag",index + "ret_code:" + ret_code);
        
        teller = recdata.substring(index, index + lenteller);
        index = index + lenteller;
        
        Log.i("tag",index + "teller:" + teller);
        
        filler = recdata.substring(index,index +  lenfiller);
        index = index + lenfiller;
        
        Log.i("tag",index + "filler:" + filler);
        
        data_len = recdata.substring(index,index +  lendata_len);
        index = index + lendata_len;
        
        Log.i("tag",index + "data_len:" + data_len);
        
        ret_data =  recdata.substring(index);
        //ret_data = URLEncoder.encode(ret_data,"UTF-8");
        //ret_data = new String(ret_data.getBytes("UTF-8"),"GBK");
        //ret_data = new String(recdata.substring(index).getBytes(),"UTF-8");
        //ret_data = recdata.substring(index,ret_data.length());
        //ret_data = recdata.substring(index,Integer.parseInt(data_len));
        
        index = index + lendata_len;
        
        Log.i("tag",index + "ret_data:" + ret_data);
        
        return ret_data;
    }
}
