package com.ditenun.appditenun.dependency.modules;

import android.app.Application;

import com.ditenun.appditenun.dependency.App;
import com.ditenun.appditenun.function.activity.ListTenunActivity;
import com.ditenun.appditenun.function.activity.ModuleSelectionActivity;
import com.ditenun.appditenun.function.activity.commerce.cart.CartActivity;
import com.ditenun.appditenun.function.activity.commerce.dashboard.DashboardActivity;
import com.ditenun.appditenun.function.activity.DetailMotifActivity;
import com.ditenun.appditenun.function.activity.DetailKristikActivity;
import com.ditenun.appditenun.function.activity.DetailTenunActivity;
import com.ditenun.appditenun.function.activity.DuplicationPreviewActivity;
import com.ditenun.appditenun.function.activity.EditKristikActivity;
import com.ditenun.appditenun.function.activity.EditMotifActivity;
import com.ditenun.appditenun.function.activity.EditShapeSizeKristikActivity;
import com.ditenun.appditenun.function.activity.FaqActivity;
import com.ditenun.appditenun.function.activity.FeedbackActivity;
import com.ditenun.appditenun.function.activity.GenerateKristikActivity;
import com.ditenun.appditenun.function.activity.GenerateMotifActivity;
import com.ditenun.appditenun.function.activity.HomeActivity;
import com.ditenun.appditenun.function.activity.LoginActivity;
import com.ditenun.appditenun.function.activity.RegisterActivity;
import com.ditenun.appditenun.function.activity.SplashscreenActivity;
import com.ditenun.appditenun.function.activity.commerce.catalogue.DetailProductActivity;
import com.ditenun.appditenun.function.activity.commerce.delivery.DeliveryActivity;
import com.ditenun.appditenun.function.activity.commerce.order.list.ListOrderActivity;
import com.ditenun.appditenun.function.activity.commerce.payment.PaymentActivity;
import com.ditenun.appditenun.function.activity.commerce.payment.ReviewPaymentActivity;
import com.ditenun.appditenun.function.activity.commerce.track.TrackActivity;
import com.ditenun.appditenun.function.fragment.BrowseMotifFragment;
import com.ditenun.appditenun.function.fragment.MyMotifFragment;
import com.ditenun.appditenun.function.fragment.NationalMotifFragment;

import dagger.Module;
import dagger.Provides;


@Module(
        includes = {
                APIModule.class,
                DataModule.class
        },
        library = true,
        injects = {
                SplashscreenActivity.class,
                LoginActivity.class,
                RegisterActivity.class,
                FeedbackActivity.class,
                FaqActivity.class,
                HomeActivity.class,
                MyMotifFragment.class,
                NationalMotifFragment.class,
                BrowseMotifFragment.class,
                EditMotifActivity.class,
                GenerateMotifActivity.class,
                DetailMotifActivity.class,
                DetailTenunActivity.class,
                GenerateKristikActivity.class,
                DuplicationPreviewActivity.class,
                DetailKristikActivity.class,
                EditKristikActivity.class,
                EditShapeSizeKristikActivity.class,
                DashboardActivity.class,
                CartActivity.class,
                DetailProductActivity.class,
                PaymentActivity.class,
                DeliveryActivity.class,
                TrackActivity.class,
                ModuleSelectionActivity.class,
                ReviewPaymentActivity.class,
                ListOrderActivity.class,
                ListTenunActivity.class
        }
)

public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }
}
