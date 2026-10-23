package pe.edu.upc.calculator.features.calculator.infrastructure

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.calculator.features.calculator.domain.HistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalculatorRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        implementation: HistoryRepositoryImpl
    ): HistoryRepository
}