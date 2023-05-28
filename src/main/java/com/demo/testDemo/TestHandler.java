package com.demo.testDemo;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TestHandler implements RequestStreamHandler {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static Transaction startTransaction(String queueName, String requestId) {
        Transaction transaction = ElasticApm.startTransaction();
        transaction.setName(queueName);
        transaction.setLabel("APM_LABEL_QNAME", queueName);
        transaction.setLabel("APM_LABEL_REQUEST_ID", requestId);
        return transaction;
    }

    public static void endTransaction(Transaction transaction, long startTime) {
        transaction.setLabel("APM_LABEL_TOTAL_ET", System.currentTimeMillis() - startTime);
        transaction.end();
    }

    public static void main(String[] args) {
  //      String name = handleRequest("Pratiksha Nagoshe");
        String jsonInput = "{\"path\":\"/v1/special_client/findAll\", \"headers\":{\"accept\":\"*/*\", \"connection\":\"keep-alive\", \"content-length\":275, \"content-type\":\"application/json\", \"host\":\"vertica-api-631277299.us-east-1.elb.amazonaws.com\"}, \"httpMethod\":\"POST\", \"body\":\"\"}";
      System.out.println("name");
    }


    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println("Input =====>>>>>>>>>>>>>>>>>"+input);
        Transaction transaction = startTransaction("Spring","Spring_boot_test");
        endTransaction(transaction , System.currentTimeMillis());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(generateResponseALB());
        writer.close();
    }

    public static String generateResponseALB() {
        ObjectNode responseJson = objectMapper.createObjectNode();
        responseJson.put("statusCode", 200);
        responseJson.put("body", "Aws call apm server");
        return responseJson.toString();
    }
}
