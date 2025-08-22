package com.example.demo.controller;

import com.example.demo.domain.service.DatasetService;
import com.example.demo.dto.InsertRecordResponse;
import com.example.demo.dto.QueryResponseGrouped;
import com.example.demo.dto.QueryResponseSorted;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {

  private final DatasetService service;

  public DatasetController(DatasetService service) {
    this.service = service;
  }

  @PostMapping(
      value = "/{datasetName}/record",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.CREATED)
  public InsertRecordResponse insert(
      @PathVariable String datasetName,
      @RequestBody Map<String, Object> body
  ) {
    Long recordId = service.insertRecord(datasetName, body);
    return new InsertRecordResponse("Record added successfully", datasetName, recordId);
  }

  @GetMapping(
      value = "/{datasetName}/query",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Object query(
      @PathVariable String datasetName,
      @RequestParam(required = false) String groupBy,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String order
  ) {
    if (groupBy != null && !groupBy.isBlank()) {
      Map<String, List<Map<String, Object>>> grouped = service.queryGrouped(datasetName, groupBy);
      return new QueryResponseGrouped(grouped);
    }
    if (sortBy != null && !sortBy.isBlank()) {
      boolean ascending = !"desc".equalsIgnoreCase(order);
      List<Map<String, Object>> sorted = service.querySorted(datasetName, sortBy, ascending);
      return new QueryResponseSorted(sorted);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either groupBy or sortBy");
  }
}
