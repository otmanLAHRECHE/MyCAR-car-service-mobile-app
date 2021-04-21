package co.example.hp.myapplication.ui_register;

import androidx.annotation.Nullable;

public class RegisterResult {

    @Nullable
    private RegistredUserView success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable RegistredUserView success) {
        this.success = success;
    }

    @Nullable
    public RegistredUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
