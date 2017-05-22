package com.six.domain;

import java.util.Date;

public class BagDetail {

	private String detailId; // 记录ID
	private String bagId; // 航班ID
	private String arriveCode; // 到达机场三字码
	private String passengerName; // 旅客姓名
	private String passengerRecord; // 旅客PNR
	private String passengerTicketnum; // 票号
	private String passengerSeattype; // 座位号
	private String credentialsType; // 旅客证件类型
	private String credentialsValue; // 旅客证件编号
	private String cardNum; // 常旅客卡号
	private Date aircraftDate; // 航班日期
	private String departureCode; // 起始机场三字码
	private String aircraftNum; // 航班号
	private String ticketType; // 客票类型
	private String deleteFlag; // 客票类型
	private String coupon; // Coupon
	private String statusId; // 电子票状态码
	private String icsTicketnum; // 订座系统的票号
	private String icsFlightno; // 订座系统的航班号
	private String ticketStatus; // 客票状态
	private String icsDeptCode; // 订座系统的起始机场三字码
	private String icsArrCode; // 订座系统的到达机场三字码
	private String isDuplicate; // 重复票标志位
	private String closeFlag; // 手动关闭标志位
	private Date lmd; // 最后修改时间
	private String lmu; // 最终修改人
	private String lmname; // 最终修改人姓名
	private String icsCarbin; // 订座系统的舱位
	private String originalTicketType; // 票的原始类型
	private Date icsFlightdate; // 订座系统航班日期
	private String interFlg; // 0国内，1国际
	private String isRide; // 空或者0表示未乘行，1表示已乗行
	private String isMultipe; // 标志是否是一人多票
	private String parentId; // 父票号的Id
	private String passengerName2; // 旅客中文名
	private String seat; // 座位号

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getBagId() {
		return bagId;
	}

	public void setBagId(String bagId) {
		this.bagId = bagId;
	}

	public String getArriveCode() {
		return arriveCode;
	}

	public void setArriveCode(String arriveCode) {
		this.arriveCode = arriveCode;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerRecord() {
		return passengerRecord;
	}

	public void setPassengerRecord(String passengerRecord) {
		this.passengerRecord = passengerRecord;
	}

	public String getPassengerTicketnum() {
		return passengerTicketnum;
	}

	public void setPassengerTicketnum(String passengerTicketnum) {
		this.passengerTicketnum = passengerTicketnum;
	}

	public String getPassengerSeattype() {
		return passengerSeattype;
	}

	public void setPassengerSeattype(String passengerSeattype) {
		this.passengerSeattype = passengerSeattype;
	}

	public String getCredentialsType() {
		return credentialsType;
	}

	public void setCredentialsType(String credentialsType) {
		this.credentialsType = credentialsType;
	}

	public String getCredentialsValue() {
		return credentialsValue;
	}

	public void setCredentialsValue(String credentialsValue) {
		this.credentialsValue = credentialsValue;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Date getAircraftDate() {
		return aircraftDate;
	}

	public void setAircraftDate(Date aircraftDate) {
		this.aircraftDate = aircraftDate;
	}

	public String getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}

	public String getAircraftNum() {
		return aircraftNum;
	}

	public void setAircraftNum(String aircraftNum) {
		this.aircraftNum = aircraftNum;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getIcsTicketnum() {
		return icsTicketnum;
	}

	public void setIcsTicketnum(String icsTicketnum) {
		this.icsTicketnum = icsTicketnum;
	}

	public String getIcsFlightno() {
		return icsFlightno;
	}

	public void setIcsFlightno(String icsFlightno) {
		this.icsFlightno = icsFlightno;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getIcsDeptCode() {
		return icsDeptCode;
	}

	public void setIcsDeptCode(String icsDeptCode) {
		this.icsDeptCode = icsDeptCode;
	}

	public String getIcsArrCode() {
		return icsArrCode;
	}

	public void setIcsArrCode(String icsArrCode) {
		this.icsArrCode = icsArrCode;
	}

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}

	public Date getLmd() {
		return lmd;
	}

	public void setLmd(Date lmd) {
		this.lmd = lmd;
	}

	public String getLmu() {
		return lmu;
	}

	public void setLmu(String lmu) {
		this.lmu = lmu;
	}

	public String getLmname() {
		return lmname;
	}

	public void setLmname(String lmname) {
		this.lmname = lmname;
	}

	public String getIcsCarbin() {
		return icsCarbin;
	}

	public void setIcsCarbin(String icsCarbin) {
		this.icsCarbin = icsCarbin;
	}

	public String getOriginalTicketType() {
		return originalTicketType;
	}

	public void setOriginalTicketType(String originalTicketType) {
		this.originalTicketType = originalTicketType;
	}

	public Date getIcsFlightdate() {
		return icsFlightdate;
	}

	public void setIcsFlightdate(Date icsFlightdate) {
		this.icsFlightdate = icsFlightdate;
	}

	public String getInterFlg() {
		return interFlg;
	}

	public void setInterFlg(String interFlg) {
		this.interFlg = interFlg;
	}

	public String getIsRide() {
		return isRide;
	}

	public void setIsRide(String isRide) {
		this.isRide = isRide;
	}

	public String getIsMultipe() {
		return isMultipe;
	}

	public void setIsMultipe(String isMultipe) {
		this.isMultipe = isMultipe;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPassengerName2() {
		return passengerName2;
	}

	public void setPassengerName2(String passengerName2) {
		this.passengerName2 = passengerName2;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	@Override
	public String toString() {
		return "BagDetail [detailId=" + detailId + ", bagId=" + bagId
				+ ", arriveCode=" + arriveCode + ", passengerName="
				+ passengerName + ", passengerRecord=" + passengerRecord
				+ ", passengerTicketnum=" + passengerTicketnum
				+ ", passengerSeattype=" + passengerSeattype
				+ ", credentialsType=" + credentialsType
				+ ", credentialsValue=" + credentialsValue + ", cardNum="
				+ cardNum + ", aircraftDate=" + aircraftDate
				+ ", departureCode=" + departureCode + ", aircraftNum="
				+ aircraftNum + ", ticketType=" + ticketType + ", deleteFlag="
				+ deleteFlag + ", coupon=" + coupon + ", statusId=" + statusId
				+ ", icsTicketnum=" + icsTicketnum + ", icsFlightno="
				+ icsFlightno + ", ticketStatus=" + ticketStatus
				+ ", icsDeptCode=" + icsDeptCode + ", icsArrCode=" + icsArrCode
				+ ", isDuplicate=" + isDuplicate + ", closeFlag=" + closeFlag
				+ ", lmd=" + lmd + ", lmu=" + lmu + ", lmname=" + lmname
				+ ", icsCarbin=" + icsCarbin + ", originalTicketType="
				+ originalTicketType + ", icsFlightdate=" + icsFlightdate
				+ ", interFlg=" + interFlg + ", isRide=" + isRide
				+ ", isMultipe=" + isMultipe + ", parentId=" + parentId
				+ ", passengerName2=" + passengerName2 + ", seat=" + seat + "]";
	}

}
