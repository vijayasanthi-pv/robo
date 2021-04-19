package cc.robart.iot.demoproject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
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
import org.mockito.Spy;
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
	
	@Spy
	private FirmwareService firmwareService;
	
	@Mock
	private DomainModelToViewConverter domainModelToViewConverter;
	
	@InjectMocks
	private RobotService robotService;
	
	@Captor
	private ArgumentCaptor<RobotEntity> robotEntityArgumentCaptor;
	
//	@Test
//	@DisplayName("Should list the robots")
//	public void shouldListAllRobots() {
//		List<RobotEntity> robotEntities = new ArrayList<RobotEntity>();
//		robotEntities.add(new RobotEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"),"robot_1",
//								new FirmwareEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"),"firmware1","data1")));
//		robotEntities.add(new RobotEntity(UUID.fromString("bc122001-78e1-1eb9-8178-e16a2ada1111"),"robot_1",
//				new FirmwareEntity(UUID.fromString("bc122001-78e1-1eb9-8178-e16a2ada0001"),"firmware1","data1")));
//		robotEntities.add(new RobotEntity(UUID.fromString("cc122001-78e1-1eb9-8178-e16a2ada1111"),"robot_1",
//				new FirmwareEntity(UUID.fromString("cc122001-78e1-1eb9-8178-e16a2ada0001"),"firmware1","data1")));
//
//		List<Firmware> expectedFirmwares = new ArrayList<Firmware>();
//		expectedFirmwares.add(new Firmware("name1","data1"));
//		expectedFirmwares.add(new Firmware("name2","data2"));
//		expectedFirmwares.add(new Firmware("name3","data3"));
//
//		Mockito.when(robotRepository.findAll())
//		.thenReturn(robotEntities);
//		robotEntities.stream().forEach((robotEntity)->{
//			Mockito.when(domainModelToViewConverter.convert(firmwareEntity, Firmware.class))
//			.thenReturn(robotEntity);
//			Mockito.when(domainModelToViewConverter.convert(robotEntity, Robot.class))
//			.thenReturn(new Robot(robotEntity.getName(), robotEntity.getFirmware()));
//		});
//		List<Robot> actualRobots = robotService.list();
//		assertThat(actualRobots).hasSize(expectedFirmwares.size());
//		assertThat(actualFirmwares).hasSameElementsAs(expectedFirmwares);
//	}

	
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
	
	@Test
	@DisplayName("Should return latest firmware of a given robot")
	public void shouldReturnLatestFirmware() {
		String name = "robot_1";
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111");
		FirmwareEntity firmwareEntity = new FirmwareEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"),"firmware1","data1");
		Firmware firmware = new Firmware("firmware1","data1");
		RobotEntity robotEntity = new RobotEntity(id,name,firmwareEntity);
		Mockito.when(robotRepository.findByName(name))
		.thenReturn(Optional.of(robotEntity));
		Mockito.when(domainModelToViewConverter.convert(robotEntity.getFirmware(), Firmware.class))
		.thenReturn(firmware);
		
		robotService.latestFirmware(name);
		Assertions.assertThat(firmware.getName()).isEqualTo("firmware1");
		Assertions.assertThat(firmware.getData()).isEqualTo("data1");
	}
	
	@Test
	@DisplayName("Should assign the firmware to the list of robots")
	public void shouldAssignFirmwareToRobots() {
		
	}

}
