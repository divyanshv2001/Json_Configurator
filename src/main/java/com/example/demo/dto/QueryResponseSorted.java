package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record QueryResponseSorted(List<Map<String, Object>> sortedRecords) {}
