package com.indoormap.framework.model;

import java.io.Serializable;

public class POI implements Serializable {
	private static final long serialVersionUID = 2013111910310001L;

	private String id;

	private Point point;

	private String mapName;

	private String name;

	private int poiMode = 0;

	private String icon;

	private float startVisibilityLevel = 0f;

	private float endVisibilityLevel = 99f;

	public static final int POI_MODE_LABEL = 0;

	public static final int POI_MODE_LETTERING = 1;

	public static final int POI_MODE_ICON = 2;

	public static final String ELEVATOR = "����";

	public static final String ESCALATOR = "����";

	public static final String INFOMATION_DESK = "��ѯ̨";

	public static final String STAIRS = "¥��";

	public static final String TOILET = "����";

	public static final String ENTRANCE_EXIT = "�����";
	
	public static final String GATE = "բ��";
	
	public static final String GUARD = "����";
	
	public static final String ATM = "ATM";
	
	public static final String FIRE = "������";

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return point;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStartVisibilityLevel(float visibilityLevel) {
		this.startVisibilityLevel = visibilityLevel;
	}

	public float getStartVisibilityLevel() {
		return startVisibilityLevel;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setEndVisibilityLevel(float endVisibilityLevel) {
		this.endVisibilityLevel = endVisibilityLevel;
	}

	public float getEndVisibilityLevel() {
		return endVisibilityLevel;
	}

	public void setPoiMode(int poiMode) {
		this.poiMode = poiMode;
	}

	public int getPoiMode() {
		return poiMode;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
}
