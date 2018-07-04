package com.bocop.xfjr.view.brokelineview;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

public interface ShowRule {
    int[] landsTitle();

    int[] verticalTitle();

    boolean showImaginaryLine();

    boolean showLandsTitle(int position);

    boolean showVerticalTitle(int position);

    boolean markLandsTitle(String title);

    String landsUnit();

    int intervalTime();
}
