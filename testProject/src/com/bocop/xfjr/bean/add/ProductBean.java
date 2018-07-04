package com.bocop.xfjr.bean.add;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 产品实体
 * 
 * @author wujunliu
 *
 */
public class ProductBean extends RootRsp {

	@Override
	public String toString() {
		return productName;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String fxModel;
	private String maxAge;// 男
	private String maxMoney;
	private String maxAge2; // 女
	private String minAge;// 男
	private String minAge2; // 女
	private String percent;
	private String productId;
	private String productName;
	private QzBean qz = new QzBean();
	private List<CustTypeBean> custType;
	private int from; // 记录步骤从详情还是列表出发
	private String userName;// 申请人跳转身份证页面比较名字
	private String custCardId; // 申请人银联验证卡号
    private String custCardIdThird; // 申请人第三方征信卡号
    private String telephone; // 申请人手机号
    private String idCard; // 申请人身份证
    private String periods; // 分期期数（单位：月）
 	private boolean personalSubmit = false;// 人行征信是否提交
 	private boolean bankCardSubmit = false;// 是否已经提交银联验证的信息
 	private boolean threeSubmit = false;// 是否已经提交第三方的信息

 	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getMaxAge2() {
		return maxAge2;
	}

	public void setMaxAge2(String maxAge2) {
		this.maxAge2 = maxAge2;
	}

	public String getMinAge2() {
		return minAge2;
	}

	public void setMinAge2(String minAge2) {
		this.minAge2 = minAge2;
	}

	public boolean isPersonalSubmit() {
 		return personalSubmit;
 	}

 	public void setPersonalSubmit(boolean personalSubmit) {
 		this.personalSubmit = personalSubmit;
 	}

 	public boolean isBankCardSubmit() {
 		return bankCardSubmit;
 	}

 	public void setBankCardSubmit(boolean bankCardSubmit) {
 		this.bankCardSubmit = bankCardSubmit;
 	}

 	public boolean isThreeSubmit() {
 		return threeSubmit;
 	}

 	public void setThreeSubmit(boolean threeSubmit) {
 		this.threeSubmit = threeSubmit;
 	}
	
	public String getCustCardId() {
		return custCardId;
	}

	public void setCustCardId(String custCardId) {
		this.custCardId = custCardId;
	}

	public String getCustCardIdThird() {
		return custCardIdThird;
	}

	public void setCustCardIdThird(String custCardIdThird) {
		this.custCardIdThird = custCardIdThird;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

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

	public List<CustTypeBean> getCustType() {
		return custType;
	}

	public void setCustType(List<CustTypeBean> custType) {
		this.custType = custType;
	}

	public static class QzBean extends RootRsp {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String faceSimDegree = "0";
		private String zcCredit;
		private String zcFace;
		private String zcMobile;
		private String zcUnion;

		public QzBean() {
			super();
		}
		
		public QzBean(String faceSimDegree, String zcCredit, String zcFace, String zcMobile, String zcUnion) {
			super();
			this.zcCredit = zcCredit;
			this.zcFace = zcFace;
			this.zcMobile = zcMobile;
			this.zcUnion = zcUnion;
		}

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
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "[ 相似度 = "+faceSimDegree+", 手机验证 = "+zcMobile + ", 人脸识别 = "+zcFace+", 银联卡 = "+zcUnion+", 第三方 = "+zcCredit+"]";
		}
	}

	public static class CustTypeBean extends RootRsp {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String typeKey;
		private String typeValue;

		public String getTypeKey() {
			return typeKey;
		}

		public void setTypeKey(String typeKey) {
			this.typeKey = typeKey;
		}

		public String getTypeValue() {
			return typeValue;
		}

		public void setTypeValue(String typeValue) {
			this.typeValue = typeValue;
		}
	}
}
