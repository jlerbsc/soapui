/*
 * SoapUI, Copyright (C) 2004-2022 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.impl.wsdl.panels.testcase;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.support.components.JPropertiesTable;
import com.eviware.soapui.ui.desktop.DesktopPanel;

import java.awt.Component;

/**
 * PanelBuilder for WsdlTestCase
 *
 * @author Ole.Matzura
 */

public class WsdlTestCasePanelBuilder<T extends WsdlTestCase> extends EmptyPanelBuilder<T> {
    public WsdlTestCasePanelBuilder() {
    }

    public DesktopPanel buildDesktopPanel(T testCase) {
        return new WsdlTestCaseDesktopPanel(testCase);
    }

    public boolean hasDesktopPanel() {
        return true;
    }

    public Component buildOverviewPanel(T modelItem) {
        JPropertiesTable<WsdlTestCase> table = new JPropertiesTable<>("TestCase Properties", modelItem);

        table.addProperty("Name", "name", true);

        table.setPropertyObject(modelItem);

        return table;
    }
}
