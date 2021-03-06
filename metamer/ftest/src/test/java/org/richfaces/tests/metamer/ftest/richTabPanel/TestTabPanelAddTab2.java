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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.fragment.switchable.SwitchType.SERVER;
import static org.testng.AssertJUnit.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.switchable.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/addTab2.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestTabPanelAddTab2 extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> tabPanelAttributes = getAttributes();

    @FindBy(xpath = "//div[contains(@id, 'tabPanel')]//*[@class='rf-tab-cnt']")
    private List<WebElement> activeContent;
    @FindByJQuery("td[id*='dynamic'] a")
    private List<WebElement> dynamicHeadersCloseHandler;

    @Page
    private TabPanelSimplePage page;

    private void createAndVerifyTab(WebElement buttonToClick) {
        // create 3 pages
        for (int i = 0; i < 3; i++) {
            if (buttonToClick.getAttribute("name").contains("a4j")) {
                guardAjax(buttonToClick).click();
            } else {
                guardHttp(buttonToClick).click();
            }
            assertEquals(i + 1, dynamicHeadersCloseHandler.size());
        }

    }

    private WebElement getActiveContent() {
        for (WebElement e : activeContent) {
            if (e.isDisplayed()) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/addTab2.xhtml";
    }

    /**
     * Verify that all tabs displays correct content when switch tab
     */
    @Test(groups = "smoke")
    public void testContentOfDynamicTab() {
        int basicTabCount = page.getTabPanel().getNumberOfTabs();

        createAndVerifyTab(page.getCreateTabButtonA4j());
        assertEquals(8, page.getTabPanel().getNumberOfTabs());

        for (int i = 0; i < 3; i++) {
            page.getTabPanel().switchTo(i + basicTabCount);
            assertEquals("Content of dynamicaly created tab" + (basicTabCount + i + 1), getActiveContent().getText());
        }
    }

    /**
     * Create new tab by clicking on a4j:commandButton
     */
    @Test(groups = "smoke")
    public void testCreateTabAjax() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
    }

    /**
     * Create new tab by clicking on h:commandButton
     */
    @Test
    public void testCreateTabJSF() {
        createAndVerifyTab(page.getCreateTabButtonHButton());
    }

    /**
     * Delete newly created tabs
     */
    @Test(groups = "smoke")
    public void testRemoveTab() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 2; i >= 0; i--) {
            guardAjax(dynamicHeadersCloseHandler.get(i)).click();
            assertEquals(i, dynamicHeadersCloseHandler.size());
        }
    }

    @Test
    @CoversAttributes("switchType")
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11081", "https://issues.jboss.org/browse/RF-12945" })
    public void testSwitchTypeAjax() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "ajax");
        page.fullPageRefresh();
        testSwitchTypeNull();
    }

    @Test
    @CoversAttributes("switchType")
    public void testSwitchTypeClient() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "client");
        page.getTabPanel().advanced().setSwitchType(SwitchType.CLIENT);

        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 4; i < 8; i++) {
            List<WebElement> elems = page.getTabPanel().advanced().getAllVisibleHeadersElements();
            WebElement elem = elems.get(i);
            page.getTabPanel().switchTo(i);
            if (i != 4) {
                assertEquals("Content of dynamicaly created tab" + (i + 1), getActiveContent().getText());
            }
        }
    }

    /**
     * Test plan: 1. click on 'create tab' btn 3 time and verify that new tabs appeared 2. verify that switch between newly
     * created tabs still works as in previous tabs (staticaly created) 3. verify a4j ajax btn to create new tabs
     */
    @Test
    @CoversAttributes("switchType")
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11081", "https://issues.jboss.org/browse/RF-12945" })
    public void testSwitchTypeNull() {
        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 4; i < 8; i++) {
            page.getTabPanel().switchTo(i);
            if (i != 4) {
                assertEquals("Content of dynamicaly created tab" + (i + 1), getActiveContent().getText());
            }
        }
    }

    @Test
    @CoversAttributes("switchType")
    @IssueTracking("https://issues.jboss.org/browse/RF-11054")
    public void testSwitchTypeServer() {
        tabPanelAttributes.set(TabPanelAttributes.switchType, "server");
        page.getTabPanel().advanced().setSwitchType(SERVER);

        createAndVerifyTab(page.getCreateTabButtonA4j());
        for (int i = 4; i < 8; i++) {
            page.getTabPanel().switchTo(i);
            if (i != 4) {
                assertEquals("Content of dynamicaly created tab" + (i + 1), getActiveContent().getText());
            }
        }
    }
}
