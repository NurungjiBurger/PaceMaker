package com.maker.pacemaker.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.BootBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule { // abstract를 제거하고 비추상 클래스

    @Provides
    @Singleton
    fun provideBaseViewModel(auth: FirebaseAuth): BaseViewModel {
        return BaseViewModel(auth) // BaseViewModel 생성
    }

    @Provides
    fun provideMainBaseViewModel(baseViewModel: BaseViewModel): MainBaseViewModel {
        return MainBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

    @Provides
    fun provideSettingBaseViewModel(baseViewModel: BaseViewModel): SettingBaseViewModel {
        return SettingBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

    @Provides
    fun provideSignInBaseViewModel(baseViewModel: BaseViewModel): SignInBaseViewModel {
        return SignInBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

    @Provides
    fun provideSignUpBaseViewModel(baseViewModel: BaseViewModel): SignUpBaseViewModel {
        return SignUpBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

    @Provides
    fun provideBootBaseViewModel(baseViewModel: BaseViewModel): BootBaseViewModel {
        return BootBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

}