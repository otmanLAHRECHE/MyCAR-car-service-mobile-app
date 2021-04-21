package co.example.hp.myapplication.ui_register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer firstnameError;
    @Nullable
    private Integer lastnameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer emaiError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer firstnameError,@Nullable Integer lastnameError, @Nullable Integer passwordError, @Nullable Integer emaiError) {
        this.firstnameError = firstnameError;
        this.lastnameError = lastnameError;
        this.passwordError = passwordError;
        this.emaiError = emaiError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid){
        this.firstnameError = null;
        this.lastnameError = null;
        this.passwordError = null;
        this.emaiError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getFirstnameError() {
        return firstnameError;
    }

    @Nullable
    public Integer getLastnameError() {
        return lastnameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getEmaiError() {
        return emaiError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
