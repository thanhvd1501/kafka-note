package account.account.mapper;

import java.util.List;

public interface Mapper <E, D>{

  D toDto(E entity);

  E toEntity(D dto);

  List<D> toDtoList(List<E> entities);

  List<E> toEntityList(List<D> dtoList);

  Class<D> getDtoClass();

  Class<E> getEntityClass();

}
