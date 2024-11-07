package com.itwillbs.bookjuk.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;

public class PaymentDTO {
	
	private String merchantMid; //주문번호
    private String paymentId; //결제ID
    private String paymentMethod; //결제수단
	private Long paidAmount; //결제금액
    private PaymentStatus status; //결제상태
    
    private Long userNum; //유저번호
    private String userName; //유저이름
    
    private String priceName; //결제품목
    private LocalDateTime reqDate; //요청일시

	public PaymentDTO() {
		// TODO Auto-generated constructor stub
	}
    
    public PaymentDTO(String merchantMid, String paymentId, Long paidAmount, String paymentMethod, PaymentStatus status, Long userNum, String userName, String priceName) {
        this.merchantMid = merchantMid;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paidAmount = paidAmount;
        this.status = status;
        this.userNum = userNum;
        this.userName = userName;
        this.priceName = priceName;
    }

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getMerchantMid() {
		return merchantMid;
	}

	public void setMerchantMid(String merchantMid) {
		this.merchantMid = merchantMid;
	}

   public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Long getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Long paidAmount) {
		this.paidAmount = paidAmount;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
	
    public LocalDateTime getReqDate() {
		return reqDate;
	}

	public void setReq_date(LocalDateTime req_date) {
		this.reqDate = req_date;
	}
    
	  // toString method
    @Override
    public String toString() {
    	return "PaymentDTO{" +
				"merchantMid='" + merchantMid + '\'' +
				", paymentId='" + paymentId + '\'' +
				", paymentMethod=" + paymentMethod +
				", paidAmount=" + paidAmount +
				", status=" + status +
				", userNum=" + userNum +
				", priceName='" + priceName + '\'' +
				'}';
	}
//
// // equals and hashCode methods
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PaymentDTO that = (PaymentDTO) o;
//        return paidAmount == that.paidAmount &&
//                Objects.equals(merchantMid, that.merchantMid) &&
//                Objects.equals(paymentId, that.paymentId) &&
//              //  status == that.status &&
//                Objects.equals(priceName, that.priceName) &&
//                Objects.equals(userNum, that.userNum);
//    }
//
//    @Override
//    public int hashCode() {
//    	return Objects.hash(merchantMid, paymentId, paidAmount, priceName, userNum);
//    	// return Objects.hash(merchantMid, paymentId, paidAmount, status, priceName, userNum);
//    }

}
