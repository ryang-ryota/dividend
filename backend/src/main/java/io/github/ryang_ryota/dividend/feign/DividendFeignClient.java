package io.github.ryang_ryota.dividend.feign;

import io.github.ryang_ryota.dividend.model.DividendRawResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "dividendService",
        url = "${spring.cloud.openfeign.client.config.dividendService.url}"
)
public interface DividendFeignClient {
    @GetMapping("/dividend/{code}")
    DividendRawResponse getDividendRaw(@PathVariable("code") String code);
}

