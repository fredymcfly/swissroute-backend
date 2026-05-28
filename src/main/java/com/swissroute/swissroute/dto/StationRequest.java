package com.swissroute.swissroute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationRequest {

   private Long stationId;
   private String stationName;
}
