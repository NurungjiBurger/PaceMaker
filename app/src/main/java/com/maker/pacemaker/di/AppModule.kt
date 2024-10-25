package com.maker.pacemaker.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule { // abstract를 제거하고 비추상 클래스

    @Provides
    @Singleton
    fun provideBaseViewModel(): BaseViewModel {
        return BaseViewModel() // BaseViewModel 생성
    }

    @Provides
    fun provideMainBaseViewModel(baseViewModel: BaseViewModel): MainBaseViewModel {
        return MainBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

    @Provides
    fun provideSettingBaseViewModel(baseViewModel: BaseViewModel): SettingBaseViewModel {
        return SettingBaseViewModel(baseViewModel) // MainBaseViewModel 생성
    }

//    @Binds
//    @Singleton
//    abstract fun bindBaseViewModel(impl: BaseViewModel): BaseViewModel
//
//    @Binds
//    @Singleton
//    abstract fun bindMainBaseViewModel(impl: MainBaseViewModel): MainBaseViewModel

}