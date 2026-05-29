package com.swissroute.swissroute.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationApiResponse {

    private List<StationApiRequest> stations;


}
