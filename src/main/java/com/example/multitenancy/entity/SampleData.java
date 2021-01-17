package com.example.multitenancy.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sample_data")
public class SampleData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "s_name")
	private String name;

	@Column(name = "s_description")
	private String description;

	@Column(name = "created_on", insertable = false, updatable = false)
	private LocalDateTime createdOn;
}