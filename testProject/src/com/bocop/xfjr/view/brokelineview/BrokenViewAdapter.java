package com.bocop.xfjr.view.brokelineview;

import java.util.List;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

public abstract class BrokenViewAdapter implements BrokenAdapter {

    public BrokenViewAdapter() {
        
    }

    @Override
    public int getSize() {
    	return 12;
    }

    @Override
    public void notifyChanged() {

    }
}
