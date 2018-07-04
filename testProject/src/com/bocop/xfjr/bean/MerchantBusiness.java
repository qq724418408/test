package com.bocop.xfjr.bean;

import java.util.List;


/**
 * 
 * 商户-业务列表bean
 * 
 * @author formssi
 *
 */
public class MerchantBusiness {
	/**
     * business : [{"applyMoney":"200000","businessId":"1","customerName":"郑云","productId":"10010","productName":"房贷分期"},{"applyMoney":"400000","businessId":"6","customerName":"张正云","productId":"10011","productName":"汽车分期"},{"applyMoney":"400000","businessId":"8","customerName":"郑赟","productId":"10011","productName":"汽车分期"},{"applyMoney":"435","businessId":"BUS170915151944000001","customerName":"郑赟","productId":"10010","productName":"房贷分期"}]
     * count : 4
     * totalMoney : 1000435.0
     */

    private String count = "0";
    private String totalMoney = "0";
    private List<BusinessBean> business;
    

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<BusinessBean> getBusiness() {
        return business;
    }

    public void setBusiness(List<BusinessBean> business) {
        this.business = business;
    }

//    public static class BusinessBean {
//        /**
//         * applyMoney : 200000
//         * businessId : 1
//         * customerName : 郑云
//         * productId : 10010
//         * productName : 房贷分期
//         */
//
//        private String applyMoney;
//        private String businessId;
//        private String customerName;
//        private String productId;
//        private String productName;
//
//        public String getApplyMoney() {
//            return applyMoney;
//        }
//
//        public void setApplyMoney(String applyMoney) {
//            this.applyMoney = applyMoney;
//        }
//
//        public String getBusinessId() {
//            return businessId;
//        }
//
//        public void setBusinessId(String businessId) {
//            this.businessId = businessId;
//        }
//
//        public String getCustomerName() {
//            return customerName;
//        }
//
//        public void setCustomerName(String customerName) {
//            this.customerName = customerName;
//        }
//
//        public String getProductId() {
//            return productId;
//        }
//
//        public void setProductId(String productId) {
//            this.productId = productId;
//        }
//
//        public String getProductName() {
//            return productName;
//        }
//
//        public void setProductName(String productName) {
//            this.productName = productName;
//        }
//    }
}
