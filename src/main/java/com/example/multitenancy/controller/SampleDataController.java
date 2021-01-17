package com.example.multitenancy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.multitenancy.annotation.TenantSpecific;
import com.example.multitenancy.entity.SampleData;
import com.example.multitenancy.repository.SampleDataRepository;

@RestController
@RequestMapping("/sampledata")
public class SampleDataController {

	@Autowired
	private SampleDataRepository sampleDataRepository;

	@TenantSpecific
	@GetMapping
	public ResponseEntity<List<SampleData>> getSampleData() {
		return ResponseEntity.ok(sampleDataRepository.findAll());
	}
}
