package com.bocop.xfjr.bean;

import java.util.List;

public class TypeDataBean {
	private List<YearBean> lastYear;
    private List<YearBean> thisYear;

    public List<YearBean> getLastYear() {
        return lastYear;
    }

    public void setLastYear(List<YearBean> lastYear) {
        this.lastYear = lastYear;
    }

    public List<YearBean> getThisYear() {
        return thisYear;
    }

    public void setThisYear(List<YearBean> thisYear) {
        this.thisYear = thisYear;
    }

    public static class YearBean {
        /**
         * money : 0
         * number : 0
         * type : 0
         */

        private String money;
        private String number;
        private String type;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
