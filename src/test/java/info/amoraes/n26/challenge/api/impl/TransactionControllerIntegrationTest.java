package info.amoraes.n26.challenge.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.services.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCanCreateTransaction() throws Exception {
        final Long now = OffsetDateTime.now().toInstant().toEpochMilli();
        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(Transaction.builder().amount(1D).timestamp(now).build())))
                .andExpect(status().isCreated());

        mvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    public void testCannotCreateOldTransactions() throws Exception {
        final Long now = OffsetDateTime.now().toInstant().toEpochMilli()
                - (60 * 1000 + 1);
        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(Transaction.builder().amount(1D).timestamp(now).build())))
                .andExpect(status().isNoContent());
    }

}
