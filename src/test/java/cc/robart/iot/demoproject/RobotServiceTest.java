package cc.robart.iot.demoproject;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

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
import org.mockito.junit.jupiter.MockitoExtension;

import cc.robart.iot.demoproject.builder.FirmwareBuilder;
import cc.robart.iot.demoproject.builder.FirmwareEntityBuilder;
import cc.robart.iot.demoproject.builder.RobotBuilder;
import cc.robart.iot.demoproject.builder.RobotEntityBuilder;
import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.dto.Robot;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.FirmwareEntity;
import cc.robart.iot.demoproject.persistent.RobotEntity;
import cc.robart.iot.demoproject.repository.RobotRepository;
import cc.robart.iot.demoproject.service.FirmwareService;
import cc.robart.iot.demoproject.service.RobotService;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

/**
 * Tests for the Robot persistent layer
 * 
 */
@ExtendWith(MockitoExtension.class)
public class RobotServiceTest {


	@Mock private RobotRepository robotRepository;

	@Mock private FirmwareService firmwareService;

	@Mock private DomainModelToViewConverter domainModelToViewConverter;

	@InjectMocks private RobotService robotService;

	@Captor private ArgumentCaptor<RobotEntity> robotEntityArgumentCaptor;

	@Captor private ArgumentCaptor<List<RobotEntity>> robotEntityListArgumentCaptor;


	@Test
	@DisplayName("Should save the robot") 
	public void shouldSaveTheRobot() {
		String name = "robot_1"; 
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"); 
		Firmware firmware = FirmwareBuilder.builder().name("firmware1").data("data1").build(); 
		FirmwareEntity firmwareEntity =  FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"))
				.name("firmware1").data("data1").build();
		Robot robot = RobotBuilder.builder().name(name).firmware(firmware).build(); 
		RobotEntity	robotEntity = RobotEntityBuilder.builder().id(id).name(name).firmwareEntity(firmwareEntity).build();
		Mockito.when(domainModelToViewConverter.convert(robot, RobotEntity.class))
		.thenReturn(robotEntity);

		robotService.create(robot);
		Mockito.verify(robotRepository,Mockito.times(1)).save(robotEntityArgumentCaptor.capture());

		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getId()).isEqualTo(id); 
		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getName()).isEqualTo(name);
		Assertions.assertThat(robotEntityArgumentCaptor.getValue().getFirmware()).isEqualTo(firmwareEntity); 
	}

	@Test
	@DisplayName("Should not save the robot") 
	public void shouldNotSaveTheRobot()	{ 
		String name = "robot_1"; 
		Firmware firmware = FirmwareBuilder.builder().name("firmware1").data("data1").build(); 
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"))
				.name("firmware1").data("data1").build();

		Robot robot = RobotBuilder.builder().name(name).firmware(firmware).build(); 
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"); 
		RobotEntity robotEntity = RobotEntityBuilder.builder().id(id).name(name).firmwareEntity(firmwareEntity).build();
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
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"))
				.name("firmware1").data("data1").build();
		Firmware firmware = FirmwareBuilder.builder().name("firmware1").data("data1").build();
		RobotEntity robotEntity = RobotEntityBuilder.builder().id(id).name(name).firmwareEntity(firmwareEntity).build();
		Mockito.when(robotRepository.findByName(name))
		.thenReturn(Optional.of(robotEntity));
		Mockito.when(domainModelToViewConverter.convert(robotEntity.getFirmware(),
				Firmware.class)) .thenReturn(firmware);

		robotService.latestFirmware(name);
		Assertions.assertThat(firmware.getName()).isEqualTo("firmware1");
		Assertions.assertThat(firmware.getData()).isEqualTo("data1"); 
	}

	@Test
	@DisplayName("Should throw exception if any of the robot doesnot exist and shouldnot assign firmware to any robots") 
	public void shouldNotAssignFirmware() { 
		String name="firmware1"; 
		String robotName = "robot_1";
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"))
				.name(name).data("data1").build();
		doReturn(Optional.of(firmwareEntity)).when(firmwareService).findByName(name);
		doReturn(Optional.empty()).when(robotRepository).findByName(robotName);

		ArrayList<String> robotNames = new ArrayList<String>();
		robotNames.add("robot_1");

		assertThatThrownBy(()->{ robotService.assignFirmware(name, robotNames);
		}).isInstanceOf(NotFoundException.class)
		.hasMessage("Some robots does not exist"); 
	}

	@Test
	@DisplayName("Should Assign the firmware") 
	public void shouldAssignFirmware() { 
		String name="firmware1"; 
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"))
				.name(name).data("data1").build();
		RobotEntity robotEntity = RobotEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"))
				.name("robot_1").firmwareEntity(null).build();

		doReturn(Optional.of(firmwareEntity)).when(firmwareService).findByName(name);

		List<String> robotNames = new ArrayList<>(); robotNames.add("robot_1");

		List<RobotEntity> robotEntities = new ArrayList<>(); 
		robotEntities.add(RobotEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada1111"))
												.name("robot_1").firmwareEntity(firmwareEntity).build());

		robotNames.stream().forEach(roboName->doReturn(Optional.of(robotEntity)).when
				(robotRepository).findByName(roboName));
		doReturn(robotEntities).when(robotRepository).findByNameIn(robotNames);

		robotService.assignFirmware(name, robotNames);

		Mockito.verify(robotRepository,
				Mockito.times(1)).saveAll(robotEntityListArgumentCaptor.capture());
		Assertions.assertThat(robotEntityListArgumentCaptor.getValue().size()).
		isEqualTo(1);
		Assertions.assertThat(robotEntityListArgumentCaptor.getValue().get(0).
				getFirmware().getData()).isEqualTo("data1"); 
	}

}
