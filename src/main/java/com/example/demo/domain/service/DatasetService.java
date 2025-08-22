package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;

public interface DatasetService {
  Long insertRecord(String datasetName, Map<String, Object> recordMap);
  Map<String, List<Map<String, Object>>> queryGrouped(String datasetName, String groupBy);
  List<Map<String, Object>> querySorted(String datasetName, String sortBy, boolean ascending);
}
