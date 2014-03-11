/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bridgedb.cytoscape.internal.task;

import static org.bridgedb.cytoscape.internal.BridgeDbApp.mapSrcAttrIDTypes;
import org.bridgedb.cytoscape.internal.IDMapperClientManager;
import org.bridgedb.cytoscape.internal.ui.BridgeDbDialog;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

/**
 *
 * @author gaoj
 */
public class OpenMainDialogTask extends AbstractTask {
    @Tunable(description="Application name (optional) for application-specific ID mapping resources"
            + " -- do not specify if use the globel resources", context="nogui")
    public String appName = null;
    
    private final CyApplicationManager cyApplicationManager;
    private final CySwingApplication swingApp;
    private final TaskManager taskManager;
    private final OpenBrowser openBrowser;
    private final FileUtil fileUtil;
    private BridgeDbDialog dialog;

    public OpenMainDialogTask(CyApplicationManager cyApplicationManager,
            CySwingApplication swingApp,
            TaskManager taskManager, OpenBrowser openBrowser,
            FileUtil fileUtil) {
        this.cyApplicationManager = cyApplicationManager;
        this.swingApp = swingApp;
        this.taskManager = taskManager;
        this.openBrowser = openBrowser;
        this.fileUtil = fileUtil;
    }

    @Override
    public void cancel() {
        if (dialog!=null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
            taskMonitor.setTitle("BridgeDb App");
            try {
                    taskMonitor.setStatusMessage("Initializing...");
                    BridgeDbDialog dialog = new BridgeDbDialog(swingApp.getJFrame(),
                            cyApplicationManager, taskManager, openBrowser, fileUtil,
                            IDMapperClientManager.getIDMapperClientManager(appName),
                            false);
                    dialog.setLocationRelativeTo(swingApp.getJFrame());
                    dialog.setMapSrcAttrIDTypes(mapSrcAttrIDTypes);
                    dialog.setVisible(true);
                    mapSrcAttrIDTypes = dialog.getMapSrcAttrIDTypes();
                    taskMonitor.setProgress(1.00);
            } catch (Exception e) {
                    taskMonitor.showMessage(TaskMonitor.Level.ERROR, "Failed to open BridgeDb main dialog.");
                    taskMonitor.setProgress(1.00);
                    e.printStackTrace();
            }
    }

}