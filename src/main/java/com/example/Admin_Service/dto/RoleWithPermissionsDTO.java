package com.example.Admin_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleWithPermissionsDTO extends RoleDTO {

    private List<PermissionDTO> permissions;
}
