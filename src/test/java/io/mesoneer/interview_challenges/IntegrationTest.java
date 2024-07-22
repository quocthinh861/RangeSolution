package io.mesoneer.interview_challenges;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testShouldReturnTrue() throws Exception {

        String requestBody = "{\"value\": \"5\", \"range\": \"[1,10]\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Range.Response rangeResponse = objectMapper.readValue(response, Range.Response.class);

        assert rangeResponse.result == true;
        assert result.getResponse().getStatus() == 200;
    }

    @Test
    public void testShouldReturnFalse() throws Exception {

        String requestBody = "{\"value\": \"11\", \"range\": \"[1,10]\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Range.Response rangeResponse = objectMapper.readValue(response, Range.Response.class);

        assert rangeResponse.result == false;
        assert result.getResponse().getStatus() == 200;
    }

    @Test
    public void testShouldReturnBadRequestWithInvalidRange() throws Exception {

        String requestBody = "{\"value\": \"11\", \"range\": \"[1,10\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Range.Response rangeResponse = objectMapper.readValue(response, Range.Response.class);

        assert rangeResponse.result == null;
        assert rangeResponse.message.equals("Invalid range string");
        assert result.getResponse().getStatus() == 400;
    }

    @Test
    public void testShouldReturnBadRequestWithInvalidValue() throws Exception {

        String requestBody = "{\"value\": \"T\", \"range\": \"[1,10]\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Range.Response rangeResponse = objectMapper.readValue(response, Range.Response.class);

        assert rangeResponse.result == null;
        assert rangeResponse.message.equals("Invalid value");
        assert result.getResponse().getStatus() == 400;
    }

}
