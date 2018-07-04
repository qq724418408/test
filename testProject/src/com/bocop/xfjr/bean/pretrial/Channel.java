package com.bocop.xfjr.bean.pretrial;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */

public class Channel {
    /**
     * channelId : 14456647
     * channelName : 渠道3
     * productList : [{"custType":[{"typeValue":"房贷类客户","typeKey":"10"},{"typeValue":"优质类客户","typeKey":"11"},{"typeValue":"公积金类客户","typeKey":"12"}],"fxModel":"1","maxAge":"65","maxMoney":"1000000","minAge":"18","percent":"90","productId":"10010","productName":"房贷分期","qz":{"faceSimDegree":"20","zcCredit":"N","zcFace":"Y","zcMobile":"Y","zcUnion":"Y"},"qzNo":""},{"custType":[{"typeValue":"普通类客户","typeKey":"30"}],"fxModel":"3","maxAge":"65","maxMoney":"1000000","minAge":"18","percent":"90","productId":"10010","productName":"房贷分期","qz":{"faceSimDegree":"20","zcCredit":"N","zcFace":"Y","zcMobile":"Y","zcUnion":"Y"},"qzNo":""}]
     */

    private String channelId;
    private String channelName;
    private List<ProductListBean> productList;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean {
        /**
         * custType : [{"typeValue":"房贷类客户","typeKey":"10"},{"typeValue":"优质类客户","typeKey":"11"},{"typeValue":"公积金类客户","typeKey":"12"}]
         * fxModel : 1
         * maxAge : 65
         * maxMoney : 1000000
         * minAge : 18
         * percent : 90
         * productId : 10010
         * productName : 房贷分期
         * qz : {"faceSimDegree":"20","zcCredit":"N","zcFace":"Y","zcMobile":"Y","zcUnion":"Y"}
         * qzNo :
         */

        private String fxModel;
        private String maxAge;
        private String maxMoney;
        private String minAge;
        private String percent;
        private String productId;
        private String productName;
        private QzBean qz;
        private String qzNo;
        private List<CustTypeBean> custType;

        public String getFxModel() {
            return fxModel;
        }

        public void setFxModel(String fxModel) {
            this.fxModel = fxModel;
        }

        public String getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(String maxAge) {
            this.maxAge = maxAge;
        }

        public String getMaxMoney() {
            return maxMoney;
        }

        public void setMaxMoney(String maxMoney) {
            this.maxMoney = maxMoney;
        }

        public String getMinAge() {
            return minAge;
        }

        public void setMinAge(String minAge) {
            this.minAge = minAge;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public QzBean getQz() {
            return qz;
        }

        public void setQz(QzBean qz) {
            this.qz = qz;
        }

        public String getQzNo() {
            return qzNo;
        }

        public void setQzNo(String qzNo) {
            this.qzNo = qzNo;
        }

        public List<CustTypeBean> getCustType() {
            return custType;
        }

        public void setCustType(List<CustTypeBean> custType) {
            this.custType = custType;
        }

        public static class QzBean {
            /**
             * faceSimDegree : 20
             * zcCredit : N
             * zcFace : Y
             * zcMobile : Y
             * zcUnion : Y
             */

            private String faceSimDegree;
            private String zcCredit;
            private String zcFace;
            private String zcMobile;
            private String zcUnion;

            public String getFaceSimDegree() {
                return faceSimDegree;
            }

            public void setFaceSimDegree(String faceSimDegree) {
                this.faceSimDegree = faceSimDegree;
            }

            public String getZcCredit() {
                return zcCredit;
            }

            public void setZcCredit(String zcCredit) {
                this.zcCredit = zcCredit;
            }

            public String getZcFace() {
                return zcFace;
            }

            public void setZcFace(String zcFace) {
                this.zcFace = zcFace;
            }

            public String getZcMobile() {
                return zcMobile;
            }

            public void setZcMobile(String zcMobile) {
                this.zcMobile = zcMobile;
            }

            public String getZcUnion() {
                return zcUnion;
            }

            public void setZcUnion(String zcUnion) {
                this.zcUnion = zcUnion;
            }
        }

        public static class CustTypeBean {
            /**
             * typeValue : 房贷类客户
             * typeKey : 10
             */

            private String typeValue;
            private String typeKey;

            public String getTypeValue() {
                return typeValue;
            }

            public void setTypeValue(String typeValue) {
                this.typeValue = typeValue;
            }

            public String getTypeKey() {
                return typeKey;
            }

            public void setTypeKey(String typeKey) {
                this.typeKey = typeKey;
            }
        }
    }
}
