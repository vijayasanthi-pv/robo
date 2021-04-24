package cc.robart.iot.demoproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cc.robart.iot.demoproject.controller.FirmwareController;
import cc.robart.iot.demoproject.service.FirmwareService;

@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(controllers=FirmwareController.class)
public class FirmwareControllerTest {

	@MockBean
	private FirmwareService firmwareService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@DisplayName("Should lists all firmwares")
	public void shouldListAllFirmwares() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
			   .andExpect(MockMvcResultMatchers.status().is(200));
	}
}
