package com.bocop.xfjr.bean.pretrial;

import java.io.Serializable;

public  class NormalBean  implements Serializable{
    /**
     * workYears : 5
     * duty : 客户经理
     * income : 21000.00
     * spouse : {"companyName":"四方精创","workYears":"3","duty":"产品经理","income":"15000.00"}
     * house : {"housesAddress":"江西省xx市xx区xx路10号傍边","housesArea":"200","housesValue":"3000000","housesOwner":"李防冬","relation":"兄弟"}
     * guarantor : {"companyName":"四方精创","workYears":"3","duty":"项目经理","income":"18000.00","maritalStatus":"0","guarantorSpouse":{"companyName":"四方精创","workYears":"3","duty":"产品经理","income":"15000.00"},"guarantorHouse":{"housesAddress":"江西省xx市xx区xx路10号对面","housesArea":"180","validatedStatus":"3"}}
     */
	private int type=-1;
	private boolean isShow=true;
    private String workYears;
    private String duty;
    private String income;
    private SpouseBean spouse;
    private HouseBeanX house;
    private GuarantorBean guarantor;

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public SpouseBean getSpouse() {
        return spouse;
    }

    public void setSpouse(SpouseBean spouse) {
        this.spouse = spouse;
    }

    public HouseBeanX getHouse() {
        return house;
    }

    public void setHouse(HouseBeanX house) {
        this.house = house;
    }

    public GuarantorBean getGuarantor() {
        return guarantor;
    }

    public void setGuarantor(GuarantorBean guarantor) {
        this.guarantor = guarantor;
    }

    public static class SpouseBean implements Serializable{
        /**
         * companyName : 四方精创
         * workYears : 3
         * duty : 产品经理
         * income : 15000.00
         */

        private String companyName;
        private String workYears;
        private String duty;
        private String income;
        private boolean isShow=true;
        public boolean isShow() {
			return isShow;
		}

		public void setShow(boolean isShow) {
			this.isShow = isShow;
		}

		public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getWorkYears() {
            return workYears;
        }

        public void setWorkYears(String workYears) {
            this.workYears = workYears;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }
    }

    public static class HouseBeanX implements Serializable{
        /**
         * housesAddress : 江西省xx市xx区xx路10号傍边
         * housesArea : 200
         * housesValue : 3000000
         * housesOwner : 李防冬
         * relation : 兄弟
         */

        private String housesAddress;
        private String housesArea;
        private String housesValue;
        private String housesOwner;
        private String relation;
        private boolean isShow=true;
        public String getHousesAddress() {
            return housesAddress;
        }

        public boolean isShow() {
			return isShow;
		}

		public void setShow(boolean isShow) {
			this.isShow = isShow;
		}

		public void setHousesAddress(String housesAddress) {
            this.housesAddress = housesAddress;
        }

        public String getHousesArea() {
            return housesArea;
        }

        public void setHousesArea(String housesArea) {
            this.housesArea = housesArea;
        }

        public String getHousesValue() {
            return housesValue;
        }

        public void setHousesValue(String housesValue) {
            this.housesValue = housesValue;
        }

        public String getHousesOwner() {
            return housesOwner;
        }

        public void setHousesOwner(String housesOwner) {
            this.housesOwner = housesOwner;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }
    }

    public static class GuarantorBean implements Serializable{
        /**
         * companyName : 四方精创
         * workYears : 3
         * duty : 项目经理
         * income : 18000.00
         * maritalStatus : 0
         * guarantorSpouse : {"companyName":"四方精创","workYears":"3","duty":"产品经理","income":"15000.00"}
         * guarantorHouse : {"housesAddress":"江西省xx市xx区xx路10号对面","housesArea":"180","validatedStatus":"3"}
         */

        private String companyName;
        private String workYears;
        private String duty;
        private String income;
        private String maritalStatus;
        private GuarantorSpouseBean guarantorSpouse;
        private GuarantorHouseBean guarantorHouse;
        private boolean isShow=true;
        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public boolean isShow() {
			return isShow;
		}

		public void setShow(boolean isShow) {
			this.isShow = isShow;
		}

		public String getWorkYears() {
            return workYears;
        }

        public void setWorkYears(String workYears) {
            this.workYears = workYears;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public GuarantorSpouseBean getGuarantorSpouse() {
            return guarantorSpouse;
        }

        public void setGuarantorSpouse(GuarantorSpouseBean guarantorSpouse) {
            this.guarantorSpouse = guarantorSpouse;
        }

        public GuarantorHouseBean getGuarantorHouse() {
            return guarantorHouse;
        }

        public void setGuarantorHouse(GuarantorHouseBean guarantorHouse) {
            this.guarantorHouse = guarantorHouse;
        }

        public static class GuarantorSpouseBean implements Serializable{
            /**
             * companyName : 四方精创
             * workYears : 3
             * duty : 产品经理
             * income : 15000.00
             */

            private String companyName;
            private String workYears;
            private String duty;
            private String income;

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getWorkYears() {
                return workYears;
            }

            public void setWorkYears(String workYears) {
                this.workYears = workYears;
            }

            public String getDuty() {
                return duty;
            }

            public void setDuty(String duty) {
                this.duty = duty;
            }

            public String getIncome() {
                return income;
            }

            public void setIncome(String income) {
                this.income = income;
            }
        }

        public static class GuarantorHouseBean implements Serializable{
            /**
             * housesAddress : 江西省xx市xx区xx路10号对面
             * housesArea : 180
             * validatedStatus : 3
             */

            private String housesAddress;
            private String housesArea;
            private String validatedStatus;

            public String getHousesAddress() {
                return housesAddress;
            }

            public void setHousesAddress(String housesAddress) {
                this.housesAddress = housesAddress;
            }

            public String getHousesArea() {
                return housesArea;
            }

            public void setHousesArea(String housesArea) {
                this.housesArea = housesArea;
            }

            public String getValidatedStatus() {
                return validatedStatus;
            }

            public void setValidatedStatus(String validatedStatus) {
                this.validatedStatus = validatedStatus;
            }
        }
    }
}