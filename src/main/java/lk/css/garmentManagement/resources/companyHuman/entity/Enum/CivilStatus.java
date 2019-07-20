package lk.css.garmentManagement.resources.companyHuman.entity.Enum;

public enum CivilStatus {
    MARRIED("Married"),
    UNMARRIED("Unmarried"),
    WIDOW("Widow");

    private final String civilStatus;

    CivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getCivilStatus() {
        return civilStatus;
    }
}
