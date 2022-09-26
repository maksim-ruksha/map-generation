package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.security.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleDto create(RoleDto roleDto);
    RoleDto read(Long roleId);
    RoleDto update(RoleDto roleDto);
    Boolean delete(Long roleId);
    Page<RoleDto> findAll(Pageable pageable);
    RoleDto findById(Long roleId);
    RoleDto findByName(String name);
}
