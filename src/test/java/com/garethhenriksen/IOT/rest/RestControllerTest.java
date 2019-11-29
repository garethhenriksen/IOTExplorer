package com.garethhenriksen.IOT.rest;

import com.garethhenriksen.IOT.controller.IOTController;
import com.garethhenriksen.IOT.dao.PostgresIOTDataAccessService;
import com.garethhenriksen.IOT.model.BusPositionsDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTest {

    @Autowired
    @InjectMocks
    private IOTController controller;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    PostgresIOTDataAccessService IOTDAO;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void getBusPositionsTestSimple() throws Exception {
        assertThat(this.controller.getBusPositions().equals(BusPositionsDTO.class));
    }
}
