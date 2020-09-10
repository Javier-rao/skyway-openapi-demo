package org.seirobotics.skyway.openapidemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Javier
 * @date 2020/9/9 - 11:54
 */
@RestController
@SuppressWarnings("unused")
public class APIDemoController {

    private static Logger logger = LoggerFactory.getLogger(APIDemoController.class);

    @Resource
    OpenApiHelper helper;

    @GetMapping("/getDeviceList")
    public String getDeviceList() {
        logger.info("*** START API REQUEST");
        // TODO: Replace the API URL and parameter as you want
        HttpGet httpGet = helper.httpGetGenerator("http://localhost/SKYWAY-OPEN-GATEWAY/device/query", "pageNum=1&pageSize=10");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            String responseResult = EntityUtils.toString(responseEntity);
            logger.info("API Response : {}", responseResult);
            logger.info("*** END API REQUEST");
            return responseResult;
        } catch (Exception e) {
            logger.info("*** END API REQUEST");
            return e.getMessage();
        }
    }
}