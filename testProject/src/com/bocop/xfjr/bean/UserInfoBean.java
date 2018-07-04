package com.bocop.xfjr.bean;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

public class UserInfoBean extends RootRsp {

//	private String role;
//    private String name;
    private String merchantId;
    private String address;
    private String blankName;
    private List<YearBean> lastYear;
    private List<YearBean> thisYear;

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlankName() {
        return blankName;
    }

    public void setBlankName(String blankName) {
        this.blankName = blankName;
    }

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
         * month : 2
         * total : 10
         * passed : 7
         */
        private int month;
        private int total;
        private int passed;
        
        public YearBean(){}
        
		public YearBean(int month, float total, float passed) {
			super();
			this.month = month;
			this.total = (int) total;
			this.passed = (int) passed;
		}
		public int getMonth() {
			return month;
		}
		public void setMonth(int month) {
			this.month = month;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(float total) {
			this.total = (int) total;
		}
		public int getPassed() {
			return passed;
		}
		public void setPassed(float passed) {
			this.passed = (int) passed;
		}

        
    }

//    public static class ThisYearBean {
//        /**
//         * month : 2
//         * total : 10
//         * passed : 7
//         */
//
//        private String month;
//        private String total;
//        private String passed;
//
//        public String getMonth() {
//            return month;
//        }
//
//        public void setMonth(String month) {
//            this.month = month;
//        }
//
//        public String getTotal() {
//            return total;
//        }
//
//        public void setTotal(String total) {
//            this.total = total;
//        }
//
//        public String getPassed() {
//            return passed;
//        }
//
//        public void setPassed(String passed) {
//            this.passed = passed;
//        }
//    }

}
