package account.account.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbtractMapper<E, D> implements Mapper<E, D> {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public D toDto(E entity) {
    return modelMapper.map(entity, getDtoClass());
  }

  @Override
  public E toEntity(D dto) {
    return modelMapper.map(dto, getEntityClass());
  }

  @Override
  public List<D> toDtoList(List<E> entities) {
    if (entities == null || entities.isEmpty()) {
      return Collections.emptyList();
    }
    return entities.stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public List<E> toEntityList(List<D> dtoList) {
    if (dtoList == null || dtoList.isEmpty()) {
      return Collections.emptyList();
    }
    return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
  }
}
