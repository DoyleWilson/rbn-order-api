package com.rcn.order.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
@Table(name = "farm_data")
public class FarmData {
	@Id
	@Column(name = "farm_id")
	private String farmId;

	@Column(name = "farm_name")
	private String farmName;

	@Column(name = "status")
	private String farmStatus;
}
