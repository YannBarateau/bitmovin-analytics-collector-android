package com.bitmovin.analytics

import android.test.mock.MockContext
import com.bitmovin.analytics.data.*
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class EventDataFactoryTest {


    val userIdProvider = mock<UserIdProvider>() {
        on { userId() }.thenReturn("my-user-id")
    }

    @Test
    fun testRetrievesDeviceInformationAndSetsItOnEventData() {

        val deviceInfo = DeviceInformation("Foo", "Bar", "user-agent", "de", "package", 100, 200)


        val myMock = Mockito.mock(DeviceInformationProvider::class.java)
        Mockito.`when`(myMock.getDeviceInformation()).thenReturn(deviceInfo)
        val mockContext = MockContext()
        val eventData: EventData = EventDataFactory(BitmovinAnalyticsConfig(), mockContext, myMock, userIdProvider).build("impression-id")

        assertThat(eventData.deviceInformation.manufacturer).isEqualTo(deviceInfo.manufacturer)
        assertThat(eventData.deviceInformation.model).isEqualTo(deviceInfo.model)
        assertThat(eventData.userAgent).isEqualTo(deviceInfo.userAgent)
        assertThat(eventData.screenHeight).isEqualTo(deviceInfo.screenHeight)
        assertThat(eventData.screenWidth).isEqualTo(deviceInfo.screenWidth)

    }

    @Test
    fun testAssignsDeviceInformationPackageNameAsDomainToEventData() {

        val deviceInfo = DeviceInformation("Foo", "Bar", "user-agent", "de", "package", 100, 200)


        val myMock = Mockito.mock(DeviceInformationProvider::class.java)
        Mockito.`when`(myMock.getDeviceInformation()).thenReturn(deviceInfo)
        val mockContext = MockContext()
        val eventData: EventData = EventDataFactory(BitmovinAnalyticsConfig(), mockContext, myMock, userIdProvider).build("impression-id")

        assertThat(eventData.domain).isEqualTo(deviceInfo.packageName)
    }

    @Test
    fun testRetrievesUserIdFromUserIdProviderAndAssignsToEventData() {
        val deviceInfo = DeviceInformation("Foo", "Bar", "user-agent", "de", "package", 100, 200)


        val deviceInfoMock = Mockito.mock(DeviceInformationProvider::class.java)
        Mockito.`when`(deviceInfoMock.getDeviceInformation()).thenReturn(deviceInfo)

        val mockContext = MockContext()
        val eventData: EventData = EventDataFactory(BitmovinAnalyticsConfig(), mockContext, deviceInfoMock, userIdProvider).build("impression-id")

        assertThat(eventData.userId).isEqualTo("my-user-id")
    }

    @Test
    fun testAssignsCorrectImpressionId() {
        val deviceInfo = DeviceInformation("Foo", "Bar", "user-agent", "de", "package", 100, 200)


        val deviceInfoMock = Mockito.mock(DeviceInformationProvider::class.java)
        Mockito.`when`(deviceInfoMock.getDeviceInformation()).thenReturn(deviceInfo)

        val mockContext = MockContext()
        val impressionId = "impression-id"
        val eventData: EventData = EventDataFactory(BitmovinAnalyticsConfig(), mockContext, deviceInfoMock, userIdProvider).build(impressionId)

        assertThat(eventData.impressionId).isEqualTo(impressionId)
    }
}