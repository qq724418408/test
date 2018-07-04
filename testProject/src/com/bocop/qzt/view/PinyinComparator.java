package com.bocop.qzt.view;

import java.util.Comparator;

/**
 * 鐢ㄦ潵瀵筁istView涓殑鏁版嵁鏍规嵁A-Z杩涜鎺掑簭锛屽墠闈袱涓猧f鍒ゆ柇涓昏鏄皢涓嶆槸浠ユ眽瀛楀紑澶寸殑鏁版嵁鏀惧湪鍚庨潰
 */
public class PinyinComparator implements Comparator<ContactSortModel> {

    public int compare(ContactSortModel o1, ContactSortModel o2) {
        //杩欓噷涓昏鏄敤鏉ュListView閲岄潰鐨勬暟鎹牴鎹瓵BCDEFG...鏉ユ帓搴�
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
