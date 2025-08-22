package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record QueryResponseGrouped(Map<String, List<Map<String, Object>>> groupedRecords) {}
