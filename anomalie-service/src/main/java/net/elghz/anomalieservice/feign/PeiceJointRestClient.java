package net.elghz.anomalieservice.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PIECES-JOINTES" , url = "https://localhost:8088")
public interface PeiceJointRestClient {





}
