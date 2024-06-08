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

    void inject(NewTimeEntryHandler handler);

    void inject(UpdateTimeEntriesHandler handler);

    void inject(GetLastFiveTimeEntriesHandler handler);

    void inject(GetTimeEntriesHandler handler);

    void inject(UpdateTimeEntryHandler handler);
}
