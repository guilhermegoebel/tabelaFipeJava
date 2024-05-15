package service;

import java.util.List;

public interface IConverter {
    <T> T  stringToDTO(String json, Class<T> classe);

    <T> List<T> stringToList(String json, Class<T> elementType);

    <T> List<T> stringToList(String json, Class<T> elementType, String dataType);
}
