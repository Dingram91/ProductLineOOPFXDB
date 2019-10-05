package productionapp;

public enum ItemType {
    Audio ("AU"),
    Visual("VI"),
    AudioMobile("AM"),
    VisualMobile("VM");

    public String code;

    // Constructor
    ItemType(String selection) {
        this.code = selection;
    }
}

