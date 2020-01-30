package com.rcn.order.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor  @AllArgsConstructor
@Table(name = "order_data")
@IdClass(FarmTypeId.class)
public class OrderData {

	@Id
	@Column(name = "farm_id")
	private String farmId;

	@Id
	@Column(name = "start_date_time")
	private Date startOrderDateTime;

	@Column(name = "duration_of_water_flow_reqd")
	private int durationReqd;

	@Column(name = "end_date_time")
	private Date endDateTime;

	@Column(name = "status")
	private String farmReqStatus;

	@Column(name = "mod_date")
	private String timeStampFrm;

}