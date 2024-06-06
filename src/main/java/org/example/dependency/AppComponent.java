package org.example.dependency;

import dagger.Component;
import javax.inject.Singleton;

import org.example.lambda.*;

@Singleton
@Component(modules = {DaoModule.class})
public interface AppComponent {
    void inject(LoginHandler handler);

    void inject(NewEmployeeHandler handler);

    void inject(UpdatePasswordHandler handler);

    void inject(AdminResetPasswordHandler handler);

    void inject(GetEmployeeHandler handler);

    void inject(GetAllEmployeesHandler handler);

    void inject(UpdateEmployeeHandler handler);
}
