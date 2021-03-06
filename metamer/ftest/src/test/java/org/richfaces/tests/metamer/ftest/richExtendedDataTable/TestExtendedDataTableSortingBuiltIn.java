package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestExtendedDataTableSortingBuiltIn extends ExtendedDataTableSortingTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            getTable().getHeader().sortByName(true);
        }
    };

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/builtInFilteringAndSorting.xhtml";
    }

    @BeforeTest
    public void setUp() {
        super.setBuiltIn(true);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRender(ajaxAction);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRender(ajaxAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlHideAllColumnsAndScroll() {
        super.testShowColumnControlHideAllColumnsAndScroll();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlWithSorting() {
        super.testShowColumnControlWithSorting();
    }

    @Test
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test
    @Skip
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @Skip
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }
}
