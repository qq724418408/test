package com.bocop.xfjr.bean.pretrial;

import java.io.Serializable;

public  class HouseBean  implements Serializable{
    /**
     * housesAddress : 江西省xx市xx区xx路10号对面
     * housesType : 3
     * housesArea : 120
     * validatedStatus : 3
     */
	private int type=-1;
	private boolean isShow=true;
    private String housesAddress;
    private String housesType;
    private String housesArea;
    private String validatedStatus;

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

	public String getHousesAddress() {
        return housesAddress;
    }

    public void setHousesAddress(String housesAddress) {
        this.housesAddress = housesAddress;
    }

    public String getHousesType() {
        return housesType;
    }

    public void setHousesType(String housesType) {
        this.housesType = housesType;
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