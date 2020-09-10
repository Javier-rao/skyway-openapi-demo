package org.seirobotics.skyway.openapidemo;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Javier
 * @date 2020/9/9 - 12:14
 */
@SuppressWarnings("unused")
@Component
class OpenApiHelper {
    /**
     * api key is the tenant identification
     *
     * TODO : Replace the API KEY as you want
     */
    private static final String API_KEY = "[REPLACE YOUR API KEY]";

    /**
     * api secret is very important. Ensure that the secret will not be leaked
     * Please do not add the secret to the request header
     *
     * TODO : Replace the API secret as you want
     */
    private static final String API_SECRET = "[REPLACE YOUR API SECRET]";


    private static Logger logger = LoggerFactory.getLogger(OpenApiHelper.class);

    /**
     * This method is use for generation the request signature.
     * Firstly splicing parameters according to signature rules
     * Then use the API secret and HMAC-SHA256 to encryption the signature source string and generation the correct signature
     *
     * @param requestMethod http request method (GET / POST / PUT / DELETE),must be capital letters
     * @param api           open api url without parameters. such as "http://localhost/OPEN-GATEWAY/device/query"
     * @param parameters    request parameters. For GET method it is the part after the ? . Such as "pageNum=1&pageSize=10"
     * @param random        16 character random string
     * @param timestamp     UTC time, millisecond
     * @return signature
     */
    private String signatureGenerator(String requestMethod, String api, String parameters, String random, long timestamp) {
        String signatureSrc = "[" + requestMethod + "," + api + "," + parameters + "," + timestamp + "," + random + "]";
        return HMACSHA256.sha256_HMAC(signatureSrc, API_SECRET);
    }

    /**
     * Generation http get request
     *
     * @param api       open api url. Such as "http://localhost/gateway/device/query"
     * @param parameter api parameter. Such as "pageNum=1&pageSize=10".
     * @return http get
     */
    HttpGet httpGetGenerator(String api, String parameter) {
        // generator 16 character random string
        String random = RandomHelper.getRandomString(16);
        long timestamp = System.currentTimeMillis();
        String method = "GET";

        String signature = signatureGenerator(method, api, parameter, random, timestamp);

        String requestUrl = api + "?" + parameter;
        logger.info("request url : {}", requestUrl);

        HttpGet httpGet = new HttpGet(requestUrl);
        setHeader(httpGet, timestamp, signature, random);

        return httpGet;
    }

    /**
     * Generation http post request
     *
     * @param api       open api url. Such as "http://localhost/gateway/device/query"
     * @param parameter api parameter. Such as json string
     * @return http post
     */
    HttpPost httpPostGenerator(String api, String parameter) {
        // generator 16 character random string
        String random = RandomHelper.getRandomString(16);
        long timestamp = System.currentTimeMillis();
        String method = "POST";

        String signature = signatureGenerator(method, api, parameter, random, timestamp);

        HttpPost httpPost = new HttpPost(api);
        setHeader(httpPost, timestamp, signature, random);
        httpPost.setEntity(new StringEntity(parameter, "UTF-8"));

        return httpPost;
    }

    /**
     * The request header must include apikey / timestamp / sign / random
     * apikey is the tenant identification
     * timestamp is generate by yourself
     * sign is generate by signatureGenerator()
     * random is generate by yourself
     */
    private void setHeader(HttpUriRequest request, long timestamp, String signature, String random) {
        request.setHeader("apikey", API_KEY);
        request.setHeader("timestamp", String.valueOf(timestamp));
        request.setHeader("sign", signature);
        request.setHeader("random", random);
        request.setHeader("Content-Type", "application/json");

        logger.info("apikey:{}", API_KEY);
        logger.info("timestamp:{}", timestamp);
        logger.info("sign:{}", signature);
        logger.info("random:{}", random);
    }
}