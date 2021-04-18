package cc.robart.iot.demoproject;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.dto.Robot;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.persistent.FirmwareEntity;
import cc.robart.iot.demoproject.persistent.RobotEntity;
import cc.robart.iot.demoproject.repository.RobotRepository;
import cc.robart.iot.demoproject.service.FirmwareService;
import cc.robart.iot.demoproject.service.RobotService;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

@ExtendWith(MockitoExtension.class)
public class RobotServiceTest {
	
	@Mock
	private RobotRepository robotRepository;
	
	@Mock
	private FirmwareService firmwareService;
	
	@Mock
	private DomainModelToViewConverter domainModelToViewConverter;
	
	@InjectMocks
	private RobotService robotService;
	
	@Captor
	private ArgumentCaptor<RobotEntity> robotEntityArgumentCaptor;
	
	@Test
	@DisplayName("Should save the robot")
	public void shouldSaveTheRobot() {
		String name = "robot_1";
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111");
		Firmware firmware = new Firmware("firmware1","data1");
		FirmwareEntity firmwareEntity = new FirmwareEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"),"firmware1","data1");
		Robot robot = new Robot(name, firmware);
		RobotEntity robotEntity = new RobotEntity(id, name, firmwareEntity);
		Mockito.when(robotRepository.findByName(name))
		.thenReturn(Optional.empty());
		Mockito.when(domainModelToViewConverter.convert(robot, RobotEntity.class))
		.thenReturn(robotEntity);

		robotService.create(robot);

		Mockito.verify(robotRepository, Mockito.times(1)).save(robotEntityArgumentCaptor.capture());

		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getName()).isEqualTo(name);
		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getFirmware()).isEqualTo(firmwareEntity);
	}

	@Test
	@DisplayName("Should not save the robot")
	public void shouldNotSaveTheRobot() {
		String name = "robot_1";
		Firmware firmware = new Firmware("firmware1","data1");
		FirmwareEntity firmwareEntity = new FirmwareEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"),"firmware1","data1");
		Robot robot = new Robot(name,firmware);
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111");
		RobotEntity robotEntity = new RobotEntity(id,name,firmwareEntity);
		Mockito.when(robotRepository.findByName(name))
		.thenReturn(Optional.of(robotEntity));

		assertThatThrownBy(()->{
			robotService.create(robot);
		}).isInstanceOf(EntityAlreadyExistsException.class)
		.hasMessage("Robot already exist");
	}
	
//	@Test
//	@DisplayName("Should return latest firmware of a given robot")
//	public void shouldReturnLatestFirmware() {
//		String name = "robot_1";
//	}

}
