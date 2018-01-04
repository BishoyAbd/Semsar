
package prod;

import android.content.Context;
import android.support.annotation.NonNull;

import com.projects.cactus.maskn.authentication.model.AuthenticationDataManager;
import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.DataSource;
import com.projects.cactus.maskn.data.local.LocalDataSource;
import com.projects.cactus.maskn.data.remote.RemoteDataSource;
import com.projects.cactus.maskn.profile.ProfileActivity;
import com.projects.cactus.maskn.utils.schedulers.BaseSchedulerProvider;
import com.projects.cactus.maskn.utils.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link DataSource} at compile time.
 */
public class Injection {

    public static ApartmentRepository provideApartmentRepository(@NonNull Context context) {
          checkNotNull(context);
        return ApartmentRepository.getInstance(RemoteDataSource.getInstance(),
        LocalDataSource.getInstance(context));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    public static AuthenticationDataManager provideAuthenticationManager(Context context) {
        checkNotNull(context);
        return AuthenticationDataManager.getInstance(context);
    }
}
