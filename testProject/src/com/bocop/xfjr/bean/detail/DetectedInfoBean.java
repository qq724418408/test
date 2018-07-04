package com.bocop.xfjr.bean.detail;

import java.io.Serializable;

import com.boc.jx.httptools.network.base.RootRsp;

public class DetectedInfoBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private AssessBean assess;
	private ComtomerInformationBean comtomerInformation;
	private TelephoneInformationBean telephoneInformation;
	private DetectedItem faceInformation;
	private DetectedItem checkRiskInfromation;
	private DetectedItem creditCardInformation;
	private DetectedItem thirdPartyInformation;

	public AssessBean getAssess() {
		return assess;
	}

	public void setAssess(AssessBean assess) {
		this.assess = assess;
	}

	public ComtomerInformationBean getComtomerInformation() {
		return comtomerInformation;
	}

	public void setComtomerInformation(ComtomerInformationBean comtomerInformation) {
		this.comtomerInformation = comtomerInformation;
	}

	public TelephoneInformationBean getTelephoneInformation() {
		return telephoneInformation;
	}

	public void setTelephoneInformation(TelephoneInformationBean telephoneInformation) {
		this.telephoneInformation = telephoneInformation;
	}

	public DetectedItem getFaceInformation() {
		return faceInformation;
	}

	public void setFaceInformation(DetectedItem faceInformation) {
		this.faceInformation = faceInformation;
	}

	public DetectedItem getCheckRiskInfromation() {
		return checkRiskInfromation;
	}

	public void setCheckRiskInfromation(DetectedItem checkRiskInfromation) {
		this.checkRiskInfromation = checkRiskInfromation;
	}

	public DetectedItem getCreditCardInformation() {
		return creditCardInformation;
	}

	public void setCreditCardInformation(DetectedItem creditCardInformation) {
		this.creditCardInformation = creditCardInformation;
	}

	public DetectedItem getThirdPartyInformation() {
		return thirdPartyInformation;
	}

	public void setThirdPartyInformation(DetectedItem thirdPartyInformation) {
		this.thirdPartyInformation = thirdPartyInformation;
	}

	public static class AssessBean extends DetectedItem {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String file1;
		private String file2;

		public String getFile1() {
			return file1;
		}

		public void setFile1(String file1) {
			this.file1 = file1;
		}

		public String getFile2() {
			return file2;
		}

		public void setFile2(String file2) {
			this.file2 = file2;
		}
	}

	public static class ComtomerInformationBean extends DetectedItem {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private String idCard;
		private String image1;
		private String image2;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdCard() {
			return idCard;
		}

		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}

		public String getImage1() {
			return image1;
		}

		public void setImage1(String image1) {
			this.image1 = image1;
		}

		public String getImage2() {
			return image2;
		}

		public void setImage2(String image2) {
			this.image2 = image2;
		}
	}

	public static class TelephoneInformationBean extends DetectedItem {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String telephone;

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
	}

	public static class DetectedItem implements Serializable {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

}
