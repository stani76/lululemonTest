package com.example.lululemonassessment

import com.example.lululemonassessment.database.IGarmentRepository
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.models.Screens
import com.example.lululemonassessment.models.SortOrder
import com.example.lululemonassessment.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

/**
 * No Mock library was used. On a larger project mock libraries can be very helpful.
 */
@ExperimentalCoroutinesApi
class MainViewModelTests {

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val seedGarmentList = listOf(
        Garment(0, "Item 1.3", LocalDateTime.now().minusDays(3)),
        Garment(0, "Item 2.2", LocalDateTime.now().minusDays(2)),
        Garment(0, "Item 3.0", LocalDateTime.now().minusDays(0)),
        Garment(0, "Item 4.1", LocalDateTime.now().minusDays(1))
    )
    private val testGarmentRepo = object : IGarmentRepository {
        var insertedGarment: Garment? = null
        override fun allGarments(): Flow<List<Garment>> = flow { emit(seedGarmentList) }
        override fun insertGarment(garment: Garment) {
            insertedGarment = garment
        }
    }

    @Test
    fun `When currentSort is set Then it is set as expected`() {
        // Arrange
        val vm = MainViewModel(testGarmentRepo)
        val oldSortOrder = vm.currentSort

        // Act
        vm.currentSort = SortOrder.Creation

        // Assert
        assert(oldSortOrder == SortOrder.Alpha) { "currentSort original value was not expected value." }
        assert(vm.currentSort == SortOrder.Creation) { "currentSort value did not change to expected value." }
    }

    @Test
    fun `When currentScreen is set Then it is set as expected`() {
        // Arrange
        val vm = MainViewModel(testGarmentRepo)
        val oldScreen = vm.currentScreen

        // Act
        vm.currentScreen = Screens.Add

        // Assert
        assert(oldScreen == Screens.List) { "currentScreen original value was not expected value." }
        assert(vm.currentScreen == Screens.Add) { "currentScreen value did not change to expected value." }
    }

    @Test
    fun `When newGarmentName is set Then it is set as expected`() {
        // Arrange
        val vm = MainViewModel(testGarmentRepo)
        val oldNewGarmentName = vm.newGarmentName

        // Act
        vm.newGarmentName = "Jeans"

        // Assert
        assert(oldNewGarmentName.isEmpty()) { "newGarmentName original value was not expected value." }
        assert(vm.newGarmentName == "Jeans") { "currentScreen value did not change to expected value." }
    }

    @Test
    fun `When viewModel is initialized Then garmentList is populated correctly`() {
        // Arrange
        val emittedList = mutableListOf<Garment>()
        val vm = MainViewModel(testGarmentRepo)

        // Act
        runBlockingTest {
            val collectJob = launch {
                vm.garmentList.collect {
                    emittedList.addAll(it)
                }
            }
            // Force the collect in the MainViewModel's init to execute again by calling sort
            vm.sortGarments()
            collectJob.cancel()
        }

        // Assert
        assert(emittedList.size == 4) { "Expected the emittedList.count() to be 4, but it was: ${emittedList.count()}" }
        assert(emittedList[0] == seedGarmentList[0]) { "Expected emittedList[0] to match seedGarmentList[0], but it did not. " }
        assert(emittedList[1] == seedGarmentList[1]) { "Expected emittedList[1] to match seedGarmentList[1], but it did not. " }
        assert(emittedList[2] == seedGarmentList[2]) { "Expected emittedList[2] to match seedGarmentList[2], but it did not. " }
        assert(emittedList[3] == seedGarmentList[3]) { "Expected emittedList[3] to match seedGarmentList[3], but it did not. " }
    }

    @Test
    fun `When sortGarments is called with a different value than what is in currentSort Then garmentList is sorted with the new sort order`() {
        // Arrange
        val emittedList = mutableListOf<Garment>()
        val vm = MainViewModel(testGarmentRepo)
        val originalSortOrder = SortOrder.Alpha
        val newSortOrder = SortOrder.Creation
        vm.currentSort = originalSortOrder

        // Act
        runBlockingTest {
            val collectJob = launch {
                vm.garmentList.collect {
                    emittedList.clear()
                    emittedList.addAll(it)
                }
            }
            // Force the collect in the MainViewModel's init to execute again by calling sort
            vm.sortGarments(newSortOrder)
            collectJob.cancel()
        }

        // Assert
        assert(emittedList.count() == 4) { "Expected the emittedList.count() to be 4, but it was: ${emittedList.count()}" }
        assert(emittedList[0] == seedGarmentList[0]) { "Expected emittedList[0] to match seedGarmentList[0], but it did not. " }
        assert(emittedList[1] == seedGarmentList[1]) { "Expected emittedList[1] to match seedGarmentList[1], but it did not. " }
        assert(emittedList[3] == seedGarmentList[2]) { "Expected emittedList[3] to match seedGarmentList[2], but it did not. " }
        assert(emittedList[2] == seedGarmentList[3]) { "Expected emittedList[2] to match seedGarmentList[3], but it did not. " }
    }

    @Test
    fun `When addNewGarment is called with a new garment Then repo insertGarment is called and newGarmentName property is empty`() {
        // Arrange
        val vm = MainViewModel(testGarmentRepo)
        vm.currentSort = SortOrder.Alpha
        val expectedNewGarmentName = "Item 0.1"

        // Act
        vm.newGarmentName = expectedNewGarmentName
        vm.addNewGarment()

        // Assert
        assert(testGarmentRepo.insertedGarment?.title == expectedNewGarmentName) { "Expected the repo to be called with the expectedNewGarmentName value, and it was not." }
        assert(vm.newGarmentName.isEmpty()) { "Expected the newGarmentName property to be empty " }
    }
}
