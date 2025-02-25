package com.jihad.edunest.web.vms.mapper;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.web.vms.request.RegisterVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserVMMapper {
    Member registerVMtoUser(RegisterVM registerVM);

}
