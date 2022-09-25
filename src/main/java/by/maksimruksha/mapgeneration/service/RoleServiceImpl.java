package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.repository.RoleRepository;
import by.maksimruksha.mapgeneration.api.service.RoleService;
import by.maksimruksha.mapgeneration.exceptions.ResourceNotFoundException;
import by.maksimruksha.mapgeneration.security.dto.RoleDto;
import by.maksimruksha.mapgeneration.security.entities.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role response = roleRepository.save(mapper.map(roleDto, Role.class));
        return mapper.map(response, RoleDto.class);
    }

    @Override
    public RoleDto read(Long roleId) {
        Role response = roleRepository.findById(roleId).orElseThrow(()->new ResourceNotFoundException("Role " + roleId + " not found."));
        return mapper.map(response, RoleDto.class);
    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        Role role = roleRepository.save(mapper.map(roleDto, Role.class));
        return mapper.map(role, RoleDto.class);
    }

    @Override
    public Boolean delete(Long roleId) {
        if(roleRepository.existsById(roleId))
        {
            roleRepository.deleteById(roleId);
            return true;
        }
        return false;
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable).map(role -> mapper.map(role, RoleDto.class));
    }

    @Override
    public RoleDto findById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role " + roleId + " was ot found"));
        return mapper.map(role, RoleDto.class);
    }

    @Override
    public RoleDto findByName(String name) {
        Role role = roleRepository.findRoleByName(name);
        return mapper.map(role, RoleDto.class);
    }
}
