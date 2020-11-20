package com.robosolutions.temixtopsmarket.di

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.robosolutions.temixtopsmarket.database.AppDatabase
import com.robosolutions.temixtopsmarket.utils.LifecycleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
class FragmentModules {
    @Provides
    fun provideBarcodeScanner(): BarcodeScanner {
        val barcodeOption = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        return BarcodeScanning.getClient(barcodeOption)
    }

    @Provides
    fun provideLifecycleExoPlayer(@ApplicationContext context: Context) =
        SimpleExoPlayer.Builder(context)
            .build()
            .apply { repeatMode = SimpleExoPlayer.REPEAT_MODE_ONE }
            .run { LifecycleExoPlayer(this) }
}

@Module
@InstallIn(ApplicationComponent::class)
class AppModules {
    @Provides
    fun provideStaffDao(database: AppDatabase) = database.staffDao

    @Provides
    fun provideVideoDao(database: AppDatabase) = database.videoDao

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.create(context)
}