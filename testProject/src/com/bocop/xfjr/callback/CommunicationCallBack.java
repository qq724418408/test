package com.bocop.xfjr.callback;

import java.util.Map;

public interface CommunicationCallBack {
	boolean backCallBack();
	void nextCallBack(Map<String,Object> map, String fxModel);
}
