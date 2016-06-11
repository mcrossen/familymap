package com.markcrossen.familymap.model;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;

public class Settings {

    public enum LineColor {
        BLACK("Black", Color.BLACK), GRAY("Gray", Color.GRAY), WHITE("White", Color.WHITE), RED("Red", Color.RED), GREEN("Green", Color.GREEN), BLUE("Blue", Color.BLUE), YELLOW("Yellow", Color.YELLOW), CYAN("Cyan", Color.CYAN), MAGENTA("Magenta", Color.MAGENTA);

        private final int color_code;
        private final String friendly_name;


        LineColor(String friendly_name, int color_code)
        {
            this.friendly_name = friendly_name;
            this.color_code = color_code;
        }

        public int getIndex()
        {
            return ordinal();
        }

        public int getColorCode()
        {
            return color_code;
        }

        @Override
        public String toString()
        {
            return friendly_name;
        }
    }

    public enum MapBackground{
        NORMAL("Normal", GoogleMap.MAP_TYPE_NORMAL), HYBRID("Hybrid", GoogleMap.MAP_TYPE_HYBRID), SATELLITE("Satellite", GoogleMap.MAP_TYPE_SATELLITE), TERRAIN("Terrain", GoogleMap.MAP_TYPE_TERRAIN);

        private final String friendly_name;
        private final int display_code;

        MapBackground(String friendly_name, int display_code)
        {
            this.friendly_name = friendly_name;
            this.display_code = display_code;
        }

        public int getDisplayCode()
        {
            return display_code;
        }

        public int getIndex()
        {
            return ordinal();
        }

        @Override
        public String toString()
        {
            return friendly_name;
        }
    }

    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
    }

    private LineColor LifeLineColor = LineColor.GREEN;
    private LineColor FamilyTreeColor = LineColor.BLUE;
    private LineColor SpouseLineColor = LineColor.RED;
    private MapBackground map_background = MapBackground.NORMAL;
    private boolean isLifeLineEnabled = true;
    private boolean isFamilyTreeEnabled = true;
    private boolean isSpouseLineEnabled = true;

    public LineColor getLifeLineColor() {
        return LifeLineColor;
    }

    public void setLifeLineColor(LineColor lifeLineColor) {
        LifeLineColor = lifeLineColor;
    }

    public LineColor getFamilyTreeColor() {
        return FamilyTreeColor;
    }

    public void setFamilyTreeColor(LineColor familyTreeColor) {
        FamilyTreeColor = familyTreeColor;
    }

    public LineColor getSpouseLineColor() {
        return SpouseLineColor;
    }

    public void setSpouseLineColor(LineColor spouseLineColor) {
        SpouseLineColor = spouseLineColor;
    }

    public MapBackground getMapBackground() {
        return map_background;
    }

    public void setMapBackground(MapBackground map_background) {
        this.map_background = map_background;
    }

    public boolean isLifeLineEnabled() {
        return isLifeLineEnabled;
    }

    public void setLifeLine(boolean isLifeLineEnabled) {
        this.isLifeLineEnabled = isLifeLineEnabled;
    }

    public boolean isFamilyTreeEnabled() {
        return isFamilyTreeEnabled;
    }

    public void setFamilyTree(boolean isFamilyTreeEnabled) {
        this.isFamilyTreeEnabled = isFamilyTreeEnabled;
    }

    public boolean isSpouseLineEnabled() {
        return isSpouseLineEnabled;
    }

    public void setSpouseLine(boolean isSpouseLineEnabled) {
        this.isSpouseLineEnabled = isSpouseLineEnabled;
    }
}
