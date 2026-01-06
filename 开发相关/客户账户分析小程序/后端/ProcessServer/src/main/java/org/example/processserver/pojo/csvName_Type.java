package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class csvName_Type {
    private Integer id;
    private String csvName;
    private String type;
    private String alias;

    public csvName_Type() {
    }

    public csvName_Type(Integer id, String csvName, String type, String alias) {
        this.id = id;
        this.csvName = csvName;
        this.type = type;
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return csvName;
    }

    public void setName(String csvName) {
        this.csvName = csvName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "csvName_Type{" +
                "id=" + id +
                ", csvName='" + csvName + '\'' +
                ", type='" + type + '\'' +
                ", alias='" + alias + '\'' ;
    }
}
