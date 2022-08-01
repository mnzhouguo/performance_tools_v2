package zg.per.tool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    public Project(String id) {
        this.id = id;
    }

    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    private String type;

    private Employee manager;
}
