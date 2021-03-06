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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.fragment.calendar.PopupCalendar;
import org.richfaces.fragment.calendar.TimeEditor;
import org.richfaces.fragment.calendar.TimeEditor.SetValueBy;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for time editor of a calendar on page faces/components/richCalendar/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21568 $
 */
public class TestCalendarTimeEditor extends AbstractCalendarTest {

    @Override
    public String getComponentTestPagePath() {
        return "richCalendar/simple.xhtml";
    }

    @Test
    @Templates("plain")
    public void testCancelButton() {
        int plusMinutes = 5;
        setTodaysDate();
        PopupCalendar openedPopup = popupCalendar.openPopup();
        TimeEditor openedTimeEditor = openedPopup.getFooterControls().openTimeEditor();
        openedTimeEditor.setTime(todayMidday.plusMinutes(plusMinutes), SetValueBy.BUTTONS);
        DateTime time1 = openedTimeEditor.getTime();
        assertEquals(time1.getMinuteOfHour(), plusMinutes);

        openedTimeEditor.cancelTime();
        assertFalse(openedTimeEditor.isVisible());
        openedTimeEditor = openedPopup.getFooterControls().openTimeEditor();
        time1 = openedTimeEditor.getTime();
        assertEquals(time1.getHourOfDay(), 12);//default value
        assertEquals(time1.getMinuteOfHour(), 0);//default value
    }

    @Test
    @Templates("plain")
    public void testHoursInputClick() {
        testTimeSet(new int[] { 2, 15 }, Time.hours, SetValueBy.BUTTONS);
    }

    @Test
    @Templates("plain")
    public void testHoursInputType() {
        testTimeSet(new int[] { 2, 15 }, Time.hours, SetValueBy.TYPING);
    }

    @Test
    @Templates("plain")
    public void testMinutesInputClick() {
        testTimeSet(new int[] { 1, 59 }, Time.minutes, SetValueBy.BUTTONS);
    }

    @Test
    @Templates("plain")
    public void testMinutesInputType() {
        testTimeSet(new int[] { 1, 59 }, Time.minutes, SetValueBy.TYPING);
    }

    @Test
    @Templates("plain")
    public void testSecondsInputClick() {
        calendarAttributes.set(CalendarAttributes.datePattern, "MMM d, yyyy HH:mm:ss");
        testTimeSet(new int[] { 1, 59 }, Time.seconds, SetValueBy.BUTTONS);
    }

    @Test
    @Templates("plain")
    public void testSecondsInputType() {
        calendarAttributes.set(CalendarAttributes.datePattern, "MMM d, yyyy HH:mm:ss");
        testTimeSet(new int[] { 1, 59 }, Time.seconds, SetValueBy.TYPING);
    }

    @Test
    @Templates("plain")
    public void testShowTimeEditor() {
        setTodaysDate();
        TimeEditor openedTimeEditor = popupCalendar.openPopup().getFooterControls().openTimeEditor();
        assertTrue(openedTimeEditor.isVisible());
        DateTime time1 = openedTimeEditor.getTime();
        assertEquals(time1.getHourOfDay(), 12);
        assertEquals(time1.getMinuteOfHour(), 0);
    }

    private void setTodaysDate() {
        Graphene.guardAjax(popupCalendar.openPopup().getFooterControls()).setTodaysDate();
        blur(WaitRequestType.NONE);
    }

    private void testTimeSet(int[] valuesToTest, Time time, SetValueBy interaction) {
        DateTime changedTime, collectedTime;
        TimeEditor openedTimeEditor;
        DateTimeFormatter formatter = DateTimeFormat.forPattern(calendarAttributes.get(CalendarAttributes.datePattern));
        boolean firstRun = true;
        for (int value : valuesToTest) {
            if (firstRun) {
                setTodaysDate();
            } else {
                popupCalendar.openPopup().getFooterControls().setTodaysDate();
                blur(WaitRequestType.NONE);
            }
            PopupCalendar openedPopup = popupCalendar.openPopup();
            openedTimeEditor = openedPopup.getFooterControls().openTimeEditor();
            changedTime = time.change(todayMidday, value);
            openedTimeEditor.setTime(changedTime, interaction).confirmTime();
            // check time in time editor
            openedTimeEditor = openedPopup.getFooterControls().openTimeEditor();
            collectedTime = openedTimeEditor.getTime();
            time.checkTimeChanged(changedTime, collectedTime);
            openedTimeEditor.cancelTime();// close the timeditor popup
            Graphene.guardAjax(openedPopup.getFooterControls()).applyDate();
            blur(WaitRequestType.NONE);
            // check time in calendar input
            collectedTime = formatter.parseDateTime(popupCalendar.getInput().getStringValue());
            time.checkTimeChanged(changedTime, collectedTime);
            firstRun = false;
        }
    }

    private enum Time {

        hours {
                @Override
                public DateTime change(DateTime time, int value) {
                    return time.plusHours(value);
                }

                @Override
                public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                    assertEquals(changedTime.getHourOfDay(), referenceTime.getHourOfDay());
                }
            },
        minutes {
                @Override
                public DateTime change(DateTime time, int value) {
                    return time.plusMinutes(value);
                }

                @Override
                public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                    assertEquals(changedTime.getMinuteOfHour(), referenceTime.getMinuteOfHour());
                }
            },
        seconds {
                @Override
                public DateTime change(DateTime time, int value) {
                    return time.plusSeconds(value);
                }

                @Override
                public void checkTimeChanged(DateTime referenceTime, DateTime changedTime) {
                    assertEquals(changedTime.getSecondOfMinute(), referenceTime.getSecondOfMinute());
                }
            };

        public abstract DateTime change(DateTime time, int value);

        public abstract void checkTimeChanged(DateTime referenceTime, DateTime changedTime);
    }
}
