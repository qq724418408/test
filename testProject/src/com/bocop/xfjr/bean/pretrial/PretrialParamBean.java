package com.bocop.xfjr.bean.pretrial;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 风险预决策-提交预决策
 * 
 * 通用字段
 *
 * @author 骆志文 2017年9月28日 下午7:41:31
 */
public class PretrialParamBean extends RootRsp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String businessId;		//业务进件ID
	private String married;			//婚姻状况(1:结婚 2:离婚 3:单身)
	private String unitId;			//行业类型
	private String comAddress;		//单位地址
	private String comName;			//单位名称
	private String jobId;			//职位等级
	private String workYear;		//职位等级
	private String monthIncome;		//现单位工作年限
	private String cityId;			//平均月收入
	private String liveAddress;		//居住地址
	private String education;		//学历
	private String payType;			//支付方式
	private String linkMan1;		//联系人1
	private String linkMobile1;		//联系方式1
	private String linkMan2;		//联系人2
	private String linkMobile2;		//联系方式2
	private String hasHouse;		//是否有房产（Y/N）
	private String photoPosition;	//合影地址
	private String photoTime;		//合影时间yyyy-MM-dd HH:mm:ss
//	private List<DecisionOrdinary> decisionOrdinaryList;	//普通类客户List
//	private List<DecisionGroup> decisionGroupList;			//客群类客户List
//	private List<DecisionScene> decisionSceneList;			//场景类客户List
//	private String businessId; // 业务进件ID
//	private String cityId = "";
//	private String comAddress = ""; // 单位地址
//	private String comName = "";
//	private String education = "";
//	private String jobId = "";
//	private String linkMan1 = "";
//	private String linkMan2 = "";
//	private String linkMobile1 = "";
//	private String linkMobile2 = "";
//	private String liveAddress = ""; // 居住地址
//	private String married = ""; // 婚姻状况 1:结婚 2:离婚 3:单身
//	private String payType = "";
//	private String unitId = ""; // 行业类型
//	private List<DecisionGroupListBean> decisionGroupList = new ArrayList<>();
//	private List<DecisionSceneListBean> decisionSceneList = new ArrayList<>();
//	private List<DecisionOrdinaryListBean> decisionOrdinaryList = new ArrayList<>();
//	private List<DecisionOrdinaryListBean> descA; // 普通客户资料
//	private DescB descB; // 房贷类资料
	private DescC descC; // 公积金资料
	private DescD descD; // 社保资料
	private List<DescE> descE; // 房产类资料

	/**
	 * 房贷类资料
	 */
	public static class DescB extends RootRsp {

		private static final long serialVersionUID = 1L;
		private String firPayPay = "";
		private String repayYear = "";
//		private String firPayPay;		//首付款金额（无贷款金额存0）
//		private String repayYear;		//房贷已还款年限（年）

		public String getFirPayPay() {
			return firPayPay;
		}

		public void setFirPayPay(String firPayPay) {
			this.firPayPay = firPayPay;
		}

		public String getRepayYear() {
			return repayYear;
		}

		public void setRepayYear(String repayYear) {
			this.repayYear = repayYear;
		}
	}

	/**
	 * 公积金资料
	 */
	public static class DescC extends RootRsp {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String gjPayCom;		//公积金缴存单位
		private String gjPayStart;		//公积金缴存起始日
		private String gjMatch;			//公积金与客户单位是否匹配
		private String gjCheck;			//公积金核查方式
		private String gjPreMonthPay;	//公积金个人月缴金额
		private String gjPreMonthRate;	//公积金个人月缴比例
		private String gjComMonthPay;	//公积金单位月缴金额
		private String gjComMonthRate;	//公积金单位月缴比例
//		private String gjCheck = "";
//		private String gjComMonthPay = "";
//		private String gjComMonthRate = "";
//		private String gjMatch = "";
//		private String gjPayCom = "";
//		private String gjPayStart = "";
//		private String gjPreMonthPay = "";
//		private String gjPreMonthRate = "";

		public String getGjCheck() {
			return gjCheck;
		}

		public void setGjCheck(String gjCheck) {
			this.gjCheck = gjCheck;
		}

		public String getGjComMonthPay() {
			return gjComMonthPay;
		}

		public void setGjComMonthPay(String gjComMonthPay) {
			this.gjComMonthPay = gjComMonthPay;
		}

		public String getGjComMonthRate() {
			return gjComMonthRate;
		}

		public void setGjComMonthRate(String gjComMonthRate) {
			this.gjComMonthRate = gjComMonthRate;
		}

		public String getGjMatch() {
			return gjMatch;
		}

		public void setGjMatch(String gjMatch) {
			this.gjMatch = gjMatch;
		}

		public String getGjPayCom() {
			return gjPayCom;
		}

		public void setGjPayCom(String gjPayCom) {
			this.gjPayCom = gjPayCom;
		}

		public String getGjPayStart() {
			return gjPayStart;
		}

		public void setGjPayStart(String gjPayStart) {
			this.gjPayStart = gjPayStart;
		}

		public String getGjPreMonthPay() {
			return gjPreMonthPay;
		}

		public void setGjPreMonthPay(String gjPreMonthPay) {
			this.gjPreMonthPay = gjPreMonthPay;
		}

		public String getGjPreMonthRate() {
			return gjPreMonthRate;
		}

		public void setGjPreMonthRate(String gjPreMonthRate) {
			this.gjPreMonthRate = gjPreMonthRate;
		}

	}

	/**
	 * 社保资料
	 */
	public static class DescD extends RootRsp {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String sjPayCom;		//社保金缴存单位
		private String sjPayStart;		//社保金缴存起始日
		private String sjPreMonthPay;		//社保个人月缴金额
//		private String sjPreMonthRate;		//社保个人月缴比例
		private String sjComMonthPay;		//社保单位月缴金额
//		private String sjComMonthRate;		//社保单位月缴比例
		private String sjMatch;			//社保金与客户单位是否匹配
		private String sjChect;			//社保金核查方式
//		private String sjChect = "";
//		private String sjMatch = "";
//		private String sjPayCom = "";
//		private String sjPayStart = "";
//		private String sjPreMonthPay = "";
//		private String sjPreMonthRate = "";
//		private String sjComMonthPay = "";
//		private String sjComMonthRate = "";

		public String getSjChect() {
			return sjChect;
		}

		public void setSjChect(String sjChect) {
			this.sjChect = sjChect;
		}

		public String getSjMatch() {
			return sjMatch;
		}

		public void setSjMatch(String sjMatch) {
			this.sjMatch = sjMatch;
		}

		public String getSjPayCom() {
			return sjPayCom;
		}

		public void setSjPayCom(String sjPayCom) {
			this.sjPayCom = sjPayCom;
		}

		public String getSjPayStart() {
			return sjPayStart;
		}

		public void setSjPayStart(String sjPayStart) {
			this.sjPayStart = sjPayStart;
		}

		public String getSjPreMonthPay() {
			return sjPreMonthPay;
		}

		public void setSjPreMonthPay(String sjPreMonthPay) {
			this.sjPreMonthPay = sjPreMonthPay;
		}

//		public String getSjPreMonthRate() {
//			return sjPreMonthRate;
//		}
//
//		public void setSjPreMonthRate(String sjPreMonthRate) {
//			this.sjPreMonthRate = sjPreMonthRate;
//		}

		public String getSjComMonthPay() {
			return sjComMonthPay;
		}

		public void setSjComMonthPay(String sjComMonthPay) {
			this.sjComMonthPay = sjComMonthPay;
		}

//		public String getSjComMonthRate() {
//			return sjComMonthRate;
//		}
//
//		public void setSjComMonthRate(String sjComMonthRate) {
//			this.sjComMonthRate = sjComMonthRate;
//		}
	}

	/**
	 * 房产类资料
	 */
	public static class DescE extends RootRsp {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String houseOwner;		//业主姓名1
		private String cityId;			//房产所在城市1
		private String houseArea;		//房产面积1
//		private String housePropId;		//房产类型ID1
		private String houseCheck;		//房产信息核查方式1
//		private String houseOwner2;		//业主姓名2
//		private String cityId2;			//房产所在城市2
//		private String houseArea2;		//房产面积2
//		private String housePropId2;	//房产类型ID2
		private String isMortgage;		//是否按揭贷款(Y/N)
		private String firPayPay;		//房贷金额
		private String repayYear;		//已还款年限
		private String nopayoffAmt;		//未结清住房贷款金额
//		private String cityId = "";
//		private String cityId2 = "";
//		private String houseArea = "";
//		private String houseArea2 = "";
//		private String houseCheck = "";
//		private String houseCheck2 = "";
//		private String houseOwner = "";
//		private String houseOwner2 = "";
//		private String housePropId = "";
//		private String housePropId2 = "";

		public DescE() {
			super();
		}
		
		public DescE(String houseOwner, String cityId, String houseArea, String houseCheck, String isMortgage) {
			super();
			this.houseOwner = houseOwner;
			this.cityId = cityId;
			this.houseArea = houseArea;
			this.houseCheck = houseCheck;
			this.isMortgage = isMortgage;
		}
		
		public DescE(String houseOwner, String cityId, String houseArea, String houseCheck, String isMortgage, String nopayoffAmt) {
			super();
			this.houseOwner = houseOwner;
			this.cityId = cityId;
			this.houseArea = houseArea;
			this.houseCheck = houseCheck;
			this.isMortgage = isMortgage;
			this.nopayoffAmt = nopayoffAmt;
		}
		
		public String getNopayoffAmt() {
			return nopayoffAmt;
		}

		public void setNopayoffAmt(String nopayoffAmt) {
			this.nopayoffAmt = nopayoffAmt;
		}

		public String getCityId() {
			return cityId;
		}

		public String getIsMortgage() {
			return isMortgage;
		}

		public void setIsMortgage(String isMortgage) {
			this.isMortgage = isMortgage;
		}

		public String getFirPayPay() {
			return firPayPay;
		}

		public void setFirPayPay(String firPayPay) {
			this.firPayPay = firPayPay;
		}

		public String getRepayYear() {
			return repayYear;
		}

		public void setRepayYear(String repayYear) {
			this.repayYear = repayYear;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getHouseArea() {
			return houseArea;
		}

		public void setHouseArea(String houseArea) {
			this.houseArea = houseArea;
		}

		public String getHouseCheck() {
			return houseCheck;
		}

		public void setHouseCheck(String houseCheck) {
			this.houseCheck = houseCheck;
		}

		public String getHouseOwner() {
			return houseOwner;
		}

		public void setHouseOwner(String houseOwner) {
			this.houseOwner = houseOwner;
		}


	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getComAddress() {
		return comAddress;
	}

	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getLinkMan1() {
		return linkMan1;
	}

	public void setLinkMan1(String linkMan1) {
		this.linkMan1 = linkMan1;
	}

	public String getLinkMan2() {
		return linkMan2;
	}

	public void setLinkMan2(String linkMan2) {
		this.linkMan2 = linkMan2;
	}

	public String getLinkMobile1() {
		return linkMobile1;
	}

	public void setLinkMobile1(String linkMobile1) {
		this.linkMobile1 = linkMobile1;
	}

	public String getLinkMobile2() {
		return linkMobile2;
	}

	public void setLinkMobile2(String linkMobile2) {
		this.linkMobile2 = linkMobile2;
	}

	public String getLiveAddress() {
		return liveAddress;
	}

	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

//	public List<DecisionGroupListBean> getDecisionGroupList() {
//		return decisionGroupList;
//	}
//
//	public void setDecisionGroupList(List<DecisionGroupListBean> decisionGroupList) {
//		this.decisionGroupList = decisionGroupList;
//	}
//
//	public List<DecisionOrdinaryListBean> getDecisionOrdinaryList() {
//		return decisionOrdinaryList;
//	}
//
//	public void setDecisionOrdinaryList(List<DecisionOrdinaryListBean> decisionOrdinaryList) {
//		this.decisionOrdinaryList = decisionOrdinaryList;
//	}
//
//	public List<DecisionSceneListBean> getDecisionSceneList() {
//		return decisionSceneList;
//	}
//
//	public void setDecisionSceneList(List<DecisionSceneListBean> decisionSceneList) {
//		this.decisionSceneList = decisionSceneList;
//	}

	/**
	 * 风险预决策-提交预决策
	 * 
	 * 客群类字段
	 *
	 * @author 骆志文 2017年9月28日 下午8:06:44
	 */
	public static class DecisionGroupListBean extends RootRsp {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String customerType;	//客户类型key
		private String firPayPay;		//首付款金额（无贷款金额存0）
		private String repayYear;		//房贷已还款年限（年）
		private String gjPayCom;		//公积金缴存单位
		private String gjPayStart;		//公积金缴存起始日
		private String gjMatch;			//公积金与客户单位是否匹配
		private String gjCheck;			//公积金核查方式
		private String gjPreMonthPay;	//公积金个人月缴金额
		private String gjPreMonthRate;	//公积金个人月缴比例
		private String gjComMonthPay;	//公积金单位月缴金额
		private String gjComMonthRate;	//公积金单位月缴比例

//		private String customerType = "";
//		private String firPayPay = "";
//		private String repayYear = "";
//		private String gjCheck = "";
//		private String gjComMonthPay = "";
//		private String gjComMonthRate = "";
//		private String gjMatch = "";
//		private String gjPayCom = "";
//		private String gjPayStart = "";
//		private String gjPreMonthPay = "";
//		private String gjPreMonthRate = "";

		public String getCustomerType() {
			return customerType;
		}

		public void setCustomerType(String customerType) {
			this.customerType = customerType;
		}

		public String getFirPayPay() {
			return firPayPay;
		}

		public void setFirPayPay(String firPayPay) {
			this.firPayPay = firPayPay;
		}

		public String getGjCheck() {
			return gjCheck;
		}

		public void setGjCheck(String gjCheck) {
			this.gjCheck = gjCheck;
		}

		public String getGjComMonthPay() {
			return gjComMonthPay;
		}

		public void setGjComMonthPay(String gjComMonthPay) {
			this.gjComMonthPay = gjComMonthPay;
		}

		public String getGjComMonthRate() {
			return gjComMonthRate;
		}

		public void setGjComMonthRate(String gjComMonthRate) {
			this.gjComMonthRate = gjComMonthRate;
		}

		public String getGjMatch() {
			return gjMatch;
		}

		public void setGjMatch(String gjMatch) {
			this.gjMatch = gjMatch;
		}

		public String getGjPayCom() {
			return gjPayCom;
		}

		public void setGjPayCom(String gjPayCom) {
			this.gjPayCom = gjPayCom;
		}

		public String getGjPayStart() {
			return gjPayStart;
		}

		public void setGjPayStart(String gjPayStart) {
			this.gjPayStart = gjPayStart;
		}

		public String getGjPreMonthPay() {
			return gjPreMonthPay;
		}

		public void setGjPreMonthPay(String gjPreMonthPay) {
			this.gjPreMonthPay = gjPreMonthPay;
		}

		public String getGjPreMonthRate() {
			return gjPreMonthRate;
		}

		public void setGjPreMonthRate(String gjPreMonthRate) {
			this.gjPreMonthRate = gjPreMonthRate;
		}

		public String getRepayYear() {
			return repayYear;
		}

		public void setRepayYear(String repayYear) {
			this.repayYear = repayYear;
		}
	}

	/**
	 * 风险预决策-提交预决策
	 * 
	 * 普通类字段
	 *
	 * @author 骆志文 2017年9月28日 下午7:56:08
	 */
	public static class DecisionOrdinaryListBean extends RootRsp {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String customerType;	//客户类型key
		private String peoType;			//人员类型(00:申请人 01:申请人配偶 02:保证人 03:保证人配偶)
		private String comName;			//工作单位名称（自己的自动带出）
		private String workYear;		//现单位工作年限
		private String jobId;			//职务等级
		private String monthIncome;		//平均月收入
		private String houseAddress;	//房产地址
		private String houseArea;		//房产面积
		private String houseValue;		//房产价值

//		private String comName = "";
//		private String customerType = "";
//		private String houseAddress = "";
//		private String houseArea = "";
//		private String houseValue = "";
//		private String jobId = "";
//		private String monthIncome = "";
//		private String peoType = "";
//		private String workYear = "";

		public String getComName() {
			return comName;
		}

		public void setComName(String comName) {
			this.comName = comName;
		}

		public String getCustomerType() {
			return customerType;
		}

		public void setCustomerType(String customerType) {
			this.customerType = customerType;
		}

		public String getHouseAddress() {
			return houseAddress;
		}

		public void setHouseAddress(String houseAddress) {
			this.houseAddress = houseAddress;
		}

		public String getHouseArea() {
			return houseArea;
		}

		public void setHouseArea(String houseArea) {
			this.houseArea = houseArea;
		}

		public String getHouseValue() {
			return houseValue;
		}

		public void setHouseValue(String houseValue) {
			this.houseValue = houseValue;
		}

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getMonthIncome() {
			return monthIncome;
		}

		public void setMonthIncome(String monthIncome) {
			this.monthIncome = monthIncome;
		}

		public String getPeoType() {
			return peoType;
		}

		public void setPeoType(String peoType) {
			this.peoType = peoType;
		}

		public String getWorkYear() {
			return workYear;
		}

		public void setWorkYear(String workYear) {
			this.workYear = workYear;
		}
	}

	/**
	 * 风险预决策-提交预决策
	 * 
	 * 场景类字段
	 *
	 * @author 骆志文 2017年9月28日 下午8:15:30
	 */
	public static class DecisionSceneListBean extends RootRsp {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String customerType;	//客户类型key
		private String gjPayCom;		//公积金缴存单位
		private String gjPayStart;		//公积金缴存起始日
		private String gjMatch;			//公积金与客户单位是否匹配
		private String gjCheck;			//公积金核查方式
		private String gjPreMonthPay;	//公积金个人月缴金额
		private String gjPreMonthRate;	//公积金个人月缴比例
		private String gjComMonthPay;	//公积金单位月缴金额
		private String gjComMonthRate;	//公积金单位月缴比例
		private String sjPayCom;		//社保金缴存单位
		private String sjPayStart;		//社保金缴存起始日
		private String sjPreMonthPay;		//社保个人月缴金额
		private String sjPreMonthRate;		//社保个人月缴比例
		private String sjComMonthPay;		//社保单位月缴金额
		private String sjComMonthRate;		//社保单位月缴比例
		private String sjMatch;			//社保金与客户单位是否匹配
		private String sjChect;			//社保金核查方式
		private String houseOwner;		//业主姓名1
		private String cityId;			//房产所在城市1
		private String houseArea;		//房产面积1
		private String housePropId;		//房产类型ID1
		private String houseCheck;		//房产信息核查方式1
		private String houseOwner2;		//业主姓名2
		private String cityId2;			//房产所在城市2
		private String houseArea2;		//房产面积2
		private String housePropId2;	//房产类型ID2
		private String houseCheck2;		//房产信息核查方式2
//		private String cityId = "";
//		private String cityId2 = "";
//		private String customerType = "";
//		private String gjCheck = "";
//		private String gjComMonthPay = "";
//		private String gjComMonthRate = "";
//		private String gjMatch = "";
//		private String gjPayCom = "";
//		private String gjPayStart = "";
//		private String gjPreMonthPay = "";
//		private String gjPreMonthRate = "";
//		private String houseArea = "";
//		private String houseArea2 = "";
//		private String houseCheck = "";
//		private String houseCheck2 = "";
//		private String houseOwner = "";
//		private String houseOwner2 = "";
//		private String housePropId = "";
//		private String housePropId2 = "";
//		private String sjChect = "";
//		private String sjMatch = "";
//		private String sjPayCom = "";
//		private String sjPayStart = "";
//		private String sjPreMonthPay = "";
//		private String sjPreMonthRate = "";
//		private String sjComMonthPay = "";
//		private String sjComMonthRate = "";

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getCityId2() {
			return cityId2;
		}

		public void setCityId2(String cityId2) {
			this.cityId2 = cityId2;
		}

		public String getCustomerType() {
			return customerType;
		}

		public void setCustomerType(String customerType) {
			this.customerType = customerType;
		}

		public String getGjCheck() {
			return gjCheck;
		}

		public void setGjCheck(String gjCheck) {
			this.gjCheck = gjCheck;
		}

		public String getGjComMonthPay() {
			return gjComMonthPay;
		}

		public void setGjComMonthPay(String gjComMonthPay) {
			this.gjComMonthPay = gjComMonthPay;
		}

		public String getGjComMonthRate() {
			return gjComMonthRate;
		}

		public void setGjComMonthRate(String gjComMonthRate) {
			this.gjComMonthRate = gjComMonthRate;
		}

		public String getGjMatch() {
			return gjMatch;
		}

		public void setGjMatch(String gjMatch) {
			this.gjMatch = gjMatch;
		}

		public String getGjPayCom() {
			return gjPayCom;
		}

		public void setGjPayCom(String gjPayCom) {
			this.gjPayCom = gjPayCom;
		}

		public String getGjPayStart() {
			return gjPayStart;
		}

		public void setGjPayStart(String gjPayStart) {
			this.gjPayStart = gjPayStart;
		}

		public String getGjPreMonthPay() {
			return gjPreMonthPay;
		}

		public void setGjPreMonthPay(String gjPreMonthPay) {
			this.gjPreMonthPay = gjPreMonthPay;
		}

		public String getGjPreMonthRate() {
			return gjPreMonthRate;
		}

		public void setGjPreMonthRate(String gjPreMonthRate) {
			this.gjPreMonthRate = gjPreMonthRate;
		}

		public String getHouseArea() {
			return houseArea;
		}

		public void setHouseArea(String houseArea) {
			this.houseArea = houseArea;
		}

		public String getHouseArea2() {
			return houseArea2;
		}

		public void setHouseArea2(String houseArea2) {
			this.houseArea2 = houseArea2;
		}

		public String getHouseCheck() {
			return houseCheck;
		}

		public void setHouseCheck(String houseCheck) {
			this.houseCheck = houseCheck;
		}

		public String getHouseCheck2() {
			return houseCheck2;
		}

		public void setHouseCheck2(String houseCheck2) {
			this.houseCheck2 = houseCheck2;
		}

		public String getHouseOwner() {
			return houseOwner;
		}

		public void setHouseOwner(String houseOwner) {
			this.houseOwner = houseOwner;
		}

		public String getHouseOwner2() {
			return houseOwner2;
		}

		public void setHouseOwner2(String houseOwner2) {
			this.houseOwner2 = houseOwner2;
		}

		public String getHousePropId() {
			return housePropId;
		}

		public void setHousePropId(String housePropId) {
			this.housePropId = housePropId;
		}

		public String getHousePropId2() {
			return housePropId2;
		}

		public void setHousePropId2(String housePropId2) {
			this.housePropId2 = housePropId2;
		}

		public String getSjChect() {
			return sjChect;
		}

		public void setSjChect(String sjChect) {
			this.sjChect = sjChect;
		}

		public String getSjMatch() {
			return sjMatch;
		}

		public void setSjMatch(String sjMatch) {
			this.sjMatch = sjMatch;
		}

		public String getSjPayCom() {
			return sjPayCom;
		}

		public void setSjPayCom(String sjPayCom) {
			this.sjPayCom = sjPayCom;
		}

		public String getSjPayStart() {
			return sjPayStart;
		}

		public void setSjPayStart(String sjPayStart) {
			this.sjPayStart = sjPayStart;
		}

		public String getSjPreMonthPay() {
			return sjPreMonthPay;
		}

		public void setSjPreMonthPay(String sjPreMonthPay) {
			this.sjPreMonthPay = sjPreMonthPay;
		}

		public String getSjPreMonthRate() {
			return sjPreMonthRate;
		}

		public void setSjPreMonthRate(String sjPreMonthRate) {
			this.sjPreMonthRate = sjPreMonthRate;
		}

		public String getSjComMonthPay() {
			return sjComMonthPay;
		}

		public void setSjComMonthPay(String sjComMonthPay) {
			this.sjComMonthPay = sjComMonthPay;
		}

		public String getSjComMonthRate() {
			return sjComMonthRate;
		}

		public void setSjComMonthRate(String sjComMonthRate) {
			this.sjComMonthRate = sjComMonthRate;
		}

	}

//	public List<DecisionOrdinaryListBean> getDescA() {
//		return descA;
//	}
//
//	public void setDescA(List<DecisionOrdinaryListBean> descA) {
//		this.descA = descA;
//	}
//
//	public DescB getDescB() {
//		return descB;
//	}
//
//	public void setDescB(DescB descB) {
//		this.descB = descB;
//	}

	public String getHasHouse() {
		return hasHouse;
	}

	public void setHasHouse(String hasHouse) {
		this.hasHouse = hasHouse;
	}

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getMonthIncome() {
		return monthIncome;
	}

	public void setMonthIncome(String monthIncome) {
		this.monthIncome = monthIncome;
	}

	public DescC getDescC() {
		return descC;
	}

	public void setDescC(DescC descC) {
		this.descC = descC;
	}

	public DescD getDescD() {
		return descD;
	}

	public void setDescD(DescD descD) {
		this.descD = descD;
	}

	public List<DescE> getDescE() {
		return descE;
	}

	public void setDescE(List<DescE> descE) {
		this.descE = descE;
	}

	public String getPhotoPosition() {
		return photoPosition;
	}

	public void setPhotoPosition(String photoPosition) {
		this.photoPosition = photoPosition;
	}

	public String getPhotoTime() {
		return photoTime;
	}

	public void setPhotoTime(String photoTime) {
		this.photoTime = photoTime;
	}

}
