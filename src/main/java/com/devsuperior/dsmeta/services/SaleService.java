package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {
	private final LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

    public Page<SaleReportDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate min = validateMinDate(minDate);
		LocalDate max = validateMaxDate(maxDate);

		return repository.getReport(min, max, name, pageable);
	}

	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate min = validateMinDate(minDate);
		LocalDate max = validateMaxDate(maxDate);

		return repository.getSummary(min, max);
	}

	private LocalDate validateMaxDate(String maxDate) {
		LocalDate max;
		try{
			max = LocalDate.parse(maxDate);
		}catch(DateTimeParseException e){
			max = today;
		}
		return max;
	}

	private LocalDate validateMinDate(String minDate){
		LocalDate min;
		try{
			min = LocalDate.parse(minDate);
		}catch(DateTimeParseException e){
			min = today.minusYears(1L);
		}
		return min;
	}


}
