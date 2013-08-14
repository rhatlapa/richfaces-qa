/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.list;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

/**
 * Basic ListComponent implementation.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ListComponentImpl implements ListComponent {

    @Root
    private WebElement root;

    @FindBy(jquery = "> *")
    private List<WebElement> items;

    @Override
    public ListItem getItem(int index) {
        return getItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public ListItem getItem(String text) {
        return getItem(ChoicePickerHelper.byVisibleText().match(text));
    }

    @Override
    public ListItem getItem(ChoicePicker picker) {
        return Graphene.createPageFragment(RichFacesListItem.class, picker.pick(getItems()));
    }

    @Override
    public int size() {
        return getItems().size();
    }

    protected List<WebElement> getItems() {
        return items;
    }

    protected WebElement getRoot() {
        return root;
    }
}
