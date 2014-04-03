/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.fragment.dataTable;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.TypeResolver;

public abstract class RichFacesDataTable<ROW> implements DataTable<ROW>,
        AdvancedInteractions<RichFacesDataTable.AdvancedDataTableInteractions> {

    @SuppressWarnings("unchecked")
    private final Class<ROW> rowClass = (Class<ROW>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[0];

    @FindBy(css = ".rf-dt-b .rf-dt-r")
    private List<WebElement> tableRows;

    @FindByJQuery(".rf-dt-b .rf-dt-r:eq(0) .rf-dt-c")
    private List<WebElement> firstRowCells;

    @FindBy(css = ".rf-dt-nd > .rf-dt-nd-c")
    private WebElement noData;

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    private final AdvancedDataTableInteractions advancedInteractions = new AdvancedDataTableInteractions();

    @Override
    public ROW getRow(int n) {
        return Graphene.createPageFragment(rowClass, tableRows.get(n));
    }

    @Override
    public ROW getFirstRow() {
        return getRow(0);
    }

    @Override
    public ROW getLastRow() {
        return getRow(advanced().getNumberOfRows() - 1);
    }

    @Override
    public List<ROW> getAllRows() {
        List<ROW> result = new ArrayList<ROW>();
        for (int i = 0; i < advanced().getNumberOfRows(); i++) {
            result.add(getRow(i));
        }
        return result;
    }

    @Override
    public AdvancedDataTableInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataTableInteractions {

        public int getNumberOfColumns() {
            if (!isVisible()) {
                return 0;
            } else {
                return firstRowCells.size();
            }
        }

        public int getNumberOfRows() {
            if (!isVisible()) {
                return 0;
            } else {
                return tableRows.size();
            }
        }

        public boolean isVisible() {
            return new WebElementConditionFactory(root).isVisible().apply(browser);
        }

        public boolean isNoData() {
            return new WebElementConditionFactory(noData).isVisible().apply(browser);
        }

        public WebElement getNoData() {
            return noData;
        }

        public WebElement getCell(int column, int row) {
            return tableRows.get(row).findElement(By.cssSelector(".rf-dt-c:nth-of-type(" + column + ")"));
        }
    }
}
