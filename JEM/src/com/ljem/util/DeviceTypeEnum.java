package com.ljem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName
public class DeviceTypeEnum extends BaseEnum implements Comparable<DeviceTypeEnum> {

	protected static TreeMap<String,DeviceTypeEnum> map = new TreeMap<String,DeviceTypeEnum>();
	protected static TreeMap<Integer,DeviceTypeEnum> idMap = new TreeMap<Integer,DeviceTypeEnum>();

    public static final DeviceTypeEnum PUMP = new DeviceTypeEnum(1, "PUMP","Pump");
    public static final DeviceTypeEnum TABLET = new DeviceTypeEnum(2, "TABLET","Tablet");
    public static final DeviceTypeEnum POS = new DeviceTypeEnum(3, "POS","POS");
    public static final DeviceTypeEnum BEACON = new DeviceTypeEnum(4, "BEACON","Beacon");
    public static final DeviceTypeEnum MOBILE = new DeviceTypeEnum(5, "MOBILE","Mobile");

    protected DeviceTypeEnum() {}
    
    protected DeviceTypeEnum(Integer id, String name, String label) {
    	super(id, name, label);
		map.put(name, this);
		idMap.put(id, this);
    }
	
    @Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DeviceTypeEnum) || obj.hashCode() != hashCode()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int hashCode() {
    	return (getClass().getName() + "." + name).hashCode();
	}

	public static DeviceTypeEnum valueOf(String name) {
		return map.get(name);
	}
	
	public static DeviceTypeEnum valueOf(Integer id) {
		return idMap.get(id);
	}

	public static DeviceTypeEnum[] values() {
		return idMap.values().toArray(new DeviceTypeEnum[idMap.values().size()]);
	}
	
	public static List<DeviceTypeEnum> toList() {
		List<DeviceTypeEnum> list = new ArrayList<DeviceTypeEnum>();
		for (Integer id : idMap.keySet()) {
			DeviceTypeEnum obj = idMap.get(id);
			if (obj != null) list.add(obj);
		}
		return list;
	}

	public int compareTo(DeviceTypeEnum obj) {
		String name1 = name().toUpperCase();
		String name2 = obj.name().toUpperCase();
	    //ascending order
	    return name1.compareTo(name2);
    }
	
}
