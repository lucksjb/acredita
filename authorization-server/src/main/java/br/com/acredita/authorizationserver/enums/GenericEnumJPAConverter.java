package br.com.acredita.authorizationserver.enums;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// MATERIAL QUE UTILIZEI PARA CRIAR 
// 
// https://www.baeldung.com/jpa-persisting-enums-in-jpa
// https://gist.github.com/lucksjb/f95c6d52b947bd4d789360dad48769cd

@Converter(autoApply = true)
public abstract class GenericEnumJPAConverter<T extends Enum<T> & PersistableEnum<E>,E> implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    
    public GenericEnumJPAConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T enumerate) {
        return enumerate != null ? enumerate.getCode() : null;
    }

    @Override
    public T convertToEntityAttribute(E enumerate) {
        if(enumerate == null) {
            return null;
        }

       T[] enums = clazz.getEnumConstants();
        return Arrays.asList(enums)
            .stream()
            .filter(p -> p.toString().equals(enumerate))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
