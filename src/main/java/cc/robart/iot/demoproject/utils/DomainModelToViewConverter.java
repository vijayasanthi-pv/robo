package cc.robart.iot.demoproject.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DomainModelToViewConverter {

	private ModelMapper modelMapper;
	
	public DomainModelToViewConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public <T> T convert(Object source, Class<T> destinationType) {
		return modelMapper.map(source, destinationType);
	}
}
