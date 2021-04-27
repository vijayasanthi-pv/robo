package cc.robo.iot.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Domain model to view controller
 */
@Component
public class DomainModelToViewConverter {

	private ModelMapper modelMapper;
	
	public DomainModelToViewConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public <T> T convert(Object source, Class<T> destinationType) {
		return modelMapper.map(source, destinationType);
	}
	
	public void convert(Object source, Object destination) {
		modelMapper.map(source, destination);
	}
}
