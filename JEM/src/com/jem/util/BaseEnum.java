package com.jem.util;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
 
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="@type")
@JsonSubTypes({
      @JsonSubTypes.Type(DeviceTypeEnum.class)
 
  })
public abstract class BaseEnum {
                @JsonProperty
                protected Integer id;
                @JsonProperty
                protected String name;
                @JsonProperty
                protected String label;
    protected BaseEnum(){}
                protected BaseEnum(Integer id, String name, String label) {
                                this.id = id;
                                this.name = name;
                                this.label = label;
                }
                public Integer id() {
                                return id;
                }
                public String name() {
                                return name;
                }
                public String label() {
                                return label;
                }
                protected void setLabel(String label) {
                                this.label = label;
                }
    public abstract boolean equals(Object obj);
                public abstract int hashCode();
}
