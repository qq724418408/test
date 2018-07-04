package com.boc.jx.baseUtil.net;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hwt on 4/8/15.
 */
public interface UiObject {

    String TAG = UUID.randomUUID().toString();
    List<Action> actionList = new ArrayList<Action>();

    void requestSuccess(RetCode retCode, Object response);

    void requestFalied(RetCode retCode);

    void addAction(Action action);

    void resetUiOject();

}
