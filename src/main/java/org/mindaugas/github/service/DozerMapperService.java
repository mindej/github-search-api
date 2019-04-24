package org.mindaugas.github.service;

import org.springframework.stereotype.Service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MappingException;

@Service
public class DozerMapperService {
	Mapper mapper = DozerBeanMapperBuilder.buildDefault();

	public Mapper getMapper() {
		return mapper;
	}

	<T> T map(Object source, Class<T> destinationClass) throws MappingException {
		return this.mapper.map(source, destinationClass);
	}

}
