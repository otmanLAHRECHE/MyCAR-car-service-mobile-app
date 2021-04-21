package co.example.hp.myapplication.ui_register;

public class RegistredUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    RegistredUserView(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
