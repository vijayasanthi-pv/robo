package cc.robart.iot.demoproject.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import cc.robart.iot.demoproject.dto.FirmwareDTO;
import cc.robart.iot.demoproject.dto.RobotDTO;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;

@Component
public class DomainModelToViewConverter {

	private ModelMapper modelMapper;
	
	public DomainModelToViewConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public RobotDTO robotToRobotDto(Robot robot) {
		RobotDTO robotDTO = new RobotDTO();

		TypeMap<Robot, RobotDTO> typeMap = modelMapper.getTypeMap(Robot.class, RobotDTO.class);
		if (typeMap == null) { // if not  already added
			modelMapper.addMappings(new PropertyMap<Robot, RobotDTO>() {
				@Override
				protected void configure() {
					map().setFirmwareName(source.getHardwareVersion().getName());
					map().setFirmwareData(source.getHardwareVersion().getData());
				}
			});
		}

		modelMapper.map(robot, robotDTO);
		return robotDTO;
	}

	public FirmwareDTO firmwareToFirmwareDto(Firmware firmware) {
		FirmwareDTO firmwareDTO = modelMapper.map(firmware, FirmwareDTO.class);
		return firmwareDTO;
	}
}
