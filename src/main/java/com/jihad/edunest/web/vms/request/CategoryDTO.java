package com.jihad.edunest.web.vms.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Boolean active;
}
