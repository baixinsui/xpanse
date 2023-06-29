package org.eclipse.xpanse.modules.orchestrator.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.xpanse.modules.models.monitor.enums.MonitorResourceType;
import org.eclipse.xpanse.modules.models.service.deploy.DeployResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceMetricRequestTest {

    DeployResource deployResource = new DeployResource();
    private ResourceMetricRequest test;

    @BeforeEach
    void setUp() {
        test = new ResourceMetricRequest(deployResource, MonitorResourceType.CPU, 0L, 0L, 1, false,
                "user");
    }

    @Test
    void testGetters() {
        assertEquals(MonitorResourceType.CPU, test.getMonitorResourceType());
        assertEquals(0L, test.getFrom());
        assertEquals(0L, test.getTo());
        assertEquals(1, test.getGranularity());
        assertFalse(test.isOnlyLastKnownMetric());
        assertEquals("user", test.getXpanseUserName());
        assertEquals(deployResource, test.getDeployResource());
    }

    @Test
    void testEqualsAndHashCode() {
        assertEquals(test, test);
        assertNotEquals(test.hashCode(), 0);

        Object object = new Object();
        assertNotEquals(test, object);
        assertNotEquals(test.hashCode(), object.hashCode());

        ResourceMetricRequest test1 =
                new ResourceMetricRequest(deployResource, null, null, null, null, true, null);
        ResourceMetricRequest test2 =
                new ResourceMetricRequest(new DeployResource(), null, null, null, null, true, null);
        ResourceMetricRequest test3 =
                new ResourceMetricRequest(null, null, null, null, null, false, null);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setMonitorResourceType(MonitorResourceType.CPU);
        test2.setMonitorResourceType(MonitorResourceType.MEM);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setFrom(0L);
        test2.setFrom(1L);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setTo(0L);
        test2.setTo(1L);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setGranularity(1);
        test2.setGranularity(300);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setOnlyLastKnownMetric(true);
        test2.setOnlyLastKnownMetric(false);
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());

        test1.setXpanseUserName("user");
        test2.setXpanseUserName("user1");
        assertNotEquals(test, test1);
        assertNotEquals(test, test2);
        assertNotEquals(test, test3);
        assertNotEquals(test1, test2);
        assertNotEquals(test2, test3);
        assertNotEquals(test.hashCode(), test1.hashCode());
        assertNotEquals(test2.hashCode(), test3.hashCode());
    }

    @Test
    void testToString() {
        assertNotEquals(test.toString(), null);

        String exceptedString = "MetricRequest(monitorResourceType=CPU, from=0, "
                + "to=0, granularity=1, onlyLastKnownMetric=false, xpanseUserName=user)";
        assertEquals(test.toString(), exceptedString);
    }
}