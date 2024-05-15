package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Converter implements IConverter{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T stringToDTO(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> stringToList(String json, Class<T> elementType, String dataType) {
        try {
            List<Map<String, Object>> dataList;

            if (dataType != null) {
                Map<String, Object> dataMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
                if (dataMap.containsKey(dataType)) {
                    Object data = dataMap.get(dataType);
                    dataList = objectMapper.convertValue(data, new TypeReference<List<Map<String, Object>>>() {});
                } else {
                    return Collections.emptyList();
                }
            } else {
                dataList = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            }

            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return objectMapper.convertValue(dataList, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> stringToList(String json, Class<T> elementType) {
        return stringToList(json, elementType, null);
    }
}
