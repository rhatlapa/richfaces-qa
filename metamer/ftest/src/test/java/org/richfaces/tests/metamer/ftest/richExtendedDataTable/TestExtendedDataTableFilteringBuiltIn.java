/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.popupPanel.PopupPanel.ResizerLocation;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestExtendedDataTableFilteringBuiltIn extends ExtendedDataTableFilteringTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            getTable().getHeader().filterNameBuiltIn("a");
        }
    };

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/builtInFilteringAndSorting.xhtml";
    }

    @BeforeMethod(dependsOnMethods = { "loadPage", "setup" })
    public void resizePopupTemplate() {
        if (isInPopupTemplate()) {
            popupTemplate.advanced().resizeFromLocation(ResizerLocation.SE, 50, 50);
        }
    }

    @Test
    @CoversAttributes("filterVar")
    public void testCombination() {
        super.testFilterCombinations(true);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(ajaxAction);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterName() {
        super.testFilterName(true);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14150")
    public void testFilterNameBuiltInAppliesAfterEnterPressed() {
        super.testFilterNameBuiltInAppliesAfterEnterPressed();
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterTitle() {
        super.testFilterTitle(true);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRender(ajaxAction);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testNumberOfKids() {
        super.testFilterNumberOfKindsBuiltIn();
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent("onbeforedomupdate", ajaxAction);
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent("oncomplete", ajaxAction);
    }

    @Test
    @CoversAttributes("onready")
    public void testOnready() {
        // after page re-render
        testFireEvent("onready", new Action() {
            @Override
            public void perform() {
                getMetamerPage().rerenderAll();
            }
        });
        // after full page refresh
        testFireEvent("onready", new Action() {
            @Override
            public void perform() {
                getMetamerPage().fullPageRefresh();
            }
        });
        // after ajax request
        testFireEvent("onready", ajaxAction);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRender(ajaxAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11171")
    public void testScrollingPositionIsPerservedAfterFiltering() {
        final int tolerance = 10;
        // setup frozen columns and decrease the size of the EDT so the table's scroller will be displayed
        attsSetter()
            .setAttribute("frozenColumns").toValue(1)
            .setAttribute("style").toValue("width: 500px !important;")
            .asSingleAction().perform();

        WebElement scroller = getTable().advanced().getRootElement().findElement(By.className("rf-edt-scrl"));
        // get scroller position
        long l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 0, "Default position of the scroller should be 0.");

        // filter by name
        getTable().getHeader().filterName("alex", true);
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 0, tolerance, "Position of the scroller should be same as before filtering.");

        // reset filtering by name
        getTable().getHeader().filterName("", true);
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 0, tolerance, "Position of the scroller should be same as before filtering.");

        // scroll to the last column
        jsUtils.scrollToView(getTable().getFirstRow().getNumberOfKids1ColumnElement());
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 200, tolerance, "Position of the scroller should be around maximum -- 200px.");

        // filter by number of kids
        getTable().getHeader().filterNumberOfKidsBuiltIn(2);
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 200, tolerance, "Position of the scroller should be same as before filtering.");

        // scroll to the second column
        jsUtils.scrollToView(getTable().getFirstRow().getNameColumnElement());
        jsUtils.scrollToView(getTable().advanced().getHeaderElement());
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 150, tolerance, "Position of the scroller should be around 150 px.");

        // filter by title
        getTable().getHeader().filterTitle("Director", true);
        l = Long.parseLong(Utils.returningJQ("scrollLeft()", scroller));
        assertEquals(l, 150, tolerance, "Position of the scroller should be same as before filtering.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlHideAllColumnsAndScroll() {
        super.testShowColumnControlHideAllColumnsAndScroll();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlWithFiltering() {
        super.testShowColumnControlWithFiltering(true);
    }
}
