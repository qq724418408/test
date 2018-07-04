package com.bocop.xfjr.bean;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 系统基本信息
 * 
 * @author wujunliu
 *
 */
public class SystemBasicInfo extends RootRsp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CityListBean> cityList; // 地市地区列表
    private List<IndustryListBean> industryList; // 行业单位列表
    private List<PeriodListBean> periodList; // 分期期数列表
    private List<HouseTypeBean> houseTypeList; // 列表
    private List<JobLevelListBean> jobLevelList; // 列表
    private List<PayMethodsListBean> payMethodsList; // 列表
    
	public List<HouseTypeBean> getHouseTypeList() {
		return houseTypeList;
	}

	public void setHouseTypeList(List<HouseTypeBean> houseTypeList) {
		this.houseTypeList = houseTypeList;
	}

	public List<JobLevelListBean> getJobLevelList() {
		return jobLevelList;
	}

	public void setJobLevelList(List<JobLevelListBean> jobLevelList) {
		this.jobLevelList = jobLevelList;
	}

	public List<PayMethodsListBean> getPayMethodsList() {
		return payMethodsList;
	}

	public void setPayMethodsList(List<PayMethodsListBean> payMethodsList) {
		this.payMethodsList = payMethodsList;
	}

	@Override
	public String toString() {
		return "SystemBasicInfo [cityList=" + cityList + ", industryList=" + industryList + ", periodList=" + periodList
				+ ", houseTypeList=" + houseTypeList + ", jobLevelList=" + jobLevelList + ", payMethodsList="
				+ payMethodsList + "]";
	}

	public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public List<IndustryListBean> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<IndustryListBean> industryList) {
        this.industryList = industryList;
    }

    public List<PeriodListBean> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<PeriodListBean> periodList) {
        this.periodList = periodList;
    }

    public static class CityListBean extends RootRsp {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

        private String cityId; // 城市ID
        private String upCity; // 上级城市ID
        private String cityName; // 地市名称
        private String cpLiveAmt; // 已婚最低生活标准
        private String sgLiveAmt; // 单身最低生活标准
        private String houseLoadAmt; // 房贷金额准入标准
        private String houseLoadArea; // 房产面积准入标准
        private List<ChildCityBean> childCity;

        @Override
		public String toString() {
			return cityName;
		}

		public CityListBean() {
			super();
		}
		
		public CityListBean(String cityId, String cityName) {
			super();
			this.cityId = cityId;
			this.cityName = cityName;
		}

		public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getUpCity() {
            return upCity;
        }

        public void setUpCity(String upCity) {
            this.upCity = upCity;
        }

		public String getCpLiveAmt() {
			return cpLiveAmt;
		}

		public void setCpLiveAmt(String cpLiveAmt) {
			this.cpLiveAmt = cpLiveAmt;
		}

		public String getSgLiveAmt() {
			return sgLiveAmt;
		}

		public void setSgLiveAmt(String sgLiveAmt) {
			this.sgLiveAmt = sgLiveAmt;
		}

		public String getHouseLoadAmt() {
			return houseLoadAmt;
		}

		public void setHouseLoadAmt(String houseLoanAmt) {
			this.houseLoadAmt = houseLoanAmt;
		}

		public String getHouseLoadArea() {
			return houseLoadArea;
		}

		public void setHouseLoadArea(String houseLoanArea) {
			this.houseLoadArea = houseLoanArea;
		}

		public List<ChildCityBean> getChildCity() {
			return childCity;
		}

		public void setChildCity(List<ChildCityBean> childCity) {
			this.childCity = childCity;
		}
        
    }
    
    public static class ChildCityBean extends RootRsp {
    	/**
    	 * 
    	 */
    	private static final long serialVersionUID = 1L;
    	
    	private String cityId; // 城市ID
    	private String upCity; // 上级城市ID
    	private String cityName; // 地市名称
    	private String cpLiveAmt; // 已婚最低生活标准
    	private String sgLiveAmt; // 单身最低生活标准
    	private String houseLoadAmt; // 房贷金额准入标准
    	private String houseLoadArea; // 房产面积准入标准
    	
    	@Override
    	public String toString() {
    		return cityName;
    	}
    	
    	public ChildCityBean() {
    		super();
    	}
    	
    	public ChildCityBean(String cityId, String cityName) {
    		super();
    		this.cityId = cityId;
    		this.cityName = cityName;
    	}
    	
    	public String getCityId() {
    		return cityId;
    	}
    	
    	public void setCityId(String cityId) {
    		this.cityId = cityId;
    	}
    	
    	public String getCityName() {
    		return cityName;
    	}
    	
    	public void setCityName(String cityName) {
    		this.cityName = cityName;
    	}
    	
    	public String getUpCity() {
    		return upCity;
    	}
    	
    	public void setUpCity(String upCity) {
    		this.upCity = upCity;
    	}
    	
    	public String getCpLiveAmt() {
    		return cpLiveAmt;
    	}
    	
    	public void setCpLiveAmt(String cpLiveAmt) {
    		this.cpLiveAmt = cpLiveAmt;
    	}
    	
    	public String getSgLiveAmt() {
    		return sgLiveAmt;
    	}
    	
    	public void setSgLiveAmt(String sgLiveAmt) {
    		this.sgLiveAmt = sgLiveAmt;
    	}
    	
    	public String getHouseLoadAmt() {
    		return houseLoadAmt;
    	}
    	
    	public void setHouseLoadAmt(String houseLoanAmt) {
    		this.houseLoadAmt = houseLoanAmt;
    	}
    	
    	public String getHouseLoadArea() {
    		return houseLoadArea;
    	}
    	
    	public void setHouseLoadArea(String houseLoanArea) {
    		this.houseLoadArea = houseLoanArea;
    	}
    	
    }

    public static class IndustryListBean extends RootRsp {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
         * coefficient : .2
         * unitId : 1
         * unitName : 金融
         */
        private String coefficient; // 系数
        private String unitId; // 行业单位ID
        private String unitName; // 行业单位名称

        public IndustryListBean(String unitId, String unitName) {
			super();
			this.unitId = unitId;
			this.unitName = unitName;
		}
        
        public IndustryListBean() {
        	super();
        }

		@Override
		public String toString() {
			return unitName;
		}

		public String getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(String coefficient) {
            this.coefficient = coefficient;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
    }

    public static class PayMethodsListBean extends RootRsp {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String payType;
    	private String payName;
    	private String coefficient;
    	private String authMaxAmt;

		public PayMethodsListBean() {
			super();
		}
		
		public PayMethodsListBean(String payType, String payName) {
			super();
			this.payType = payType;
			this.payName = payName;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getPayName() {
			return payName;
		}

		public void setPayName(String payName) {
			this.payName = payName;
		}

		public String getCoefficient() {
			return coefficient;
		}

		public void setCoefficient(String coefficient) {
			this.coefficient = coefficient;
		}

		public String getAuthMaxAmt() {
			return authMaxAmt;
		}

		public void setAuthMaxAmt(String authMaxAmt) {
			this.authMaxAmt = authMaxAmt;
		}

		@Override
		public String toString() {
			return payName;
		}
    	
    }
    
    public static class JobLevelListBean extends RootRsp {
    
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String jobId;
    	private String jobName;
    	private String coefficient;
    	
		public JobLevelListBean() {
			super();
		}
		
		public JobLevelListBean(String jobId, String jobName) {
			super();
			this.jobId = jobId;
			this.jobName = jobName;
		}

		public String getJobId() {
			return jobId;
		}
		
		public void setJobId(String jobId) {
			this.jobId = jobId;
		}
		
		public String getJobName() {
			return jobName;
		}
		
		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public String getCoefficient() {
			return coefficient;
		}

		public void setCoefficient(String coefficient) {
			this.coefficient = coefficient;
		}

		@Override
		public String toString() {
			return jobName;
		}
    	
    }
    
    public static class HouseTypeBean extends RootRsp {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String housePropId;
    	private String housePropName;
		
    	public HouseTypeBean(String housePropId, String housePropName) {
			super();
			this.housePropId = housePropId;
			this.housePropName = housePropName;
		}
    	
    	public HouseTypeBean() {
    		super();
    	}

		public String getHousePropId() {
			return housePropId;
		}
		
    	public void setHousePropId(String housePropId) {
			this.housePropId = housePropId;
		}
		
    	public String getHousePropName() {
			return housePropName;
		}
		
    	public void setHousePropName(String housePropName) {
			this.housePropName = housePropName;
		}

		@Override
		public String toString() {
			return housePropName;
		}
    	
    }
    
    
    public static class PeriodListBean extends RootRsp {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
         * coefficient : 2.1
         * commonStandart : 8
         * convNum : 1
         * creditStandart : 3
         * periodsId : 1
         * periodsName : er
         */

        private String coefficient; // 期限系数
        private String commonStandart; //  一般人群授信标准
        private String convNum; // 折算期数
        private String creditStandart; // 优质人群授信标准
        private String periodsId; // 分期期数ID
        private String periodsName; // 期数名称

        @Override
		public String toString() {
        	if (periodsName.contains("期")) {
        		return periodsName;
        	} else {
        		return periodsName + "期";
			}
		}

		public PeriodListBean() {
			super();
		}
        
        public PeriodListBean(String periodsId, String periodsName) {
        	super();
        	this.periodsId = periodsId;
        	this.periodsName = periodsName;
        }

		public String getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(String coefficient) {
            this.coefficient = coefficient;
        }

        public String getCommonStandart() {
            return commonStandart;
        }

        public void setCommonStandart(String commonStandart) {
            this.commonStandart = commonStandart;
        }

        public String getConvNum() {
            return convNum;
        }

        public void setConvNum(String convNum) {
            this.convNum = convNum;
        }

        public String getCreditStandart() {
            return creditStandart;
        }

        public void setCreditStandart(String creditStandart) {
            this.creditStandart = creditStandart;
        }

        public String getPeriodsId() {
            return periodsId;
        }

        public void setPeriodsId(String periodsId) {
            this.periodsId = periodsId;
        }

        public String getPeriodsName() {
            return periodsName;
        }

        public void setPeriodsName(String periodsName) {
            this.periodsName = periodsName;
        }
    }
}
