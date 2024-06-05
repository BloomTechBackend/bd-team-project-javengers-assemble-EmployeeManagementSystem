package org.example.dependency;

import dagger.Component;
import javax.inject.Singleton;

import org.example.lambda.AdminResetPasswordHandler;
import org.example.lambda.LoginHandler;
import org.example.lambda.NewEmployeeHandler;
import org.example.lambda.UpdatePasswordHandler;

@Singleton
@Component(modules = {DaoModule.class})
public interface AppComponent {
    void inject(LoginHandler handler);

    void inject(NewEmployeeHandler handler);

    void inject(UpdatePasswordHandler handler);

    void inject(AdminResetPasswordHandler handler);

    // void inject(EmployeeHandler); TODO: IMPLEMENT EMPLOYEE HANDLER
}
