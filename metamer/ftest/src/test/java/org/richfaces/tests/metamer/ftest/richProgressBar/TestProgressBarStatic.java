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
package org.richfaces.tests.metamer.ftest.richProgressBar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richProgressBar/static.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class TestProgressBarStatic extends AbstractWebDriverTest {

    @Page
    private ProgressBarPage page;
    private final Attributes<ProgressBarAttributes> progressBarAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "richProgressBar/static.xhtml";
    }

    /**
     * @return progress size in %
     */
    private int getProgress() {
        String width = page.getProgressElement().getCssValue("width");
        if (width.contains("%")) {
            return Integer.parseInt(width.replace("%", ""));
        } else {
            float widthInPixels = Float.parseFloat(width.replace("px", "")) / 2.0f;
            // round the decimal number to integer and divide by 2 to obtain per cents because progress bar width is 200px
            return Math.round(widthInPixels);
        }
    }

    @Test
    @CoversAttributes("finishClass")
    @Templates(value = "plain")
    public void testFinishClass() {
        testStyleClass(page.getFinishElement(), BasicAttributes.finishClass);
    }

    @Test(groups = "smoke")
    public void testFinishFacet() {
        attsSetter()
            .setAttribute(ProgressBarAttributes.maxValue).toValue(100)
            .setAttribute(ProgressBarAttributes.value).toValue(100)
            .asSingleAction().perform();

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotVisible(page.getInitialOutputElement(), "Initial output should not be visible on the page.");
        assertVisible(page.getFinishOutputElement(), "Finish output should be visible on the page.");
        assertEquals(page.getFinishOutputElement().getText(), "Finish", "Content of finish facet.");

        assertNotVisible(page.getRemainElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getProgressElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show progress.");

        MetamerPage.waitRequest(page.getFinishFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotVisible(page.getInitialOutputElement(), "Initial output should not be visible on the page.");
        assertNotPresent(page.getFinishOutputElement(), "Finish output should not be present on the page.");

        assertVisible(page.getRemainElement(), "Progress bar should show progress.");
        assertVisible(page.getProgressElement(), "Progress bar should show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show label.");
    }

    @Test
    @CoversAttributes("initialClass")
    @Templates(value = "plain")
    public void testInitialClass() {
        testStyleClass(page.getInitElement(), BasicAttributes.initialClass);
    }

    @Test
    @CoversAttributes("mode")// static
    public void testInitialFacet() {
        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertVisible(page.getInitialOutputElement(), "Initial output should be visible on the page.");
        assertNotVisible(page.getFinishOutputElement(), "Finish output should not be visible on the page.");
        assertEquals(page.getInitialOutputElement().getText(), "Initial", "Content of initial facet.");

        assertNotVisible(page.getRemainElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getProgressElement(), "Progress bar should not show progress.");
        assertNotVisible(page.getLabelElement(), "Progress bar should not show progress.");

        MetamerPage.waitRequest(page.getInitialFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertPresent(page.getProgressBarElement(), "Progress bar is not present on the page.");
        assertVisible(page.getProgressBarElement(), "Progress bar should be visible on the page.");
        assertNotPresent(page.getInitialOutputElement(), "Initial output should not be present on the page.");
        assertNotVisible(page.getFinishOutputElement(), "Finish output should not be visible on the page.");

        assertPresent(page.getRemainElement(), "Progress bar should show progress.");
        assertPresent(page.getProgressElement(), "Progress bar should show progress.");
        assertPresent(page.getLabelElement(), "Progress bar should show progress.");
    }

    @Test
    @CoversAttributes("label")
    @Templates("plain")
    public void testLabel() {
        MetamerPage.waitRequest(page.getInitialFacetRenderedCheckboxElement(), WaitRequestType.HTTP).click();

        assertEquals(page.getLabelElement().getText(), "", "Label when not set.");

        progressBarAttributes.set(ProgressBarAttributes.label, "metamer");
        assertEquals(page.getLabelElement().getText(), "metamer", "Label when set to metamer.");

        MetamerPage.waitRequest(page.getChildrenRenderedCheckboxElement(), WaitRequestType.HTTP).click();
        assertEquals(page.getLabelElement().getText(), "child + metamer",
            "Label when set to metamer and children are rendered too.");
    }

    @Test
    @CoversAttributes("maxValue")
    @Templates("plain")
    public void testMaxValue() {
        attsSetter()
            .setAttribute(ProgressBarAttributes.maxValue).toValue(1000)
            .setAttribute(ProgressBarAttributes.value).toValue(100)
            .asSingleAction().perform();
        assertEquals(getProgress(), 10, "Progress when value=100 and maxValue=1000.");
    }

    @Test
    @CoversAttributes("minValue")
    @Templates("plain")
    public void testMinValue() {
        attsSetter()
            .setAttribute(ProgressBarAttributes.maxValue).toValue(100)
            .setAttribute(ProgressBarAttributes.minValue).toValue(90)
            .setAttribute(ProgressBarAttributes.value).toValue(95)
            .asSingleAction().perform();
        assertEquals(getProgress(), 50, "Progress when value=95 and minValue=90.");
    }

    @Test
    @CoversAttributes("progressClass")
    @Templates(value = "plain")
    public void testProgressClass() {
        testStyleClass(page.getProgressElement(), BasicAttributes.progressClass);
    }

    @Test
    @CoversAttributes("remainingClass")
    @Templates(value = "plain")
    public void testRemainingClass() {
        testStyleClass(page.getRemainElement(), BasicAttributes.remainingClass);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getProgressBarElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getProgressBarElement());
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        attsSetter()
            .setAttribute(ProgressBarAttributes.maxValue).toValue(100)
            .setAttribute(ProgressBarAttributes.value).toValue(0)
            .asSingleAction().perform();
        assertEquals(getProgress(), 0, "Progress when value=0.");

        progressBarAttributes.set(ProgressBarAttributes.value, 37);
        assertEquals(getProgress(), 37, "Progress when value=37.");

        progressBarAttributes.set(ProgressBarAttributes.value, 100);
        assertEquals(getProgress(), 100, "Progress when value=100.");

        progressBarAttributes.set(ProgressBarAttributes.value, -345);
        assertEquals(getProgress(), 0, "Progress when value=-345.");
        assertTrue(page.getInitialOutputElement().isDisplayed(), "Initial output should be visible on the page.");
        assertFalse(page.getFinishOutputElement().isDisplayed(), "Finish output should not be visible on the page.");

        progressBarAttributes.set(ProgressBarAttributes.value, 456);
        assertEquals(getProgress(), 100, "Progress when value=456.");
        assertFalse(page.getInitialOutputElement().isDisplayed(), "Initial output should not be visible on the page.");
        assertTrue(page.getFinishOutputElement().isDisplayed(), "Finish output should be visible on the page.");
    }

    @Test
    @CoversAttributes("style")
    @RegressionTest("https://issues.jboss.org/browse/RF-10969")
    @Templates(value = "plain")
    public void testWidthInStyleInfluencesProgressBarWidth() {
        final int tolerance = 10;
        final int minimalAndDefaultWidth = 200;
        progressBarAttributes.set(ProgressBarAttributes.value, 1);// set some value to show the progress bar

        // get actual width
        int width = Integer.parseInt(Utils.returningJQ("width()", page.getRemainElement()).replaceAll("px", ""));
        // check default width
        assertEquals(width, minimalAndDefaultWidth, tolerance, "The default width of a progress bar should be around " + minimalAndDefaultWidth + " px.");

        for (int testedWidth : new int[] { 500, 100 }) {
            // change width value through @style attribute
            progressBarAttributes.set(ProgressBarAttributes.style, "width: " + testedWidth + "px;");
            // get and check actual width
            width = Integer.parseInt(Utils.returningJQ("width()", page.getRemainElement()).replaceAll("px", ""));
            assertEquals(width, Math.max(testedWidth, minimalAndDefaultWidth), tolerance);
        }
    }
}
