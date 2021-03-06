package cc.robo.iot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.robo.iot.builder.FirmwareBuilder;
import cc.robo.iot.builder.FirmwareEntityBuilder;
import cc.robo.iot.dto.Firmware;
import cc.robo.iot.exceptions.EntityAlreadyExistsException;
import cc.robo.iot.exceptions.NotFoundException;
import cc.robo.iot.persistent.FirmwareEntity;
import cc.robo.iot.repository.FirmwareRepository;
import cc.robo.iot.service.FirmwareService;
import cc.robo.iot.utils.DomainModelToViewConverter;

/**
 * Tests the firmware persistent layer
 * 
 */
@ExtendWith(MockitoExtension.class)
public class FirmwareServiceTest {
	
	Logger logger = LoggerFactory.getLogger(FirmwareServiceTest.class);

	@Mock
	private FirmwareRepository firmwareRepository;

	@Mock
	private DomainModelToViewConverter domainModelToViewConverter;

	@InjectMocks
	FirmwareService firmwareService;

	@Captor
	private ArgumentCaptor<FirmwareEntity> firmwareEntityArgumentCaptor;
	
	@Test
	@DisplayName("Should list the firmwares")
	public void shouldListAllFirmwares() {
		List<FirmwareEntity> firmwareEntities = new ArrayList<FirmwareEntity>();
		firmwareEntities.add(FirmwareEntityBuilder.builder().id(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"))
												.name("firmware1").data("data1").build());
		firmwareEntities.add(FirmwareEntityBuilder.builder().id(UUID.fromString("bc122001-78e1-1eb9-8178-e16a2ada0001"))
				.name("firmware2").data("data2").build());

		List<Firmware> expectedFirmwares = new ArrayList<Firmware>();
		expectedFirmwares.add(FirmwareBuilder.builder().name("firmware1").data("data1").build());
		expectedFirmwares.add(FirmwareBuilder.builder().name("firmware2").data("data2").build());
		
		Mockito.when(firmwareRepository.findAll())
		.thenReturn(firmwareEntities);
		firmwareEntities.stream().forEach((firmwareEntity)->{
			Mockito.when(domainModelToViewConverter.convert(firmwareEntity, Firmware.class))
			.thenReturn(FirmwareBuilder.builder().name(firmwareEntity.getName()).data(firmwareEntity.getData()).build());
		});
		List<Firmware> actualFirmwares = firmwareService.list();
		assertThat(actualFirmwares).hasSize(expectedFirmwares.size());
		assertThat(actualFirmwares).hasSameElementsAs(expectedFirmwares);
	}

	@Test
	@DisplayName("Should save the firmware")
	public void shouldSaveTheFirmware() {
		String name = "firmware1";
		Firmware firmware = FirmwareBuilder.builder().name(name).data("data1").build();
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(id)
													.name(name).data("data1").build();
		Mockito.when(firmwareRepository.findByName(name))
													.thenReturn(Optional.empty());
		Mockito.when(domainModelToViewConverter.convert(firmware, FirmwareEntity.class))
													.thenReturn(firmwareEntity);

		firmwareService.create(firmware);

		Mockito.verify(firmwareRepository, Mockito.times(1)).save(firmwareEntityArgumentCaptor.capture());

		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getName()).isEqualTo(name);
	}

	@Test
	@DisplayName("Should not save the firmware")
	public void shouldNotSaveTheFirmware() {
		String name = "firmware1";
		Firmware firmware = FirmwareBuilder.builder().name(name).data("data1").build();
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(id)
													.name(name).data("data1").build();
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(firmwareEntity));

		assertThatThrownBy(()->{
			firmwareService.create(firmware);
		}).isInstanceOf(EntityAlreadyExistsException.class)
		.hasMessage("Firmware already exist");
	}

	@Test
	@DisplayName("Should delete the firmware")
	public void shouldDeleteTheFirmware() {
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		String name = "firmware1";
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(id)
														.name(name).data("data1").build();
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(firmwareEntity));

		firmwareService.delete(name);
		Mockito.verify(firmwareRepository, Mockito.times(1)).delete(firmwareEntityArgumentCaptor.capture());

		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getName()).isEqualTo(name);
	}

	@Test
	@DisplayName("Should throw exception when firmware does not exists")
	public void shouldThrowExceptionIfNoFirmware_delete() {
		String name = "firmware1";
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.empty());

		assertThatThrownBy(()->{
			firmwareService.delete(name);
		}).isInstanceOf(NotFoundException.class)
		.hasMessage("Firmware with the name "+name+" doesn't exist");
	}

	@Test
	@DisplayName("Should update firmware data")
	public void shouldUpdateFirmwareData() {
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		String name = "firmware1";
		Firmware firmware = FirmwareBuilder.builder().name(name).data("data2").build();
		FirmwareEntity existingFirmwareEntity = FirmwareEntityBuilder.builder().id(id)
																.name(name).data("data1").build();
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doNothing().when(domainModelToViewConverter).convert(firmware, existingFirmwareEntity);
		
		firmwareService.update(name, firmware);
		existingFirmwareEntity.setData("data2");

		Mockito.verify(firmwareRepository, Mockito.times(1)).save(firmwareEntityArgumentCaptor.capture());
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getName()).isEqualTo(name);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getData()).isEqualTo("data2");
	}
	
	@Test
	@DisplayName("Should update firmware name")
	public void shouldUpdateFirmwareName() {
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		String name = "firmware1";
		String changedName = "firmware2";
		Firmware firmware = FirmwareBuilder.builder().name(changedName).data("data1").build();
		FirmwareEntity existingFirmwareEntity = FirmwareEntityBuilder.builder().id(id)
															.name(name).data("data1").build();
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doNothing().when(domainModelToViewConverter).convert(firmware, existingFirmwareEntity);
		
		firmwareService.update(name, firmware);
		existingFirmwareEntity.setName(changedName);
		
		Mockito.verify(firmwareRepository, Mockito.times(1)).save(firmwareEntityArgumentCaptor.capture());
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getName()).isEqualTo(changedName);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getData()).isEqualTo("data1");
	}

	
	@Test
	@DisplayName("Should update both firmware name and data")
	public void shouldUpdateFirmwareNameData() {
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		String name = "firmware1";
		String changedName = "firmware2";
		Firmware firmware = FirmwareBuilder.builder().name(changedName).data("data2").build();
		FirmwareEntity existingFirmwareEntity = FirmwareEntityBuilder.builder().id(id)
															.name(name).data("data1").build();
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doNothing().when(domainModelToViewConverter).convert(firmware, existingFirmwareEntity);
		
		firmwareService.update(name, firmware);
		existingFirmwareEntity.setName(changedName);
		existingFirmwareEntity.setData("data2");

		Mockito.verify(firmwareRepository, Mockito.times(1)).save(firmwareEntityArgumentCaptor.capture());
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getId()).isEqualTo(id);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getName()).isEqualTo(changedName);
		Assertions.assertThat(firmwareEntityArgumentCaptor.getValue().getData()).isEqualTo("data2");
	}

	
	@Test
	@DisplayName("Should throw exception if firmware doesnot exist")
	public void shouldThrowExceptionIfNoFirmware_update() {
		String name = "firmware1";
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->{
			firmwareService.update(name,FirmwareBuilder.builder().name(name).data("data2").build());
		}).isInstanceOf(NotFoundException.class)
		.hasMessage("Firmware with the name "+name+" does not exist");
	}
	
	@Test
	@DisplayName("Should return the firmware Entity")
	public void shouldReturnFirmwareEntity() {
		String name = "firmware1";
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = FirmwareEntityBuilder.builder().id(id)
																.name(name).data("data1").build();
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(firmwareEntity));
		
		firmwareService.findByName(name);
		Assertions.assertThat(firmwareEntity.getName()).isEqualTo(name);
		Assertions.assertThat(firmwareEntity.getId()).isEqualTo(id);
	}

}
