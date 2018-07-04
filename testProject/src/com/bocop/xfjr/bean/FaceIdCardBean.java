package com.bocop.xfjr.bean;

import android.R.integer;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/9/14
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class FaceIdCardBean {

    /**
     * birthday : {"year":"1992","day":"2","month":"11"}
     * name : 田锋
     * race :
     * address : 湖南省临澧县陈二乡长冲村田家组
     * time_used : 359
     * gender : 男
     * head_rect : {"rt":{"y":0.19554456,"x":0.8768769},"lt":{"y":0.19306931,"x":0.5990991},"lb":{"y":0.7128713,"x":0.6096096},"rb":{"y":0.7128713,"x":0.8948949}}
     * request_id : 1505360173,8a5f52ad-e3c5-4061-9889-d8111f90a03b
     * id_card_number : 430724199211023916
     * side : front
     */
	/**
	 * 反面
	 * {"time_used": 513, "valid_date": "2015.07.06-2025.07.06", 
	 * "issued_by": "东乡县公安局", "side": "back", 
	 * "request_id": "1512748050,e7914135-018e-4865-9420-d0687fa80af3"}
	 */
	
    private BirthdayBean birthday;
    private String name;
    private String race;
    private String address;
    private int time_used;
    private String gender;
    private HeadRectBean head_rect;
    private String request_id;
    private String id_card_number;
    private String side;
    private String valid_date;//有效日期
    private String issued_by;//签证机关
    private String confidence;
    private String reason;
    
    public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getValid_date() {
		return valid_date;
	}

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public String getIssued_by() {
		return issued_by;
	}

	public void setIssued_by(String issued_by) {
		this.issued_by = issued_by;
	}

	public BirthdayBean getBirthday() {
        return birthday;
    }

    public void setBirthday(BirthdayBean birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public HeadRectBean getHead_rect() {
        return head_rect;
    }

    public void setHead_rect(HeadRectBean head_rect) {
        this.head_rect = head_rect;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public static class BirthdayBean {
        /**
         * year : 1992
         * day : 2
         * month : 11
         */

        private String year;
        private String day;
        private String month;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public static class HeadRectBean {
        /**
         * rt : {"y":0.19554456,"x":0.8768769}
         * lt : {"y":0.19306931,"x":0.5990991}
         * lb : {"y":0.7128713,"x":0.6096096}
         * rb : {"y":0.7128713,"x":0.8948949}
         */

        private RtBean rt;
        private LtBean lt;
        private LbBean lb;
        private RbBean rb;

        public RtBean getRt() {
            return rt;
        }

        public void setRt(RtBean rt) {
            this.rt = rt;
        }

        public LtBean getLt() {
            return lt;
        }

        public void setLt(LtBean lt) {
            this.lt = lt;
        }

        public LbBean getLb() {
            return lb;
        }

        public void setLb(LbBean lb) {
            this.lb = lb;
        }

        public RbBean getRb() {
            return rb;
        }

        public void setRb(RbBean rb) {
            this.rb = rb;
        }

        public static class RtBean {
            /**
             * y : 0.19554456
             * x : 0.8768769
             */

            private double y;
            private double x;

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }
        }

        public static class LtBean {
            /**
             * y : 0.19306931
             * x : 0.5990991
             */

            private double y;
            private double x;

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }
        }

        public static class LbBean {
            /**
             * y : 0.7128713
             * x : 0.6096096
             */

            private double y;
            private double x;

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }
        }

        public static class RbBean {
            /**
             * y : 0.7128713
             * x : 0.8948949
             */

            private double y;
            private double x;

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }
        }
    }
    
}
