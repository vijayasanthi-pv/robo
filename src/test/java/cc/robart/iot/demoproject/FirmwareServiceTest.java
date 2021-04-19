package cc.robart.iot.demoproject;

import static org.assertj.core.api.Assertions.assertThat;
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

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.FirmwareEntity;
import cc.robart.iot.demoproject.repository.FirmwareRepository;
import cc.robart.iot.demoproject.service.FirmwareService;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

@ExtendWith(MockitoExtension.class)
public class FirmwareServiceTest {

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
		firmwareEntities.add(new FirmwareEntity(UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001"),"name1","data1"));
		firmwareEntities.add(new FirmwareEntity(UUID.fromString("bc122001-78e1-1eb9-8178-e16a2ada0001"),"name2","data2"));
		firmwareEntities.add(new FirmwareEntity(UUID.fromString("cc122001-78e1-1eb9-8178-e16a2ada0001"),"name3","data3"));

		List<Firmware> expectedFirmwares = new ArrayList<Firmware>();
		expectedFirmwares.add(new Firmware("name1","data1"));
		expectedFirmwares.add(new Firmware("name2","data2"));
		expectedFirmwares.add(new Firmware("name3","data3"));

		Mockito.when(firmwareRepository.findAll())
		.thenReturn(firmwareEntities);
		firmwareEntities.stream().forEach((firmwareEntity)->{
			Mockito.when(domainModelToViewConverter.convert(firmwareEntity, Firmware.class))
			.thenReturn(new Firmware(firmwareEntity.getName(), firmwareEntity.getData()));
		});
		List<Firmware> actualFirmwares = firmwareService.list();
		assertThat(actualFirmwares).hasSize(expectedFirmwares.size());
		assertThat(actualFirmwares).hasSameElementsAs(expectedFirmwares);
	}

	@Test
	@DisplayName("Should save the firmware")
	public void shouldSaveTheFirmware() {
		String name = "firmware1";
		Firmware firmware = new Firmware(name,"data1");
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = new FirmwareEntity(id,name,"data1");
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
		Firmware firmware = new Firmware(name,"data1");
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = new FirmwareEntity(id,name,"data1");
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
		FirmwareEntity firmwareEntity = new FirmwareEntity(id,name,"data1");
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
		Firmware firmware = new Firmware(name,"data2");
		FirmwareEntity existingFirmwareEntity = new FirmwareEntity(id,name,"data1");
		FirmwareEntity newFirmwareEntity = new FirmwareEntity(id,name,"data2");
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doReturn(newFirmwareEntity).when(domainModelToViewConverter).convert(firmware, FirmwareEntity.class);
		
		firmwareService.update(name, firmware);

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
		Firmware firmware = new Firmware(changedName,"data1");
		FirmwareEntity existingFirmwareEntity = new FirmwareEntity(id,name,"data1");
		FirmwareEntity newFirmwareEntity = new FirmwareEntity(id,changedName,"data1");
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doReturn(newFirmwareEntity).when(domainModelToViewConverter).convert(firmware, FirmwareEntity.class);
		
		firmwareService.update(name, firmware);

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
		Firmware firmware = new Firmware(changedName,"data2");
		FirmwareEntity existingFirmwareEntity = new FirmwareEntity(id,name,"data1");
		FirmwareEntity newFirmwareEntity = new FirmwareEntity(id,changedName,"data2");
		
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(existingFirmwareEntity));
		doReturn(newFirmwareEntity).when(domainModelToViewConverter).convert(firmware, FirmwareEntity.class);
		
		firmwareService.update(name, firmware);

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
			firmwareService.update(name,new Firmware(name,"data2"));
		}).isInstanceOf(NotFoundException.class)
		.hasMessage("Firmware with the name "+name+" doesnot exist");
	}
	
	@Test
	@DisplayName("Should return the firmware Entity")
	public void shouldReturnFirmwareEntity() {
		String name = "firmware1";
		UUID id = UUID.fromString("ac122001-78e1-1eb9-8178-e16a2ada0001");
		FirmwareEntity firmwareEntity = new FirmwareEntity(id,name,"data1");
		Mockito.when(firmwareRepository.findByName(name))
		.thenReturn(Optional.of(firmwareEntity));
		
		firmwareService.findByName(name);
		Assertions.assertThat(firmwareEntity.getName()).isEqualTo(name);
		Assertions.assertThat(firmwareEntity.getId()).isEqualTo(id);
	}

}
